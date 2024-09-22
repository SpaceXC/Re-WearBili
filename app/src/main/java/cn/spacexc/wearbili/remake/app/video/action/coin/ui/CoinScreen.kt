package cn.spacexc.wearbili.remake.app.video.action.coin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding

@kotlinx.serialization.Serializable
data class CoinScreen(
    val videoIdType: String,
    val videoId: String
)

@Composable
fun CoinScreen(
    navController: NavController,
    viewModel: CoinViewModel = hiltViewModel(),
    videoIdType: String,
    videoId: String
) {
    TitleBackground(
        navController = navController,
        title = "",
        onRetry = { /*因为如果失败了的话会直接返回所以这里就不用做重试了*/ },
        onBack = navController::navigateUp,
        uiState = viewModel.uiState
    ) {
        var coinCount by remember {
            mutableIntStateOf(1)
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                horizontal = titleBackgroundHorizontalPadding()
            )
        ) {
            Text(
                text = "给UP主投${coinCount}枚硬币",
                fontFamily = wearbiliFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(Modifier.weight(1f)) {
                Card(
                    isHighlighted = coinCount == 1,
                    onClick = { coinCount = 1 },
                    fillMaxSize = false,
                    modifier = Modifier.weight(1f),
                    outerPaddingValues = PaddingValues(end = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.img_coin_22_x1),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Card(
                    isHighlighted = coinCount == 2,
                    onClick = { coinCount = 2 },
                    fillMaxSize = false,
                    modifier = Modifier.weight(1f),
                    outerPaddingValues = PaddingValues(start = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.img_coin_33_x2),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Card(modifier = Modifier, onClick = {
                viewModel.coin(coinCount, videoIdType, videoId, navController)
            }, contentAlignment = Alignment.Center) {
                Text(
                    text = "确定",
                    fontFamily = wearbiliFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}