package cn.spacexc.wearbili.remake.app.settings.ui

import AutoSizedBiliTextIcon
import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.feedback.ui.issues.AllIssuesActivity
import cn.spacexc.wearbili.remake.app.settings.domain.SettingsItem
import cn.spacexc.wearbili.remake.app.settings.experimantal.ExperimentalFunctionsActivity
import cn.spacexc.wearbili.remake.app.settings.scaling.ScaleAdjustingActivity
import cn.spacexc.wearbili.remake.app.settings.toolbar.ui.QuickToolbarCustomizationActivity
import cn.spacexc.wearbili.remake.app.welcome.screens.ReWearBiliText
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.Checkbox
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

/**
 * Created by XC-Qan on 2023/8/7.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Activity.SettingsActivityScreen(
    items: List<SettingsItem>
) {
    TitleBackground(title = "", onBack = ::finish, onRetry = {}) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = titleBackgroundHorizontalPadding()),
            horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = "设置",
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
            )
            Text(
                text = "调整选项与设置",
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.7f),
            )


            val localDensity = LocalDensity.current
            var cardHeight by remember {
                mutableStateOf(0.dp)
            }
            Card(
                innerPaddingValues = PaddingValues(),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fillMaxSize = false,
                isClickEnabled = false,
                borderColor = Color.Transparent,
                onClick = {

                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_silk_background),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.height(cardHeight)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged {
                            with(localDensity) {
                                cardHeight = it.height.toDp()
                            }
                        }
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ReWearBiliText(fontSize = 14.sp)
                    Text(
                        text = "${Application.getVersionName()} Ver.${Application.getVersionCode()} Rel.${Application.getReleaseNumber()}",
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 9.sp,
                        modifier = Modifier.alpha(0.7f)
                    )
                }
            }

            FlowRow(
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        AutoSizedBiliTextIcon(icon = "EAF7", modifier = Modifier.align(Alignment.Center))
                    },
                    name = "播放选项"
                ) {

                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.icon_interface_scaling_settings_item), contentDescription = null, modifier = Modifier.fillMaxSize().padding(1.5.dp), tint = Color.White)
                    },
                    name = "界面缩放"
                ) {
                    startActivity(Intent(this@SettingsActivityScreen, ScaleAdjustingActivity::class.java))
                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.icon_quick_toolbar_settings_item), contentDescription = null, modifier = Modifier.fillMaxSize().padding(1.5.dp), tint = Color.White)
                    },
                    name = "快捷功能"
                ) {
                    startActivity(Intent(this@SettingsActivityScreen, QuickToolbarCustomizationActivity::class.java))
                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        AutoSizedBiliTextIcon(icon = "EA86", modifier = Modifier.align(Alignment.Center))
                    },
                    name = "弹幕设置"
                ) {

                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        AutoSizedBiliTextIcon(icon = "EADF", modifier = Modifier.align(Alignment.Center))
                    },
                    name = "个性化"
                ) {

                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.icon_experimental_function), contentDescription = null, modifier = Modifier.fillMaxSize().padding(1.5.dp), tint = Color.White)
                    },
                    name = "实验功能"
                ) {
                    startActivity(Intent(this@SettingsActivityScreen, ExperimentalFunctionsActivity::class.java))
                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.icon_report), contentDescription = null, modifier = Modifier.fillMaxSize().padding(1.5.dp), tint = Color.White)
                    },
                    name = "反馈中心"
                ) {
                    startActivity(Intent(this@SettingsActivityScreen, AllIssuesActivity::class.java))
                }
                SettingsItemV2(
                    modifier = Modifier.weight(1f).alpha(0f),
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.icon_report), contentDescription = null, modifier = Modifier.fillMaxSize().padding(1.5.dp), tint = Color.White)
                    },
                    name = "反馈中心"
                ) {
                    //startActivity(Intent(this@SettingsActivityScreen, AllIssuesActivity::class.java))
                }
            }
        }
        /*LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                horizontal = titleBackgroundHorizontalPadding()
            )
        ) {
            item {
                Text(
                    text = "设置",
                    color = Color.White,
                    fontFamily = wearbiliFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                )
            }
            for (item in items) {
                item {
                    SettingsItem(item = item)
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
            item { if (isRound()) Spacer(modifier = Modifier.height(30.dp)) }
        }*/
    }
}

@Composable
fun SettingsItemV2(
    modifier: Modifier = Modifier,
    icon: @Composable BoxScope.() -> Unit,
    name: String,
    onClick: () -> Unit
) {
    Card(modifier = modifier, onClick = onClick, fillMaxSize = false, outerPaddingValues = PaddingValues()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f / 3f)
                    .aspectRatio(1f)
            ) {
                icon()
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                fontFamily = wearbiliFontFamily,
                fontSize = 13.sp,
                color = Color.White
            )
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
    var isShowingList by remember {
        mutableStateOf(false)
    }
    Card(shape = RoundedCornerShape(15.dp), onClick = {
        if (item.options.isNullOrEmpty()) {
            item.action?.invoke(null)
        } else {
            isShowingList = !isShowingList
        }
    }, isHighlighted = item.isOn?.invoke() ?: false) {
        Column {
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
                Box {
                    Column(modifier = Modifier) {
                        Text(
                            text = item.name,
                            color = Color.White,
                            fontFamily = wearbiliFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.description,
                            color = Color.White,
                            fontFamily = wearbiliFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                    Column(modifier = Modifier.onSizeChanged {
                        textHeight = with(localDensity) {
                            it.height.toDp()
                        }
                    }) {
                        Text(
                            text = "",
                            color = Color.White,
                            fontFamily = wearbiliFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "",
                            color = Color.White,
                            fontFamily = wearbiliFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }   //Height Indicator
                }
                /*Spacer(modifier = Modifier.weight(1f))
                if (item.displayedValue != null) {
                    AnimatedVisibility(visible = !isShowingList, enter = expandHorizontally(), exit = shrinkHorizontally()) {
                        Text(
                            text = item.displayedValue.invoke(),
                            fontFamily = wearbiliFontFamily,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }*/
            }
            if (!item.options.isNullOrEmpty()) {
                AnimatedVisibility(
                    visible = isShowingList,
                    modifier = Modifier.padding(horizontal = 3.dp)
                ) {
                    Column {
                        item.options.forEach { option ->
                            Row(modifier = Modifier
                                .clickVfx { item.action?.invoke(option) }
                                .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                AnimatedVisibility(
                                    visible = item.currentValue?.invoke() == option.option,
                                    enter = expandHorizontally(),
                                    exit = shrinkHorizontally()
                                ) {
                                    Row {
                                        Checkbox(
                                            isChecked = true,
                                            indication = true,
                                            size = 12.dp
                                        ) {

                                        }
                                        Spacer(modifier = Modifier.width(2.dp))
                                    }
                                }
                                Text(
                                    text = option.displayedValue,
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}