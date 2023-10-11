package com.authblue.authbluekotlinsdk.Model

import kotlinx.serialization.Serializable

@Serializable
data class AgreementModel(
    var name: Boolean = false,
    var birthday: Boolean =false,
    var age: Boolean = false,
    var picture: Boolean = false,
    var sex: Boolean = false,
    var address: Boolean = false,
    var phone: Boolean = false,
    var email: Boolean = false,
)

@Serializable
data class RequestingInfo(
    var name: String? = null,
    var birthday: String? = null,
    var age: String? = null,
    var picture: String? = null,
    var sex: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var email: String? = null
)

@Serializable
data class AgreementMethod(
    var face_id: Boolean = false,
    var my_number_card: Boolean = false,
    var signature: Boolean = false,
)
