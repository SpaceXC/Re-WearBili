package cn.spacexc.wearbili.remake.app.settings.scaling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.common.ifNullOrZero
import cn.spacexc.wearbili.remake.app.settings.LocalConfiguration
import cn.spacexc.wearbili.remake.app.settings.SettingsManager
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.GradientSlider
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.isRound
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.titleBackgroundHorizontalPadding
import cn.spacexc.wearbili.remake.proto.settings.copy
import kotlinx.coroutines.launch

class ScaleAdjustingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TitleBackground(title = "", onRetry = { }, onBack = ::finish) {
                val configuration = LocalConfiguration.current
                var scale by remember {
                    mutableFloatStateOf(configuration.screenDisplayScaleFactor.ifNullOrZero {
                        setScale(1f)
                        1f
                    }.toFloat())
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = titleBackgroundHorizontalPadding(),
                            vertical = titleBackgroundHorizontalPadding()
                        )
                ) {
                    Text(
                        text = "缩放调整",
                        color = Color.White,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Text(
                        text = "在下方条型区域点击以调整控件缩放",
                        color = Color.White,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Text(
                        text = "点击此处恢复默认",
                        color = BilibiliPink,
                        fontFamily = wearbiliFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickVfx(onClick = {
                                setScale(1f)
                                scale = 1f
                            }),
                        textAlign = if (isRound()) TextAlign.Center else TextAlign.Start
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GradientSlider(value = scale, range = 0.5f..2f, onValueChanged = {
                            scale = it
                        }, onSlideFinished = {
                            setScale(scale)
                        }, modifier = Modifier.weight(1f))
                        Box(contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = "${(scale * 100).toInt()}%",
                                fontFamily = wearbiliFontFamily,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Text(
                                text = "1000%",
                                fontFamily = wearbiliFontFamily,
                                fontSize = 14.sp,
                                color = Color.Transparent
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setScale(scale: Float) {
        lifecycleScope.launch {
            SettingsManager.updateConfiguration {
                copy {
                    screenDisplayScaleFactor = scale
                }
            }
        }
    }
}