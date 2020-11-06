package com.patchself.compose.navigator

import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class PageController:CoroutineScope by MainScope() {

    open abstract fun getId():Int

    @Composable
    open abstract fun screenContent()

    @CallSuper
    open fun onFocus() {
    }

    @CallSuper
    open fun onBlur(){

    }

    open fun navigateTo(controller: PageController): Boolean {
        return navigationController.navigateTo(controller)
    }

    open fun navigateBack(): Boolean {
        return navigationController.navigateBack()
    }

    fun <T : PageController> resetTo(targetId: Int,reinit:(T.()->Unit)?=null): Boolean {
        return navigationController.resetTo(targetId, reinit)
    }

    fun resetTo(targetId: Int): Boolean {
        return resetTo<PageController>(targetId)
    }

    open fun setController(controller: PageController){
        navigationController.initController(controller)
    }

    @CallSuper
    open fun destory(){
        cancel()
    }

    /**
     * @param fromTop
     * @return intercept navigate back
     */
    open fun onBackPressed(fromTop: Boolean): Boolean {
        return false
    }
}

class EmptyPage: PageController() {
    override fun getId(): Int {
        return 0
    }

    @Composable
    override fun screenContent() {

    }
}