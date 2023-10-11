package com.authblue.authbluekotlinsdk.View.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.authblue.authbluekotlinsdk.R
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource

@Composable
fun AnimatedCompleteMark(
    visible: Boolean,
    onComplete: () -> Unit
) {
    val animatedVisible = remember { mutableStateOf(visible) }

    LaunchedEffect(visible) {
        animatedVisible.value = visible
    }

    Crossfade(
        targetState = animatedVisible.value,
        modifier = Modifier.size(48.dp)
    ) { isVisible ->
        if (isVisible) {
            Image(
                painter = painterResource(R.drawable.baseline_check_circle_outline_24),
                contentDescription = "Complete Mark",
                modifier = Modifier
                    .size(48.dp)
                    .alpha(0.5f)
                    .then(Modifier.animateContentSize()) // Optional: animate size change
                    .then(Modifier.padding(8.dp))
            ) // Optional: Apply any additional transformations or animations

            // Call onComplete() when the animation completes
            DisposableEffect(isVisible) {
                if (!isVisible) {
                    onComplete()
                }
                onDispose { }
            }
        }
    }
}