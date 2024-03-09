package cn.spacexc.wearbili.remake.app.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cn.spacexc.wearbili.remake.app.welcome.screens.InfiniteRippleEffect
import cn.spacexc.wearbili.remake.common.ui.CirclesBackground

class UITest : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CirclesBackground {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    //OffsetBigIcon(imageVector = Icons.Outlined.Policy, offset = Offset(0.2f, 0.3f))
                    //AnimationDemo()
                    //ToastUtils.ToastContent(content = "这是一个测试内容！")
                    /*var isFinished by remember {
                        mutableStateOf(false)
                    }
                    Column {
                        SanlianDemo(isFinished, {
                            ToastUtils.showText("三连爆棚，感谢推荐！")
                            isFinished = true
                        }, {
                            ToastUtils.showText("你已经三连过咯！")
                        })
                        Button(onClick = { isFinished = false }) {
                            Text(text = "reset")
                        }
                    }*/
                    //QrCodeScreen(qrCodeMessage = "扫描二维码", qrCodeContent = "baidu.com")
                    /*Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                        val stroke = Stroke(5f)
                        val diameterOffset = stroke.width / 2
                        val arcDimen = size.width - 2 * diameterOffset
                        drawArc(
                            color = BilibiliPink,
                            startAngle = -90f,
                            sweepAngle = -360f,
                            useCenter = false,
                            topLeft = Offset(diameterOffset, diameterOffset),
                            size = Size(arcDimen, arcDimen),
                            style = stroke
                        )
                    })*/
                    /*var likeButtonState = rememberLikeButtonState()
                    var isNormal by remember {
                        mutableStateOf(false)
                    }
                    val scope = rememberCoroutineScope()
                    LikeButton(
                        likeButtonState = likeButtonState,
                        bubbleColor = BubbleColor(
                            dotPrimaryColor = BilibiliPink,
                            dotSecondaryColor = BilibiliPink,
                            dotThirdColor = BilibiliPink,
                            dotLastColor = BilibiliPink
                        ),
                        circleColor = CircleColor(BilibiliPink, BilibiliPink),
                        likeContent = { isLiked ->
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = "",
                                tint = if (isLiked) BilibiliPink else Color.Gray,
                            )
                        },
                        modifier = Modifier.align(Alignment.Center),

                    )*/
                    /*var count by remember {
                        mutableIntStateOf(50)
                    }
                    Column {
                        AnimatedCounter(
                            count = count, style = TextStyle(
                                fontFamily = wearbiliFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        )
                        Button(onClick = { count++ }) {
                            Text(text = "+")
                        }
                        Button(onClick = { count-- }) {
                            Text(text = "-")
                        }
                    }*/
                    InfiniteRippleEffect()
                }
            }
        }
    }
}