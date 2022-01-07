package com.patchself.compose.navigator

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

@ExperimentalComposeUiApi
@Composable
internal fun NavigationWrapper(current: NavigationMode, stack: NavigationStack, modifier: Modifier = Modifier){
    var isAnimating = remember { false }
    //Fix slid bug
    var isRested = remember { false }
    BoxWithConstraints(modifier = modifier
        .fillMaxSize()
        .pointerInteropFilter { isAnimating }) {
        val coroutineScope = rememberCoroutineScope()

        val state = remember { NavigationState() }
        val swipeOffset = remember { Animatable(0f) }
        val minValue = 0f
        val maxValue = constraints.maxWidth.toFloat()
        val left = mutableStateOf<PageController?>(null)
        val right = mutableStateOf<PageController?>(null)

        when (current) {
            is NavigationMode.Backward -> {
                left.value = stack.getPrevious()
                right.value = stack.getCurrent()
            }
            is NavigationMode.Forward -> {
                left.value = state.current
                runBlocking {
                    swipeOffset.snapTo(maxValue)
                }
                right.value = current.current!!
            }
            is NavigationMode.Rebase -> {
                left.value = null
                right.value = current.current
                state.current = current.current
            }
            is NavigationMode.Reset -> {
                if(!isRested) {//Fix slid bug
                    left.value = stack.getCurrent()
                } else {
                    left.value = stack.getPrevious()
                }
                right.value = state.current
                if(right.value == state.current) {
                    isRested = true
                }
            }
        }
        DisposableEffect(current, effect = {
            var autoAnimTargetValue = 0f
            var autoAnimStartValue = 0f
            when (current) {
                is NavigationMode.Backward->{
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = swipeOffset.value
                }is NavigationMode.Reset ->{
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = minValue
                }
                is NavigationMode.Forward ->{
                    autoAnimTargetValue = minValue
                    autoAnimStartValue = maxValue
                }
            }
            coroutineScope.launch {
                isAnimating = true
                if (current !is NavigationMode.Backward) {
                    swipeOffset.snapTo(autoAnimStartValue)
                }
                swipeOffset.animateTo(autoAnimTargetValue, tween(400))
                when (current) {
                    is NavigationMode.Forward -> {
                    }
                    is NavigationMode.Backward -> {
                        stack.removeLast()
//                        right.value = left.value
                        right.value = stack.getCurrent()
                        left.value = stack.getPrevious()
                        state.current = right.value
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
            .draggable(
                state = DraggableState {
                    if (stack.size() <= 1 || isAnimating){
                        return@DraggableState
                    }
                    runBlocking {
                        swipeOffset.snapTo(min(max((swipeOffset.value + it), minValue), maxValue))
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    if (stack.size() <= 1 || isAnimating){
                        return@draggable
                    }
                    val targetValue = if (FloatExponentialDecaySpec().getTargetValue(
                            swipeOffset.value,
                            velocity
                        ) > maxValue / 2f
                    ) maxValue else minValue
                    isAnimating = true
                    swipeOffset.animateTo(targetValue)
                    isAnimating = false
                    if (targetValue != 0f) {
                        navigationController.navigateBack()
                    }
                }
            )
        ) {
            Layout(content = {
                Box(Modifier.layoutId(0)) { left.value?.ScreenContent() }
                Box(
                    Modifier
                        .layoutId(1)
                        .shadow(Dp(8f))) { right.value?.ScreenContent() }
            }, measurePolicy = { list, constraints ->
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