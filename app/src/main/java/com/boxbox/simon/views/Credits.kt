package com.boxbox.simon.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R

@Composable
fun CreditsInterface() {

    val primaryColor = Color(0xFF000000)
    val textColor = Color(0xFF212121)
    val scrollState = rememberScrollState()

    // List of credits paragraph texts
    val credits = listOf(
        stringResource(R.string.credits_line_1).replace("\\n", "\n"),
        stringResource(R.string.credits_line_2),
        stringResource(R.string.credits_line_3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //main title
        Text(
            text = stringResource(R.string.credits),
            fontSize = 50.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = primaryColor,
            lineHeight = 58.sp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //cicle through paragraphs
            credits.forEach { line ->
                Text(
                    text = line,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
    }
}