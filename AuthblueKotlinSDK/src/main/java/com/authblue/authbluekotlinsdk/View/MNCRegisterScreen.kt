package com.authblue.authbluekotlinsdk.View

import android.content.Context
import android.nfc.NfcAdapter
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.authblue.authbluekotlinsdk.API.APIErrorType
import com.authblue.authbluekotlinsdk.R
import com.authblue.authbluekotlinsdk.View.Component.CustomButton
import com.authblue.authbluekotlinsdk.View.Helper.MyReaderCallbackForUploadCertificate

@Composable
fun MNCRegisterScreen(
    skipCallback: () -> Unit,
    goBackToHomeCallback: () -> Unit
){

    val activity = LocalContext.current as FragmentActivity
    val context = LocalContext.current
    val executor = ContextCompat.getMainExecutor(activity)
    val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)

    val nfcAdapter: NfcAdapter by lazy{
        NfcAdapter.getDefaultAdapter(context)
    }

//    val vmUserState = UserCertificateUploadState.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val observer = LifecycleEventObserver { _, event ->
        when(event){
//                Lifecycle.Event.ON_CREATE -> {}
//                Lifecycle.Event.ON_START-> {}
            Lifecycle.Event.ON_PAUSE-> {
                try {
                    Log.d("EVENT", "ON_PAUSE")
                    nfcAdapter.disableReaderMode(activity)
                } catch (e: Exception) {
                    Log.d("DEBUG", "Failed to disable reader mode", e)
                }
            }
            Lifecycle.Event.ON_RESUME-> {
                Log.d("EVENT", "ON_RESUME")
                nfcAdapter.enableReaderMode(
                    activity,
                    MyReaderCallbackForUploadCertificate(
                        context=context,
                        callback = { res ->
                            //callbackForTag(signature_byte_array=signature_byte_array)
                            if(res.has_error){
                                if(res.error_message == APIErrorType.UNAUTHORIZED.content){
//                                    vmUserState.setSignInNeededTrue()
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.APIErrorToastUnauthorized),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }else if(res.error_message == APIErrorType.NOT_FOUND.content){
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.APIErrorToastNotFound),
                                        Toast.LENGTH_LONG
                                    ).show()

                                }
                                Log.d("DEBUG", "nfc reader callback1")
                                nfcAdapter.disableReaderMode(activity)
                            }else{
                                val editor = sharedPref.edit()
                                editor.putBoolean("mnc_register_status", true)
                                editor.apply()
//                                vmUserState.setUploadNeededFalse()
                                goBackToHomeCallback()
                                Log.d("DEBUG", "nfc reader callback1")
                                nfcAdapter.disableReaderMode(activity)
                            }
                        }
                    ),
                    NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                    null
                )
            }
            else -> {}
        }
    }


    lifecycleOwner.lifecycle.addObserver(observer)

    TopAppBar(
        backgroundColor = Color.White,
        title = {
            Text(
                text = context.getString(R.string.MNCRegisterTitle),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                //Log.d("DEBUG", "HERE")
                goBackToHomeCallback()
            }) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(start = 8.dp)
                )
            }
        },
        actions = {
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 42.dp, end = 32.dp, top = 32.dp),

        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ){
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ){
                CustomButton(
                    text = context.getString(R.string.MNCRegisterScanSkipButtonLabel),
                    callback = skipCallback
                )
            }

            Text(
                context.getString(R.string.MNCRegisterScanLabel1),
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                color = Color.Black
            )
            Text(
                context.getString(R.string.MNCRegisterScanLabel2),
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(10.dp))

            Text(
                context.getString(R.string.MNCRegisterScanSecondLabel1),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.LightGray
            )
            Spacer(Modifier.height(20.dp))

            Image(

                painter = painterResource(id = R.drawable.password_flatline),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(300.dp)
            )
        }

    }

}