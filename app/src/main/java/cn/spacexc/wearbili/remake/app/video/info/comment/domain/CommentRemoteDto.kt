package cn.spacexc.wearbili.remake.app.video.info.comment.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by XC-Qan on 2023/4/28.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class VideoComment(
    val code: Int,
    val message: String,
    val ttl: Int,
    val data: CommentData
)

data class CommentData(
    val cursor: CommentCursor,
    val hots: List<CommentContentData>,
    val notice: CommentNotice,
    val replies: List<CommentContentData>?,
    val top: TopComment?,
    val upper: UpperInfo
)

data class UpperInfo(
    val mid: Long
)

data class CommentCursor(
    val all_count: Int,
    val is_begin: Boolean,
    val is_end: Boolean,
    val name: String
)

data class CommentNotice(
    val content: String,
    val link: String,
    val id: Long,
    val title: String
)

data class TopComment(val upper: CommentContentData)
data class CommentContentData(
    var rpid: Long,
    var oid: Long,
    var type: Int,
    var mid: Long,
    var root: Long,
    var parent: Long,
    var dialog: Long,
    var count: Int,
    var rcount: Int,
    var state: Int,
    var fansgrade: Int,
    var attr: Long,
    var ctime: Long,
    var rpid_str: String,
    var root_str: String,
    var parent_str: String,
    var like: Int,
    var action: Int,
    var member: Member?,
    var content: Content?,
    var assist: Int,
    var folder: Folder,
    var up_action: UpAction,
    var show_follow: Boolean,
    var invisible: Boolean,
    var reply_control: ReplyControl?,
    var replies: List<Replies>?,
    var card_label: List<CardLabel>,
    var is_top: Boolean = false
) {
    data class Picture(
        val img_src: String
    )

    data class CardLabel(
        var rpid: Long,
        var text_content: String,
        var text_color_day: String,
        var text_color_night: String,
        var label_color_day: String,
        var label_color_night: String,
        var image: String,
        var type: Int,
        var background: String,
        var background_width: Int,
        var background_height: Int,
        var jump_url: String
    )

    data class Member(
        var mid: Long,
        var uname: String,
        var sex: String,
        var sign: String,
        var avatar: String,
        var rank: String,
        var DisplayRank: String,
        var face_nft_new: Int,
        var is_senior_member: Int,
        var level_info: LevelInfo,
        var pendant: Pendant,
        var nameplate: Nameplate,
        var official_verify: OfficialVerify,
        var vip: Vip,
        var fans_detail: Any,
        var following: Int,
        var is_followed: Int,
        var user_sailing: UserSailing,
        var is_contractor: Boolean,
        var contract_desc: String,
        var nft_interaction: Any
    ) {
        data class LevelInfo(
            var current_level: Int,
            var current_min: Int,
            var current_exp: Int,
            var next_exp: Int
        )

        data class Pendant(
            var pid: Long,
            var name: String,
            var image: String,
            var expire: Int,
            var image_enhance: String,
            var image_enhance_frame: String
        )

        data class Nameplate(
            var nid: Long,
            var name: String,
            var image: String,
            var image_small: String,
            var level: String,
            var condition: String
        )

        data class OfficialVerify(var type: Int, var desc: String)
        data class Vip(
            var vipType: Int,
            var vipDueDate: Long,
            var dueRemark: String,
            var accessStatus: Int,
            var vipStatus: Int,
            var vipStatusWarn: String,
            var themeType: Int,
            var label: Label,
            var avatar_subscript: Int,
            var nickname_color: String
        ) {
            data class Label(
                var path: String,
                var text: String,
                var label_theme: String,
                var text_color: String,
                var bg_style: Int,
                var bg_color: String,
                var border_color: String
            )
        }

        data class UserSailing(var pendant: Any, var cardbg: Any, var cardbg_with_focus: Any)
    }

    data class Content(
        var message: String,
        var plat: Int,
        var device: String,
        var max_line: Int,
        var emote: Map<String, EmoteObject>?,
        var jump_url: Map<String, JumpUrlObject>?,
        //var members: List<ReplyAttentionMember>?,
        var at_name_to_mid: Map<String, Long>?,
        var pictures: List<Picture>?
    )

    data class ReplyAttentionMember(
        @SerializedName("avatar")
        val avatar: String,
        @SerializedName("face_nft_new")
        val faceNftNew: Int,
        @SerializedName("is_senior_member")
        val isSeniorMember: Int,
        @SerializedName("level_info")
        val levelInfo: Member.LevelInfo,
        @SerializedName("mid")
        val mid: String,
        @SerializedName("nameplate")
        val nameplate: Member.Nameplate,
        @SerializedName("official_verify")
        val officialVerify: Member.OfficialVerify,
        @SerializedName("pendant")
        val pendant: UserProfilePendant,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("sex")
        val sex: String,
        @SerializedName("sign")
        val sign: String,
        @SerializedName("uname")
        val uname: String,
        @SerializedName("vip")
        val vip: Member.Vip
    )

    data class JumpUrlObject(val title: String, val prefix_icon: String?, val extra: JumpUrlExtra)
    data class JumpUrlExtra(val is_word_search: Boolean)


    data class Folder(var has_folded: Boolean, var is_folded: Boolean, var rule: String)
    data class UpAction(var like: Boolean, var reply: Boolean)
    data class ReplyControl(
        var up_reply: Boolean,
        var sub_reply_entry_text: String,
        var sub_reply_title_text: String,
        var time_desc: String,
        var max_line: Int,
        var location: String?,
    )

    data class Replies(
        var rpid: Long,
        var oid: Long,
        var type: Int,
        var mid: Long,
        var root: Long,
        var parent: Long,
        var dialog: Long,
        var count: Int,
        var rcount: Int,
        var state: Int,
        var fansgrade: Int,
        var attr: Long,
        var ctime: Int,
        var rpid_str: String,
        var root_str: String,
        var parent_str: String,
        var like: Int,
        var action: Int,
        var member: Member,
        var content: CommentContentData.Content?,
        var replies: Any,
        var assist: Int,
        var folder: Folder,
        var up_action: UpAction,
        var show_follow: Boolean,
        var invisible: Boolean,
        var reply_control: ReplyControl
    ) {
        data class Member(
            var mid: Long,
            var uname: String,
            var sex: String,
            var sign: String,
            var avatar: String,
            var rank: String,
            var DisplayRank: String,
            var face_nft_new: Int,
            var is_senior_member: Int,
            var level_info: LevelInfo,
            var pendant: Pendant,
            var nameplate: Nameplate,
            var official_verify: OfficialVerify,
            var vip: Vip,
            var fans_detail: Any,
            var following: Int,
            var is_followed: Int,
            var user_sailing: UserSailing,
            var is_contractor: Boolean,
            var contract_desc: String,
            var nft_interaction: Any
        ) {
            data class LevelInfo(
                var current_level: Int,
                var current_min: Int,
                var current_exp: Int,
                var next_exp: Int
            )

            data class Pendant(
                var pid: Long,
                var name: String,
                var image: String,
                var expire: Int,
                var image_enhance: String,
                var image_enhance_frame: String
            )

            data class Nameplate(
                var nid: Long,
                var name: String,
                var image: String,
                var image_small: String,
                var level: String,
                var condition: String
            )

            data class OfficialVerify(var type: Int, var desc: String)
            data class Vip(
                var vipType: Int,
                var vipDueDate: Long,
                var dueRemark: String,
                var accessStatus: Int,
                var vipStatus: Int,
                var vipStatusWarn: String,
                var themeType: Int,
                var label: Label,
                var avatar_subscript: Int,
                var nickname_color: String
            ) {
                data class Label(
                    var path: String,
                    var text: String,
                    var label_theme: String,
                    var text_color: String,
                    var bg_style: Int,
                    var bg_color: String,
                    var border_color: String
                )
            }

            data class UserSailing(
                var pendant: Pendant,
                var cardbg: Any,
                var cardbg_with_focus: Any
            ) {
                data class Pendant(
                    var id: Long,
                    var name: String,
                    var image: String,
                    var jump_url: String,
                    var type: String,
                    var image_enhance: String,
                    var image_enhance_frame: String
                )
            }
        }

        data class Content(
            var message: String,
            var plat: Int,
            var device: String,
            var max_line: Int,
        )
    }
}

data class EmoteObject(
    val attr: Long,
    val id: Long,
    val jump_title: String,
    val meta: Meta,
    val mtime: Int,
    val package_id: Long,
    val state: Int,
    val text: String,
    val type: Int,
    val url: String
)

data class Meta(
    val size: Int
)

data class UserProfilePendant(
    val expire: Int,
    val image: String,
    var image_enhance: String?,
    val image_enhance_frame: String,
    val name: String,
    val pid: Long
)

data class VIPLabel(
    val text: String,
    val bg_color: String,
)