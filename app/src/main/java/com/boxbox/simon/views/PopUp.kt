package com.boxbox.simon.views

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.boxbox.simon.R
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.boxbox.simon.viewmodel.SimonViewModel
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.boxbox.simon.ui.theme.ThemeManager
import com.boxbox.simon.ui.theme.simpson

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
                    color = theme.popUpTextColor,
                    fontFamily = theme.chosenFont
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = theme.popupEndIcon),
                        contentDescription = "End Icon",
                        modifier = when(theme) {
                            is simpson -> Modifier.size(150.dp)
                            else -> Modifier.size(72.dp)},
                        tint = Color.Unspecified
                    )

                    Text(
                        text = stringResource(R.string.score) + "${state.score}",
                        fontSize = 18.sp,
                        color = theme.scoreColor,
                        modifier = Modifier.padding(top = 4.dp, bottom = 3.dp),
                        fontFamily = theme.chosenFont
                    )

                    Text(
                        text = stringResource(R.string.difficulty) + "${state.difficulty}",
                        fontSize = 18.sp,
                        color = theme.popUpTextColor,
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

@Composable
fun ExitPopUp(showPopUp: MutableState<Boolean>, activity: Activity?) {
    val theme = ThemeManager.currentTheme

    if (showPopUp.value) {
        Dialog(onDismissRequest = { showPopUp.value = false }) {
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
                        text = stringResource(R.string.uscita),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = theme.popUpTextColor,
                        fontFamily = theme.chosenFont
                    )
                    Icon(
                        painter = painterResource(id = theme.popupExitIcon),
                        contentDescription = "Icona Mario",
                        modifier = when(theme) {
                            is simpson -> Modifier.size(150.dp)
                            else -> Modifier.size(72.dp)},
                        tint = Color.Unspecified,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.vuoi_uscire_dal_gioco),
                        fontSize = 18.sp,
                        color = theme.popUpTextColor,
                        fontFamily = theme.chosenFont,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                showPopUp.value = false
                                activity?.finishAffinity()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = theme.buttonBackground,
                                contentColor = theme.buttonTextColor
                            )
                        ) {
                            Text(text = stringResource(R.string.s), fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showPopUp.value = false },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = theme.buttonBackground,
                                contentColor = theme.buttonTextColor
                            )
                        ) {
                            Text(text = stringResource(R.string.no), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

