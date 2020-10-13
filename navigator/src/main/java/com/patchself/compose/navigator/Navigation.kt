package com.patchself.compose.navigator

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.WithConstraints
import androidx.ui.tooling.preview.Preview

val navigationController = NavigationController()

class NavigationController() {
    //    @Composable
    private val stack = NavigationStack()
    private var current: NavigationMode by mutableStateOf(NavigationMode.Rebase(EmptyPage()))//:ScreenController by state<ScreenController>{ SplashController() }
    private var currentIndex=0
    @Preview
    @Composable
    fun viewContent() {
        navigationWrapper(current = current,stack = stack)
    }

    fun navigateBack(): Boolean {
        stack.getPrevious()?.let {
            current = NavigationMode.Backward(it)
        }
        return true
    }

    fun navigateTo(controller: PageController): Boolean {
        stack.push(controller,true)
        current = NavigationMode.Forward(controller)
        return true
    }

    fun initController(controller: PageController){
        stack.clear()
        stack.push(controller,true)
        current = NavigationMode.Rebase(current = controller)
    }

    fun findLastById(id: Int):PageController?{
        return stack.findLastById(id)
    }

    fun onBackPressed(): Boolean {
        val page = stack.getCurrent()
        if(page?.onBackPressed(false) == true){
            return true
        }
        if (stack.size()>1){
            navigateBack()
            return true
        }
        return false
    }
}

sealed class NavigationMode(var current: PageController?=null) {
    class Forward(current: PageController) : NavigationMode(current)
    class Backward(current: PageController) : NavigationMode(current)
    class Rebase(current: PageController): NavigationMode(current)
    class Fade(current: PageController): NavigationMode(current)
}