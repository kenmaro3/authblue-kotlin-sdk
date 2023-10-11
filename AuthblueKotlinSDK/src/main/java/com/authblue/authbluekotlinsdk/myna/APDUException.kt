package com.authblue.authbluekotlinsdk.myna

class APDUException(val sw1: Byte, val sw2: Byte): Exception()