package cn.spacexc.wearbili.remake.common.domain.manager

import cn.spacexc.wearbili.remake.common.domain.data.DataManager
import cn.spacexc.wearbili.remake.common.domain.log.logd
import cn.spacexc.wearbili.remake.common.domain.network.KtorNetworkUtils
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
    suspend fun isUserLoggedIn(): Boolean = !networkUtils.getCookie("SESSDATA").logd("SESSDATA").isNullOrEmpty()
    suspend fun userMid(): Long? = networkUtils.getCookie("DedeUserID")?.toLong()
    suspend fun getAccessKey(): String? = dataManager.getString("accessKey", null)
}