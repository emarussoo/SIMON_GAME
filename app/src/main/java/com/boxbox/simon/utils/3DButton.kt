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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.platform.LocalContext
import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb



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

//classe usata per il tipo di prospettiva
sealed class Perspective {
    data class Left(
        val bottomEdgeColor: Color,
        val rightEdgeColor: Color
    ) : Perspective()

    data class Right(
        val topEdgeColor: Color,
        val leftEdgeColor: Color
    ) : Perspective()

    data class Top(
        val bottomEdgeColor: Color
    ) : Perspective()
}

//funzione di base per fare il bottone 3D, dentro gli passo la onClick per far funzionare il gameplay, non toccare niente qui
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeDimensionalLayout(onClick: () -> Unit,
                           perspective: Perspective = Perspective.Left(bottomEdgeColor = Color.Black, rightEdgeColor = Color.Black),
                           edgeOffset: Dp = 10.dp,
                           sound: Int,
                           isEnabled: Boolean,
                           content: @Composable () -> Unit

) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val offsetInPx = with(LocalDensity.current) { edgeOffset.toPx() }

    val elevationOffset by remember {
        derivedStateOf {
            if (isPressed) {
                when (perspective) {
                    is Perspective.Left -> IntOffset(offsetInPx.toInt(), offsetInPx.toInt())
                    is Perspective.Right -> IntOffset(-offsetInPx.toInt(), -offsetInPx.toInt())
                    is Perspective.Top -> IntOffset(0, offsetInPx.toInt())
                }
            } else {
                IntOffset.Zero
            }
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
                    playSound(sound,context)
                },
                enabled = isEnabled
            )
            .graphicsLayer {
                rotationX = if (perspective is Perspective.Top) 16f else 0f
            }
            .drawBehind {
                if (isPressed.not()) {
                    when (perspective) {
                        is Perspective.Left -> {
                            val rightEdge = Path().apply {
                                moveTo(size.width, 0f)
                                lineTo(size.width + offsetInPx, offsetInPx)
                                lineTo(size.width + offsetInPx, size.height + offsetInPx)
                                lineTo(size.width, size.height)
                                close()
                            }
                            val bottomEdge = Path().apply {
                                moveTo(size.width, size.height)
                                lineTo(size.width + offsetInPx, size.height + offsetInPx)
                                lineTo(offsetInPx, size.height + offsetInPx)
                                lineTo(0f, size.height)
                                close()
                            }
                            drawPath(rightEdge, perspective.rightEdgeColor, style = Fill)
                            drawPath(bottomEdge, perspective.bottomEdgeColor, style = Fill)
                        }

                        is Perspective.Top -> {
                            val bottomEdge = Path().apply {
                                moveTo(0f, size.height)
                                lineTo(size.width, size.height)
                                lineTo(size.width - offsetInPx, size.height + offsetInPx)
                                lineTo(offsetInPx, size.height + offsetInPx)
                                close()
                            }
                            drawPath(bottomEdge, perspective.bottomEdgeColor, style = Fill)
                        }

                        is Perspective.Right -> {
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
                            drawPath(topEdge, perspective.topEdgeColor, style = Fill)
                            drawPath(leftEdge, perspective.leftEdgeColor, style = Fill)
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.offset { elevationOffset },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

//funzione di supporto per la scelta della prospettiva
fun getPerspective(type: String, baseColor: Color): Perspective {
    //qui ci vanno i colori dei bordi
    return when (type) {
        "left" -> Perspective.Left(
            bottomEdgeColor = baseColor.lighter(),
            rightEdgeColor = baseColor.darker()
        )
        "right" -> Perspective.Right(
            topEdgeColor = baseColor.lighter(),
            leftEdgeColor = baseColor.darker()
        )
        "top" -> Perspective.Top(
            bottomEdgeColor = baseColor.darker()
        )
        else -> Perspective.Left( // fallback di default
            bottomEdgeColor = baseColor.lighter(),
            rightEdgeColor = baseColor.darker()
        )
    }
}

//tasto effettivo 3d
/*
parametri:
baseColor = colore di base a cui poi vengono applicate le varie sfumature per effetto 3d
onClick() = funzione della roba di emananuele per far funzionare il gioco
prospettivaScelta = può essere "left","right","top"
height = altezza del tasto
*/
@Composable
fun ThreeDButton(baseColor: Color, onClick: () -> Unit, prospettivaScelta: String, height: Int, sound: Int,highlighted: Boolean, buttonSize: Dp, isEnabled: Boolean) {
    //altezza del tasto
    val edgeOffset = height.dp
    // Colori per sfumatura parte sopra del tasto:
    val topColor = baseColor.copy(alpha = 0.8f).lighter()
    val bottomColor = baseColor.copy(alpha = 0.8f).darker()
    val prospettiva = getPerspective(prospettivaScelta, baseColor.darker())



    ThreeDimensionalLayout(
        onClick,
        prospettiva,
        edgeOffset,
        sound,
        isEnabled
    ) {
        //questo è il box a cui viene applicato l'effetto 3D
        Box(
            modifier = Modifier
                .size(buttonSize)
                .border(BorderStroke(1.dp, Color.Black))
                .background(
                    //qui ci va il colore della parte sopra del tasto
                    //brush = linearGradient(colors = listOf(topColor,bottomColor ))
                    color = baseColor
                    )
                ,
            contentAlignment = Alignment.Center
        ) {
            if(highlighted) GlowingCard(modifier = Modifier.size(buttonSize), glowingColor = baseColor, containerColor = Color.White, glowingRadius = (buttonSize-10.dp)){}


            }
        }
    }


@Composable
fun GlowingCard(
    glowingColor: Color,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    cornersRadius: Dp = 0.dp,
    glowingRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .drawBehind {
                val canvasSize = size
                // Gradient radiale centrato con raggio uguale a glowingRadius per simulare luce interna
                val gradient = Brush.radialGradient(
                    colors = listOf(
                        glowingColor.copy(alpha = 0.6f),
                        containerColor.copy(alpha = 0f)
                    ),
                    center = Offset(canvasSize.width / 2, canvasSize.height / 2),
                    radius = glowingRadius.toPx()
                )
                // Disegna prima il rettangolo di sfondo normale
                drawRoundRect(
                    color = containerColor,
                    size = canvasSize,
                    cornerRadius = CornerRadius(cornersRadius.toPx())
                )
                // Poi disegna sopra il gradiente radiale (bagliore interno)
                drawRoundRect(
                    brush = gradient,
                    size = canvasSize,
                    cornerRadius = CornerRadius(cornersRadius.toPx())
                )
            }
    ) {
        content()
    }
}


fun Color.lighter(factor: Float = 0.3f): Color {
    // Schiarisce portando il colore verso il bianco
    val r = (this.red + (1 - this.red) * factor).coerceIn(0f, 1f)
    val g = (this.green + (1 - this.green) * factor).coerceIn(0f, 1f)
    val b = (this.blue + (1 - this.blue) * factor).coerceIn(0f, 1f)

    // Aumenta leggermente la saturazione agendo sulla differenza tra canali
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val diff = max - min
    val saturationBoost = 0.1f

    val newR = (r + (r - min) / diff * saturationBoost).coerceIn(0f, 1f)
    val newG = (g + (g - min) / diff * saturationBoost).coerceIn(0f, 1f)
    val newB = (b + (b - min) / diff * saturationBoost).coerceIn(0f, 1f)

    return Color(newR, newG, newB, this.alpha)
}

fun Color.darker(factor: Float = 0.5f): Color {
    // Oscura i colori verso nero, mantenendo saturazione alta
    val r = (this.red * (1 - factor)).coerceIn(0f, 1f)
    val g = (this.green * (1 - factor)).coerceIn(0f, 1f)
    val b = (this.blue * (1 - factor)).coerceIn(0f, 1f)

    // Piccolo boost di contrasto per mantenere accesi i colori scuri
    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val diff = max - min
    val saturationBoost = 0.05f

    val newR = (r + (r - min) / diff * saturationBoost).coerceIn(0f, 1f)
    val newG = (g + (g - min) / diff * saturationBoost).coerceIn(0f, 1f)
    val newB = (b + (b - min) / diff * saturationBoost).coerceIn(0f, 1f)

    return Color(newR, newG, newB, this.alpha)
}