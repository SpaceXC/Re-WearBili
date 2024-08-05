package cn.spacexc.wearbili.remake.app.message.direct.sessions.domain.content.notification

data class NotificationContent(
    val biz_content: Any,
    val jump_text: String,
    val jump_text_2: String,
    val jump_text_3: String,
    val jump_uri: String,
    val jump_uri_2: String,
    val jump_uri_2_config: JumpUri2Config,
    val jump_uri_3: String,
    val jump_uri_3_config: JumpUri2Config,
    val jump_uri_config: JumpUriConfig,
    val modules: List<Module>,
    val notifier: Any,
    val text: String,
    val title: String
)