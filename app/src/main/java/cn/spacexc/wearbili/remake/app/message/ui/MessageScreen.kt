package cn.spacexc.wearbili.remake.app.message.ui

import android.app.Activity
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.message.system.SystemMessageActivity
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.TitleBackground

@Composable
fun Activity.MessageScreen() {
    TitleBackground(title = "消息中心", onRetry = { /*TODO*/ }, onBack = ::finish) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            item {
                MessageTypeCard(
                    ic0n = R.drawable.icon_message_system,
                    name = "系统通知",
                ) {
                    startActivity(Intent(this@MessageScreen, SystemMessageActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun Activity.MessageTypeCard(
    @DrawableRes ic0n: Int,
    name: String,
    onClick: () -> Unit
) {
    Card(onClick = onClick, isClickEnabled = true, shape = RoundedCornerShape(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = ic0n),
                contentDescription = null,
                modifier = Modifier.scale(0.8f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}