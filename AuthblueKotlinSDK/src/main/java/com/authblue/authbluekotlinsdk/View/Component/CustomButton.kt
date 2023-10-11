package com.authblue.authbluekotlinsdk.View.Component

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(text: String, callback: () -> Unit){

    Button(
        onClick = {
            callback()
            Log.d("HERE", "1")
        },
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White,
            disabledContentColor = Color.LightGray
        ),
    ) {
        Text(text)
    }
}
