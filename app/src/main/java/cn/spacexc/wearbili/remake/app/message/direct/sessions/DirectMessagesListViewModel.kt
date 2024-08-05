package cn.spacexc.wearbili.remake.app.message.direct.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.DirectMessageListPagingSource
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.activity.ActivityContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.article.ArticleContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.eighteen.ContentCard18
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.image.ImageContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.notification.NotificationContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.plain.PlainContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.sixteen.ContentCard16
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.video.VideoContent
import cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.video_forward.VideoForwardContent
import com.google.gson.Gson

class DirectMessagesListViewModel : ViewModel() {
    val flow = Pager(config = PagingConfig(1)) {
        DirectMessageListPagingSource()
    }.flow.cachedIn(viewModelScope)
}

fun getMessageContentByString(type: Int, string: String): String? {
    return when (type) {
        1 -> {
            val content = Gson().fromJson(string, PlainContent::class.java)
            content.content
        }

        2 -> {
            "[图片]"
        }

        7 -> {
            val content = Gson().fromJson(string, VideoForwardContent::class.java)
            "[转发]" + content.title
        }

        10 -> {
            val content = Gson().fromJson(string, NotificationContent::class.java)
            content.title
        }

        11 -> {
            val content = Gson().fromJson(string, VideoContent::class.java)
            "[视频]" + content.title
        }

        12 -> {
            val content = Gson().fromJson(string, ArticleContent::class.java)
            "[专栏]" + content.title
        }

        13 -> {
            val content = Gson().fromJson(string, ActivityContent::class.java)
            content.title
        }

        16 -> {
            val content = Gson().fromJson(string, ContentCard16::class.java)
            content.reply_content
        }

        18 -> {
            val content = Gson().fromJson(string, ContentCard18::class.java)
            val text =
                Gson().fromJson(content.content, ContentCard18.ContentCard18Content::class.java)
            text.text
        }

        else -> null
    }


}

fun getMessageContentObjectByString(type: Int, string: String): Any? {
    return when (type) {
        1 -> {  //文字
            Gson().fromJson(string, PlainContent::class.java)
        }

        2 -> {  //图片
            Gson().fromJson(string, ImageContent::class.java)
        }

        7 -> {  //视频转发
            Gson().fromJson(string, VideoForwardContent::class.java)
        }

        10 -> { //通知
            Gson().fromJson(string, NotificationContent::class.java)
        }

        11 -> { //投稿更新
            Gson().fromJson(string, VideoContent::class.java)
        }

        12 -> { //专栏更新
            Gson().fromJson(string, ArticleContent::class.java)
        }

        13 -> { //活动
            Gson().fromJson(string, ActivityContent::class.java)
        }

        16 -> { //?
            Gson().fromJson(string, ContentCard16::class.java)
        }

        18 -> { //灰色小字
            val content = Gson().fromJson(string, ContentCard18::class.java)
            Gson().fromJson(content.content, ContentCard18.ContentCard18Content::class.java)
        }

        else -> null
    }


}