package com.patchself.compose.sample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.patchself.compose.navigator.PageController
import com.patchself.compose.sample.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashPage: PageController() {
    override fun getId() = R.id.SplashPage

    @Preview
    @Composable
    override fun ScreenContent() {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)){
            Column(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)) {
                Text(text = "Navigator Sample",fontSize = 30.sp)
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(text = "Splash Page",fontSize = 40.sp)
            }
        }
    }

    override fun onFocus() {
        super.onFocus()
        launch {
            delay(2000)
            setController(HomePage())
        }
    }

}