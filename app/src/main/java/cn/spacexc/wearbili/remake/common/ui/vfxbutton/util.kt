package like

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI

@Composable
fun DefaultContent(
    isLike: Boolean = false,
    size: Dp = 30.dp,
) {
    Icon(
        modifier = Modifier.size(size),
        imageVector = Icons.Default.Favorite,
        contentDescription = "",
        tint = if (isLike) Color.Red else Color.Gray,
    )
}

fun degToRad(deg: Float) = deg * (PI / 180.0)

fun mapValueFromRangeToRange(
    value: Float,
    fromLow: Float,
    fromHigh: Float,
    toLow: Float,
    toHigh: Float,
): Float {
    return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
}

fun Int.forEach(
    block: (Int) -> Unit,
) {
    for (i in 0 until this) {
        block.invoke(i)
    }
}

operator fun Dp.times(d: Double) = this.value.times(d).dp
