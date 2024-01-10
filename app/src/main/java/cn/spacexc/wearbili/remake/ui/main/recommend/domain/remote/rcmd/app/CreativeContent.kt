package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.app

data class CreativeContent(
    val description: String,
    val image_md5: String,
    val image_url: String,
    val title: String,
    val url: String,
    val video_id: Long
)