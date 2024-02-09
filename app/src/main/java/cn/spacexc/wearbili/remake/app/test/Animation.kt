package cn.spacexc.wearbili.remake.app.test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.delay

@Composable
fun AnimationDemo() {
    val localDensity = LocalDensity.current
    var alphaChange by remember {
        mutableStateOf(false)
    }
    var mainTextAppear by remember {
        mutableStateOf(false)
    }
    var buttonAppear by remember {
        mutableStateOf(false)
    }

    val alpha by animateFloatAsState(
        targetValue = if (alphaChange) 1f else 0f,
        label = "",
        animationSpec = tween(durationMillis = 700)
    )
    val spacerHeight by animateDpAsState(
        targetValue = if (buttonAppear) 0.dp else 24.dp,
        animationSpec = tween(durationMillis = 700),
        label = ""
    )
    val textPadding by animateDpAsState(
        targetValue = if (buttonAppear) 0.dp else 16.dp,
        animationSpec = tween(durationMillis = 700),
        label = ""
    )

    val spacing by animateDpAsState(
        targetValue = if (buttonAppear) 18.dp else 2.dp,
        animationSpec = tween(1000)
    )


    LaunchedEffect(key1 = Unit) {
        delay(500)
        alphaChange = true
    }
    LaunchedEffect(key1 = Unit) {
        delay(1000)
        mainTextAppear = true
    }
    LaunchedEffect(key1 = Unit) {
        delay(2100)
        buttonAppear = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 10.dp, top = 10.dp)
                    .size(16.dp)
                    .align(Alignment.Start)
            )
            Image(
                painter = painterResource(id = R.drawable.icon_liang),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            AnimatedVisibility(visible = mainTextAppear,
                enter = slideInVertically(
                    tween(durationMillis = 700)
                ) { it / 2 } + fadeIn(tween(durationMillis = 700)),
                label = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                Column(
                    modifier = Modifier
                        .padding(top = textPadding)
                        .animateContentSize(tween(durationMillis = 400)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = Color(164, 203, 63))) {
                                    append("凉腕")
                                }
                                append("播放器")
                            },
                            fontFamily = wearbiliFontFamily,
                            fontSize = 18.spx,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        AlphaBadge()
                    }
                    Spacer(modifier = Modifier.height(spacerHeight))
                    Text(
                        text = "方寸之间 大千世界",
                        fontFamily = wearbiliFontFamily,
                        fontSize = 14.spx,
                        color = Color.White
                    )

                }
            }
        }
        AnimatedVisibility(visible = buttonAppear,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 10.dp),
            enter = slideInVertically(
                tween(durationMillis = 700)
            ) { it / 2 } + fadeIn(tween(durationMillis = 700)),
            label = "") {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 18.dp)
                    .fillMaxWidth()
                    .background(Color(164, 203, 63), CircleShape)
                    .padding(8.dp), contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = 4.dp)
                ) {
                    Text(
                        text = "轻触开始",
                        fontFamily = wearbiliFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.spx,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(spacing))
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AlphaBadge() {
    androidx.compose.material.Text(
        text = "Alpha",
        modifier = Modifier
            .offset(y = 3.dp)
            .background(
                color = Color(164, 203, 63), shape = RoundedCornerShape(
                    topStart = 7.dp, topEnd = 7.dp, bottomEnd = 7.dp, bottomStart = 2.dp
                )
            )
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
        fontFamily = wearbiliFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 8.spx
    )
}