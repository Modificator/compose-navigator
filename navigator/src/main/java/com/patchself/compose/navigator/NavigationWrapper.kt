package com.patchself.compose.navigator

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.ExponentialDecay
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import kotlin.math.max
import kotlin.math.min

@Composable
internal fun navigationWrapper(current: NavigationMode, stack: NavigationStack, modifier: Modifier = Modifier) {
    var isAnimating = remember { false }
    BoxWithConstraints(modifier = modifier
        .fillMaxSize()
        .pointerInteropFilter { isAnimating }) {
        val state = remember { NavigationState() }
        val swipeOffset = animatedFloat(0f)
        val minValue = 0f
        val maxValue = constraints.maxWidth.toFloat()
        val left = mutableStateOf<PageController?>(null)
        val right = mutableStateOf<PageController?>(null)
        if (state.current == current.current){
            return@BoxWithConstraints
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
            is NavigationMode.Reset -> {
                left.value = stack.getCurrent()
                right.value = state.current
            }
        }
        DisposableEffect(current, effect = {
            var autoAnimTargetValue = 0f
            var autoAnimStartValue = 0f
            when (current) {
                is NavigationMode.Backward,is NavigationMode.Reset ->{
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
            swipeOffset.animateTo(autoAnimTargetValue, tween(400)) { _, _ ->
                when (current) {
                    is NavigationMode.Forward -> {
                    }
                    is NavigationMode.Backward -> {
                        stack.removeLast()
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }
                    is NavigationMode.Reset -> {
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
            }
            onDispose {  }
        })

        Box(Modifier
            .dragGestureFilter(
                dragObserver = object : DragObserver {
                    override fun onDrag(dragDistance: Offset): Offset {
                        val old = swipeOffset.value
                        swipeOffset.snapTo(min(max((swipeOffset.value + dragDistance.x), minValue), maxValue))
                        swipeOffset.value - old
                        return Offset(swipeOffset.value - old,dragDistance.y)
                    }

                    override fun onStop(velocity: Offset) {
                        val targetValue = if (FloatExponentialDecaySpec().getTarget(
                                swipeOffset.value,
                                velocity.x
                            ) > maxValue / 2f
                        ) maxValue else minValue
                        isAnimating = true
                        swipeOffset.animateTo(targetValue, onEnd = { _, _ ->
                            if (targetValue == 0f) {
//                        state.current = right
                            } else {
                                val removePage = stack.removeLast()
                                removePage?.onBlur()
                                right.value = left.value
                                state.current = right.value
                                left.value = stack.getPrevious()
                                right.value?.onFocus()
                            }
                            swipeOffset.snapTo(0f)
                            isAnimating = false
                        })
                    }
                                                     },
                canDrag = {
                    stack.size() > 1 && !isAnimating
                },
                startDragImmediately = false
            )
        ) {
            Layout(content = {
                Box(Modifier.layoutId(0)) { left.value?.screenContent() }
                Box(
                    Modifier
                        .layoutId(1)
                        .shadow(Dp(8f))) { right.value?.screenContent() }
            }, measureBlock = { list, constraints ->
                val placeables = list.map { it.measure(constraints) to it.layoutId }
                val height = placeables.maxByOrNull { it.first.height }?.first?.height ?: 0
                layout(constraints.maxWidth, height) {
                    placeables.forEach { (placeable, tag) ->
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