package cn.spacexc.wearbili.remake.app.settings.user

import BiliTextIcon
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.ifNullOrEmpty
import cn.spacexc.wearbili.common.isZeroOrNull
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.login.qrcode.web.ui.QrCodeLoginActivity
import cn.spacexc.wearbili.remake.app.main.ui.MainActivity
import cn.spacexc.wearbili.remake.app.settings.ProvideConfiguration
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.OfficialVerify
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.UserAvatar
import cn.spacexc.wearbili.remake.common.ui.WearBiliAnimatedVisibility
import cn.spacexc.wearbili.remake.common.ui.clickAlpha
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify
import cn.spacexc.wearbili.remake.common.ui.wearBiliAnimateFloatAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class SwitchUserActivity : ComponentActivity() {
    private val viewModel by viewModels<SwitchUserViewModel>()

    @SuppressLint("WearRecents")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var users by mutableStateOf(emptyList<Long>())
        var currentUser: Long? by mutableStateOf(null)
        var isLoaded by mutableStateOf(false)

        lifecycleScope.launch {
            users = UserUtils.getUsers()
            viewModel.getUserInfo()
            Log.d(TAG, "onCreate: ${UserUtils.mid()}")
            currentUser = UserUtils.mid()
            isLoaded = true
        }
        var isTransitioning by mutableStateOf(false)
        var isTitleVisible by mutableStateOf(true)

        setContent {
            ProvideConfiguration {
                val titleAlpha by wearBiliAnimateFloatAsState(
                    targetValue = if (isTitleVisible) 1f else 0f, animationSpec = tween(600)
                )
                val pagerState = rememberPagerState {
                    users.size + 1
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    TitleBackground(
                        title = "是谁在使用？",
                        onRetry = { },
                        onBack = ::finish,
                        titleAlpha = titleAlpha,
                        isTitleClipToBounds = false,
                        //uiState = if(isLoaded) UIState.Success else UIState.Loading
                    ) {
                        if(!currentUser.isZeroOrNull()) {
                                val pageSize = object : PageSize {
                                    override fun Density.calculateMainAxisPageSize(
                                        availableSpace: Int,
                                        pageSpacing: Int
                                    ): Int {
                                        return ((availableSpace - 2 * pageSpacing) * 0.8f).toInt()
                                    }
                                }
                                val coroutineScope = rememberCoroutineScope()
                                var selectedUser by remember {
                                    mutableLongStateOf(currentUser!!)
                                }
                                var isPagerEnabled by remember {
                                    mutableStateOf(true)
                                }
                                var isFinished by remember {
                                    mutableStateOf(false)
                                }
                                var containerHeight by remember { mutableFloatStateOf(0f) }
                                val contentOffset by wearBiliAnimateFloatAsState(
                                    targetValue = if (isFinished) -containerHeight * 1.5f else 0f,
                                    animationSpec = tween(1500)
                                )
                                val contentAlpha by wearBiliAnimateFloatAsState(
                                    targetValue = if (isFinished) 0f else 1f,
                                    animationSpec = tween(1500)
                                )
                                LaunchedEffect(key1 = currentUser) {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage(users.indexOfFirst { it == currentUser })
                                    }
                                }
                                LaunchedEffect(key1 = selectedUser) {
                                    if (selectedUser != currentUser) {
                                        isTitleVisible = false
                                        isPagerEnabled = false
                                        delay(600)
                                        isTransitioning = true
                                        delay(2000)
                                        isTransitioning = false
                                        UserUtils.setCurrentUid(selectedUser)
                                        delay(800)
                                        currentUser = selectedUser
                                        delay(1000)
                                        isFinished = true
                                        delay(1500)
                                        startActivity(
                                            Intent(
                                                this@SwitchUserActivity,
                                                MainActivity::class.java
                                            ).apply {
                                                flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            })
                                        finish()
                                        overridePendingTransition(
                                            cn.spacexc.wearbili.remake.R.anim.activity_fade_in,
                                            cn.spacexc.wearbili.remake.R.anim.activity_fade_out
                                        )
                                    }
                                }
                                Box(modifier = Modifier
                                    .fillMaxSize()
                                    .onSizeChanged {
                                        containerHeight = it.height.toFloat()
                                    }
                                    .offset {
                                        IntOffset(0, contentOffset.roundToInt())
                                    }
                                    .alpha(contentAlpha)) {
                                    VerticalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize(),
                                        pageSize = pageSize,
                                        userScrollEnabled = isPagerEnabled
                                    ) { page ->
                                        val alpha by wearBiliAnimateFloatAsState(
                                            targetValue = if (isTransitioning && selectedUser != users[page.coerceAtMost(
                                                    users.size - 1
                                                )]
                                            ) 0f else 1f,
                                            animationSpec = tween(durationMillis = 800)
                                        )
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .alpha(alpha)
                                                .fillMaxSize()
                                                .clickAlpha {
                                                    if (page < users.size) {
                                                        coroutineScope.launch {
                                                            pagerState.animateScrollToPage(
                                                                page,
                                                                animationSpec = tween(500)
                                                            )
                                                            selectedUser = users[page]
                                                        }
                                                    } else {
                                                        startActivity(
                                                            Intent(
                                                                this@SwitchUserActivity,
                                                                QrCodeLoginActivity::class.java
                                                            )
                                                        )
                                                    }
                                                }
                                                .graphicsLayer {
                                                    // Calculate the absolute offset for the current page from the
                                                    // scroll position. We use the absolute value which allows us to mirror
                                                    // any effects for both directions
                                                    val pageOffset = (
                                                            (pagerState.currentPage - page) + pagerState
                                                                .currentPageOffsetFraction
                                                            ).absoluteValue

                                                    this.alpha = lerp(
                                                        start = 0.3f,
                                                        stop = 1f,
                                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                                    )
                                                    scaleX = lerp(
                                                        start = 0.7f,
                                                        stop = 1f,
                                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                                    )
                                                    scaleY = lerp(
                                                        start = 0.7f,
                                                        stop = 1f,
                                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                                    )
                                                }
                                        ) {
                                            if (viewModel.userInfo.isNotEmpty()) {
                                                if (page < users.size) {
                                                    val userInfo = viewModel.userInfo[page]
                                                    val circleScale by wearBiliAnimateFloatAsState(
                                                        targetValue = if (currentUser == users[page]) 1f else 1.8f,
                                                        animationSpec = tween(1200),
                                                    )
                                                    val circleAlpha by wearBiliAnimateFloatAsState(
                                                        targetValue = if (currentUser == users[page]) 1f else 0f,
                                                        animationSpec = tween(1200),
                                                    )
                                                    val uidFontSize by wearBiliAnimateFloatAsState(
                                                        targetValue = if (userInfo == null) 12f else 9f,
                                                        tween(400)
                                                    )
                                                    val uidAlpha by wearBiliAnimateFloatAsState(
                                                        targetValue = if (userInfo == null) 1f else 0.6f,
                                                        tween(400)
                                                    )

                                                    Box(modifier = Modifier
                                                        .padding(10.dp)
                                                        .drawBehind {
                                                            scale(circleScale) {
                                                                drawCircle(
                                                                    BilibiliPink,
                                                                    radius = size.width / 2,
                                                                    style = Stroke(width = 5f),
                                                                    alpha = circleAlpha.coerceIn(0f..1f)
                                                                )
                                                            }
                                                        }) {
                                                        UserAvatar(
                                                            avatar = userInfo?.data?.face ?: "",
                                                            size = DpSize.Unspecified,
                                                            modifier = Modifier
                                                                .fillMaxWidth(0.5f),
                                                            pendant = userInfo?.data?.pendant?.imageEnhanceFrame
                                                                ?: "",
                                                            officialVerify = userInfo?.data?.official?.type?.toOfficialVerify()
                                                                ?: OfficialVerify.NONE
                                                        )
                                                    }
                                                    WearBiliAnimatedVisibility(
                                                        visible = !isTransitioning,
                                                        exit = shrinkVertically(
                                                            tween(600)
                                                        ) + fadeOut(tween(600)),
                                                        enter = expandVertically(tween(600)) + fadeIn(
                                                            tween(
                                                                600
                                                            )
                                                        )
                                                    ) {
                                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                            WearBiliAnimatedVisibility(
                                                                visible = userInfo?.data?.name != null,
                                                                enter = expandVertically(
                                                                    tween(400)
                                                                ) + fadeIn(tween(400)),
                                                                exit = shrinkVertically(tween(400)) + fadeOut(
                                                                    tween(400)
                                                                )
                                                            ) {
                                                                Text(
                                                                    text = userInfo?.data?.name
                                                                        ?: users[page].toString(),
                                                                    fontFamily = wearbiliFontFamily,
                                                                    fontWeight = FontWeight.Medium,
                                                                    fontSize = 12.sp,
                                                                    color = parseColor(userInfo?.data?.vip?.nicknameColor?.ifNullOrEmpty { "#FFFFFF" }
                                                                        ?: "#FFFFFF"),
                                                                    modifier = Modifier
                                                                )
                                                            }
                                                            Text(
                                                                text = users[page].toString(),
                                                                fontFamily = wearbiliFontFamily,
                                                                fontWeight = FontWeight.Medium,
                                                                fontSize = uidFontSize.sp,
                                                                color = Color.White,
                                                                modifier = Modifier.alpha(uidAlpha)
                                                            )
                                                        }
                                                    }
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.4f)
                                                            .aspectRatio(1f)
                                                            .background(Color(38, 38, 38, 255), CircleShape)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Add,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier
                                                                .fillMaxSize(0.5f)
                                                                .align(Alignment.Center)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "添加",
                                                        fontFamily = wearbiliFontFamily,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 12.sp,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    WearBiliAnimatedVisibility(
                                        visible = isTransitioning,
                                        modifier = Modifier
                                            .align(Alignment.TopCenter)
                                            .offset(y = (-10).dp),
                                        enter = fadeIn(
                                            tween(600)
                                        ) + slideInVertically(tween(600)),
                                        exit = fadeOut(
                                            tween(600)
                                        ) + slideOutVertically(tween(600))
                                    ) {
                                        Text(
                                            text = "正在切换",
                                            fontSize = 15.sp,
                                            fontFamily = wearbiliFontFamily,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    WearBiliAnimatedVisibility(
                                        visible = isTransitioning,
                                        modifier = Modifier.align(Alignment.BottomCenter),
                                        enter = fadeIn(
                                            tween(600)
                                        ) + slideInVertically(tween(600)) { it / 2 },
                                        exit = fadeOut(
                                            tween(600)
                                        ) + slideOutVertically(tween(600)) { it / 2 }
                                    ) {
                                        BiliTextIcon(
                                            icon = "EAED",
                                            size = 18.sp,
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 8.dp)
                                            .alpha(titleAlpha)
                                            .offset(y = (-20).dp),
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        repeat(pagerState.pageCount) {
                                            val indicationAlpha by wearBiliAnimateFloatAsState(targetValue = if (pagerState.currentPage == it) 0.8f else 0.3f)
                                            Box(
                                                modifier = Modifier
                                                    .alpha(indicationAlpha)
                                                    .size(5.dp)
                                                    .background(Color.White, CircleShape)
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