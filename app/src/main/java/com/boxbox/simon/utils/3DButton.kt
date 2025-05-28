package com.boxbox.simon.utils

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import com.boxbox.simon.ui.theme.ThemeManager


//usata per il suono
private var currentMediaPlayer: MediaPlayer? = null
fun playSound(soundResId: Int, context: Context) {
    currentMediaPlayer?.let {
        if (it.isPlaying) {
            it.stop()
        }
        it.release()
    }

    // Crea e avvia il nuovo suono
    currentMediaPlayer = MediaPlayer.create(context, soundResId).apply {
        setOnCompletionListener {
            release()
            currentMediaPlayer = null
        }
        start()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeDButton(
    baseColor: Color,
    onClick: () -> Unit,
    height: Int,
    sound: Int,
    highlighted: Boolean,
    buttonSize: Dp,
    isEnabled: Boolean
) {
    val edgeOffset = height.dp

    //val highlightedColor = Brush.linearGradient(listOf(topColor, bottomColor))
    //val highlightedColor = Brush.linearGradient(listOf(ThemeManager.currentTheme.Red, ThemeManager.currentTheme.Blue,ThemeManager.currentTheme.Green))
    val highlightedColor = baseColor.lighter(0.5f)


    val topEdgeColor = baseColor.lighter()
    val leftEdgeColor = baseColor.darker()

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val offsetInPx = with(LocalDensity.current) { edgeOffset.toPx() }

    val elevationOffset by remember {
        derivedStateOf {
            if (isPressed) IntOffset(-offsetInPx.toInt(), -offsetInPx.toInt())
            else IntOffset.Zero
        }
    }

    val hapticFeedBack = LocalHapticFeedback.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedBack.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                    playSound(sound, context)
                },
                enabled = isEnabled
            )
            .drawBehind {
                if (!isPressed) {
                    val topEdge = Path().apply {
                        moveTo(-offsetInPx, -offsetInPx)
                        lineTo(size.width - offsetInPx, -offsetInPx)
                        lineTo(size.width, 0f)
                        lineTo(0f, 0f)
                        close()
                    }

                    val leftEdge = Path().apply {
                        moveTo(-offsetInPx, -offsetInPx)
                        lineTo(0f, 0f)
                        lineTo(0f, size.height)
                        lineTo(-offsetInPx, size.height - offsetInPx)
                        close()
                    }

                    drawPath(topEdge, if (highlighted) highlightedColor else topEdgeColor, style = Fill)
                    drawPath(topEdge, Color.Black, style = Stroke(width = 0.5.dp.toPx()))

                    drawPath(leftEdge, if (highlighted) highlightedColor else leftEdgeColor, style = Fill)
                    drawPath(leftEdge, Color.Black, style = Stroke(width = 0.5.dp.toPx()))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { elevationOffset }
                .size(buttonSize)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val color = Color.Black

                    // Bordo sotto
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )

                    // Bordo a destra
                    drawLine(
                        color = color,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                .background(if (highlighted) highlightedColor else baseColor),
            contentAlignment = Alignment.Center
        )
        {

            }
        }
    }


fun Color.lighter(factor: Float = 0.4f): Color {
    return Color(
        red = (red + (1f - red) * factor).coerceIn(0f, 1f),
        green = (green + (1f - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun Color.darker(factor: Float = 0.4f): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}