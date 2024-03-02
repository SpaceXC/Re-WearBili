package cn.spacexc.wearbili.remake.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/4/13.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object ToastUtils {
    var toastContent by mutableStateOf("")

    var snackBarObject: SnackBarObject? by mutableStateOf(null)


    fun showText(content: String) {
        toastContent = content
    }

    fun showSnackBar(
        content: String,
        leadingIcon: ImageVector,
        trailingIcon: ImageVector,
        onClick: () -> Unit
    ) {
        snackBarObject = SnackBarObject(
            text = content,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            onClick = onClick
        )
    }

    @Composable
    fun ToastContent(content: String) {
        Box(
            modifier = Modifier
                .padding(TitleBackgroundHorizontalPadding() - 4.dp)
                .fillMaxWidth()
                .border(
                    width = 0.3f.dp,
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.linearGradient(
                        listOf(
                            Color(54, 54, 54, 255),
                            Color.Transparent
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black,
                            .5f to Color.Black,
                            1f to Color(39, 16, 23)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = content,
                fontFamily = wearbiliFontFamily,
                fontSize = 12.spx,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun SnackBarContent(
        snackBarObject: SnackBarObject
    ) {
        val localDensity = LocalDensity.current
        Row(
            modifier = Modifier
                .clickVfx {
                    snackBarObject.onClick()
                    this.snackBarObject = null
                }
                .padding(TitleBackgroundHorizontalPadding() - 4.dp)
                .fillMaxWidth()
                .border(
                    width = 0.3f.dp,
                    shape = RoundedCornerShape(10.dp),
                    brush = Brush.linearGradient(
                        listOf(
                            Color(54, 54, 54, 255),
                            Color.Transparent
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Black,
                            .5f to Color.Black,
                            1f to Color(39, 16, 23)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = snackBarObject.leadingIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(
                    with(localDensity) { 15.spx.toDp() }
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = snackBarObject.text,
                fontFamily = wearbiliFontFamily,
                fontSize = 12.spx,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium
            )
            Icon(imageVector = snackBarObject.trailingIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(
                    with(localDensity) { 13.spx.toDp() }
                )
            )
        }
    }

    data class SnackBarObject(
        val text: String,
        val leadingIcon: ImageVector,
        val trailingIcon: ImageVector,
        val onClick: () -> Unit
    )
}