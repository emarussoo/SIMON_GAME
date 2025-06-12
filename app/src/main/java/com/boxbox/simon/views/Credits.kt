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

//Pays tribute to the brilliant creators of this app
@Composable
fun creditsInterface() {

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
            text = "Credits",
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

            val credits = listOf(
                stringResource(R.string.app_sviluppata_da_emanuele_russo_simone_marmor_marco_supino).replace("\\n", "\n"),
                stringResource(R.string.progetto_realizzato_per_il_corso_di_mobile_programming_del_prof_massimo_regoli),
                stringResource(R.string.questa_applicazione_stata_creata_esclusivamente_a_scopo_didattico_e_non_destinata_alla_distribuzione_o_al_commercio)
            )

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