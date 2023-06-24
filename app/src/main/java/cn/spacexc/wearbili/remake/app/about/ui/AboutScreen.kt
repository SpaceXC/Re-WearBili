package cn.spacexc.wearbili.remake.app.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme

/**
 * Created by XC-Qan on 2023/4/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    state: LazyListState
) {
    val firstVisibleElement by remember { derivedStateOf { state.firstVisibleItemIndex } }
    var bannerClickedTimes by rememberSaveable {
        mutableStateOf(0)
    }
    var titleClickedTimes by rememberSaveable {
        mutableStateOf(0)
    }
    //val scope = rememberCoroutineScope()
    val isTitleVisible = firstVisibleElement == 0
    TitleBackground(title = if (isTitleVisible) "" else "关于软件", onBack = onBack) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                vertical = 6.dp,
                horizontal = TitleBackgroundHorizontalPadding
            ),
            state = state
        ) {
            item {
                Text(
                    text = "关于软件",
                    fontSize = 22.spx,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        titleClickedTimes++
                    }
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_wearbili_banner_remake),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                bannerClickedTimes++
                            }
                    )
                    Text(text = "另一个第三方Bilibili手表客户端", style = AppTheme.typography.body1)
                    Spacer(modifier = Modifier.height(400.dp))
                }
            }
        }
    }
}