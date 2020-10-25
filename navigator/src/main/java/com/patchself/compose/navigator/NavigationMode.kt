package com.patchself.compose.navigator

internal sealed class NavigationMode(var current: PageController?=null) {
    class Forward(current: PageController) :NavigationMode(current)
    class Backward(current: PageController) :NavigationMode(current)
    class Rebase(current: PageController):NavigationMode(current)
    class Fade(current: PageController):NavigationMode(current)
}