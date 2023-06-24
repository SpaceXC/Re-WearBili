package cn.spacexc.wearbili.common.domain.user

import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.common.domain.user.remote.UserExitResult
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class UserManager @Inject constructor(
    private val networkUtils: KtorNetworkUtils,
    private val dataManager: DataManager
) {
    suspend fun isUserLoggedIn(): Boolean =
        !networkUtils.getCookie("SESSDATA").logd("SESSDATA").isNullOrEmpty()
    suspend fun mid(): Long? = networkUtils.getCookie("DedeUserID")?.toLong()
    suspend fun csrf(): String? = networkUtils.getCookie("bili_jct")
    suspend fun accessKey(): String? = dataManager.getString("accessKey", null)
    suspend fun webiSign(): String? = dataManager.getString("webi_signature_key", null)

    suspend fun logout(): Boolean {
        val form = mapOf(
            "biliCSRF" to (csrf() ?: "")
        )
        val response = networkUtils.post<UserExitResult>(
            url = "https://passport.bilibili.com/login/exit/v2",
            form = form
        )
        return response.code == 0
    }
}