package cn.spacexc.wearbili.remake.app.video.info.info.ui.v2.basic

import BiliTextIcon
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.player.audio.AudioPlayerActivity
import cn.spacexc.wearbili.remake.app.player.videoplayer.defaultplayer.Media3PlayerActivity
import cn.spacexc.wearbili.remake.app.video.info.info.ui.VideoInformationViewModel
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_CID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VIDEO_TYPE_BVID
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.ImageAmbient
import cn.spacexc.wearbili.remake.common.ui.OutlinedRoundButton
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SimpleVideoInformation(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    context: Context,
    viewModel: VideoInformationViewModel,
    onGoToDetail: () -> Unit
) {
    viewModel.state.videoData?.let { video ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = -8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ImageAmbient(url = video.view.pic, scale = 1.1f)
                BiliImage(
                    url = video.view.pic,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .aspectRatio(16f / 10f)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = video.view.title,
                style = TextStyle(
                    fontFamily = wearbiliFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = with(sharedTransitionScope) {
                    Modifier
                        .offset(y = -4.dp)
                        .fillMaxWidth()
                        .padding(horizontal = titleBackgroundHorizontalPadding())
                        .sharedBounds(sharedContentState = rememberSharedContentState(key = "title"), animatedVisibilityScope = animatedContentScope)
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                minLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                //.padding(bottom = 8.dp)
                ,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedRoundButton(
                    modifier = Modifier.weight(1f),
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAB4", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {
                        Intent(context, AudioPlayerActivity::class.java).apply {
                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            putExtra(PARAM_VIDEO_ID, video.view.bvid)
                            putExtra(PARAM_VIDEO_CID, video.view.cid.logd("cid"))
                            context.startActivity(this)
                        }
                    }
                )
                OutlinedRoundButton(
                    modifier = Modifier.weight(1f),
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAF7", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {
                        Intent(
                            context,
                            Media3PlayerActivity::class.java
                        ).apply {
                            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
                            putExtra(PARAM_VIDEO_ID, video.view.bvid)
                            putExtra(PARAM_VIDEO_CID, video.view.cid.logd("cid"))
                            context.startActivity(this)
                        }
                    }
                )
                OutlinedRoundButton(
                    modifier = Modifier.weight(1f),
                    buttonModifier = Modifier.aspectRatio(1f),
                    interactionSource = rememberMutableInteractionSource(), icon = {
                        Row(
                            modifier = Modifier.align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BiliTextIcon(icon = "EAC0", size = 18.sp)
                        }
                    },
                    text = "",
                    onClick = {
                        onGoToDetail()
                    }
                )
            }
        }
    }
}