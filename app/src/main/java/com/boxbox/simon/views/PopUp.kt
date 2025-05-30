package com.boxbox.simon.views

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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