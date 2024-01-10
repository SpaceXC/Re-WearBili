package cn.spacexc.wearbili.remake.ui.settings.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.ui.settings.domain.SettingsItem

/**
 * Created by XC-Qan on 2023/8/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Activity.SettingsActivityScreen(
    items: List<SettingsItem>
) {
    TitleBackground(title = "", onBack = ::finish) {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
            item {
                Text(
                    text = "设置",
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 22.spx,
                    fontWeight = FontWeight.Bold
                )
            }
            for (item in items) {
                item {
                    SettingsItem(item = item)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    item: SettingsItem
) {
    val localDensity = LocalDensity.current
    var textHeight by remember {
        mutableStateOf(0.dp)
    }
    Card(shape = RoundedCornerShape(15.dp), onClick = {
        item.action?.invoke()
    }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(textHeight * 0.6f)) {
                item.icon()
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.onSizeChanged {
                textHeight = with(localDensity) {
                    it.height.toDp()
                }
            }) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 13.spx,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.description,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 10.spx,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }
}