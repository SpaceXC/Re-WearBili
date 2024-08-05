package cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.video

data class VideoContent(
    val attach_msg: Any,
    val bvid: String,
    val cover: String,
    val danmaku: Int,
    val desc: String,
    val pub_date: Int,
    val rid: Long,
    val times: Int,
    val title: String,
    val type_: Int,
    val view: Int
)