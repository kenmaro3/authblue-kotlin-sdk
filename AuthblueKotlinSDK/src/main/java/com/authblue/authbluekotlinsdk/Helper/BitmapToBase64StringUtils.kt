package com.authblue.authbluekotlinsdk.Helper

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

class BitmapToBase64StringUtils {

    fun convertImageBitmapToBase64(imageBitmap: ImageBitmap): String {
        val tmp = imageBitmap.asAndroidBitmap()
        val outputStream = ByteArrayOutputStream()
        tmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return base64String
    }
}