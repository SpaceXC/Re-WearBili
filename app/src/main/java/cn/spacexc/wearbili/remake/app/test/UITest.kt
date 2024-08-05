package cn.spacexc.wearbili.remake.app.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import coil.transform.CustomBlurTransformation

class UITest : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*CirclesBackground {

                //OffsetBigIcon(imageVector = Icons.Outlined.Policy, offset = Offset(0.2f, 0.3f))
                //AnimationDemo()
                //ToastUtils.ToastContent(content = "这是一个测试内容！")
                *//*var isFinished by remember {
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
                }*//*
                //QrCodeScreen(qrCodeMessage = "扫描二维码", qrCodeContent = "baidu.com")
                *//*Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
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
                })*//*
                *//*var likeButtonState = rememberLikeButtonState()
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

                )*//*
                *//*var count by remember {
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
                }*//*
                //InfiniteRippleEffect()
                *//*var isOn by remember {
                    mutableStateOf(false)
                }
                Switch(isOn = isOn) {
                    isOn = it
                }*//*
                *//*Column {
                    Text(text = "Normal", fontFamily = wearbiliFontFamily, fontWeight = FontWeight.Normal)
                    Text(text = "Medium", fontFamily = wearbiliFontFamily, fontWeight = FontWeight.Medium)
                    Text(text = "SemiBold", fontFamily = wearbiliFontFamily, fontWeight = FontWeight.SemiBold)
                    Text(text = "Bold", fontFamily = wearbiliFontFamily, fontWeight = FontWeight.Bold)
                    Text(text = "Black", fontFamily = wearbiliFontFamily, fontWeight = FontWeight.Black)
                }*//*
                *//*val textMeasurer = rememberTextMeasurer()
                var offset by remember {
                    mutableFloatStateOf(0f)
                }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val measuredTest = textMeasurer.measure("这是一个测试文本")
                    drawText(
                        measuredTest,
                        color = Color.White,
                        topLeft = Offset(x = size.width - offset, y = 0f)
                    )
                    drawText(textMeasurer = textMeasurer, "${size.width}, ${size.width - offset + measuredTest.size.width}", style = TextStyle(color = Color.White))
                }
                GradientSlider(
                    value = offset, range = 0f..300f, modifier = Modifier.align(
                        Alignment.Center
                    )
                ) {
                    offset = it
                }*//*

                *//*Card(modifier = Modifier.fillMaxWidth(), innerPaddingValues = PaddingValues()) {
                    BiliImage(
                        url = "https://i2.hdslb.com/bfs/archive/901704f1e6850cf42de960ec12caffc4d1c73afb.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawWithContent {
                                val colors = listOf(
                                    Color.Black, Color.Transparent
                                )
                                drawContent()
                                drawRect(
                                    brush = Brush.horizontalGradient(colors),
                                    blendMode = BlendMode.DstIn
                                )
                            },
                        contentScale = ContentScale.Crop,

                        )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    ) {

                    }
                }

            }*//*

                //http://i0.hdslb.com/bfs/archive/a21d8a840d46c3724ad5ec2e0f069bae34bd7d92.jpg

            }*/
            val scale = 1.1f
            val translateFactor = 0.2f
            Box(modifier = Modifier) {
                BiliImage(
                    url = "https://i0.hdslb.com/bfs/archive/a21d8a840d46c3724ad5ec2e0f069bae34bd7d92.jpg",
                    contentDescription = null,
                    transformations = listOf(
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawWithContent {
                            scale(scale, scale) {
                                translate(left = translateFactor * size.width) {
                                    this@drawWithContent.drawContent()
                                    drawRect(
                                        brush = Brush.radialGradient(
                                            listOf(Color.Black, Color.Transparent)
                                        ),
                                        blendMode = BlendMode.DstIn,
                                    )
                                }
                            }
                            //drawContent()
                        },
                    contentScale = ContentScale.Crop,

                    )
                BiliImage(
                    url = "https://i0.hdslb.com/bfs/archive/a21d8a840d46c3724ad5ec2e0f069bae34bd7d92.jpg",
                    contentDescription = null,
                    transformations = listOf(
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f),
                        CustomBlurTransformation(this@UITest, 25f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawWithContent {
                            scale(scale, scale) {
                                translate(left = -translateFactor * size.width) {
                                    this@drawWithContent.drawContent()
                                    drawRect(
                                        brush = Brush.radialGradient(
                                            listOf(Color.Black, Color.Transparent)
                                        ),
                                        blendMode = BlendMode.DstIn,
                                    )
                                }
                            }
                            //drawContent()
                        },
                    contentScale = ContentScale.Crop,

                    )
            }
        }
    }
}