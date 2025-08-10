package com.tina.timerzeer.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tina.timerzeer.app.AppNavHost
import com.tina.timerzeer.core.theme.TimerzeerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimerzeerTheme {
                AppNavHost()
            }
        }
    }
}