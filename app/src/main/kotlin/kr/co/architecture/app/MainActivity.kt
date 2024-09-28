package kr.co.architecture.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.compositionLocalOf
import dagger.hilt.android.AndroidEntryPoint
import kr.co.architecture.app.ui.theme.BaseTheme
import kr.co.architecture.ui.home.HomeScreen



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseTheme {
                Scaffold(
                    bottomBar = {

                    }
                ) { innerPadding ->

                }
                HomeScreen()
            }
        }
    }
}
