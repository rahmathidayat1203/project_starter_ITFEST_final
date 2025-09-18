
package com.rahmat.belajar_ui.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.rahmat.belajar_ui.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Uncomment the line below to use a PNG image instead of the vector drawable
        // Image(painter = painterResource(id = R.drawable.splash_image), contentDescription = "Splash Screen Image")

        Image(
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = "Splash Screen Image"
        )
    }
}
