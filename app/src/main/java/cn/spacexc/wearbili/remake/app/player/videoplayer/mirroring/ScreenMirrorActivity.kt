package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna.DlnaDeviceDiscoverActivity
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

/**
 * Created by XC-Qan on 2023/7/16.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class ScreenMirrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TitleBackground(title = "投屏", onBack = ::finish) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "选择投屏方式:")
                    Card(modifier = Modifier.fillMaxWidth(), onClick = {
                        startActivity(
                            Intent(
                                this@ScreenMirrorActivity,
                                DlnaDeviceDiscoverActivity::class.java
                            )
                        )
                    }) {
                        Column {
                            Text(text = "DLNA")
                            Text(text = "最高支持720p")
                        }
                    }
                }
            }
        }
    }
}