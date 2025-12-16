package com.patchself.compose.sample.hyperspace

/**
 * Represents the different states of the hyperspace animation
 */
sealed class HyperspaceState {
    /**
     * Cruise mode - stars move slowly from center to edges as points
     */
    object Cruise : HyperspaceState()
    
    /**
     * Warp/Hyperspace mode - stars stretch into lines moving rapidly
     */
    object Warp : HyperspaceState()
    
    /**
     * Deceleration mode - transitioning from Warp back to Cruise
     */
    object Deceleration : HyperspaceState()
}
