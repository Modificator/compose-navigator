package com.patchself.compose.sample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
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

class ThirdPage : PageController() {
    override fun getId() = R.id.ThirdPage

    @Composable
    override fun ScreenContent() {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Third Page") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.Filled.ArrowBack,contentDescription = null)
                    }
                },
                elevation = 4.dp
            )
        }) {
            Column(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()) {
                FloatingActionButton(onClick = {
                    navigateTo(ThirdPage())
                },Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        Icons.Filled.ArrowForward,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(
                    text = "open new ThirdPage",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(Dp(35f)))
                FloatingActionButton(onClick = {
                    resetTo(R.id.SecondPage)
                },Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        Icons.Filled.ArrowBack,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(
                    text = "Reset to SecondPage",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(Dp(35f)))
                FloatingActionButton(onClick = {
                    resetTo<HomePage>(R.id.HomePage) {
                        fromThirdPage = true
                    }
                },Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        Icons.Filled.ArrowBack,
                        colorFilter = ColorFilter.tint(Color.White),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(
                    text = "Reset to HomePage",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}