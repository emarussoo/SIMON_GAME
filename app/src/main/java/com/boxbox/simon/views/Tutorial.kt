package com.boxbox.simon.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R

@Composable
fun howToPlayInterface(){

    val primaryColor = Color(0xFF000000)
    val textColor = Color(0xFF212121)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How to Play?",
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
                .background(Color.Transparent)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val steps = listOf(
                stringResource(R.string.the_game_will_show_a_sequence_of_colors_and_play_sounds),
                stringResource(R.string.tap_the_buttons_in_the_same_order_as_shown),
                stringResource(R.string.a_new_color_is_added_to_the_sequence_each_turn),
                stringResource(R.string.game_over_try_again_and_beat_your_high_score)
            )

            val stepsTitle = listOf(
                stringResource(R.string._1_watch_and_listen),
                stringResource(R.string._2_repeat_the_sequence),
                stringResource(R.string._3_each_round_gets_harder),
                stringResource(R.string._4_make_a_mistake)
            )

            stepsTitle.zip(steps).forEach { (title, desc) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = primaryColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
