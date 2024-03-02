package cn.spacexc.wearbili.remake.app.main.recommend.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.app.RecommendVideo
import cn.spacexc.wearbili.remake.app.main.recommend.domain.remote.rcmd.web.WebRecommendVideo
import cn.spacexc.wearbili.remake.common.UIState
import cn.spacexc.wearbili.remake.proto.settings.RecommendSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/4/6.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private val networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils,
    private val userManager: cn.spacexc.wearbili.common.domain.user.UserManager
) : ViewModel() {
    var screenState by mutableStateOf(
        RecommendScreenState()
    )

    fun getRecommendVideos(isRefresh: Boolean, recommendSource: RecommendSource) {
        viewModelScope.launch {
            if (screenState.videoList.isEmpty()) {
                screenState = screenState.copy(uiState = UIState.Loading)
            }
            if (isRefresh) screenState = screenState.copy(isRefreshing = true)
            when (recommendSource) {
                RecommendSource.App -> {
                    val url =
                        if (!userManager.accessKey().logd("accessKey")
                                .isNullOrEmpty()
                        ) "http://app.bilibili.com/x/v2/feed/index?access_key=${userManager.accessKey()}&actionKey=appkey&appkey=27eb53fc9058f8c3&build=70000100&c_locale=zh-Hans_CN&column=1&disable_rcmd=0&flush=0&fnval=976&fnver=0&force_host=0&fourk=1&guidance=1&https_url_req=0&login_event=2&pull=1&qn=32&recsys_mode=0&s_locale=zh-Hans_CH&screen_window_type=0"
                        else "http://app.bilibili.com/x/v2/feed/index?column=1"
                    val response = networkUtils.get<RecommendVideo>(url)
                    screenState = if (response.code != 0) {
                        screenState.copy(
                            uiState = UIState.Failed(response.code),
                            isRefreshing = false
                        )
                    } else {
                        if (isRefresh) {
                            screenState.copy(
                                uiState = UIState.Success,
                                videoList = response.data?.data?.items
                                    ?: emptyList(),
                                isRefreshing = false
                            )
                        } else {
                            screenState.copy(
                                uiState = UIState.Success,
                                videoList = screenState.videoList + (response.data?.data?.items
                                    ?: emptyList()),
                                isRefreshing = false
                            )
                        }
                    }
                }

                RecommendSource.Web -> {
                    val url =
                        "https://api.bilibili.com/x/web-interface/index/top/rcmd?fresh_type=10&version=1&ps=8&fresh_idx=&fresh_idx_1h=&homepage_ver=1"
                    val response = networkUtils.get<WebRecommendVideo>(url)
                    screenState = if (response.code != 0) {
                        screenState.copy(uiState = UIState.Failed(response.code))
                    } else {
                        if (isRefresh) {
                            screenState.copy(
                                uiState = UIState.Success,
                                videoList = response.data?.data?.item
                                    ?: emptyList(),
                                isRefreshing = false
                            )
                        } else {
                            screenState.copy(
                                uiState = UIState.Success,
                                videoList = screenState.videoList + (response.data?.data?.item
                                    ?: emptyList()),
                                isRefreshing = false
                            )
                        }
                    }
                }

                else -> {
                    throw IllegalArgumentException("叫你乱改配置文件！")
                }
            }
        }
    }
}