package com.authblue.authbluekotlinsdk.View

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.authblue.authbluekotlinsdk.API.APIClient
import com.authblue.authbluekotlinsdk.API.APIErrorType
import com.authblue.authbluekotlinsdk.Model.AgreementMethod
import com.authblue.authbluekotlinsdk.Model.AgreementModel
import com.authblue.authbluekotlinsdk.Model.AuthMethod
import com.authblue.authbluekotlinsdk.Model.AuthMethodState
import com.authblue.authbluekotlinsdk.R
import com.authblue.authbluekotlinsdk.View.Component.AgreementSent
import com.authblue.authbluekotlinsdk.View.Component.AgreementSteps
import com.authblue.authbluekotlinsdk.View.Component.CustomButton
import com.authblue.authbluekotlinsdk.View.Component.ReceiptDetailScreen
import com.authblue.authbluekotlinsdk.View.Component.Card.AgreementRequestCard

@Composable
fun AgreementRequest(
    client_name: String,
    client_id: String,
    dynamic_link_uid: String,
    goBackToLoginCallback: () -> Unit,
    goBackToHomeCallback: () -> Unit,
    goToNotificationCallback: () -> Unit
){

    val context = LocalContext.current
    val navController = rememberNavController()

    var userName = remember{ mutableStateOf("") }
    var clientId = remember{ mutableStateOf("") }
    var dynamicLinkUid = remember{ mutableStateOf("") }

    var isShowDetail = remember { mutableStateOf(false) }
    var clientName = remember { mutableStateOf("") }
    var content = remember { mutableStateOf("") }
    var date = remember { mutableStateOf("") }
    var refCode = remember { mutableStateOf("") }
    var requestingInfo = remember { mutableStateOf(AgreementModel()) }
    var agreementMethod = remember { mutableStateOf(AgreementMethod()) }

    var agreementMethodsStates = remember{mutableListOf<AuthMethodState>()}

    var isUserDataNotReadyForAgreement = remember{ mutableStateOf(false) }
    var isUserCertificateNotReadyForAgreement = remember{ mutableStateOf(false) }
    var isShowingNotificationModal = remember{ mutableStateOf(false) }

    fun listAuthMethodToAgreementMethod(methods: List<AuthMethod>): AgreementMethod {
        var res = AgreementMethod()
        methods.forEach{
            if(it == AuthMethod.FACEID){
                res.face_id = true
            }else if(it == AuthMethod.MNC){
                res.my_number_card = true
            }else if(it == AuthMethod.SIGNATURE){
                res.signature = true
            }
        }
        return res;
    }

    fun agreementMethodtoListAuthMethod(agreementMethod: AgreementMethod): List<AuthMethod>{
        var res = mutableListOf<AuthMethod>()
        if(agreementMethod.face_id){
            res.add(AuthMethod.FACEID)
        }
        if(agreementMethod.my_number_card){
            res.add(AuthMethod.MNC)
        }
        if(agreementMethod.signature){
            res.add(AuthMethod.SIGNATURE)
        }
        return res
    }

    fun checkIfUserDataIsReadyForAgreement(){

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)

        // name
        val tmp_personal_name = sharedPref.getString("personal_name", "")
        if(requestingInfo.value.name){
            if(tmp_personal_name == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // birthday
        val tmp_personal_birthday = sharedPref.getString("personal_birthday", "")
        if(requestingInfo.value.birthday){
            if(tmp_personal_birthday == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // age
        val tmp_personal_age = sharedPref.getString("personal_age", "")
        if(requestingInfo.value.age){
            if(tmp_personal_age == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // address
        val tmp_personal_address = sharedPref.getString("personal_address", "")
        if(requestingInfo.value.address){
            if(tmp_personal_address == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // sex
        val tmp_personal_sex = sharedPref.getString("personal_sex", "")
        if(requestingInfo.value.sex){
            if(tmp_personal_sex == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // phone
        val tmp_personal_phone = sharedPref.getString("personal_phone", "")
        if(requestingInfo.value.phone){
            if(tmp_personal_phone == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }

        // email
        val tmp_personal_email = sharedPref.getString("personal_email", "")
        if(requestingInfo.value.email){
            if(tmp_personal_email == ""){
                isUserDataNotReadyForAgreement.value = true
            }
        }
    }

    fun checkIfUserCertificateIsReadyForAgreement(){
        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val tmp_mnc_register_status = sharedPref.getBoolean("mnc_register_status", false)
        if(agreementMethod.value.my_number_card){
            if(!tmp_mnc_register_status){
                isUserCertificateNotReadyForAgreement.value = true
            }
        }
    }


    LaunchedEffect(key1=Unit){
        clientName.value = client_name
        clientId.value = client_id
        dynamicLinkUid.value = dynamic_link_uid
        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val tmp = sharedPref.getString("personal_name", "")
        tmp?.let{
            userName.value = tmp
        }



        APIClient().getClientForAgreement(context = context, client_id=client_id, client_name=client_name) { res ->
            if(res.has_error){
                if(res.error_message == APIErrorType.UNAUTHORIZED.content){
                    Toast.makeText(
                        context,
                        context.getString(R.string.APIErrorToastUnauthorized),
                        Toast.LENGTH_LONG
                    ).show()
                    goBackToLoginCallback()
                }else if(res.error_message == APIErrorType.NOT_FOUND.content){
                    Toast.makeText(
                        context,
                        context.getString(R.string.APIErrorToastNotFound),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }else{
                res?.let{
                    res.result?.let{

                        content.value = it.content.agreement
                        requestingInfo.value = it.requesting_info
                        agreementMethod.value = it.agreement_method

                        agreementMethodtoListAuthMethod(agreementMethod.value).forEachIndexed{ index, element ->
                            if(index==0){
                                agreementMethodsStates.add(AuthMethodState.CURRENT)
                            }else{
                                agreementMethodsStates.add(AuthMethodState.YET)
                            }
                        }
                        Log.d("DEBUG", "here ${agreementMethodsStates}")
                    }
                }
            }

        }
    }


    // isShowDetailの値に基づいて表示するコンポーザブルを選択するロジック
    val composableContent: @Composable () -> Unit = if (isShowDetail.value) {
        {
            Column(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 12.dp,
                    start = 14.dp,
                    end = 14.dp
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .padding(end = 30.dp),
                    contentAlignment = Alignment.CenterEnd

                ){
                    CustomButton(text = context.getString(R.string.DeepLinkAgreementRequestBackFromDetailButton),) {
                        isShowDetail.value = false
                    }
                }

                ReceiptDetailScreen(
                    clientName = clientName.value,
                    refCode = refCode.value,
                    content = content.value,
                    date = date.value,
                    requesting_info = requestingInfo.value,
                    agreementMethod = agreementMethod.value
                )
            }
        }
    } else {
        {
            NavHost(
                navController = navController,
                startDestination = "request"
            ) {
                composable(route = "request") {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box() {
                            Image(
                                painter = painterResource(id = R.drawable.handshake_flatline),
                                contentDescription = "handshake_flatline",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Column(
                            modifier = Modifier.padding(
                                top = 12.dp,
                                bottom = 12.dp,
                                start = 14.dp,
                                end = 14.dp
                            )
                        ) {
                            Text(
                                text = context.getString(R.string.DeepLinkAgreementRequestTitle),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = context.getString(R.string.DeepLinkAgreementRequestSubTitle),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(bottom=20.dp)
                                    .clickable {
                                        isShowDetail.value = true
                                    }
                            ) {
                                AgreementRequestCard(
                                    client_name = clientName.value,
                                    clientId = clientId.value,
                                    user_name = userName.value,
                                    methods = agreementMethodtoListAuthMethod(agreementMethod = agreementMethod.value),
                                    content = content.value,
                                    agreementModel = requestingInfo.value
                                )
                            }

                            CustomButton(text = context.getString(R.string.DeepLinkAgreementRequestNextButtonLabel)) {
                                checkIfUserDataIsReadyForAgreement()
                                checkIfUserCertificateIsReadyForAgreement()
                                if(!isUserCertificateNotReadyForAgreement.value && !isUserDataNotReadyForAgreement.value){
                                    navController.navigate("agreement_steps")
                                }
                            }
                        }
                    }
                    if (isUserDataNotReadyForAgreement.value) {
                        AlertDialog(
                            onDismissRequest = {   },
                            title = {
                                Text(text = context.getString(R.string.AgreementRequestUserDataNotReadyAlertTitle))
                            },
                            text = {
                                Text(context.getString(R.string.AgreementRequestUserDataNotReadyAlertDescription))
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = { // confirmをタップしたとき
                                        //isShowingNotificationModal.value = true
                                        goToNotificationCallback()
                                    }
                                ) {
                                    Text(context.getString(R.string.AgreementRequestUserDataNotReadyAlertPrimaryButtonLabel))
                                }
                            },
                            dismissButton = null
                        )
                    }
                    if (isUserCertificateNotReadyForAgreement.value) {
                        AlertDialog(
                            onDismissRequest = {   },
                            title = {
                                Text(text = context.getString(R.string.AgreementRequestUserCertificateNotReadyAlertTitle))
                            },
                            text = {
                                Text(text= context.getString(R.string.AgreementRequestUserCertificateNotReadyAlertDescription))
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = { // confirmをタップしたとき
                                        //isShowingNotificationModal.value = true
                                        goToNotificationCallback()
                                    }
                                ) {
                                    Text(text=context.getString(R.string.AgreementRequestUserCertificateNotReadyAlertPrimaryButtonLabel))
                                }
                            },
                            dismissButton = null
                        )
                    }
                }

                composable(route = "agreement_steps") {
                    AgreementSteps(
                        clientId = clientId.value,
                        methods = agreementMethodtoListAuthMethod(agreementMethod = agreementMethod.value),
                        methodsStates = agreementMethodsStates,
                        requestingInfo = requestingInfo.value,
                        uid = dynamicLinkUid.value,
                        callbackGoToAgreementSent = {
                            navController.navigate("agreement_sent")
                        }
                    )
                }


                composable("agreement_sent") {
                    AgreementSent(callback = {
                        Log.d("DEBUG", "sent button callback")
                        goBackToHomeCallback()
                    })
                }

            }
        }
    }

    Box {
        composableContent()
    }


}