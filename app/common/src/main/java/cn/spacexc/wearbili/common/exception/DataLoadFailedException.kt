package cn.spacexc.wearbili.common.exception

/**
 * Created by XC-Qan on 2023/4/20.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class DataLoadFailedException(
    apiUrl: String = ""
) : Exception(/*message = "Data from remote is failed to fetch and load! ${if (apiUrl.isNotEmpty()) "API URL: $apiUrl" else ""}"*/)