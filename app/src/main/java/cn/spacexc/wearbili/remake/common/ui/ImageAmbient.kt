package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.transform.CustomBlurTransformation

@Composable
fun ImageAmbient(modifier: Modifier = Modifier, url: String, scale: Float = 1.5f, translateFactor: Float = 0.15f) {
    val context = LocalContext.current
    Box(modifier = modifier.scale(scale)) {
        BiliImage(
            url = url,
            contentDescription = null,
            transformations = listOf(
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f)
            ),
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    //scale(scale, scale) {
                        translate(left = translateFactor * size.width) {
                            this@drawWithContent.drawContent()
                            drawRect(
                                brush = Brush.radialGradient(
                                    listOf(Color.Black, Color.Transparent)
                                ),
                                blendMode = BlendMode.DstIn,
                            )
                        }
                    //}
                    //drawContent()
                },
            contentScale = ContentScale.Crop,

            )
        BiliImage(
            url = url,
            contentDescription = null,
            transformations = listOf(
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f),
                CustomBlurTransformation(context, 25f)
            ),
            modifier = Modifier
                .scale(scale)
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    //scale(scale, scale) {
                        translate(left = -translateFactor * size.width) {
                            this@drawWithContent.drawContent()
                            drawRect(
                                brush = Brush.radialGradient(
                                    listOf(Color.Black, Color.Transparent)
                                ),
                                blendMode = BlendMode.DstIn,
                            )
                        }
                    //}
                    //drawContent()
                },
            contentScale = ContentScale.Crop,

            )
    }
}