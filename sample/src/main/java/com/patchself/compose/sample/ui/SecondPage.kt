package com.patchself.compose.sample.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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

class SecondPage : PageController() {
    override fun getId() = R.id.SecondPage

    @Composable
    override fun screenContent() {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Second Page") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.Filled.ArrowBack)
                    }
                },
                elevation = 4.dp
            )
        }) {
            Column(modifier = Modifier.fillMaxSize().wrapContentSize()) {
                FloatingActionButton(onClick = {
                    navigateTo(ThirdPage())
                },Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        Icons.Filled.ArrowForward,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Spacer(modifier = Modifier.height(Dp(15f)))
                Text(
                    text = "Next Page",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}