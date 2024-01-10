package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Vip(
    @SerializedName("avatar_subscript")
    val avatarSubscript: Int,
    @SerializedName("avatar_subscript_url")
    val avatarSubscriptUrl: String,
    @SerializedName("due_date")
    val dueDate: Long,
    @SerializedName("label")
    val label: Label,
    @SerializedName("nickname_color")
    val nicknameColor: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("theme_type")
    val themeType: Int,
    @SerializedName("type")
    val type: Int
)