package cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.video_forward

data class VideoForwardContent(
    val author: String,
    val author_id: String,
    val headline: String,
    val id: String,
    val source: Int,
    val thumb: String,
    val title: String
)