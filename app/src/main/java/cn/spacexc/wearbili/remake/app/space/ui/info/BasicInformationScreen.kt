package cn.spacexc.wearbili.remake.app.space.ui.info

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceViewModel
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.UserAvatar
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.shimmerPlaceHolder
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BasicInformationScreen(
    viewModel: UserSpaceViewModel,
    animatedContentScope: AnimatedContentScope,
    onShowDetailClicked: () -> Unit
) {
    viewModel.info?.let { user ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (isRound()) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            UserAvatar(
                avatar = user.face,
                pendant = user.pendant?.image,
                size = DpSize.Unspecified,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "userAvatar"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .fillMaxWidth(0.35f),
                officialVerify = user.official?.type.toOfficialVerify(),
                useBiliImage = false
            )
            Text(
                text = user.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = wearbiliFontFamily,
                color = Color.White,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "username"),
                    animatedVisibilityScope = animatedContentScope
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    //modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material.Text(
                        text = viewModel.stat?.follower?.toShortChinese() ?: "null",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.shimmerPlaceHolder(viewModel.stat == null)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    androidx.compose.material.Text(
                        text = "粉丝",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(0.8f)
                    )
                }
                /*Box(
                    modifier = Modifier
                        .background(parseColor("#902F3134"))
                        .fillMaxHeight(0.5f)
                        .width(0.5.dp)
                    //.height(10.dp)
                )*/
                Column(
                    //modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.material.Text(
                        text = viewModel.stat?.following?.toShortChinese() ?: "null",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.shimmerPlaceHolder(viewModel.stat == null)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    androidx.compose.material.Text(
                        text = "关注",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(0.8f)
                    )
                }
            }
            Card(
                shape = CircleShape, onClick = {
                    onShowDetailClicked()
                }, fillMaxSize = false, outerPaddingValues = PaddingValues(), innerPaddingValues = PaddingValues(vertical = 6.dp, horizontal = 10.dp)
            ) {
                IconText(text = "详情", fontSize = 11.sp, modifier = Modifier) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_info),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        tint = Color.White
                    )
                }
            }
        }
    }
}