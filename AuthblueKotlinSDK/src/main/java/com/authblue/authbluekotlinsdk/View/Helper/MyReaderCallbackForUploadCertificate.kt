package com.authblue.authbluekotlinsdk.View.Helper

//import UserCertificateUploadStateViewModel
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
import com.authblue.authbluekotlinsdk.API.APIClient
import com.authblue.authbluekotlinsdk.API.CertificateRegisterResponseAPI
import com.authblue.authbluekotlinsdk.API.MyNumberResult
import com.authblue.authbluekotlinsdk.API.MyNumberResultStatus
import com.authblue.authbluekotlinsdk.CertificationService.CertificationService
import com.authblue.authbluekotlinsdk.myna.Reader
import java.io.IOException

class MyReaderCallbackForUploadCertificate(
    context: Context,
    callback: (CertificateRegisterResponseAPI) -> Unit
) : NfcAdapter.ReaderCallback {
    val context = context
    val callback = callback

    override fun onTagDiscovered(tag: Tag) {
        Log.d("Hoge", "Tag discoverd1")


        if (tag == null) return


        val reader = Reader(tag)
        try {
            reader.connect()
        } catch (e: Exception) {
            Log.d("DEBUG", "Failed to connect", e)
            return
        }

        val jpkiAP = reader.selectJpkiAp()

        // 認証用証明書取得
        val cert = jpkiAP.readAuthCertificate()
        Log.d("DEBUG", "cert: $cert")

        APIClient().postCertificateToFastAPI(
            context=context,
            certificate = cert
        ){res ->
            callback(res)


        }
        try {
            reader.close()
        } catch (e: IOException) {
            Log.d("DEBUG", "Failed to close reader", e)
        }

    }

}

class MyReaderCallbackForSignature(
    context: Context,
    signatureGotCallback: (ByteArray) -> Unit
) : NfcAdapter.ReaderCallback {
    val context = context
    val signatureGotCallback = signatureGotCallback

    override fun onTagDiscovered(tag: Tag) {
        Log.d("Hoge", "Tag discoverd2")

        if (tag == null) return

        val reader = Reader(tag)
        try {
            reader.connect()
        } catch (e: Exception) {
            Log.d("DEBUG", "Failed to connect", e)
            return
        }

        val ser = CertificationService(
            context = context
        )

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val pin = sharedPref.getString("pin", "")
        val nonce = sharedPref.getString("nonce", "")!!


        val charset = Charsets.UTF_8 // Specify the desired character encoding
        val nonceByteArray = nonce.toByteArray(charset)


        if (pin.toString().length != 4) {
            Log.d("DEBUG", "here1: $pin")

        } else {
            Log.d("DEBUG", "here2")
            val (signature_res, result) = ser.computeSignature(
                reader,
                nonceByteArray,
                pin!!
            )
            val editor = sharedPref.edit()
            editor.putString("signature", signature_res!!.toString())
            editor.apply()

            Log.d("DEBUG", "wrote to shared preference signature")

            signature_res?.let{
                signatureGotCallback(it)
            }

            try {
                reader.close()
            } catch (e: IOException) {
                Log.d("DEBUG", "Failed to close reader", e)
            }
        }
    }
}

class MyReaderGetInfoCallback(
    context: Context,
    callback: (MyNumberResult) -> Unit
) : NfcAdapter.ReaderCallback {
    val context = context
    val callback = callback



    fun procedure(pin: String, reader: Reader): MyNumberResult {
        // PIN取得
        if (pin.length != 4) {
            return MyNumberResult(MyNumberResultStatus.ERROR_INSUFFICIENT_PIN)
        }

        // AP選択
        val textAP = reader.selectTextAp()

        // PINの残りカウント取得
        val count = textAP.lookupPin()
        if (count == 0) {
            return MyNumberResult(MyNumberResultStatus.ERROR_TRY_COUNT_IS_NOT_LEFT)
        }

        // PIN解除
        if (!textAP.verifyPin(pin)) {
            return MyNumberResult(MyNumberResultStatus.ERROR_INCORRECT_PIN, count - 1)
        }

        // マイナンバー取得
        val myNumber = textAP.readMyNumber()

        // その他の情報を取得
        val attributes = textAP.readAttributes()

        return MyNumberResult(MyNumberResultStatus.SUCCESS, count, myNumber, attributes)
    }

    override fun onTagDiscovered(tag: Tag) {
        Log.d("Hoge", "Tag discoverd3")

        if (tag == null) return

        val reader = Reader(tag)
        try {
            reader.connect()
        } catch (e: Exception) {
            Log.d("DEBUG", "Failed to connect", e)
            return
        }

        val sharedPref = context.getSharedPreferences("authblue_preferences", Context.MODE_PRIVATE)
        val pin = sharedPref.getString("pin", "")

        pin?.let{
            if (pin.toString().length != 4) {
                Log.d("DEBUG", "here1: $pin")

            } else {
                Log.d("DEBUG", "here2")
                val res = procedure(pin=pin, reader=reader)
                callback(res)

            }
        }

        try {
            reader.close()
        } catch (e: IOException) {
            Log.d("DEBUG", "Failed to close reader", e)
        }
    }
}
