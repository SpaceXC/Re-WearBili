package cn.spacexc.wearbili.remake.app.main.profile.ui

import BiliTextIcon
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.bilibilisdk.sdk.user.profile.remote.info.current.Data
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.cache.list.CacheListActivity
import cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.folders.ui.FavoriteFolderActivity
import cn.spacexc.wearbili.remake.app.main.profile.detail.following.ui.FollowingUsersActivity
import cn.spacexc.wearbili.remake.app.main.profile.detail.history.ui.HistoryActivity
import cn.spacexc.wearbili.remake.app.main.profile.detail.watchlater.ui.WatchLaterActivity
import cn.spacexc.wearbili.remake.app.message.MessageActivity
import cn.spacexc.wearbili.remake.app.season.ui.PARAM_MID
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.experimantal.EXPERIMENTAL_MULTI_ACCOUNTS
import cn.spacexc.wearbili.remake.app.settings.experimantal.getActivatedExperimentalFunctions
import cn.spacexc.wearbili.remake.app.settings.ui.SettingsActivity
import cn.spacexc.wearbili.remake.app.settings.user.SwitchUserActivity
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceActivity
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.UserAvatar
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rotateInput
import cn.spacexc.wearbili.remake.common.ui.shimmerPlaceHolder
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify

/**
 * Created by XC-Qan on 2023/4/9.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
//WIP 个人页
data class ProfileScreenState(
    val user: Data?,
    val uiState: UIState,
    val scrollState: ScrollState,
)

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    context: Context,
    onRetry: () -> Unit
) {
    if (/*runBlocking { UserUtils.isUserLoggedIn() }*/true) {
        val focusRequester = remember {
            FocusRequester()
        }
        val localDensity = LocalDensity.current
        val configuration = LocalConfiguration.current
        val haveMultiAccount by remember {
            derivedStateOf {
                configuration.getActivatedExperimentalFunctions().contains(
                    EXPERIMENTAL_MULTI_ACCOUNTS
                )
            }
        }
        haveMultiAccount.apply {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state.scrollState)
                        .rotateInput(focusRequester, state.scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickVfx(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            UserSpaceActivity::class.java
                                        ).apply {
                                            putExtra(PARAM_MID, state.user?.mid)
                                        })
                                },
                                enabled = state.user?.mid != null
                            )//.placeholder(visible = state.user != null, shape = RoundedCornerShape(8.dp), highlight = PlaceholderHighlight.shimmer())
                    ) {
                        state.user.let { user ->
                            //region 个人信息
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                var usernameHeight by remember {
                                    mutableStateOf(0.dp)
                                }
                                UserAvatar(
                                    avatar = user?.face ?: "",
                                    pendant = user?.pendant?.image,
                                    size = DpSize.Unspecified,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f),
                                    officialVerify = user?.official?.type.toOfficialVerify()
                                )

                                val levelCardResourceId = when (user?.level) {
                                    0 -> R.drawable.icon_lv0_card
                                    1 -> R.drawable.icon_lv1_card
                                    2 -> R.drawable.icon_lv2_card
                                    3 -> R.drawable.icon_lv3_card
                                    4 -> R.drawable.icon_lv4_card
                                    5 -> R.drawable.icon_lv5_card
                                    6 -> R.drawable.icon_lv6_card
                                    7 -> R.drawable.icon_lv6_plus_card
                                    else -> {
                                        0
                                    }
                                }
                                val inlineTextContent = mapOf(
                                    "vip" to InlineTextContent(
                                        Placeholder(
                                            width = AppTheme.typography.h2.fontSize,
                                            height = AppTheme.typography.h2.fontSize,
                                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                        )
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.icon_vip_card),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .scale(1.1f),
                                            contentScale = ContentScale.Inside
                                        )
                                    }
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        append(user?.name ?: "WearBili")
                                        if (user?.vip?.status == 1) {
                                            append(" ")
                                            appendInlineContent("vip")
                                        }
                                    },
                                    style = AppTheme.typography.h2,
                                    color = parseColor(
                                        (user?.vip?.nickname_color
                                            ?: "#FFFFFF").ifEmpty { "#FFFFFF" }),
                                    inlineContent = inlineTextContent,
                                    modifier = Modifier
                                        .onSizeChanged {
                                            usernameHeight = with(localDensity) { it.height.toDp() }
                                        }
                                        .shimmerPlaceHolder(user?.name == null)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                if (levelCardResourceId != 0) {
                                    Image(
                                        painter = painterResource(id = if (user?.is_senior_member == 1) R.drawable.icon_lv6_plus_card else levelCardResourceId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(usernameHeight * 0.95f)
                                        //.scale(0.95f)
                                        ,
                                        contentScale = ContentScale.Inside
                                    )
                                }

                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .padding(titleBackgroundHorizontalPadding() - 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = user?.coins.toString(),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.shimmerPlaceHolder(user?.coins == null)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "硬币",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.alpha(0.8f)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(parseColor("#902F3134"))
                                        .fillMaxHeight(0.5f)
                                        .width(0.5.dp)
                                    //.height(10.dp)
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = user?.follower.toString(),
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.shimmerPlaceHolder(user?.coins == null)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "粉丝",
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.alpha(0.8f)
                                    )
                                }
                            }
                            //endregion
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = if (isRound()) 24.dp else 18.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            OutlinedRoundButton(
                                icon = {
                                    BiliTextIcon(
                                        icon = "ea25",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                },
                                text = "稍后再看",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            WatchLaterActivity::class.java
                                        )
                                    )
                                }
                            )
                            OutlinedRoundButton(
                                icon = {
                                    BiliTextIcon(
                                        icon = "eae9",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                },
                                text = "我的关注",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            FollowingUsersActivity::class.java
                                        )
                                    )
                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {


                            OutlinedRoundButton(
                                icon = {
                                    BiliTextIcon(
                                        icon = "eaa2",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                },
                                text = "离线缓存",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            CacheListActivity::class.java
                                        )
                                    )
                                }
                            )
                            OutlinedRoundButton(
                                icon = {
                                    BiliTextIcon(
                                        icon = "ea6e",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                },
                                text = "历史记录",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            HistoryActivity::class.java
                                        )
                                    )

                                }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            OutlinedRoundButton(
                                icon = {
                                    BiliTextIcon(
                                        icon = "ea94",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                },
                                text = "我的消息",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            MessageActivity::class.java
                                        )
                                    )
                                }
                            )
                            OutlinedRoundButton(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_favourite),
                                        tint = Color.White,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxSize(0.4f)
                                    )
                                },
                                text = "个人收藏",
                                modifier = Modifier.weight(1f),
                                buttonModifier = Modifier.aspectRatio(1f),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            FavoriteFolderActivity::class.java
                                        )
                                    )
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            outerPaddingValues = PaddingValues(vertical = 8.dp),
                            innerPaddingValues = PaddingValues(12.dp),
                            shape = RoundedCornerShape(40),
                            onClick = {
                                context.startActivity(Intent(context, SettingsActivity::class.java))
                            }
                        ) {
                            if (haveMultiAccount) {
                                BiliTextIcon(
                                    icon = "eb91",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            } else {
                                IconText(
                                    text = "设置",
                                    fontFamily = wearbiliFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.align(Alignment.Center),
                                    spacingWidth = 6.sp
                                ) {
                                    BiliTextIcon(
                                        icon = "eb91",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxSize(),
                                        size = 18.sp
                                    )
                                }
                            }
                        }
                        if (haveMultiAccount) {
                            Card(
                                modifier = Modifier.weight(1f),
                                outerPaddingValues = PaddingValues(vertical = 8.dp),
                                innerPaddingValues = PaddingValues(12.dp),
                                shape = RoundedCornerShape(40),
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            SwitchUserActivity::class.java
                                        )
                                    )
                                }
                            ) {
                                BiliTextIcon(
                                    icon = "eaed",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                LaunchedEffect(key1 = Unit, block = {
                    focusRequester.requestFocus()
                })

            }
        }
    } else {

    }
}