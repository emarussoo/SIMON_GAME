package com.boxbox.simon.button

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import android.media.MediaPlayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.drawscope.Stroke


// Holds a reference to the current media player instance
private var currentMediaPlayer: MediaPlayer? = null

fun playSound(soundResId: Int, context: Context) {

    // Stop and release any existing media player instance
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    currentMediaPlayer?.let {
        if (it.isPlaying) it.stop()
        it.release()
    }

    // Create a new MediaPlayer and start playing the sound if enabled
    currentMediaPlayer = MediaPlayer.create(context, soundResId).apply {
        setOnCompletionListener {
            release()
            currentMediaPlayer = null
        }

        //check if sound is enabled
        if(sharedPref.getBoolean("soundsOn",true)) start()
    }
}


/*
 This 3D button implementation is a customized and adapted version inspired by Nikhil's article:
 https://medium.com/@nikhil.here/creating-three-dimensional-views-with-jetpack-compose-930c62ea2f8
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreeDButton(
    baseColor: Color, // color of the button
    onClick: () -> Unit, // Function used for gameplay purposes
    height: Int, // height of the button
    sound: Int, // Sounds triggered on button press
    highlighted: Boolean, // Used to differentiate when the button is pressed during the playback of the sequence
    buttonSize: Dp, // size of the button
    isEnabled: Boolean // used to disable the button during the playback sequence
) {
    val edgeOffset = height.dp
    val context = LocalContext.current
    val highlightedColor = baseColor.lighter(0.5f)

    // Colors used for 3D top and left edges (lighter and darker variations)
    val topEdgeColor = baseColor.lighter()
    val leftEdgeColor = baseColor.darker()

    // Interaction source to detect button press state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Convert dp offset to pixels for drawing paths and animations
    val offsetInPx = with(LocalDensity.current) { edgeOffset.toPx() }

    // Calculate elevation offset to simulate button press (moves button visually when pressed)
    val elevationOffset by remember {
        derivedStateOf {
            if (isPressed) IntOffset(-offsetInPx.toInt(), -offsetInPx.toInt())
            else IntOffset.Zero
        }
    }

    // plays the sound during the playback of the sequence
    if(highlighted) playSound(sound, context)

    Box(
        modifier = Modifier
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick()
                    playSound(sound, context)
                },
                enabled = isEnabled
            )
            // Draw the 3D top and left edges only when not pressed
            .drawBehind {
                if (!isPressed) {

                    // Define path for the top edge border
                    val topEdge = Path().apply {
                        moveTo(-offsetInPx, -offsetInPx)
                        lineTo(size.width - offsetInPx, -offsetInPx)
                        lineTo(size.width, 0f)
                        lineTo(0f, 0f)
                        close()
                    }

                    // Define path for the left edge border
                    val leftEdge = Path().apply {
                        moveTo(-offsetInPx, -offsetInPx)
                        lineTo(0f, 0f)
                        lineTo(0f, size.height)
                        lineTo(-offsetInPx, size.height - offsetInPx)
                        close()
                    }

                    // Draw top edge with either highlighted color or normal lighter color
                    drawPath(topEdge, if (highlighted) highlightedColor else topEdgeColor, style = Fill)
                    // Draw a thin black stroke to give the edge more definition
                    drawPath(topEdge, Color.Black, style = Stroke(width = 0.5.dp.toPx()))
                    // Draw left edge with either highlighted color or normal darker color
                    drawPath(leftEdge, if (highlighted) highlightedColor else leftEdgeColor, style = Fill)
                    // Draw a thin black stroke on the left edge
                    drawPath(leftEdge, Color.Black, style = Stroke(width = 0.5.dp.toPx()))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                // Move the inner box to simulate the press "push" effect
                .offset { elevationOffset }
                .size(buttonSize)
                // Draw bottom and right black borders to complete the 3D effect
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val color = Color.Black

                    //Bottom border
                    drawLine(
                        color = color,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )

                    //Right border
                    drawLine(
                        color = color,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
                // Set the background color to highlighted or base color
                .background(if (highlighted) highlightedColor else baseColor),
            contentAlignment = Alignment.Center
        )
        {

            }
        }
    }


//A function used to lighten a color
fun Color.lighter(factor: Float = 0.4f): Color {
    return Color(
        red = (red + (1f - red) * factor).coerceIn(0f, 1f),
        green = (green + (1f - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

//Used to darken a color
fun Color.darker(factor: Float = 0.4f): Color {
    return Color(
        red = (red * (1f - factor)).coerceIn(0f, 1f),
        green = (green * (1f - factor)).coerceIn(0f, 1f),
        blue = (blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}