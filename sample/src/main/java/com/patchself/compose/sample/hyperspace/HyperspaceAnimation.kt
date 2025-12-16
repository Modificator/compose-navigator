package com.patchself.compose.sample.hyperspace

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.random.Random

/**
 * Hyperspace animation composable that renders a starfield effect
 * 
 * @param state Current animation state (Cruise, Warp, or Deceleration)
 * @param modifier Modifier for the canvas
 * @param starCount Number of stars to render
 * @param baseSpeed Base speed multiplier for star movement
 * @param backgroundColor Background color
 */
@Composable
fun HyperspaceAnimation(
    state: HyperspaceState,
    modifier: Modifier = Modifier,
    starCount: Int = 200,
    baseSpeed: Float = 1f,
    backgroundColor: Color = Color.Black
) {
    // Animation progress for smooth transitions
    val targetSpeed = when (state) {
        HyperspaceState.Cruise -> 1f
        HyperspaceState.Warp -> 15f
        HyperspaceState.Deceleration -> 1f
    }
    
    val animatedSpeed by animateFloatAsState(
        targetValue = targetSpeed * baseSpeed,
        animationSpec = tween(
            durationMillis = when (state) {
                HyperspaceState.Warp -> 1500 // Quick acceleration
                HyperspaceState.Deceleration -> 3000 // Slow deceleration
                else -> 1000
            },
            easing = when (state) {
                HyperspaceState.Warp -> FastOutSlowInEasing
                HyperspaceState.Deceleration -> LinearOutSlowInEasing
                else -> LinearEasing
            }
        ), label = "speed"
    )
    
    // Star trail length based on speed
    val targetTrailLength = when {
        animatedSpeed > 10f -> 80f
        animatedSpeed > 5f -> 40f
        else -> 5f
    }
    
    val animatedTrailLength by animateFloatAsState(
        targetValue = targetTrailLength,
        animationSpec = tween(1000), label = "trail"
    )
    
    // Initialize stars
    val stars = remember {
        List(starCount) {
            Star(
                x = 0f,
                y = 0f,
                z = Random.nextFloat() * 2f + 0.5f,
                speed = Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }
    
    // Continuous redraw for animation
    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { }
        }
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxDistance = kotlin.math.sqrt(centerX * centerX + centerY * centerY)
        
        // Draw background
        drawRect(backgroundColor)
        
        // Update and draw each star
        stars.forEach { star ->
            // Initialize star position if needed
            if (star.x == 0f && star.y == 0f) {
                star.reset(centerX, centerY)
            }
            
            // Calculate direction from center
            val dx = star.x - centerX
            val dy = star.y - centerY
            val distance = kotlin.math.sqrt(dx * dx + dy * dy)
            
            // Normalize direction (avoid division by zero)
            val nx = if (distance > 0) dx / distance else 0f
            val ny = if (distance > 0) dy / distance else 0f
            
            if (distance > 0) {
                // Move star outward
                val moveSpeed = animatedSpeed * star.speed * 0.5f
                star.x += nx * moveSpeed
                star.y += ny * moveSpeed
                
                // Update z position for depth effect
                star.z = kotlin.math.max(0.1f, star.z - moveSpeed * 0.01f)
            }
            
            // Get current position accounting for depth
            val currentPos = star.getOffset(centerX, centerY)
            
            // Reset star if it goes off screen
            if (currentPos.x < -50 || currentPos.x > size.width + 50 ||
                currentPos.y < -50 || currentPos.y > size.height + 50) {
                star.reset(centerX, centerY)
            } else {
                // Calculate previous position for trail effect
                val trailDistance = animatedTrailLength * star.speed
                val prevX = star.x - nx * trailDistance
                val prevY = star.y - ny * trailDistance
                val prevZ = kotlin.math.min(3f, star.z + trailDistance * 0.01f)
                
                val prevScale = 1f / prevZ
                val prevPos = Offset(
                    centerX + (prevX - centerX) * prevScale,
                    centerY + (prevY - centerY) * prevScale
                )
                
                val starSize = star.getSize()
                val alpha = (1f - (distance / maxDistance)).coerceIn(0f, 1f) * star.color.alpha
                
                // Draw star as line (trail) or point based on speed
                if (animatedTrailLength > 10f) {
                    // Draw as line (warp effect)
                    drawLine(
                        color = star.color.copy(alpha = alpha * 0.6f),
                        start = prevPos,
                        end = currentPos,
                        strokeWidth = starSize.coerceIn(0.5f, 3f),
                        cap = StrokeCap.Round
                    )
                    // Draw brighter point at the end
                    drawCircle(
                        color = star.color.copy(alpha = alpha),
                        radius = starSize,
                        center = currentPos
                    )
                } else {
                    // Draw as point (cruise mode)
                    drawCircle(
                        color = star.color.copy(alpha = alpha),
                        radius = starSize,
                        center = currentPos
                    )
                }
            }
        }
    }
}
