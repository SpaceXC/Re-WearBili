package cn.spacexc.wearbili.remake.app.link.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cn.spacexc.wearbili.common.domain.qrcode.QRCodeUtil
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

@kotlinx.serialization.Serializable
data class QrCodeScreen(
    val content: String,
    val message: String
)

@Composable
fun QrCodeScreen(
    navController: NavController,
    qrCodeContent: String,
    qrCodeMessage: String
) {
    TitleBackground(
        title = "",
        onRetry = {},
        onBack = navController::navigateUp,
        themeColor = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(fraction = 0.85f)
                    .background(
                        color = Color(
                            15,
                            15,
                            15,
                            255
                        ),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight(0.35f)
                        .fillMaxWidth()
                        .background(
                            color = BilibiliPink,
                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        ),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.img_head_22),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(0.75f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = cn.spacexc.wearbili.remake.R.drawable.img_head_33),
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight(0.75f)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    QRCodeUtil.createQRCodeBitmap(qrCodeContent, 512, 512)?.asImageBitmap()
                        ?.let { bitmap ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.63f)
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .padding(6.dp)
                                    .aspectRatio(1f)
                            ) {
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = qrCodeMessage,
                                fontFamily = wearbiliFontFamily,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                }
            }
        }
    }
}