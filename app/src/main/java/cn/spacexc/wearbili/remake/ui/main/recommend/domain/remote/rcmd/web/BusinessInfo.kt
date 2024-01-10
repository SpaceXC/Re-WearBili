package cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web


import com.google.gson.annotations.SerializedName

data class BusinessInfo(
    @SerializedName("activity_type")
    val activityType: Int,
    @SerializedName("ad_cb")
    val adCb: String,
    @SerializedName("ad_desc")
    val adDesc: String,
    @SerializedName("adver_name")
    val adverName: String,
    @SerializedName("agency")
    val agency: String,
    @SerializedName("area")
    val area: Int,
    @SerializedName("asg_id")
    val asgid: Long,
    @SerializedName("business_mark")
    val businessMark: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web.BusinessMark,
    @SerializedName("card_type")
    val cardType: Int,
    @SerializedName("cm_mark")
    val cmMark: Int,
    @SerializedName("contract_id")
    val contractId: String,
    @SerializedName("creative_id")
    val creativeid: Long,
    @SerializedName("creative_type")
    val creativeType: Int,
    @SerializedName("epid")
    val epid: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("inline")
    val `inline`: cn.spacexc.wearbili.remake.ui.main.recommend.domain.remote.rcmd.web.Inline,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("is_ad")
    val isAd: Boolean,
    @SerializedName("is_ad_loc")
    val isAdLoc: Boolean,
    @SerializedName("label")
    val label: String,
    @SerializedName("litpic")
    val litpic: String,
    @SerializedName("mid")
    val mid: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("null_frame")
    val nullFrame: Boolean,
    @SerializedName("operater")
    val operater: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("pic_main_color")
    val picMainColor: String,
    @SerializedName("pos_num")
    val posNum: Int,
    @SerializedName("request_id")
    val requestId: String,
    @SerializedName("res_id")
    val resid: Long,
    @SerializedName("server_type")
    val serverType: Int,
    @SerializedName("src_id")
    val srcid: Long,
    @SerializedName("stime")
    val stime: Int,
    @SerializedName("style")
    val style: Int,
    @SerializedName("sub_title")
    val subTitle: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)