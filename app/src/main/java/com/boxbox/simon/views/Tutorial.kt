package com.boxbox.simon.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun HowToPlayInterface(){

    val primaryColor = Color(0xFF000000)
    val textColor = Color(0xFF212121)

    // List of tutorial paragraph texts, one for each step
    val steps = listOf(
        stringResource(R.string.tutorial_line_1),
        stringResource(R.string.tutorial_line_2),
        stringResource(R.string.tutorial_line_3),
        stringResource(R.string.tutorial_line_4)
    )

    // Corresponding titles for each tutorial step
    val stepsTitle = listOf(
        stringResource(R.string.tutorial_title_1),
        stringResource(R.string.tutorial_title_2),
        stringResource(R.string.tutorial_title_3),
        stringResource(R.string.tutorial_title_4)
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //main title
        Text(
            text = stringResource(R.string.how_to_play),
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

            //loop through each step of the tutorial
            stepsTitle.zip(steps).forEach { (title, desc) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {

                    //paragraph title
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = primaryColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    //paragraph text
                    Text(
                        text = desc,
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }
            }
        }
    }

}
