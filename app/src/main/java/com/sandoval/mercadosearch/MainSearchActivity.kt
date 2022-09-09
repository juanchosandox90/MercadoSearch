package com.sandoval.mercadosearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sandoval.mercadosearch.ui.search_products.screens.MercadoSearchNavigation
import com.sandoval.mercadosearch.ui.theme.MercadoSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MercadoSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MercadoSearchNavigation()
                }
            }
        }
    }
}