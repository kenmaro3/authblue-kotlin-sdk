package com.authblue.authbluekotlinsdk.View.Component


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authblue.authbluekotlinsdk.Model.AgreementMethod
import com.authblue.authbluekotlinsdk.Model.AgreementModel
import com.authblue.authbluekotlinsdk.R

@Composable
fun ReceiptDetailScreen(
    clientName: String,
    refCode: String,
    date: String,
    agreementMethod: AgreementMethod,
    requesting_info: AgreementModel,
    content: String,
){
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 80.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp))
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Text(
                text=context.getString(R.string.AgreementDetailModalTitle),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
            )

            Column(
                modifier = Modifier
                    .padding(top=10.dp)
            ){
                Text(
                    text=context.getString(R.string.AgreementDetailModalHeaderBasic),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color=Color.Gray,
                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                )

                Column(){
                    Row(){
                        Text(
                            text=context.getString(R.string.AgreementDetailModalRefIdLabel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text=refCode,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )

                    }
                    Row(){
                        Text(
                            text=context.getString(R.string.AgreementDetailModalClientNameLabel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text=clientName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )
                    }

                    Row(){
                        Text(
                            text=context.getString(R.string.AgreementDetailModalUserNameLabel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text="",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )

                    }
                    Row(){
                        Text(
                            text=context.getString(R.string.AgreementDetailModalDateLabel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text=date,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                        )

                    }

                }

                Column(
                    modifier = Modifier
                        .padding(top=10.dp)
                ){

                    Text(
                        text=context.getString(R.string.AgreementDetailModalHeaderAgreementMethod),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color=Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                    )

                    Column(){

                        if(agreementMethod.face_id){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalAgreementMethodFaceIdLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )

                            }
                        }

                        if(agreementMethod.my_number_card){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalAgreementMethodMncLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(agreementMethod.signature){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalAgreementMethodSignatureLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                    }
                }


                Column(
                    modifier = Modifier
                        .padding(top=10.dp)
                ){

                    Text(
                        text=context.getString(R.string.AgreementDetailModalHeaderRequestingInfo),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color=Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                    )

                    Column(){

                        if(requesting_info.name){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoNameLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )

                            }
                        }

                        if(requesting_info.birthday){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoBirthdayLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(requesting_info.age){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoAgeLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(requesting_info.sex){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoSexLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(requesting_info.address){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoAddressLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(requesting_info.email){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoEmailLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }

                        if(requesting_info.phone){
                            Row(){
                                Text(
                                    text=context.getString(R.string.AgreementDetailModalRequestingInfoPhoneLabel),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(top=10.dp)
                        .padding(bottom=20.dp)

                ) {

                    Text(
                        text = context.getString(R.string.AgreementDetailModalHeaderContent),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                    )

                    Text(
                        text=content,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 6.dp, top = 6.dp)
                    )
                }
            }

        }
    }
}