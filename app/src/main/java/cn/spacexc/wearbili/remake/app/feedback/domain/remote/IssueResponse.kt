package cn.spacexc.wearbili.remake.app.feedback.domain.remote

import cn.spacexc.wearbili.remake.app.crash.remote.ErrorLog

data class IssueResponse(
    val code: Int,
    val message: String,
    val body: List<ErrorLog>
)
