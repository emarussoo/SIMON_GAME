package com.boxbox.simon.ui.theme

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import com.boxbox.simon.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SIMONTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val savedTheme = sharedPref.getString("theme", "Standard") ?: "Standard"

    when (savedTheme) {
        "IdraulicoIT" -> ThemeManager.switchTo1()
        "Standard" -> ThemeManager.switchTo2()
        else -> ThemeManager.switchTo3()
    }

    val darkTheme = if (ThemeManager.currentTheme.forceLightTheme) {
        false // forza il tema chiaro
    } else {
        isSystemInDarkTheme() // lascia al sistema
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

open class theme {
    //tema chiaro/scuro true = tema chiaro, false = tema scuro
    open val forceLightTheme: Boolean = false

    //immagini
    open val title: Int = 0
    open val cup: Int = 0
    open val joystick: Int = 0
    open val settings: Int = 0
    open val help: Int = 0
    open val quit: Int = 0

    //colori
    open val Red: Color = Color(0xffe71e07)
    open val Blue: Color = Color(0xff42b033)
    open val Green: Color = Color(0xff019dda)
    open val Yellow: Color = Color(0xfffcd000)

    //suoni
    open val click1Sound: Int = 0
    open val click2Sound: Int = 0
    open val click3Sound: Int = 0
    open val click4Sound: Int = 0
    open val loseSound: Int = 0
    open val winSound: Int = 0

    open val chosenFont: FontFamily = FontFamily(Font(R.font.supermario))

    //Decorazione PopUp
    open val backgroundPopup: Color = Color.DarkGray
    open val borderPopup: Color = Color.Yellow
    open val titleColor: Color = Color.White
    open val scoreColor: Color = Color.White
    open val difficultyColor: Color = Color.White
    open val buttonBackground: Color = Color.Yellow
    open val buttonTextColor: Color = Color.Black
    open val popupIcon: Int = R.drawable.mariostrar
}

class theme2 : theme() {
    override val forceLightTheme = true

    override val title: Int = R.drawable.title1
    override val cup: Int = R.drawable.arcade_cup
    override val joystick: Int = R.drawable.arcade_joystick
    override val settings: Int = R.drawable.arcade_settings
    override val help: Int = R.drawable.arcade_help
    override val quit: Int = R.drawable.arcade_poweron

    override val Red: Color = Color(0xffe71e07)
    override val Blue: Color = Color(0xff42b033)
    override val Green: Color = Color(0xff019dda)
    override val Yellow: Color = Color(0xfffcd000)

    override val click1Sound: Int = R.raw.mario_click1
    override val click2Sound: Int = R.raw.mario_click2
    override val click3Sound: Int = R.raw.mario_click3
    override val click4Sound: Int = R.raw.mario_click4
    override val loseSound: Int = R.raw.mario_lose
    override val winSound: Int = R.raw.mario_win

}

class theme1 : theme() {
    override val forceLightTheme = true

    override val title: Int = R.drawable.title2
    override val cup: Int = R.drawable.cup_mario
    override val joystick: Int = R.drawable.play_mario
    override val settings: Int = R.drawable.mario_settings
    override val help: Int = R.drawable.arcade_help
    override val quit: Int = R.drawable.mario_poweron

    override val Red: Color = Color(0xffe71e07)
    override val Blue: Color = Color(0xff42b033)
    override val Green: Color = Color(0xff019dda)
    override val Yellow: Color = Color(0xfffcd000)

    override val chosenFont: FontFamily = FontFamily(Font(R.font.supermario))

    override val backgroundPopup = Color(0xFFdb4b3f)
    override val borderPopup = Color(0xFFffd966)
    override val titleColor = Color.White
    override val scoreColor = Color(0xFFffd966)
    override val difficultyColor = Color.White
    override val buttonBackground = Color(0xFFffd966)
    override val buttonTextColor = Color.Black
    //override val popupIcon = R.drawable.mario_star
    override val click1Sound: Int = R.raw.mario_click1
    override val click2Sound: Int = R.raw.mario_click2
    override val click3Sound: Int = R.raw.mario_click3
    override val click4Sound: Int = R.raw.mario_click4
    override val loseSound: Int = R.raw.mario_lose
    override val winSound: Int = R.raw.mario_win

}

class theme3 : theme() {
    override val forceLightTheme = true

    override val title: Int = R.drawable.title2
    override val cup: Int = R.drawable.cup_mario
    override val joystick: Int = R.drawable.play_mario
    override val settings: Int = R.drawable.mario_settings
    override val help: Int = R.drawable.arcade_help
    override val quit: Int = R.drawable.mario_poweron

    override val Red: Color = Color(0xffe71e07)
    override val Blue: Color = Color(0xff42b033)
    override val Green: Color = Color(0xff019dda)
    override val Yellow: Color = Color(0xfffcd000)

    override val click1Sound: Int = R.raw.mario_click1
    override val click2Sound: Int = R.raw.mario_click2
    override val click3Sound: Int = R.raw.mario_click3
    override val click4Sound: Int = R.raw.mario_click4
    override val loseSound: Int = R.raw.mario_lose
    override val winSound: Int = R.raw.mario_win
}


object ThemeManager {
    var currentTheme: theme by mutableStateOf(theme2())

    fun switchTo1() {
        currentTheme = theme1()
    }

    fun switchTo2() {
        currentTheme = theme2()
    }

    fun switchTo3() {
        currentTheme = theme3()
    }
}