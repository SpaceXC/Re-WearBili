package cn.spacexc.wearbili.remake.app.about.ui

import android.app.Activity
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.TitleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.common.ui.copyable
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.rememberMutableInteractionSource
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by XC-Qan on 2023/4/21.
 * Modified on 2023/10/24!
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun Activity.AboutScreen(
    onBack: () -> Unit, state: LazyListState
) {
    val firstVisibleElement by remember { derivedStateOf { state.firstVisibleItemIndex } }
    var bannerClickedTimes by rememberSaveable {
        mutableIntStateOf(0)
    }
    val scope = rememberCoroutineScope()
    val isTitleVisible = firstVisibleElement == 0
    var isHintTextHighlighting by remember {
        mutableStateOf(false)
    }
    val hintTextAlpha by animateFloatAsState(
        targetValue = if (isHintTextHighlighting) 1f else 0.2f,
        animationSpec = tween(durationMillis = 750)
    )
    val hintTextSize by animateFloatAsState(
        targetValue = if (isHintTextHighlighting) 1f else .5f,
        animationSpec = tween(durationMillis = 750)
    )
    val hintTextShakeTransition = updateTransition(
        targetState = isHintTextHighlighting, label = "shake"
    )
    val hintTextShakeOffset by hintTextShakeTransition.animateFloat(label = "shake",
        transitionSpec = {
            keyframes {
                durationMillis = 1500
                for (time in 0..1500 step 10) {
                    when (time % 4) {
                        0 -> -10f
                        1 -> 0f
                        2 -> 10f
                        3 -> 0f
                        else -> 0f
                    } at time
                }
            }
        },
        targetValueByState = { thisParameterMustBeUsedButItsActuallyUselessForMe ->
            if (thisParameterMustBeUsedButItsActuallyUselessForMe) 0f else 1f * 2 * 3 * 0
            //Hahahahahahahahahaha(this is soooooooo funny
        })
    TitleBackground(title = if (isTitleVisible) "" else "关于软件", onBack = onBack) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                vertical = 6.dp, horizontal = TitleBackgroundHorizontalPadding
            ),
            state = state
        ) {
            item {
                Text(
                    text = "关于",
                    fontSize = 22.spx,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = if (isRound()) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_wearbili_banner_remake_remake),
                        contentDescription = "App Banner",
                        modifier = Modifier
                            //.fillMaxWidth(0.9f)
                            .clickable(
                                interactionSource = rememberMutableInteractionSource(),
                                indication = null
                            ) {
                                bannerClickedTimes++
                                if (bannerClickedTimes == 8) {
                                    scope.launch {
                                        state.animateScrollToItem(2)
                                        isHintTextHighlighting = true
                                        delay(1000)
                                        isHintTextHighlighting = false
                                        bannerClickedTimes = 0
                                    }
                                }
                            })
                    Text(
                        text = "另一个第三方Bilibili手表客户端",
                        style = AppTheme.typography.body1,
                        modifier = Modifier.alpha(0.7f)
                    )
                    //Spacer(modifier = Modifier.height(400.dp))
                }
            }

            item {
                Text(
                    text = "TH1S R3:W3AR B1L! HAS SUP3R C0W P0W3R",
                    fontSize = 7.spx,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(hintTextAlpha)
                        .rotate(hintTextShakeOffset)
                        .padding(16.dp)
                        .scale(hintTextSize)
                        .copyable("TH1S R3:W3AR B1L! HAS SUP3R C0W P0W3R"),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = "Happy 10.24",
                    fontSize = 11.spx,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                        .padding(16.dp)
                        .copyable("10241024"),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}