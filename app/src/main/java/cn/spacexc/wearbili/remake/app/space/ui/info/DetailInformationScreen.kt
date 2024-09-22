package cn.spacexc.wearbili.remake.app.space.ui.info

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceViewModel
import cn.spacexc.wearbili.remake.common.ui.OfficialVerify
import cn.spacexc.wearbili.remake.common.ui.SmallUserCard
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailInformationScreen(
    navController: NavController,
    viewModel: UserSpaceViewModel,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPressed: () -> Unit
) {
    val localDensity = LocalDensity.current
    viewModel.info?.let { user ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(titleBackgroundHorizontalPadding() - 3.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SmallUserCard(
                avatar = user.face,
                username = user.name,
                useBiliImage = false,
                textSizeScale = 1.1f,
                mid = 0,
                navController = navController,
                imageModifier = with(sharedTransitionScope) {
                    Modifier.sharedElement(
                        rememberSharedContentState(key = "userAvatar"),
                        animatedVisibilityScope = animatedContentScope
                    )
                },
                usernameModifier = with(sharedTransitionScope) {
                    Modifier.sharedBounds(
                        rememberSharedContentState(key = "username"),
                        animatedVisibilityScope = animatedContentScope
                    )
                },
                modifier = Modifier.clickAlpha { onBackPressed() }
            )
            Text(
                text = "详情",
                color = Color.White,
                fontFamily = wearbiliFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            val levelCardResourceId = when (user.level) {
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
            Image(painter = painterResource(id = levelCardResourceId),
                contentDescription = null,
                modifier = Modifier.height(
                    with(localDensity) { 16.sp.toDp() }
                )
            )
            //Spacer(modifier = Modifier.height(2.dp))
            Row(modifier = Modifier.alpha(0.7f), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_uploader),
                    contentDescription = null,
                    modifier = Modifier.height(
                        with(localDensity) { 14.sp.toDp() }
                    ),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = user.mid.toString(),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily
                )
            }

            if (user.sign.isNotEmpty()) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_info),
                        contentDescription = null,
                        modifier = Modifier
                            .height(
                                with(localDensity) { 12.sp.toDp() }
                            )
                            .offset(y = 3.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.sign,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = wearbiliFontFamily
                    )
                }
            }

            if (user.official?.type?.toOfficialVerify() != OfficialVerify.NONE) {
                Row {
                    Image(
                        painter = painterResource(
                            id = when (user.official?.type?.toOfficialVerify()) {
                                //OfficialVerify.NONE -> 0
                                OfficialVerify.PERSONAL -> R.drawable.icon_official_verify_personal
                                OfficialVerify.ORG -> R.drawable.icon_official_verify_org
                                else -> 0
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .height(
                                with(localDensity) { 14.sp.toDp() }
                            )
                            .offset(y = 3.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = user.official?.title ?: "",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = wearbiliFontFamily
                    )
                }
            }
            if (isRound()) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}