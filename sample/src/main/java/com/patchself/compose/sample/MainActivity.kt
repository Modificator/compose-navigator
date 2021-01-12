package com.patchself.compose.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.patchself.compose.navigator.navigationController
import com.patchself.compose.sample.ui.SplashPage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController.initController(SplashPage())
        setContent {
            navigationController.ViewContent()
        }
    }

    override fun onBackPressed() {
        if (!navigationController.onBackPressed()){
            super.onBackPressed()
        }
    }
}