package cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.article

data class ArticleContent(
    val attach_msg: Any,
    val author: String,
    val image_urls: List<String>,
    val like: Int,
    val pub_date: Int,
    val reply: Int,
    val rid: Long,
    val summary: String,
    val template_id: Long,
    val title: String,
    val view: Int
)