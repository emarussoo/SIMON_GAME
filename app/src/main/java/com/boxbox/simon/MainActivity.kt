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

        // Imposta la lingua salvata
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedLang = sharedPref.getString("language", "it") ?: "it"
        val locale = Locale(savedLang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        // Questo è ciò che effettivamente cambia la lingua visualizzata
        resources.updateConfiguration(config, resources.displayMetrics)

        setContent {
            SIMONTheme {
                screen()
            }
        }
    }
}
