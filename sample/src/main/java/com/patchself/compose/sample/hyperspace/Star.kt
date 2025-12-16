package com.patchself.compose.sample.hyperspace

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Represents a single star in the hyperspace animation
 */
data class Star(
    var x: Float,
    var y: Float,
    var z: Float, // Depth for 3D effect
    var speed: Float,
    val baseSize: Float = Random.nextFloat() * 2f + 1f,
    val color: Color = Color.White.copy(alpha = Random.nextFloat() * 0.5f + 0.5f)
) {
    /**
     * Get the current position offset accounting for depth
     */
    fun getOffset(centerX: Float, centerY: Float): Offset {
        val scale = 1f / z
        return Offset(
            centerX + (x - centerX) * scale,
            centerY + (y - centerY) * scale
        )
    }
    
    /**
     * Get the size of the star accounting for depth
     */
    fun getSize(): Float {
        return baseSize / z
    }
    
    /**
     * Reset star to center with new random properties
     */
    fun reset(centerX: Float, centerY: Float) {
        x = centerX + (Random.nextFloat() - 0.5f) * 100f
        y = centerY + (Random.nextFloat() - 0.5f) * 100f
        z = Random.nextFloat() * 2f + 0.5f
        speed = Random.nextFloat() * 0.5f + 0.5f
    }
}
