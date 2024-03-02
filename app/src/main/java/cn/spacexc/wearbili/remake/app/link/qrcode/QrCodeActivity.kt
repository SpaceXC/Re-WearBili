package cn.spacexc.wearbili.remake.app.link.qrcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

const val PARAM_QRCODE_CONTENT = "qrcodeContent"
const val PARAM_QRCODE_MESSAGE = "qrcodeMessage"

class QrCodeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val qrCodeContent = intent.getStringExtra(PARAM_QRCODE_CONTENT) ?: "你好像来到了一片荒原..."
        val qrCodeMessage = intent.getStringExtra(PARAM_QRCODE_MESSAGE) ?: ""

        setContent {
            QrCodeScreen(qrCodeContent = qrCodeContent, qrCodeMessage = qrCodeMessage)
        }
    }
}