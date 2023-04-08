package cn.spacexc.wearbili.remake.app.main.recommend.remote.rcmd.app

data class Video(
    val auto_play: Boolean,
    val auto_play_value: Int,
    val avid: Long,
    val biz_id: Long,
    val btn_dyc_color: Boolean,
    val btn_dyc_time: Int,
    val cid: Long,
    val cover: String,
    val egg_end_time: Int,
    val egg_start_time: Int,
    val ep_id: Long,
    val from: String,
    val from_spmid: String,
    val orientation: Int,
    val page: Int,
    val press_trigger_time: Int,
    val season_id: Long,
    val url: String
)