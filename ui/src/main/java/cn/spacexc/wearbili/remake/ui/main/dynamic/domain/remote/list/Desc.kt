package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class Desc(
    @SerializedName("rich_text_nodes")
    val richTextNodes: List<ItemRichTextNode>,
    @SerializedName("text")
    val text: String
)