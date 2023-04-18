package cn.spacexc.wearbili.remake.app.main.profile.remote

data class Pendant(
    val expire: Int,
    val image: String?,
    val image_enhance: String?,
    val image_enhance_frame: String?,
    val name: String,
    val pid: Long
)