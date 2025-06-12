package com.boxbox.simon
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.boxbox.simon.ui.theme.SIMONTheme
import com.boxbox.simon.views.Screen
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setLanguage(this)

        setContent {
            SIMONTheme {

                // entry point of the application
                Screen()
            }

        }
    }
}

fun setLanguage(context: Context) {
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    // Retrieve saved language code, defaulting to "it"
    val savedLang = sharedPref.getString("language", "it") ?: "it"

    // Apply the language
    val locale = Locale(savedLang)
    Locale.setDefault(locale)

    // Update app resources with the new locale
    val resources = context.resources
    val config = resources.configuration
    config.setLocale(locale)

    // Apply configuration changes to the app
    resources.updateConfiguration(config, resources.displayMetrics)
}
