import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R

/**
 * Created by Xiaochang on 2022/10/24.
 * I'm very cute so please be nice to my code!
 * 
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！

 */

val iconFontFamily = FontFamily(
    Font(R.font.segoe_fluent_icons)
)

@Composable
fun TextIcon(
    icon: String,
    modifier: Modifier = Modifier,
    size: TextUnit = 24.sp,
    color: Color = Color.White
) {
    Text(
        text = unicodeToString(icon),
        color = color,
        modifier = modifier,
        fontFamily = iconFontFamily,
        fontSize = size,
        textAlign = TextAlign.Center
    )
}

fun unicodeToString(encodeText: String): String {
    fun decode(unicode: String) = unicode.toInt(16).toChar()
    val unicodes = encodeText.split("\\u").map {
        if (it.isNotBlank()) decode(it) else null
    }.filterNotNull()
    return String(unicodes.toCharArray())
}