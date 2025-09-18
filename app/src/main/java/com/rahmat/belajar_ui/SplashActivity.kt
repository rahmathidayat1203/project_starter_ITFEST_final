package com.rahmat.belajar_ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.rahmat.belajar_ui.ui.splash.SplashScreen
import com.rahmat.belajar_ui.ui.theme.Belajar_uiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Belajar_uiTheme {
                SplashScreen()
            }

            LaunchedEffect(key1 = true) {
                delay(3000) // Delay for 3 seconds
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}