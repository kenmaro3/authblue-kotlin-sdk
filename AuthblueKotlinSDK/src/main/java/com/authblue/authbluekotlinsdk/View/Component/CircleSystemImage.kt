package com.authblue.authbluekotlinsdk.View.Component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.authblue.authbluekotlinsdk.Model.AuthMethod
import com.authblue.authbluekotlinsdk.R

@Composable
fun CircleSystemImage(hex: Long, isStroke: Boolean, authMethod: AuthMethod){
    Box(
    ) {
        val size = 48.dp
        val boxSize = 72.dp
        if(hex != -1.toLong()){
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(hex))
                    .align(Alignment.Center)
            )
        }else{
            Box(
                modifier = Modifier
                    .size(boxSize)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .align(Alignment.Center)
            )
        }

        when(authMethod){
            AuthMethod.FACEID -> {
                Image(painter = painterResource(id = R.drawable.familiar_face_and_zone_48px),
                    contentDescription = "familiar_face_and_zone_48px",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .align(Alignment.Center)
                )
            }
            AuthMethod.MNC -> {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Outlined.CreditCard),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .align(Alignment.Center)
                )
            }
            AuthMethod.SIGNATURE -> {
                Image(painter = painterResource(id = R.drawable.signature_48px),
                    contentDescription = "signature_48px",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(size)
                        .align(Alignment.Center)
                )
            }
        }
    }
}