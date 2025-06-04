package com.boxbox.simon.views

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.boxbox.simon.R
import com.boxbox.simon.viewmodel.SimonViewModel

@Composable
fun EndPopUp(context: Context, viewModel: SimonViewModel, onDismiss: () -> Unit = {}) {

    val state = viewModel.EndGame(context)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Game Over") },
        text = {
            Column {
                Text("Score: ${state.score}")
                Text("Difficulty: ${state.difficulty}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun ExitPopUp(showPopUp: MutableState<Boolean>, activity: Activity?){
    AlertDialog(
        onDismissRequest = { showPopUp.value = false },
        title = { Text(stringResource(R.string.uscita))},
        text = { Text(stringResource(R.string.vuoi_uscire_dal_gioco)) },
        confirmButton = {
            TextButton(onClick = {
                showPopUp.value = false
                activity?.finishAffinity()
            }){
                Text(stringResource(R.string.s))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showPopUp.value = false
            }) {
                Text(stringResource(R.string.no))
            }
        }
    )
}