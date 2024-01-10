package cn.spacexc.wearbili.remake.ui.video.action.favourite.ui

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.CardBackgroundColor
import cn.spacexc.wearbili.remake.common.ui.CardBorderColor
import cn.spacexc.wearbili.remake.common.ui.CardBorderWidth
import cn.spacexc.wearbili.remake.common.ui.Checkbox
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/8/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Activity.VideoFavouriteFoldersScreen(
    viewModel: VideoFavouriteViewModel
) {
    val localDensity = LocalDensity.current
    var confirmButtonHeight by remember {
        mutableStateOf(0.dp)
    }

    TitleBackground(title = "收藏视频", uiState = viewModel.uiState, onBack = ::finish) {
        Box(modifier = Modifier.fillMaxSize()) {
            val lazyColumnButtonContentPadding by animateDpAsState(
                targetValue = if (viewModel.idsToAdd.isNotEmpty() || viewModel.idsToDelete.isNotEmpty()) confirmButtonHeight else 0.dp,
                label = "wearbiliVideoFavouriteScreenLazyColumnButtonContentPadding"
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp + lazyColumnButtonContentPadding
                )/*(horizontal = 12.dp, vertical = 6.dp)*/
            ) {
                viewModel.folders.forEach { folder ->
                    item {
                        val isHighlighted = if (folder.fav_state == 0) {
                            viewModel.idsToAdd.contains(
                                folder.id
                            )
                        } else {
                            !(viewModel.idsToDelete.contains(
                                folder.id
                            ))
                        }
                        val cardBorderColor by animateColorAsState(
                            targetValue = if (isHighlighted) BilibiliPink else CardBorderColor,
                            label = "wearbiliVideoFavouriteFoldersScreenBorderColor",
                            animationSpec = tween()
                        )
                        val cardBackgroundColor by animateColorAsState(
                            targetValue = if (isHighlighted) Color(
                                231,
                                86,
                                136,
                                26
                            ) else CardBackgroundColor,
                            label = "wearbiliVideoFavouriteFoldersScreenBackgroundColor",
                            animationSpec = tween()

                        )
                        val width by animateDpAsState(
                            targetValue = if (isHighlighted) 2.dp else CardBorderWidth,
                            label = "wearbiliVideoFavouriteFoldersScreenDp",
                            animationSpec = tween()
                        )

                        Card(
                            innerPaddingValues = PaddingValues(14.dp),
                            shape = RoundedCornerShape(35),
                            backgroundColor = cardBackgroundColor,
                            onClick = {
                                if (folder.fav_state == 0) {
                                    val temp = viewModel.idsToAdd.toMutableList()
                                    if (temp.contains(folder.id)) {
                                        temp.remove(folder.id)
                                    } else {
                                        temp.add(folder.id)
                                    }
                                    viewModel.idsToAdd = temp.toList()
                                } else if (folder.fav_state == 1) {
                                    val temp = viewModel.idsToDelete.toMutableList()
                                    if (temp.contains(folder.id)) {
                                        temp.remove(folder.id)
                                    } else {
                                        temp.add(folder.id)
                                    }
                                    viewModel.idsToDelete = temp.toList()
                                }
                            }, borderColor = cardBorderColor, borderWidth = width,
                            isGradient = !isHighlighted
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(isChecked = isHighlighted) {
                                    if (folder.fav_state == 0) {
                                        val temp = viewModel.idsToAdd.toMutableList()
                                        if (temp.contains(folder.id)) {
                                            temp.remove(folder.id)
                                        } else {
                                            temp.add(folder.id)
                                        }
                                        viewModel.idsToAdd = temp.toList()
                                    } else if (folder.fav_state == 1) {
                                        val temp = viewModel.idsToDelete.toMutableList()
                                        if (temp.contains(folder.id)) {
                                            temp.remove(folder.id)
                                        } else {
                                            temp.add(folder.id)
                                        }
                                        viewModel.idsToDelete = temp.toList()
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = folder.title,
                                    fontSize = 13.spx,
                                    fontFamily = wearbiliFontFamily,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = viewModel.idsToAdd.isNotEmpty() || viewModel.idsToDelete.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                enter = slideInVertically { it / 2 } + fadeIn(),
                exit = slideOutVertically { it / 2 } + fadeOut(),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged {
                            confirmButtonHeight = with(localDensity) {
                                it.height.toDp()
                            }
                        },
                    outerPaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    innerPaddingValues = PaddingValues(12.dp),
                    shape = RoundedCornerShape(30),
                    onClick = {
                        viewModel.confirmAction(callback = { isStillFavourite ->
                            val resultIntent =
                                Intent().apply { putExtra("isStillFavourite", isStillFavourite) }
                            setResult(0, resultIntent)
                            finish()
                        }, context = this@VideoFavouriteFoldersScreen)
                    }
                ) {
                    Text(
                        text = "确定",
                        fontSize = 13.spx,
                        fontFamily = wearbiliFontFamily,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}