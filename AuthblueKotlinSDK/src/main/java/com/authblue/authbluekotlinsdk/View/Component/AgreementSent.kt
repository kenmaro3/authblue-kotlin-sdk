package com.authblue.authbluekotlinsdk.View.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authblue.authbluekotlinsdk.R

@Composable
fun AgreementSent(
    callback: () -> Unit
){
    val context = LocalContext.current
    Column(
    ) {
        Box(){
            Image(
                painter = painterResource(id = R.drawable.team_success__flatline),
                contentDescription = "team_success_flatline",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 12.dp,
                start=14.dp,
                end=14.dp
            )
        ){
            Text(
                text=context.getString(R.string.DeepLinkAgreementSentTitle),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text=context.getString(R.string.DeepLinkAgreementSentSubTitle),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ){
                CustomButton(
                    text = context.getString(R.string.DeepLinkAgreementSentGoBackLabel),
                    callback = callback
                )
            }
        }
    }
}

