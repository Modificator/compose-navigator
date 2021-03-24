package com.patchself.compose.sample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patchself.compose.navigator.PageController
import com.patchself.compose.sample.R

class HomePage : PageController() {

    var fromThirdPage = false

    override fun getId() = R.id.HomePage

    @Composable
    override fun ScreenContent() {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Navigator Sample") }, elevation = 4.dp)
        }) {
            Column(modifier = Modifier.fillMaxSize().wrapContentSize()) {
                FloatingActionButton(onClick = {
                    navigateTo(SecondPage())
                },Modifier.align(Alignment.CenterHorizontally)) {
                    Image(Icons.Filled.ArrowForward,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(
                    text = "Next Page",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (fromThirdPage){
                    Spacer(modifier = Modifier.height(Dp(25f)))
                    Text(
                        text = "Reset from Third Page",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}