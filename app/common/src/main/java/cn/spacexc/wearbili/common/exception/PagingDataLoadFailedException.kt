package cn.spacexc.wearbili.common.exception

/**
 * Created by XC-Qan on 2023/4/20.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class PagingDataLoadFailedException(
    apiUrl: String,
    val code: Int
) : java.lang.RuntimeException("There seemed to be an exception in data loading. Requested api endpoint = $apiUrl, code = $code")