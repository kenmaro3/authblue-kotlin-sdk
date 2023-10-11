package com.authblue.authbluekotlinsdk.View.Component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.composesignature.ComposeSignature
import kotlin.properties.Delegates
import com.authblue.authbluekotlinsdk.R

@Composable
fun SignatureView(
    callback: (ImageBitmap) -> Unit
) {
    var imageBitmap: ImageBitmap? by Delegates.observable<ImageBitmap?>(null) { _, oldValue, newValue ->
        // Custom logic when the property is set
        // oldValue contains the previous value, and newValue contains the new value
        // You can perform any necessary operations here
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ){
            Text(
                text = context.getString(R.string.SignatureViewTitle),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(32.dp))

            ComposeSignature(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                signaturePadColor = Color(0xFFEEEEEE),
                signaturePadHeight = 400.dp,
                signatureColor = Color.Black,
                signatureThickness = 10f,
                onComplete = { signatureBitmap ->
                    imageBitmap = signatureBitmap.asImageBitmap()
                    callback(imageBitmap!!)
                },
                onClear = {
                    imageBitmap = null
                }
            )
        }

    }
}