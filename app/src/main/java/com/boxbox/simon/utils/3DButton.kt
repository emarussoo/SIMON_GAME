package com.boxbox.simon.utils

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Box(
        modifier = Modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedBack.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
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
            bottomEdgeColor = Color.Black,
            rightEdgeColor = Color.Black
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
fun ThreeDButton(baseColor: Color, onClick: () -> Unit, prospettivaScelta: String, height: Int) {
    //altezza del tasto
    val edgeOffset = height.dp
    // Colori per sfumatura parte sopra del tasto:
    val topColor = baseColor.copy(alpha = 0.8f).lighter()
    val bottomColor = baseColor.copy(alpha = 0.8f).darker()
    //prospettiva scelta dall'utente
    val prospettiva = getPerspective(prospettivaScelta,baseColor)

    ThreeDimensionalLayout(
        onClick,
        prospettiva,
        edgeOffset
    ) {
        //questo è il box a cui viene applicato l'effetto 3D
        Box(
            modifier = Modifier
                .size(150.dp)
                .border(BorderStroke(1.dp, baseColor))
                .background(
                    //qui ci va il colore della parte sopra del tasto
                    brush = Brush.verticalGradient(colors = listOf(topColor, bottomColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

// Funzioni di utilità per schiarire/scurire colore
fun Color.lighter(factor: Float = 0.2f): Color {
    return Color(
        red = (red + factor).coerceIn(0f, 1f),
        green = (green + factor).coerceIn(0f, 1f),
        blue = (blue + factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun Color.darker(factor: Float = 0.2f): Color {
    return Color(
        red = (red - factor).coerceIn(0f, 1f),
        green = (green - factor).coerceIn(0f, 1f),
        blue = (blue - factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}