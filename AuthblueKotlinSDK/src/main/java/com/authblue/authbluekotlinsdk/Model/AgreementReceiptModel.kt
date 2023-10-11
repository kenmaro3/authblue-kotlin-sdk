package com.authblue.authbluekotlinsdk.Model

import kotlinx.serialization.Serializable

@Serializable
data class AgreementReceiptModel(
    val id: String,
    val created_at: String,
    val client_id: String,
    val client_name: String,
    val content: AgreementContentModel,
    val requesting_info: AgreementModel,
    val agreement_method: AgreementMethod,
    val type: String,
)

@Serializable
data class AgreementContentModel(
    val agreement: String
)

@Serializable
data class AgreementReceiptRepositories (
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: List<AgreementReceiptModel>,
)

@Serializable
data class AgreementRequestModel(
    val id: String,
    val client_id: String,
    val client_name: String,
    val content: AgreementContentModel,
    val requesting_info: AgreementModel,
    val agreement_method: AgreementMethod,
)

@Serializable
data class AgreementRequestResponseAPI(
    val has_error: Boolean,
    val error_message: String?,
    val req_id: String,
    val result: AgreementRequestModel?,
)
