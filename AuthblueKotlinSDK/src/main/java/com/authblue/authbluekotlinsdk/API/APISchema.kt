package com.authblue.authbluekotlinsdk.API

import com.authblue.authbluekotlinsdk.myna.TextAP
import kotlinx.serialization.Serializable

enum class MyNumberResultStatus {
    SUCCESS,
    ERROR_TRY_COUNT_IS_NOT_LEFT,
    ERROR_INSUFFICIENT_PIN,
    ERROR_INCORRECT_PIN,
    ERROR_CONNECTION,
}

data class MyNumberResult(
    val status: MyNumberResultStatus,
    val tryCountRemain: Int? = null,
    val myNumber: String? = null,
    val attributes: TextAP.Attributes? = null,
)

enum class APIErrorType(val content: String) {
    UNAUTHORIZED("unauthorized"),
    NOT_FOUND("not_found"),
    UNKNOWN_ERROR("unknown_error"),
}

@Serializable
data class createUserWithAuthResponse(
    val user_id: String,
    val username: String,
    val access_token: String
)

@Serializable
data class createUserWithAuthResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: createUserWithAuthResponse?
)

@Serializable
data class NonceResult(
    val nonce: String
)

@Serializable
data class RequestNonceResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: NonceResult?
)

@Serializable
data class DeleteUserResponse(
    val status: String
)

@Serializable
data class DeleteUserResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: DeleteUserResponse?
)

@Serializable
data class AuthAgreementWithQRResponse(
    val ref_code: String
)

@Serializable
data class AuthAgreementWithQRResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: AuthAgreementWithQRResponse?

)

@Serializable
data class RequestCertificateExistsResponse(
    val exists: Boolean
)

@Serializable
data class RequestCertificateExistsResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: RequestCertificateExistsResponse?
)

@Serializable
data class CertificateRegisterResponse(
    val user_id: String
)

@Serializable
data class CertificateRegisterResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: CertificateRegisterResponse?
)