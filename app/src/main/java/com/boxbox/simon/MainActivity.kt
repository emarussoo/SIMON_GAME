package com.boxbox.simon
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.boxbox.simon.ui.theme.SIMONTheme
import com.boxbox.simon.views.screen
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setLanguage(this)

        setContent {
            SIMONTheme {
                screen()
            }

        }
    }
}

fun setLanguage(context: Context) {
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    //Sets language
    val savedLang = sharedPref.getString("language", "it") ?: "it"
    val locale = Locale(savedLang)
    Locale.setDefault(locale)
    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}
