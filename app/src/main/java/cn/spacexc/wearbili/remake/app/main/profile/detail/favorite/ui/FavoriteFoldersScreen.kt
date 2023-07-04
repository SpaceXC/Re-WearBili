package cn.spacexc.wearbili.remake.app.main.profile.detail.favorite.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

/**
 * Created by XC-Qan on 2023/6/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun FavoriteFoldersScreen(
    viewModel: FavoriteFolderViewModel,
    onBack: () -> Unit
) {
    val folders by viewModel.folders.collectAsState(initial = emptyList())
    val defaultFolder = folders.find { it.data.title == "默认收藏夹" }
    TitleBackground(title = "个人收藏", onBack = onBack, uiState = viewModel.uiState) {
        defaultFolder?.let {
            Card(modifier = Modifier.fillMaxWidth(), innerPaddingValues = PaddingValues(4.dp)) {
                defaultFolder.let { metadata ->
                    BiliImage(
                        url = metadata.data.cover, contentDescription = null, modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}