package cn.spacexc.wearbili.remake.app.login.qrcode.tv.domain.remote.accesskey

/**
 * Created by XC-Qan on 2023/4/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

data class AccessKeyGetter(
    val code: Int,
    val `data`: Data,
    val status: Boolean,
    val ts: Int
) {
    data class Data(
        val api_host: String,
        val confirm_uri: String,
        val direct_login: Int,
        val has_login: Int,
        val user_info: UserInfo
    )

    data class UserInfo(
        val face: String,
        val mid: Long,
        val uname: String
    )
}