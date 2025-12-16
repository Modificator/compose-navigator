package com.patchself.compose.sample.hyperspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patchself.compose.navigator.PageController
import com.patchself.compose.sample.R

/**
 * Demo page for the hyperspace animation
 */
class HyperspacePage : PageController() {
    
    override fun getId() = R.id.HyperspacePage
    
    @Composable
    override fun ScreenContent() {
        var currentState by remember { mutableStateOf<HyperspaceState>(HyperspaceState.Cruise) }
        
        Box(modifier = Modifier.fillMaxSize()) {
            // Hyperspace animation background
            HyperspaceAnimation(
                state = currentState,
                modifier = Modifier.fillMaxSize()
            )
            
            // Top bar
            TopAppBar(
                title = { Text(text = "Hyperspace Animation") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color.Black.copy(alpha = 0.5f),
                contentColor = Color.White,
                elevation = 0.dp
            )
            
            // Control buttons at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = when (currentState) {
                        HyperspaceState.Cruise -> "巡航状态 (Cruise Mode)"
                        HyperspaceState.Warp -> "超光速状态 (Warp Mode)"
                        HyperspaceState.Deceleration -> "减速中 (Decelerating)"
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { currentState = HyperspaceState.Cruise },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (currentState == HyperspaceState.Cruise) 
                                Color.Cyan else Color.DarkGray
                        )
                    ) {
                        Text("巡航\nCruise")
                    }
                    
                    Button(
                        onClick = { currentState = HyperspaceState.Warp },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (currentState == HyperspaceState.Warp) 
                                Color.Red else Color.DarkGray
                        )
                    ) {
                        Text("超光速\nWarp")
                    }
                    
                    Button(
                        onClick = { currentState = HyperspaceState.Deceleration },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (currentState == HyperspaceState.Deceleration) 
                                Color.Yellow else Color.DarkGray
                        )
                    ) {
                        Text("减速\nSlow Down")
                    }
                }
                
                Text(
                    text = "点击按钮切换动画状态",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}
