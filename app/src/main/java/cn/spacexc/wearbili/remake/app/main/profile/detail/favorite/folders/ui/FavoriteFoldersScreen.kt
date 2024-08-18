package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.folders.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.detail.ui.FavouriteFolderDetailScreen
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@kotlinx.serialization.Serializable
object FavoriteFoldersScreen

@Composable
fun FavoriteFoldersScreen(
    viewModel: FavoriteFolderViewModel = viewModel(),
    navController: NavController
) {
    val defaultFolder = viewModel.folders.find { it?.data?.title == "默认收藏夹" }
    val otherFolders = viewModel.folders.filter { it?.data?.title != "默认收藏夹" }
    LaunchedEffect(key1 = Unit) {
        viewModel.getFolders()
    }
    TitleBackground(
        title = "个人收藏",
        onBack = navController::navigateUp,
        uiState = viewModel.uiState,
        onRetry = viewModel::getFolders
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            defaultFolder?.let {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        innerPaddingValues = PaddingValues(8.dp),
                        onClick = {
                            navController.navigate(
                                FavouriteFolderDetailScreen(
                                    defaultFolder.data.id,
                                    defaultFolder.data.title
                                )
                            )
                        }
                    ) {
                        Column {
                            BiliImage(
                                url = defaultFolder.data.cover,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 10f)
                                    .clip(
                                        RoundedCornerShape(6.dp)
                                    ),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = defaultFolder.data.title,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            IconText(
                                text = "${defaultFolder.data.media_count}",
                                modifier = Modifier.alpha(0.7f),
                                fontSize = 11.sp
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.VideoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
            otherFolders.forEach {
                it?.let { folder ->
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            innerPaddingValues = PaddingValues(8.dp),
                            onClick = {
                                navController.navigate(
                                    FavouriteFolderDetailScreen(
                                        folder.data.id,
                                        folder.data.title
                                    )
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BiliImage(
                                    url = folder.data.cover,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16f / 10f)
                                        .clip(
                                            RoundedCornerShape(6.dp)
                                        )
                                        .weight(3f),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(
                                    modifier = Modifier.weight(4f)
                                ) {
                                    Text(
                                        text = folder.data.title,
                                        fontFamily = wearbiliFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    IconText(
                                        text = "${folder.data.media_count}",
                                        modifier = Modifier.alpha(0.7f),
                                        fontSize = 10.sp
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.VideoLibrary,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}