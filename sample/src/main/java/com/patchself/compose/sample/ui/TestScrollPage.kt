package com.patchself.compose.sample.ui

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

class TestScrollPage: PageController() {
    override fun getId(): Int {
        return View.generateViewId()
    }

    @Composable
    override fun ScreenContent() {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "TestScroll Sample") }, elevation = 4.dp)
        }) {
            Column(modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .wrapContentSize()) {
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
                Text(text = "test",Modifier.height(30.dp))
            }
        }
    }
}