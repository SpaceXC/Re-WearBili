package cn.spacexc.wearbili.remake.app.settings.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.lifecycleScope
import cn.spacexc.bilibilisdk.utils.UserUtils
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import kotlinx.coroutines.launch

class SwitchUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var users = emptyList<Long>()
        var currentUser = 0L
        lifecycleScope.launch {
            users = UserUtils.getUsers()
            currentUser = UserUtils.mid() ?: 0
        }
        setContent {
            TitleBackground(title = "切换用户", onRetry = { }, onBack = ::finish) {
                LazyColumn {
                    items(users) {
                        Card(isHighlighted = it == currentUser, onClick = {
                            if (it == currentUser) return@Card
                            lifecycleScope.launch {
                                UserUtils.setCurrentUid(it)
                                startActivity(
                                    Intent(
                                        this@SwitchUserActivity,
                                        SplashScreenActivity::class.java
                                    ).apply {
                                        flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK// || Intent.FLAG_ACTIVITY_
                                    })
                            }
                        }) {
                            Text(
                                text = it.toString(),
                                fontFamily = wearbiliFontFamily,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}