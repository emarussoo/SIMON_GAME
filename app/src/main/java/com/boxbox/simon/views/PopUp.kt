package com.boxbox.simon.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.boxbox.simon.model.GamePhase
import com.boxbox.simon.viewmodel.SimonViewModel
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Text
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.boxbox.simon.ui.theme.ThemeManager

@Composable
fun EndPopUp(
    context: Context,
    viewModel: SimonViewModel,
    onDismiss: () -> Unit = {}
) {
    val theme = ThemeManager.currentTheme
    val state = viewModel.gameState.collectAsState().value

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(350.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = theme.backgroundPopup,
            border = BorderStroke(3.dp, theme.borderPopup)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GAME OVER",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = theme.titleColor,
                    fontFamily = theme.chosenFont
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = theme.popupIcon),
                        contentDescription = "Icona Mario",
                        modifier = Modifier.size(72.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = "Score: ${state.score}",
                        fontSize = 18.sp,
                        color = theme.scoreColor,
                        modifier = Modifier.padding(top = 4.dp, bottom = 3.dp),
                        fontFamily = theme.chosenFont
                    )

                    Text(
                        text = "Difficulty: ${state.difficulty}",
                        fontSize = 18.sp,
                        color = theme.difficultyColor,
                        fontFamily = theme.chosenFont
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.buttonBackground,
                        contentColor = theme.buttonTextColor
                    )
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
