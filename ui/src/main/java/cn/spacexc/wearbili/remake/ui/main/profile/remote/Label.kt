package cn.spacexc.wearbili.remake.app.main.profile.remote

data class Label(
    val bg_color: String,
    val bg_style: Int,
    val border_color: String,
    val img_label_uri_hans: String,
    val img_label_uri_hans_static: String,
    val img_label_uri_hant: String,
    val img_label_uri_hant_static: String,
    val label_theme: String,
    val path: String,
    val text: String,
    val text_color: String,
    val use_img_label: Boolean
)