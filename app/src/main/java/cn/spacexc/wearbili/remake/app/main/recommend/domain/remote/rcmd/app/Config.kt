package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class Config(
    val auto_refresh_time: Int,
    val auto_refresh_time_by_active: Int,
    val auto_refresh_time_by_appear: Int,
    val autoplay_card: Int,
    val card_density_exp: Int,
    val column: Int,
    val enable_rcmd_guide: Boolean,
    val feed_clean_abtest: Int,
    val history_cache_size: Int,
    val home_transfer_test: Int,
    val inline_sound: Int,
    val is_back_to_homepage: Boolean,
    val show_inline_danmaku: Int,
    val toast: Toast,
    val trigger_loadmore_left_line_num: Int,
    val visible_area: Int
)