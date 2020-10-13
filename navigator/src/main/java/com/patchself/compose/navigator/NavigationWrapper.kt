package com.patchself.compose.navigator

import android.util.Log
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
import androidx.compose.ui.layout.id
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMaxBy
import kotlin.math.max
import kotlin.math.min

@Composable
fun navigationWrapper(current: NavigationMode, stack: NavigationStack, modifier: Modifier = Modifier) {
    WithConstraints(modifier = modifier.fillMaxSize()) {
//        var state by mutableStateOf<PageController>(EmptyPage())
        val state = remember { NavigationState() }
        val swipeOffset = animatedFloat(0f)
        val minValue = 0f
        val maxValue = constraints.maxWidth.toFloat()
        var left = mutableStateOf<PageController?>(null)
        var right = mutableStateOf<PageController?>(null)
        if (state.current == null) {
            state.current = current.current
            return@WithConstraints
        }
        if (state.current == current.current){
            return@WithConstraints
        }
        left.value = state.current!!
        right.value = current.current!!
        onCommit(v1 = current, callback = {
            var autoAnimTargetValue = 0f
            var autoAnimStartValue = 0f
            when (current) {
                is NavigationMode.Backward ->{
                    left.value = right.value.also { right.value= left.value }
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = minValue
                }
                is NavigationMode.Forward ->{
//                    left.value = right.value
//                    right.value = current.current
                    autoAnimTargetValue = minValue
                    autoAnimStartValue = maxValue
                }
            }
            swipeOffset.snapTo(autoAnimStartValue)
            swipeOffset.animateTo(autoAnimTargetValue, onEnd = { _, _ ->
                Log.e("=======", "onEnd:${swipeOffset.value}")
                when (current) {
                    is NavigationMode.Forward -> {
                        state.current = current.current!!
                    }
                    is NavigationMode.Backward -> {
                        stack.removeLast()
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }
                }
            }, anim = tween(400))
        })
//        state = current.current!!
//        state.screenContent()
//        return@WithConstraints

        Box(Modifier.draggable(
            enabled = true,
            orientation = Orientation.Horizontal,
            onDrag = { fl ->
                Log.e("=======", "ondrag:$fl ${swipeOffset.value}")
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
                swipeOffset.animateTo(targetValue, onEnd = { _, _ ->
                    if (targetValue == 0f){
//                        state.current = right
                    }else{
                        stack.removeLast()
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }
                    swipeOffset.snapTo(0f)
                })
            }
        )) {
            /*Stack(modifier = Modifier.offsetPx(x=swipeOffset)) {
                left.screenContent()
            }
            Stack(modifier = Modifier.offsetPx(x=swipeOffset)) {
                right.screenContent()
            }*/

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