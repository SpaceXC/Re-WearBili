package cn.spacexc.wearbili.remake.app.settings.toolbar.ui

import BiliTextIcon
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotInterested
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.folders.ui.FavoriteFoldersScreen
import cn.spacexc.wearbili.remake.app.main.profile.detail.history.ui.HistoryScreen
import cn.spacexc.wearbili.remake.app.search.ui.SearchScreen
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import cn.spacexc.wearbili.remake.proto.settings.QuickToolBarFunction
import cn.spacexc.wearbili.remake.proto.settings.QuickToolBarSlotCount
import cn.spacexc.wearbili.remake.proto.settings.copy

/**
 * Created by XC-Qan on 2023/7/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class QuickToolbarCustomizationScreenSlots {
    SLOT_ONE, SLOT_TWO
}

data class QuickToolbarFunctionDetail(
    val name: String,
    val icon: @Composable () -> Unit,
    val type: QuickToolBarFunction,
    val action: (NavController) -> Unit
)

val functionList = listOf(QuickToolbarFunctionDetail(name = "历史", icon = {
    BiliTextIcon(icon = "ea6e", size = 20.sp, modifier = Modifier.offset(y = (-0.5).dp))
    /*Icon(
        imageVector = Icons.Default.History,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = Color.White
    )*/
}, type = QuickToolBarFunction.History, action = {
    it.navigate(HistoryScreen)
}), QuickToolbarFunctionDetail(name = "搜索", icon = {
    /*Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = Color.White
    )*/
    BiliTextIcon(icon = "eacb", size = 20.sp, modifier = Modifier.offset(y = (-0.5).dp))
}, type = QuickToolBarFunction.Search, action = {
    it.navigate(SearchScreen())
}), QuickToolbarFunctionDetail(name = "缓存", icon = {
    /*Icon(
        imageVector = Icons.Outlined.FileDownload,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = Color.White
    )*/
    BiliTextIcon(icon = "eaa2", size = 20.sp, modifier = Modifier.offset(y = (-0.5).dp))
}, type = QuickToolBarFunction.Cache, action = {
    //it.startActivity(Intent(it, CacheListActivity::class.java))
}), QuickToolbarFunctionDetail(name = "消息", icon = {
    /*Icon(
        imageVector = Icons.Outlined.FileDownload,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = Color.White
    )*/
    BiliTextIcon(icon = "ea94", size = 20.sp, modifier = Modifier.offset(y = (-0.5).dp))
}, type = QuickToolBarFunction.Message, action = {
    //it.startActivity(Intent(it, MessageActivity::class.java))
}), QuickToolbarFunctionDetail(name = "收藏", icon = {
    Icon(
        painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.icon_favourite),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-0.5).dp),
        tint = Color.White
    )
}, type = QuickToolBarFunction.Favourite, action = {
    it.navigate(FavoriteFoldersScreen)
}), QuickToolbarFunctionDetail(name = "无", icon = {
    Icon(
        imageVector = Icons.Outlined.NotInterested,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        tint = Color.White
    )
}, type = QuickToolBarFunction.None, action = {
}))

val QuickToolBarFunction.toFunctionDetail: QuickToolbarFunctionDetail?
    get() = functionList.find { it.type == this }

@kotlinx.serialization.Serializable
object QuickToolbarCustomizationScreen

@Composable
fun QuickToolbarCustomizationScreen(navController: NavController) {
    TitleBackground(
        navController = navController,
        title = "编辑功能区",
        onBack = navController::navigateUp,
        onRetry = {}) {
        val configuration = LocalConfiguration.current
        var firstFunction by remember {
            mutableStateOf(
                configuration.toolBarConfiguration.functionOne.toFunctionDetail
                    ?: QuickToolBarFunction.None.toFunctionDetail!!
            )
        }
        var secondFunction by remember {
            mutableStateOf(
                configuration.toolBarConfiguration.functionTwo.toFunctionDetail
                    ?: QuickToolBarFunction.None.toFunctionDetail!!
            )
        }
        var currentEditingSlot by remember {
            mutableStateOf(QuickToolbarCustomizationScreenSlots.SLOT_ONE)
        }

        LaunchedEffect(key1 = Unit) {
            SettingsManager.updateConfiguration {
                copy {
                    haveToolBarTipDisplayed = true
                }
            }
        }

        LaunchedEffect(key1 = firstFunction) {
            SettingsManager.updateConfiguration {
                copy {
                    toolBarConfiguration = toolBarConfiguration.copy {
                        functionOne = firstFunction.type
                        slotCount = getSlotCount(firstFunction.type, secondFunction.type)
                    }
                }
            }
        }
        LaunchedEffect(key1 = secondFunction) {
            SettingsManager.updateConfiguration {
                copy {
                    toolBarConfiguration = toolBarConfiguration.copy {
                        functionTwo = secondFunction.type
                        slotCount = getSlotCount(firstFunction.type, secondFunction.type)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = titleBackgroundHorizontalPadding(),
                    vertical = if (isRound()) titleBackgroundHorizontalPadding() else 4.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FunctionSlot(
                        function = firstFunction,
                        isCurrentlyEditing = currentEditingSlot == QuickToolbarCustomizationScreenSlots.SLOT_ONE,
                        modifier = Modifier
                            .weight(1f)
                            .animateContentSize(),
                    ) {
                        currentEditingSlot = QuickToolbarCustomizationScreenSlots.SLOT_ONE
                    }
                    FunctionSlot(
                        function = secondFunction,
                        isCurrentlyEditing = currentEditingSlot == QuickToolbarCustomizationScreenSlots.SLOT_TWO,
                        modifier = Modifier
                            .weight(1f)
                            .animateContentSize(),
                    ) {
                        currentEditingSlot = QuickToolbarCustomizationScreenSlots.SLOT_TWO
                    }
                }
                Divider(
                    color = Color(48, 48, 48), modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.2f)
                )
                functionList.forEach { function ->
                    val shouldDisplay = when (currentEditingSlot) {
                        QuickToolbarCustomizationScreenSlots.SLOT_ONE -> {
                            secondFunction != function
                        }

                        QuickToolbarCustomizationScreenSlots.SLOT_TWO -> {
                            firstFunction != function
                        }
                    }
                    if (shouldDisplay || function.type == QuickToolBarFunction.None) {
                        FunctionItem(
                            modifier = Modifier,
                            function = function,
                            currentFunction = when (currentEditingSlot) {
                                QuickToolbarCustomizationScreenSlots.SLOT_ONE -> firstFunction
                                QuickToolbarCustomizationScreenSlots.SLOT_TWO -> secondFunction
                            }
                        ) {
                            when (currentEditingSlot) {
                                QuickToolbarCustomizationScreenSlots.SLOT_ONE -> firstFunction =
                                    function

                                QuickToolbarCustomizationScreenSlots.SLOT_TWO -> secondFunction =
                                    function
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getSlotCount(
    functionOne: QuickToolBarFunction,
    functionTwo: QuickToolBarFunction
): QuickToolBarSlotCount {
    if (functionOne != QuickToolBarFunction.None && functionTwo != QuickToolBarFunction.None) return QuickToolBarSlotCount.Two
    if (functionOne != QuickToolBarFunction.None || functionTwo != QuickToolBarFunction.None) return QuickToolBarSlotCount.One
    return QuickToolBarSlotCount.Zero
}

@Composable
fun FunctionSlot(
    modifier: Modifier,
    function: QuickToolbarFunctionDetail,
    isCurrentlyEditing: Boolean,
    onClick: () -> Unit
) {
    val alpha by wearBiliAnimateFloatAsState(
        targetValue = if (function.type == QuickToolBarFunction.None) 0.5f else 1f
    )
    Card(
        modifier = modifier
            .height(45.dp)
            .alpha(alpha),
        shape = CircleShape,
        innerPaddingValues = PaddingValues(10.dp),
        outerPaddingValues = PaddingValues(0.dp),
        fillMaxSize = false,
        contentAlignment = Alignment.Center,
        isHighlighted = isCurrentlyEditing,
        onClick = onClick
    ) {
        function.icon()
    }
}

@Composable
fun StaticFunctionSlot(
    modifier: Modifier,
    function: QuickToolbarFunctionDetail,
    slotCount: QuickToolBarSlotCount,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(45.dp),
        shape = CircleShape,
        innerPaddingValues = PaddingValues(10.dp),
        outerPaddingValues = PaddingValues(0.dp),
        fillMaxSize = false,
        contentAlignment = Alignment.Center,
        onClick = onClick
    ) {
        if (slotCount == QuickToolBarSlotCount.One) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(x = (-4).dp)
            ) {
                Box(modifier = Modifier.aspectRatio(1f)) {
                    function.icon()
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = function.name,
                    fontFamily = wearbiliFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )

            }
        } else {
            function.icon()
        }
    }
}

@Composable
fun FunctionItem(
    modifier: Modifier,
    function: QuickToolbarFunctionDetail,
    currentFunction: QuickToolbarFunctionDetail,
    onClick: (QuickToolbarFunctionDetail) -> Unit
) {
    Card(modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(40),
        innerPaddingValues = PaddingValues(horizontal = 20.dp),
        outerPaddingValues = PaddingValues(0.dp),
        fillMaxSize = true,
        contentAlignment = Alignment.CenterStart,
        isHighlighted = function == currentFunction,
        onClick = {
            onClick(function)
        }) {
        IconText(text = "  ${function.name}", fontWeight = FontWeight.Medium, fontSize = 14.sp) {
            function.icon()
        }
    }
}