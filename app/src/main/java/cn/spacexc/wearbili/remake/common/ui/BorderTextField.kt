package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/11/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun BorderTextField(
    value: String,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit
) {
    Card(
        innerPaddingValues = PaddingValues(vertical = 10.dp, horizontal = 8.dp),
        outerPaddingValues = PaddingValues(0.dp),
        isClickEnabled = false,
        //modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leadingIcon,
                tint = Color.White,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(2.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    AutoResizedText(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 14.spx,
                            fontFamily = wearbiliFontFamily,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .alpha(0.6f)
                            .align(Alignment.CenterStart)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChanged,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.spx,
                        fontFamily = wearbiliFontFamily,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(
                            Alignment.CenterStart
                        ),
                    cursorBrush = SolidColor(value = BilibiliPink),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                )
            }
        }
    }
}