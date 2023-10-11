package com.authblue.authbluekotlinsdk.View.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun AgreementStepLine(isDone: Boolean){
    val shape = RectangleShape
    var colorHex: Long = 0X85e0f9
    if(isDone){
        colorHex = 0XFF85e0f9
    }else{
        colorHex = 0xFFdedede
    }
    Box(
        modifier = Modifier
            .size(width = 4.dp, height = 24.dp)
            .background(color = Color(colorHex))
    )
}