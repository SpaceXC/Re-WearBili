package cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app

data class FeedbackPanel(
    val close_rec_tips: String,
    val feedback_panel_detail: List<cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.FeedbackPanelDetail>,
    val open_rec_tips: String,
    val panel_type_text: String,
    val toast: String
)