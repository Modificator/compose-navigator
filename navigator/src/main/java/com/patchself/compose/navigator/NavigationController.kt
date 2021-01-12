package com.patchself.compose.navigator

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

val navigationController = NavigationController()

class NavigationController {
    private val stack = NavigationStack()
    private var current: NavigationMode by mutableStateOf(NavigationMode.Rebase(EmptyPage()))
    private var currentIndex=0
    @Preview
    @Composable
    fun ViewContent() {
        Column {
            navigationWrapper(current = current,stack = stack)
        }
    }

    fun navigateBack(): Boolean {
        stack.getPrevious()?.let {
            current = NavigationMode.Backward(it)
            return true
        }
        return false
    }

    fun navigateTo(controller: PageController): Boolean {
        stack.push(controller,true)
        current = NavigationMode.Forward(controller)
        return true
    }

    fun <T : PageController> resetTo(targetId: Int,reinit:(T.()->Unit)?=null): Boolean {
        stack.findLastById(targetId)?.let {
            stack.resetTo(it)
            reinit?.invoke(it as T)
            current = NavigationMode.Reset(it)
            return true
        }
        return false
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