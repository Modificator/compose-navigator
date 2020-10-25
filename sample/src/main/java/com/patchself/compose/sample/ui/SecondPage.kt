package com.patchself.compose.sample.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patchself.compose.navigator.PageController
import com.patchself.compose.sample.R

class SecondPage : PageController() {
    override fun getId() = R.id.SecondPage

    @Composable
    override fun screenContent() {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Navigator Sample") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(asset = Icons.Filled.ArrowBack)
                    }
                },
                elevation = 4.dp
            )
        }) {

        }
    }
}