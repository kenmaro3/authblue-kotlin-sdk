package com.authblue.authbluekotlinsdk.View

//import UserCertificateUploadState
import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.authblue.authbluekotlinsdk.API.MyNumberResultStatus
import com.authblue.authbluekotlinsdk.R
import com.authblue.authbluekotlinsdk.View.Component.BottomSheetForPin
import com.authblue.authbluekotlinsdk.View.Helper.MyReaderGetInfoCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun calculateAge(birthDateStr: String): Int {
    val birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.BASIC_ISO_DATE)
    val currentDate = LocalDate.now()
    val age = Period.between(birthDate, currentDate)
    return age.years
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: activity.findViewById(android.R.id.content)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@Composable
fun MNCReadInfoScreen(
    goBackToHomeCallback: () -> Unit
) {


    var isReadInfoDone: Boolean by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = isReadInfoDone){
        if(isReadInfoDone){
            goBackToHomeCallback()
        }
    }

    var pin: String = ""
    val activity = LocalContext.current as FragmentActivity
    val context = LocalContext.current
//    val vmUserState = UserCertificateUploadState.current
    val nfcAdapter: NfcAdapter by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }

    fun callbackForPin(
        value: String,
        flag: Boolean,
    ) {
        pin = value
        if (flag) {
            runBlocking {
                delay(500)
            }

            hideKeyboard(activity = activity)
            Toast.makeText(
                context,
                context.getText(R.string.MNCRegisterScanSecondLabel1), Toast.LENGTH_LONG
            ).show()
            val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("pin", pin)
            editor.apply()
        }

    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val observer = LifecycleEventObserver { _, event ->
        when (event) {
//                Lifecycle.Event.ON_CREATE -> {}
//                Lifecycle.Event.ON_START-> {}
            Lifecycle.Event.ON_PAUSE -> {
                try {
                    Log.d("EVENT", "ON_PAUSE")
                    nfcAdapter.disableReaderMode(activity)
                } catch (e: Exception) {
                    Log.d("DEBUG", "Failed to disable reader mode", e)
                }
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.d("EVENT", "ON_RESUME MNCReadInfoScreen")
                nfcAdapter.enableReaderMode(
                    activity,
                    MyReaderGetInfoCallback(
                        context = context,
                        callback = { res ->
                            when(res.status){
                                MyNumberResultStatus.SUCCESS ->{
//                                    Toast.makeText(
//                                        context,
//                                        MyNumberResultToast.SUCCESS.content,
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                    val sharedPref =
                                        context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
                                    val editor = sharedPref.edit()

                                    res.attributes?.name?.let{
                                        Log.d("DEBUG MyReaderGetInfoCallback", res.attributes?.name)
                                    }
                                    res.attributes?.address?.let{
                                        Log.d("DEBUG MyReaderGetInfoCallback", res.attributes?.address)
                                    }
                                    editor.putString("personal_name", res.attributes?.name)
                                    editor.putString("personal_address", res.attributes?.address)
                                    res.attributes?.birth.let{
                                        editor.putString("personal_birthday", it)
                                        it?.let{
                                            val age = calculateAge(it)
                                            editor.putString("personal_age", age.toString())
                                        }


                                    }
                                    editor.putString("personal_sex", res.attributes?.sex)
                                    editor.putString("personal_name", res.attributes?.name)
                                    editor.apply()
                                }
                                MyNumberResultStatus.ERROR_TRY_COUNT_IS_NOT_LEFT ->{
//                                    Toast.makeText(
//                                        context,
//                                        MyNumberResultToast.ERROR_TRY_COUNT_IS_NOT_LEFT.content,
//                                        Toast.LENGTH_LONG
//                                    ).show()


                                }
                                MyNumberResultStatus.ERROR_INSUFFICIENT_PIN ->{

//                                    Toast.makeText(
//                                        context,
//                                        MyNumberResultToast.ERROR_INSUFFICIENT_PIN.content,
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                }
                                MyNumberResultStatus.ERROR_INCORRECT_PIN ->{
//                                    Toast.makeText(
//                                        context,
//                                        MyNumberResultToast.ERROR_INCORRECT_PIN.content,
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                }
                                MyNumberResultStatus.ERROR_CONNECTION ->{
//                                    Toast.makeText(
//                                        context,
//                                        MyNumberResultToast.ERROR_CONNECTION.content,
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                }
                            }
                            isReadInfoDone = true
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
                text = context.getString(R.string.MNCReadInfoTitle),
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


    BottomSheetForPin(
        type="readInfo",
        callbackForPin = { res, flag ->
            callbackForPin(
                value = res,
                flag = flag,
            )},
    )
}