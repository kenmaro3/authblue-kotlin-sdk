package com.authblue.authbluekotlinsdk.View.Component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authblue.authbluekotlinsdk.R

@Composable
fun BottomSheetForPin(
    type: String,
    callbackForPin: (String, Boolean) -> Unit
) {
    var otpValue by remember {
        mutableStateOf("")
    }

    var showCompleteMark by remember { mutableStateOf(false) }
    var showCompleteMarkForTag by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ){

        Column(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(
                    horizontal = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center


        ) {

            Text(
                context.getString(R.string.PasscodeFieldDescriptionForGettingInfoLabel1),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.Black
            )
            Text(
                context.getString(R.string.PasscodeFieldDescriptionForGettingInfoLabel2),
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(10.dp))

            if(type == "readInfo"){

                Text(
                    text = context.getString(R.string.PasscodeFieldDescriptionForGettingInfo),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 30.dp)

                )
            }else if(type == "signature"){
                Text(
                    text = context.getString(R.string.PasscodeFieldDescriptionForSignature),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 30.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            Row(){

                OtpTextField(
                    otpText = otpValue,
                    onOtpTextChange = { value, otpInputFilled ->
                        otpValue = value
                        Log.d("Otp", otpValue)
                        if(otpInputFilled){
                            showCompleteMark = true
                        }
                        callbackForPin(otpValue, otpInputFilled)
                    }
                )

                AnimatedCompleteMark(
                    visible = showCompleteMark,
                    onComplete = {showCompleteMark = false}
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedCompleteMark(
                visible = showCompleteMarkForTag,
                onComplete = {showCompleteMarkForTag = false}
            )
        }
    }
}

