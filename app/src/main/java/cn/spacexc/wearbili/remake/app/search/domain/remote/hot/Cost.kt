package cn.spacexc.wearbili.remake.app.search.domain.remote.hot

data class Cost(
    val deserialize_response: String,
    val hotword_request: String,
    val hotword_request_format: String,
    val hotword_response_format: String,
    val main_handler: String,
    val params_check: String,
    val total: String
)