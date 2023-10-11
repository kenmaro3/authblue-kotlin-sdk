package com.authblue.authbluekotlinsdk.View.Component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.authblue.authbluekotlinsdk.Model.AuthMethod
import com.authblue.authbluekotlinsdk.Model.AuthMethodState
import com.authblue.authbluekotlinsdk.View.Component.CircleSystemImage
import com.authblue.authbluekotlinsdk.R

@Composable
fun AgreementStepRow(index: Int, method: AuthMethod, state: AuthMethodState){

    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(state){
            AuthMethodState.DONE -> {
                CircleSystemImage(hex = state.content, isStroke = false, authMethod = method)
            }
            AuthMethodState.CURRENT -> {
                CircleSystemImage(hex = state.content, isStroke = true, authMethod = method)
            }
            AuthMethodState.YET -> {
                CircleSystemImage(hex = state.content, isStroke = true, authMethod = method)
            }
        }
        Column(
            modifier = Modifier.padding(start=64.dp, end=16.dp),
        ){
            Text(
                text="${context.getString(R.string.DeepLinkAgreementStepsStepLabel)} ${index}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            var showString: String = ""
            when(method){
                AuthMethod.FACEID -> {
                    showString = context.getString(R.string.DeepLinkAgreementStepsFaceIDLabel)
                }
                AuthMethod.MNC -> {
                    showString = context.getString(R.string.DeepLinkAgreementStepsMNCLabel)
                }
                AuthMethod.SIGNATURE -> {
                    showString = context.getString(R.string.DeepLinkAgreementStepsSignatureLabel)
                }
            }
            Text(
                text=showString,
                modifier = Modifier.padding(top=4.dp),
                lineHeight = 16.sp,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }

}