package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class Adver(
    val adver_desc: String,
    val adver_id: Long,
    val adver_logo: String,
    val adver_name: String,
    val adver_page_url: String,
    val adver_type: Int
)