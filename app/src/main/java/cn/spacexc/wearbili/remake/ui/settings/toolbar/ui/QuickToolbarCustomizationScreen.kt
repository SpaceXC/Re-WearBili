package cn.spacexc.wearbili.remake.ui.settings.toolbar.ui

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.ui.settings.toolbar.ui.QuickToolbarCustomizationScreenSlotCount.NO_SLOT
import cn.spacexc.wearbili.remake.ui.settings.toolbar.ui.QuickToolbarCustomizationScreenSlotCount.ONE_SLOT
import cn.spacexc.wearbili.remake.ui.settings.toolbar.ui.QuickToolbarCustomizationScreenSlotCount.TWO_SLOT

/**
 * Created by XC-Qan on 2023/7/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class QuickToolbarCustomizationScreenSlotCount(val count: Int) {
    NO_SLOT(0),
    ONE_SLOT(1),
    TWO_SLOT(2)
}

data class QuickToolbarFunction(
    val name: String,
    val icon: @Composable () -> Unit,
    val action: () -> Unit
)

@Composable
fun Activity.QuickToolbarCustomizationScreen() {
    TitleBackground(title = "编辑功能区", onBack = ::finish) {
        var currentSlotCount by remember {
            mutableStateOf(ONE_SLOT)
        }
        val functions by remember {
            mutableStateOf(Pair<QuickToolbarFunction?, QuickToolbarFunction?>(null, null))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(TitleBackgroundHorizontalPadding - 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(), verticalAlignment = Alignment.CenterVertically
            ) {
                when (currentSlotCount) {
                    NO_SLOT -> {
                        Text(
                            text = "快捷工具栏已关闭",
                            fontSize = 15.spx,
                            color = Color.White,
                            fontFamily = wearbiliFontFamily
                        )
                    }

                    ONE_SLOT -> {

                    }

                    TWO_SLOT -> {

                    }
                }
            }
        }
    }
}