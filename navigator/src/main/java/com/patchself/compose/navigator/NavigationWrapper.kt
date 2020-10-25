package com.patchself.compose.navigator

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.ExponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.id
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMaxBy
import kotlin.math.max
import kotlin.math.min

@Composable
internal fun navigationWrapper(current: NavigationMode, stack: NavigationStack, modifier: Modifier = Modifier) {
    var isAnimating = remember { false }
    WithConstraints(modifier = modifier.fillMaxSize().pointerInteropFilter { isAnimating }) {
        val state = remember { NavigationState() }
        val swipeOffset = animatedFloat(0f)
        val minValue = 0f
        val maxValue = constraints.maxWidth.toFloat()
        val left = mutableStateOf<PageController?>(null)
        val right = mutableStateOf<PageController?>(null)
        if (state.current == current.current){
            return@WithConstraints
        }
        when (current) {
            is NavigationMode.Backward -> {
                left.value = current.current!!
                right.value = state.current
            }
            is NavigationMode.Forward -> {
                left.value = state.current!!
                right.value = current.current!!
            }
            is NavigationMode.Rebase -> {
                left.value = null
                right.value = current.current
                state.current = current.current
            }
        }
        onCommit(v1 = current, callback = {
            var autoAnimTargetValue = 0f
            var autoAnimStartValue = 0f
            when (current) {
                is NavigationMode.Backward ->{
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = minValue
                }
                is NavigationMode.Forward ->{
                    autoAnimTargetValue = minValue
                    autoAnimStartValue = maxValue
                }
            }
            swipeOffset.snapTo(autoAnimStartValue)
            isAnimating = true
            swipeOffset.animateTo(autoAnimTargetValue, onEnd = { _, _ ->
                when (current) {
                    is NavigationMode.Forward -> {
                    }
                    is NavigationMode.Backward -> {
                        stack.removeLast()
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }
                }
                state.current = current.current!!
                swipeOffset.snapTo(0f)
                isAnimating = false
                right.value?.onFocus()
                left.value?.onBlur()
            }, anim = tween(400))
        })

        Box(Modifier.draggable(
            enabled = !isAnimating,
            orientation = Orientation.Horizontal,
            canDrag = { stack.size() > 1 && !isAnimating },
            onDrag = { fl ->
                val old = swipeOffset.value
                swipeOffset.snapTo(min(max((swipeOffset.value + fl), minValue), maxValue))
                swipeOffset.value - old
            },
            onDragStopped = { velocity ->
                val targetValue = if (ExponentialDecay().getTarget(
                        swipeOffset.value,
                        velocity
                    ) > maxValue / 2f
                ) maxValue else minValue
                isAnimating = true
                swipeOffset.animateTo(targetValue, onEnd = { _, _ ->
                    if (targetValue == 0f){
//                        state.current = right
                    }else{
                        right.value?.onFocus()
                        left.value?.onBlur()
                        stack.removeLast()
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }
                    swipeOffset.snapTo(0f)
                    isAnimating = false
                })
            }
        )) {
            Layout(children = {
                Box(Modifier.layoutId(0)) { left.value?.screenContent() }
                Box(Modifier.layoutId(1).drawShadow(Dp(8f))) { right.value?.screenContent() }
            }, measureBlock = { list, constraints ->
                val placeables = list.map { it.measure(constraints) to it.id }
                val height = placeables.fastMaxBy { it.first.height }?.first?.height ?: 0
                layout(constraints.maxWidth, height) {
                    placeables.fastForEach { (placeable, tag) ->
                        if (tag is Int) {
                            placeable.place(
                                x = if (tag == 0) {
                                    ((-constraints.maxWidth + swipeOffset.value * 1f) * 0.3f).toInt()
                                } else {
                                    swipeOffset.value.toInt()
                                },
                                y = 0
                            )
                        }
                    }
                }
            })
        }
    }
}

private class NavigationState {
    var current: PageController? = null
}