package kr.co.architecture.core.ui.theme

import android.os.Build
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kr.co.architecture.core.ui.R

internal val BuddyStockFont = FontFamily(
    Font(R.font.spoqahansansneo_bold, FontWeight.W700, FontStyle.Normal),
    Font(R.font.spoqahansansneo_medium, FontWeight.W500, FontStyle.Normal),
    Font(R.font.spoqahansansneo_regular, FontWeight.W400, FontStyle.Normal),
)

private fun getLineHeight(lineHeightDimenResId: TextUnit): TextUnit {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        lineHeightDimenResId
    } else {
        TextUnit.Unspecified
    }
}

val TypoDisplay1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 24.sp,
    lineHeight = getLineHeight(34.sp)
)

val TypeHeadline = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 20.sp,
    lineHeight = getLineHeight(28.sp)
)

val TypoSubhead4 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 18.sp,
    lineHeight = getLineHeight(24.sp)
)

val TypoSubhead3 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 16.sp,
    lineHeight = getLineHeight(22.sp)
)

val TypoSubhead3Weight500 = TextStyle(
    fontWeight = FontWeight.W500,
    fontFamily = BuddyStockFont,
    fontSize = 16.sp,
    lineHeight = getLineHeight(22.sp)
)

val TypoSubhead2 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 14.sp,
    lineHeight = getLineHeight(20.sp)
)

val TypoSubhead1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontFamily = BuddyStockFont,
    fontSize = 12.sp,
    lineHeight = getLineHeight(18.sp)
)

val TypoBody2 = TextStyle(
    fontFamily = BuddyStockFont,
    fontSize = 16.sp,
    lineHeight = getLineHeight(24.sp)
)

val TypoBody1 = TextStyle(
    fontFamily = BuddyStockFont,
    fontSize = 14.sp,
    lineHeight = getLineHeight(20.sp)
)

val TypoCaption = TextStyle(
    fontFamily = BuddyStockFont,
    fontSize = 12.sp,
    lineHeight = getLineHeight(18.sp)
)

val TypoMinText = TextStyle(
    fontFamily = BuddyStockFont,
    fontSize = 11.sp,
    lineHeight = getLineHeight(18.sp)
)