package cn.spacexc.wearbili.remake.common.domain.network

/**
 * Created by XC-Qan on 2023/3/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class NetworkResponse<T>(val code: Int = 0, val message: String? = null, val data: T? = null) {
    class Success<T>(data: T): NetworkResponse<T>(code = 0, message = null, data = data)
    class Failed<T>(code: Int, message: String, data: T? = null): NetworkResponse<T>(code = code, message = message, data = data)
}
