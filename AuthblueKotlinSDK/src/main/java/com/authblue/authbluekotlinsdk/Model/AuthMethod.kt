package com.authblue.authbluekotlinsdk.Model

enum class AuthMethod(val content: String) {
    FACEID("faceid"),
    MNC("mnc"),
    SIGNATURE("signature")
}

enum class AuthMethodState(val content: Long){
    YET(-1.toLong()),
    CURRENT(0X85e0f9),
    DONE(0X63e369)
}