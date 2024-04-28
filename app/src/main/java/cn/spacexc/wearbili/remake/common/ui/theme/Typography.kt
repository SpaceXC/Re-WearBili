package cn.spacexc.wearbili.remake.common.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R

/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalTextApi::class)
val wearbiliFontFamily = FontFamily(
    Font(R.font.misans_regular, FontWeight.Normal),
    Font(R.font.misans_medium, FontWeight.Medium),
    Font(R.font.misans_bold, FontWeight.Bold),
)

val wearbiliTypography = androidx.compose.material.Typography(
    defaultFontFamily = wearbiliFontFamily,
    h1 = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold),
    h2 = TextStyle(color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium),
    h3 = TextStyle(color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium),
    body1 = TextStyle(color = Color.White, fontSize = 11.sp),
    body2 = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium),
)