package com.boxbox.simon.utils

import androidx.compose.ui.graphics.Color

enum class SimonColor {
    Red, Green, Blue, Yellow;

    fun toColor(): Color = when(this){
        SimonColor.Red -> Color.Red
        SimonColor.Green -> Color.Green
        SimonColor.Blue -> Color.Blue
        SimonColor.Yellow -> Color.Yellow
    }
}