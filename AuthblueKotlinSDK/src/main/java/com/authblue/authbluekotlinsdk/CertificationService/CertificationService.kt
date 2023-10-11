package com.authblue.authbluekotlinsdk.CertificationService

import android.content.Context
import android.util.Log
import com.authblue.authbluekotlinsdk.R
import com.authblue.authbluekotlinsdk.myna.Reader
import com.authblue.authbluekotlinsdk.myna.hexToByteArray
import com.authblue.authbluekotlinsdk.myna.toHexString
import com.authblue.authbluekotlinsdk.ViewModel.AuthViewModel
import com.github.kittinunf.result.Result
import java.security.MessageDigest

data class Agreement(
    val name: Boolean,
    val birthday: Boolean,
    val address: Boolean,
    val age: Boolean,
    val picture: Boolean,
    val sex: Boolean,
    val phone: Boolean,
    val email: Boolean,
)

class CertificationService(private val context: Context){

    fun computeSignature(reader: Reader, data: ByteArray, pin: String): Pair<ByteArray?, AuthViewModel.Result?>{
        // get PIN
        if(pin.length != 4){
            return Pair(null,
                AuthViewModel.Result(AuthViewModel.ResultStatus.ERROR_INSUFFICIENT_PIN)
            )
        }

        // AP selection
        val jpkiAP = reader.selectJpkiAp()

        // get remaining PIN count
        val count = jpkiAP.lookupAuthPin()
        if(count == 0){
            return Pair(null,
                AuthViewModel.Result(AuthViewModel.ResultStatus.ERROR_TRY_COUNT_IS_NOT_LEFT)
            )
        }

        // log in
        if(!jpkiAP.verifyAuthPin(pin)){
            return Pair(null,
                AuthViewModel.Result(AuthViewModel.ResultStatus.ERROR_INCORRECT_PIN, count - 1)
            )
        }

        // hash signature object data with SHA-1
        val digest = MessageDigest.getInstance("SHA-1").digest(data)
        Log.d(AuthViewModel.LOG_TAG, "digest: ${digest.toHexString()}")

        // convert hash value to DigestInfo type
        val header = "3021300906052B0E03021A05000414"
        val digestInfo = header.hexToByteArray() + digest
        Log.d(AuthViewModel.LOG_TAG, "digestInfo: ${digestInfo.toHexString()}")


        // sign with card secret key
        val signature = jpkiAP.authSignature(digestInfo)
        Log.d(AuthViewModel.LOG_TAG, "signature: ${signature.toHexString()}")

        return Pair(signature, null)
    }

}