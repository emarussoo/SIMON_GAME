package com.boxbox.simon.views

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R
import com.boxbox.simon.ui.theme.ThemeManager
import java.util.Locale

@Composable
fun settingInterface(){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var language by remember { mutableStateOf(sharedPref.getString("language", "it") ?: "it")  }
    var sounds by remember { mutableStateOf(sharedPref.getBoolean("soundsOn", true)) }
    // ,false è il parametro di default se ancora non è stato messo nelle shared pref is3D
    var bttnStyle by remember { mutableStateOf(if (sharedPref.getBoolean("is3D", false)) "3D" else "Flat") }
    var theme by remember { mutableStateOf(sharedPref.getString("theme", "mario") ?: "mario") }

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp).background(Color.Transparent)
    ) {

        Text(stringResource(R.string.settings), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Text(stringResource(R.string.language))
            Row {
                listOf(
                    "Italiano" to "it",
                    "English" to "en",
                ).forEach { (label, langCode) ->
                    Button(
                        onClick = {
                            language = langCode

                            sharedPref.edit().putString("language", langCode).apply()

                            val locale = Locale(langCode)
                            Locale.setDefault(locale)
                            val config = context.resources.configuration
                            config.setLocale(locale)
                            context.resources.updateConfiguration(
                                config,
                                context.resources.displayMetrics
                            )

                            (context as? Activity)?.recreate()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (language == langCode) ThemeManager.currentTheme.settingsColor else Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Music toggle
            Text(stringResource(R.string.sounds))
            Row {
                listOf("On" to true, "Off" to false).forEach { (label, value) ->
                    Button(
                        onClick = {
                            sounds = value
                            sharedPref.edit().putBoolean("soundsOn", value).apply()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (sounds == value) ThemeManager.currentTheme.settingsColor else Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(label)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.button_style))
            Row {
                listOf("3D", "Flat").forEach { option ->
                    Button(
                        onClick = {
                            bttnStyle = option
                            if (option == "3D") {
                                sharedPref.edit().putBoolean("is3D", true).apply()
                            } else {
                                sharedPref.edit().putBoolean("is3D", false).apply()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (bttnStyle == option) ThemeManager.currentTheme.settingsColor else Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(option)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.themes))
            Row {
                listOf("mario", "neon", "ScottMcTominay").forEach { option ->
                    Button(
                        onClick = {
                            theme = option
                            sharedPref.edit().putString("theme", option).apply()
                            if (option.equals("mario")) ThemeManager.switchTo1()
                            else if (option.equals("neon")) ThemeManager.switchTo2()
                            else ThemeManager.switchTo3()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (theme == option) ThemeManager.currentTheme.settingsColor else Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}