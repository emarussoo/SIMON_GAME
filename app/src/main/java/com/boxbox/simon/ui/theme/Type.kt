package com.boxbox.simon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.boxbox.simon.R

//Font management of entire application

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ))

val MarioTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.supermario)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    )

val NeonTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily =  FontFamily(Font(R.font.neon)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp
    ))

val SimpsonTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily =  FontFamily(Font(R.font.simpsonfont)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.5.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.3.sp
    ))

