package com.authblue.authbluekotlinsdk.API

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.authblue.authbluekotlinsdk.Model.AgreementModel
import com.authblue.authbluekotlinsdk.Model.AgreementRequestResponseAPI
import com.authblue.authbluekotlinsdk.R
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException
import java.security.cert.X509Certificate
import java.util.*
import kotlinx.serialization.json.Json
import kotlin.collections.HashMap


class APIClient {

    val BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    val END_CERT = "-----END CERTIFICATE-----";
    val LINE_SEPARATOR: String = System.getProperty("line.separator")


    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(CertificateEncodingException::class)
    fun formatCrtFileContents(certificate: Certificate): String? {
        val encoder =
            Base64.getMimeEncoder(64, LINE_SEPARATOR.toByteArray())
        val rawCrtText = certificate.encoded
        val encodedCertText = String(encoder.encode(rawCrtText))
        return BEGIN_CERT + LINE_SEPARATOR + encodedCertText + LINE_SEPARATOR + END_CERT
    }

    fun createUserWithAuth(context: Context, username: String, email: String, callback: (createUserWithAuthResponseAPI) -> Unit) {

        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/users_with_auth_mobile"

        val tmp1: Map<String, String> = mapOf(
            "username" to username,
            "email" to email,
        )
        val bodyJson2: String = JSONObject(tmp1).toString()
        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

        Fuel.post(url).body(bodyJson2).header(header).responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.d("FUEL", "Fuel Result failure : " + ex.toString())
                    var tmp = createUserWithAuthResponseAPI(
                        has_error = true,
                        error_message = APIErrorType.UNKNOWN_ERROR.content,
                        req_id="",
                        result= null
                    )
                    callback(tmp)
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<createUserWithAuthResponseAPI>(result.get().obj().toString())
                    Log.d("API 1", tmp.toString())
                    callback(tmp)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postCertificateToFastAPI(context: Context, certificate: X509Certificate, callback: (CertificateRegisterResponseAPI) -> Unit){
        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token == ""){
            var tmp = CertificateRegisterResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        // serialize certificate
        val byteArrayOutputStream = ByteArrayOutputStream()
        ObjectOutputStream(byteArrayOutputStream).use{
            it.writeObject(certificate)
        }
        val certString = formatCrtFileContents(certificate)!!

        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/register_certificate"

        Log.d("FUEL", "here1")
        val tmp1: Map<String, String> = mapOf(
            "certificate" to certString,
        )
        Log.d("HERE: ", certString)
        val bodyJson2: String = JSONObject(tmp1).toString()

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer $token",
            "Content-Type" to "application/json"
        )

        Fuel.post(url).body(bodyJson2).header(header).responseJson {
                request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {
                        var tmp = CertificateRegisterResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = CertificateRegisterResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<CertificateRegisterResponseAPI>(result.get().obj().toString())
                    callback(tmp)
                }
            }
        }
    }

    fun requestCertificateExists(context: Context, callback: (RequestCertificateExistsResponseAPI) -> Unit) {
        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/request_certificate_exists"

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token == ""){
            var tmp = RequestCertificateExistsResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer ${token}",
            "Content-Type" to "application/json"
        )

        Log.d("requestCertificateExists", url.toString())

        Fuel.get(url).header(header).responseJson { request, response, result ->
//        Fuel.post(url).body(bodyJson2).header(header).responseJson { request, response, result ->
            Log.d("RequestCertificateExistsResponseAPI", "here3")
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {
                        var tmp = RequestCertificateExistsResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = RequestCertificateExistsResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<RequestCertificateExistsResponseAPI>(result.get().obj().toString())
                    Log.d("requestCertificateExists 1", tmp.toString())
                    callback(tmp)
                }
                else -> {
                    var tmp = RequestCertificateExistsResponseAPI(
                        has_error = true,
                        error_message = APIErrorType.UNKNOWN_ERROR.content,
                        req_id="",
                        result= null
                    )
                    callback(tmp)
                }
            }
        }


    }

    fun requestNonceFastAPI(context: Context, callback: (RequestNonceResponseAPI) -> Unit) {
        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/request_nonce"

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")
        if(token == ""){
            var tmp = RequestNonceResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer ${token}",
            "Content-Type" to "application/json"
        )


        Fuel.get(url).header(header).responseJson { request, response, result ->
//        Fuel.post(url).body(bodyJson2).header(header).responseJson { request, response, result ->
            Log.d("API", "here3")
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {

                        var tmp = RequestNonceResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = RequestNonceResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                    Log.d("APINONCE0", "")
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<RequestNonceResponseAPI>(result.get().obj().toString())
                    Log.d("APINONCE1", tmp.toString())
                    callback(tmp)
                }
                else -> {
                    var tmp = RequestNonceResponseAPI(
                        has_error = true,
                        error_message = APIErrorType.UNKNOWN_ERROR.content,
                        req_id="",
                        result= null
                    )
                    Log.d("APINONCE2", tmp.toString())
                    callback(tmp)
                }
            }
        }
    }

    fun authAgreementWithQRRequest(
        context: Context,
        client_id: String,
        signature: String?,
        digital_signature: String?,
        requestingInfo: AgreementModel,
        uid: String,
        callback: (
            AuthAgreementWithQRResponseAPI
        ) -> Unit
    ) {
        Log.d("API", "here1")
        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/oauth/agreement_with_qr/mobile"

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token == ""){
            var tmp = AuthAgreementWithQRResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        Log.d("API", "here2: $client_id")

        //var requestingInfoToSend: RequestingInfo = RequestingInfo()
        var requestingInfoToSend: MutableMap<String, Any?> = mutableMapOf()
        if(requestingInfo.name){
            val name = sharedPref.getString("personal_name", "")
            name?.let{
                //requestingInfoToSend.name = name
                requestingInfoToSend["name"] = name
            }
        }
        if(requestingInfo.birthday){
            val birthday = sharedPref.getString("personal_birthday", "")
            birthday?.let{
                //requestingInfoToSend.birthday = birthday
                requestingInfoToSend["birthday"] = birthday
            }
        }
        if(requestingInfo.age){
            val age = sharedPref.getString("personal_age", "")
            age?.let{
                //requestingInfoToSend.age = age
                requestingInfoToSend["age"] = age
            }
        }
        if(requestingInfo.sex){
            val sex = sharedPref.getString("personal_sex", "")
            sex?.let{
                //requestingInfoToSend.sex = sex
                requestingInfoToSend["sex"] = sex
            }
        }
        if(requestingInfo.address){
            val address = sharedPref.getString("personal_address", "")
            address?.let{
                //requestingInfoToSend.address = address
                requestingInfoToSend["address"] = address
            }
        }
        if(requestingInfo.phone){
            val phone = sharedPref.getString("personal_phone", "")
            phone?.let{
                //requestingInfoToSend.phone = phone
                requestingInfoToSend["phone"] = phone
            }
        }
        if(requestingInfo.email){
            val email = sharedPref.getString("personal_email", "")
            email?.let{
                //requestingInfoToSend.email = email
                requestingInfoToSend["email"] = email
            }
        }

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer ${token}",
            "Content-Type" to "application/json"
        )

        val tmp1: Map<String, Any?> = mapOf(
            "client_id" to client_id,
            "signature" to signature,
            "digital_signature" to digital_signature,
            "requesting_info" to requestingInfoToSend,
            "uid" to uid
        )


        //Log.d("DEBUG JSON", Json.encodeToString(requestingInfoToSend).toString())
        val bodyJson2: String = JSONObject(tmp1).toString()

//        Fuel.get(url).header(header).responseJson { request, response, result ->
        Fuel.post(url).body(bodyJson2).header(header).responseJson { request, response, result ->
            Log.d("API", "here3")
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {
                        var tmp = AuthAgreementWithQRResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = AuthAgreementWithQRResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<AuthAgreementWithQRResponseAPI>(result.get().obj().toString())
                    Log.d("API 1", tmp.toString())
                    callback(tmp)
                }
                else -> {
                    var tmp = AuthAgreementWithQRResponseAPI(
                        has_error = true,
                        error_message = APIErrorType.UNKNOWN_ERROR.content,
                        req_id="",
                        result= null
                    )
                    callback(tmp)
                }
            }
        }
    }

    fun getTutorialClient(context: Context, callback: (AgreementRequestResponseAPI) -> Unit){
        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token == ""){
            var tmp = AgreementRequestResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/clients/get_tutorial_client"


        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer $token",
            "Content-Type" to "application/json"
        )

        Fuel.get(url).header(header).responseJson {
                request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {
                        var tmp = AgreementRequestResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = AgreementRequestResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<AgreementRequestResponseAPI>(result.get().obj().toString())
                    callback(tmp)
                }
            }
        }
    }

    fun getClientForAgreement(context: Context, client_id: String, client_name: String, callback: (AgreementRequestResponseAPI) -> Unit){
        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token == ""){
            var tmp = AgreementRequestResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/clients/get_for_agreement"

        Log.d("FUEL", "here1")
        val tmp1: Map<String, String> = mapOf(
            "client_id" to client_id,
            "client_name" to client_name,
        )
        val bodyJson2: String = JSONObject(tmp1).toString()

        Log.d("DEBUG", "here ${bodyJson2} =========================")

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer $token",
            "Content-Type" to "application/json"
        )

        Fuel.post(url).body(bodyJson2).header(header).responseJson {
                request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {
                        var tmp = AgreementRequestResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = AgreementRequestResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<AgreementRequestResponseAPI>(result.get().obj().toString())
                    callback(tmp)
                }
            }
        }
    }

    fun deleteUserWoSignature(context: Context, callback: (DeleteUserResponseAPI) -> Unit) {
        val apiHost = context.getString(R.string.API_HOST)
        val url = "${apiHost}/delete_user_wo_signature"

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")
        if(token == ""){
            var tmp = DeleteUserResponseAPI(
                has_error = true,
                error_message = APIErrorType.UNAUTHORIZED.content,
                req_id="",
                result= null
            )
            callback(tmp)
            return
        }

        val header: HashMap<String, String> = hashMapOf(
            "Authorization" to "Bearer ${token}",
            "Content-Type" to "application/json"
        )


        Fuel.get(url).header(header).responseJson { request, response, result ->
//        Fuel.post(url).body(bodyJson2).header(header).responseJson { request, response, result ->
            Log.d("API", "here3")
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if (ex.response.statusCode == 401) {

                        var tmp = DeleteUserResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.UNAUTHORIZED.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    } else if (ex.response.statusCode == 404) {
                        var tmp = DeleteUserResponseAPI(
                            has_error = true,
                            error_message = APIErrorType.NOT_FOUND.content,
                            req_id="",
                            result= null
                        )
                        callback(tmp)

                    }
                }
                is Result.Success -> {
                    val tmp = Json{ignoreUnknownKeys = true}.decodeFromString<DeleteUserResponseAPI>(result.get().obj().toString())
                    callback(tmp)
                }
                else -> {
                    var tmp = DeleteUserResponseAPI(
                        has_error = true,
                        error_message = APIErrorType.UNKNOWN_ERROR.content,
                        req_id="",
                        result= null
                    )
                    callback(tmp)
                }
            }
        }
    }
}
