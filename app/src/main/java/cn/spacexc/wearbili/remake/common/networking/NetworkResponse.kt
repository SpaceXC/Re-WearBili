package cn.spacexc.wearbili.remake.common.networking

/**
 * Created by XC-Qan on 2023/3/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

sealed class NetworkResponse<T>(
    val apiUrl: String,
    val code: Int = 0,
    val message: String? = null,
    val data: T? = null
) {
    class Success<T>(data: T, apiUrl: String) :
        NetworkResponse<T>(code = 0, message = null, data = data, apiUrl = apiUrl)

    class Failed<T>(code: Int, message: String, data: T? = null, apiUrl: String) :
        NetworkResponse<T>(code = code, message = message, data = data, apiUrl = apiUrl)
}
