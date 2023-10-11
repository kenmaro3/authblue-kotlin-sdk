package com.authblue.authbluekotlinsdk.View.Component


import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.authblue.authbluekotlinsdk.Model.AuthMethod
import com.authblue.authbluekotlinsdk.Model.AuthMethodState
import kotlinx.coroutines.*
import java.util.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.compose.rememberNavController
import com.authblue.authbluekotlinsdk.API.APIClient
import com.authblue.authbluekotlinsdk.API.APIErrorType
import com.authblue.authbluekotlinsdk.Helper.BitmapToBase64StringUtils
import com.authblue.authbluekotlinsdk.Model.AgreementModel
import com.authblue.authbluekotlinsdk.View.Helper.*
import com.authblue.authbluekotlinsdk.R
import kotlinx.coroutines.MainScope

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: activity.findViewById(android.R.id.content)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AgreementSteps(
    clientId: String,
    methods: List<AuthMethod>,
    methodsStates: MutableList<AuthMethodState>,
    requestingInfo: AgreementModel,
    uid: String,
    callbackGoToAgreementSent: () -> Unit
){

//    val vmUserState = UserCertificateUploadState.current

    val navController = rememberNavController()
    var isLoading = remember {
        mutableStateOf(false)
    }

    var isAuthenticationFinished by remember {
        mutableStateOf(false)
    }
    var index by remember {
        mutableStateOf(0)
    }

    var signatureStr: String? by remember{
        mutableStateOf(null)
    }

    var imageBitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    var showingScreenType: String by remember{ mutableStateOf("main") }

    val mainScope = MainScope()

    val activity = LocalContext.current as FragmentActivity
    val context = LocalContext.current
    val executor = ContextCompat.getMainExecutor(activity)

    var pin: String = ""
    var nonceByteArray: ByteArray = byteArrayOf()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.Expanded}
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    fun incrementStep(){
        Log.d("DEBUG", "incrementStep")
        Log.d("DEBUG", "index ${index}")
        Log.d("DEBUG", methodsStates.toString())
        methodsStates[index] = AuthMethodState.DONE
        if(index < methodsStates.count() - 1){
            methodsStates[index+1] = AuthMethodState.CURRENT
            index += 1
        }else if(index == methodsStates.count() - 1){
            Log.d("DEBUG", "else if")
            isAuthenticationFinished = true
            showingScreenType = "main"
        }
    }

    fun callbackForSignature(
        image_bitmap: ImageBitmap
    ){
        imageBitmap = image_bitmap
        navController.popBackStack()
        incrementStep()

    }

    fun callbackForTag(signature_byte_array: ByteArray){
        mainScope.launch(Dispatchers.Main) {
            // Call the setCurrentState method here
            navController.popBackStack()
        }
        signatureStr = Base64.getEncoder().encodeToString(signature_byte_array) // byteArray to base64Str
        incrementStep()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun callbackForPin(
        value: String,
        flag: Boolean,
    ){
        pin = value
        if(flag){
            runBlocking {
                delay(500)
            }

            hideKeyboard(activity=activity)
            Toast.makeText(context,
                context.getText(R.string.MNCRegisterScanSecondLabel1), Toast.LENGTH_LONG)
                .show()

            APIClient().requestNonceFastAPI(context = context) { res ->
                if(res.has_error){
                    if(res.error_message == APIErrorType.UNAUTHORIZED.content){
//                        vmUserState.setSignInNeededTrue()
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
                }else{
                    val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    res?.let{
                        res.result?.let{
                            nonceByteArray = Base64.getDecoder().decode(res.result.nonce)
                            val charset = Charsets.UTF_8
                            editor.putString("nonce", String(nonceByteArray, charset))
                        }
                    }
                    editor.apply()
                }

            }
        }
    }

    var promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.DeepLinkAgreementStepsBiometricPromptTitle))
        .setSubtitle(context.getString(R.string.DeepLinkAgreementStepsBiometricPromptSubTitle))
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()


    val nfcAdapter: NfcAdapter by lazy{
        NfcAdapter.getDefaultAdapter(context)
    }

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context,
                    "${context.getString(R.string.DeepLinkAgreementStepsBiometricPromptErrored)}: $errString", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                incrementStep()

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, context.getString(R.string.DeepLinkAgreementStepsBiometricPromptFailed),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        })

    fun authenticateMain(){
        Log.d("DEBUG", "authenticateMain called!")
        when(methods[index]){
            AuthMethod.FACEID -> {
                biometricPrompt.authenticate(promptInfo)
            }
            AuthMethod.MNC -> {
                //navController.navigate("mnc")
                showingScreenType = "mnc"
            }
            AuthMethod.SIGNATURE-> {
                //navController.navigate("signature")
                showingScreenType = "signature"
            }
        }
    }

    fun submitAgreement(){
        isLoading.value = true
        if(clientId == ""){
            Toast.makeText(context,
                "Client ID is empty", Toast.LENGTH_LONG)
                .show()
            return
        }else{
            Log.d("DUBUG", "here 111")
            var imageBitmapToPass: String? = null
            imageBitmap?.let{
                Log.d("DUBUG", "here 222")
                imageBitmapToPass = BitmapToBase64StringUtils().convertImageBitmapToBase64(imageBitmap = imageBitmap!!)

            }
            APIClient().authAgreementWithQRRequest(
                context = context,
                client_id = clientId,
                signature = imageBitmapToPass,
                requestingInfo = requestingInfo,
                digital_signature = signatureStr,
                uid = uid
            ){res ->
                Log.d("DUBUG", "submitAgreement DONE")
                Log.d("DEBUG", res.toString())
                if(res.has_error){
                    if(res.error_message == APIErrorType.UNAUTHORIZED.content){
//                        vmUserState.setSignInNeededTrue()
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
                    callbackGoToAgreementSent()
                }else{
                    callbackGoToAgreementSent()
                }
                isLoading.value = false


            }
        }

    }

    LaunchedEffect(Unit) {
        Log.d("LE", "called $index")

        delay(500)
        authenticateMain()
    }

    LaunchedEffect(index) {
        Log.d("LE", "called $index")
        authenticateMain()
    }

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
                    MyReaderCallbackForSignature(
                        context=context,
                        signatureGotCallback = { signature_byte_array ->
                            callbackForTag(signature_byte_array=signature_byte_array)
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

    LoaderModal(visible = isLoading.value)

    if(showingScreenType == "main"){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = context.getString(R.string.DeepLinkAgreementStepsTitle),
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(48.dp))

                methods.forEachIndexed { i, element ->
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        AgreementStepRow(index = i + 1, method = element, state = methodsStates[i])
                        Spacer(modifier = Modifier.padding(bottom = 24.dp))
                        if (i != methods.count() - 1) {
                            var lineDone: Boolean = false
                            when (methodsStates[i]) {
                                AuthMethodState.DONE -> {
                                    lineDone = true
                                }
                                AuthMethodState.CURRENT -> {
                                    lineDone = false
                                }
                                AuthMethodState.YET -> {
                                    lineDone = false
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .padding(start = 32.dp, bottom = 20.dp)
                            ) {
                                AgreementStepLine(isDone = lineDone)
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
                if (isAuthenticationFinished) {
                    showingScreenType = "main"

                    CustomButton(text = context.getString(R.string.DeepLinkAgreementStepsSubmitLabel)) {
                        submitAgreement()

                    }
                }
            }
        }
    }else if(showingScreenType == "mnc"){

        BottomSheetForPin(
            type = "signature",
            callbackForPin = { res, flag ->
                callbackForPin(
                    value = res,
                    flag = flag,
                )
            },
        )
    }else if(showingScreenType == "signature"){
        SignatureView(
            callback = { res ->
                callbackForSignature(image_bitmap = res)
            }
        )
    }
}
