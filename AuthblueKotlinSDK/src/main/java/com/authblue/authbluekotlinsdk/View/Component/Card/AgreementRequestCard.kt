package com.authblue.authbluekotlinsdk.View.Component.Card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authblue.authbluekotlinsdk.Helper.DateUtil
import com.authblue.authbluekotlinsdk.Model.AgreementModel
import com.authblue.authbluekotlinsdk.Model.AuthMethod
import com.authblue.authbluekotlinsdk.R


@Composable
fun AgreementRequestCard(
    client_name: String,
    clientId: String,
    user_name: String,
    methods: List<AuthMethod>,
    content: String,
    agreementModel: AgreementModel,
){
    Box(modifier = Modifier
        .shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp))
    ){

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
            {
                Column(horizontalAlignment = Alignment.Start){
                    Column{
                        Text(
                            text = context.getString(R.string.AgreementRequestCardFromTitle),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = client_name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                    Column(modifier = Modifier.padding(top=6.dp)){
                        Text(
                            text = context.getString(R.string.AgreementRequestCardToTitle),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Text(
                            text = user_name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Column(
                    modifier = Modifier.padding(start=64.dp, end=16.dp),
                ){
                    Column(

                    ){
                        Text(
                            text=context.getString(R.string.AgreementRequestCardAgreementMethodTitle),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Row() {
                            methods.map { method ->
                                when(method){
                                    AuthMethod.FACEID -> {
                                        Image(painter = painterResource(id = R.drawable.familiar_face_and_zone_48px),
                                            contentDescription = "familiar_face_and_zone_48px",
                                            contentScale = ContentScale.Fit,
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(20.dp)
                                        )
                                    }
                                    AuthMethod.MNC -> {
                                        Icon(
                                            painter = rememberVectorPainter(image = Icons.Outlined.CreditCard),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(4.dp)
                                        )
                                    }
                                    AuthMethod.SIGNATURE -> {
                                        Image(painter = painterResource(id = R.drawable.signature_48px),
                                            contentDescription = "signature_48px",
                                            contentScale = ContentScale.Fit,
                                            colorFilter = ColorFilter.tint(Color.Black),
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(20.dp)
                                        )
                                    }
                                }

                            }
                        }
//                        Row(){
//                            Image(painter = painterResource(id = R.drawable.familiar_face_and_zone_48px),
//                                contentDescription = "familiar_face_and_zone_48px",
//                                contentScale = ContentScale.Fit,
//                                colorFilter = ColorFilter.tint(Color.Black),
//                                modifier = Modifier
//                                    .padding(4.dp)
//                                    .size(20.dp)
//                            )
//                            Icon(
//                                painter = rememberVectorPainter(image = Icons.Outlined.CreditCard),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .padding(4.dp)
//                            )
//                            Image(painter = painterResource(id = R.drawable.signature_48px),
//                                contentDescription = "signature_48px",
//                                contentScale = ContentScale.Fit,
//                                colorFilter = ColorFilter.tint(Color.Black),
//                                modifier = Modifier
//                                    .padding(4.dp)
//                                    .size(20.dp)
//                            )
//                        }
                    }

                    Column(

                    ){
                        Text(
                            text=context.getString(R.string.AgreementRequestCardRequestingInfoTitle),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Row(){
                            if(agreementModel.name){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Sell),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            if(agreementModel.birthday){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Redeem),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            if(agreementModel.sex){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Favorite),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            if(agreementModel.address){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Home),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            if(agreementModel.phone){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Phone),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                            if(agreementModel.email){
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Outlined.Email),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(4.dp)
                                )
                            }
                        }
                    }

                }

            }
            Column(

                modifier = Modifier.padding(top=12.dp),

                ){
                Text(
                    text=context.getString(R.string.AgreementRequestCardContentTitle),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Text(
                    text=content,
                    modifier = Modifier.padding(top=4.dp),
                    lineHeight = 16.sp,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                )
            }


            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(top = 22.dp, bottom = 2.dp)
            ){
                Column{
                    Text(
                        text=context.getString(R.string.AgreementRequestCardDateTitle),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text= DateUtil().getNow(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }

            }
        }
    }
}
