package com.authblue.authbluekotlinsdk.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.authblue.authbluekotlinsdk.CertificationService.CertificationService
import com.authblue.authbluekotlinsdk.myna.Reader
import com.authblue.authbluekotlinsdk.myna.TextAP
import com.authblue.authbluekotlinsdk.myna.hexToByteArray
import com.authblue.authbluekotlinsdk.myna.toHexString
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.security.MessageDigest
import kotlin.ByteArray
import kotlin.Int
import kotlin.Pair
import kotlin.String


class AuthViewModel: ViewModel(){
    companion object{
        public const val LOG_TAG = "AuthViewModel"
    }

    private lateinit var nonce: ByteArray

    enum class ResultStatus{
        SUCCESS,
        ERROR_CERTIFICATE_NOT_SET,
        ERROR_FAILURE_TO_VERIFY_SIGNATURE,
        ERROR_TRY_COUNT_IS_NOT_LEFT,
        ERROR_INSUFFICIENT_PIN,
        ERROR_INCORRECT_PIN,
        ERROR_CONNECTION,
    }

    data class Result(
        val status: ResultStatus,
        val tryCountRemain: Int? = null,
        val myNumber: String? = null,
        val attributes: TextAP.Attributes? = null,
    )
    private lateinit var certificationService: CertificationService
    val pin = MutableLiveData("")

    private val _result = MutableSharedFlow<Result>()
    val result: SharedFlow<Result> = _result

//    fun onCreate(context: Context) {
//        certificationService = CertificationService(context)
//
//        val apiHost = R.string.API_HOST
//        val url = "${apiHost}/request_certification"
//
//        val headers = hashMapOf("xxx" to "123")
//
//        Fuel.get(url).header(headers).responseJson { request, response, result ->
//
//            when (result) {
//                is FuelResult.Failure -> {
//                    val ex = result.getException()
//                    Log.d("FUEL", "Failure : " + ex.toString())
//                }
//                is FuelResult.Success -> {
//                    /// JSONObjectに変換
//                    val data = result.get().obj()
//                    Log.d(
//                        "FUEL", "Responsed JSON : "
//                                + data.toString()
//                    )
//                    val nonce_str: String = data.get("nonce") as String
//                    val nonce_decode = Base64.getDecoder().decode(nonce_str)
//                    nonce = nonce_decode
//                }
//            }
//        }
//    }

//    fun onConnect(reader: Reader){
////        val resultData = try{
////            procedure(reader)
////        }catch (e: Exception){
////            Log.d(LOG_TAG, "onConnect: exception", e)
////            Result(ResultStatus.ERROR_CONNECTION)
////        }
////
////        viewModelScope.launch{
////            _result.emit(resultData)
////        }
////        return
//
//
//
//        // sign on nonce
//        val (signature, result) = computeSignature(reader, nonce)
//        result?.also{
//            viewModelScope.launch{
//                _result.emit(it)
//            }
//        }
//        // send it to authentication service and have it verified
//        //val verifyResult = certificationService.verifySignature(signature!!)
//
//        val apiHost = R.string.API_HOST
//        val url = "${apiHost}/verify_signature"
//
//        val signatureb64 = Base64.getEncoder().encodeToString(signature)
//        val tmp1: Map<String, String> = mapOf("signature" to signatureb64)
//        val bodyJson2: String = JSONObject(tmp1).toString()
//        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")
//
//        Fuel.post(url)
//            .body(bodyJson2)
//            .header(header)
//            .responseJson {
//                    request, response, result ->
//
//                when (result) {
//                    is com.github.kittinunf.result.Result.Failure -> {
//                        val ex = result.getException()
//                        Log.d("FUEL", "Fuel Result failure : "+ex.toString())
//                    }
//                    is com.github.kittinunf.result.Result.Success -> {
//                        val data = result.get().obj()
//                        Log.d("FUEL", data.toString())
//                        val verifyResult: Boolean = data.get("result") as Boolean
//                        if(verifyResult){
//                            viewModelScope.launch{
//                                //_result.emit(resultData)
//                                _result.emit(Result(ResultStatus.SUCCESS))
//                            }
//                        }else{
//                            Result(ResultStatus.ERROR_FAILURE_TO_VERIFY_SIGNATURE)
//                        }
//                    }
//                }
//            }
//
//    }

    private fun computeSignature(reader: Reader, data: ByteArray): Pair<ByteArray?, Result?>{
        // get PIN
        val pin = pin.value ?: ""
        if(pin.length != 4){
            return Pair(null, Result(ResultStatus.ERROR_INSUFFICIENT_PIN))
        }

        // AP selection
        val jpkiAP = reader.selectJpkiAp()

        // get remaining PIN count
        val count = jpkiAP.lookupAuthPin()
        if(count == 0){
            return Pair(null, Result(ResultStatus.ERROR_TRY_COUNT_IS_NOT_LEFT))
        }

        // log in
        if(!jpkiAP.verifyAuthPin(pin)){
            return Pair(null, Result(ResultStatus.ERROR_INCORRECT_PIN, count - 1))
        }

        // hash signature object data with SHA-1
        val digest = MessageDigest.getInstance("SHA-1").digest(data)
        Log.d(LOG_TAG, "digest: ${digest.toHexString()}")

        // convert hash value to DigestInfo type
        val header = "3021300906052B0E03021A05000414"
        val digestInfo = header.hexToByteArray() + digest
        Log.d(LOG_TAG, "digestInfo: ${digestInfo.toHexString()}")


        // sign with card secret key
        val signature = jpkiAP.authSignature(digestInfo)
        Log.d(LOG_TAG, "signature: ${signature.toHexString()}")

        return Pair(signature, null)
    }


}

