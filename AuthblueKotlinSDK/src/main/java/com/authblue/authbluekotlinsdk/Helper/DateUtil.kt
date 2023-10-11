package com.authblue.authbluekotlinsdk.Helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNow(): String{
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        println(formattedDateTime)
        return formattedDateTime
    }
}