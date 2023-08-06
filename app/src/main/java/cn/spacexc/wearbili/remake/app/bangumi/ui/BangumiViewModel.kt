package cn.spacexc.wearbili.remake.app.bangumi.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.bangumi.info.BangumiInfo
import cn.spacexc.bilibilisdk.sdk.bangumi.info.remote.Result
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.common.ToastUtils
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/7/19.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val BANGUMI_ID_TYPE_EPID = "ep_id"
const val BANGUMI_ID_TYPE_SSID = "season_id"

@HiltViewModel
class BangumiViewModel @Inject constructor(
    private val ktorNetworkUtils: KtorNetworkUtils
) : ViewModel() {
    var bangumiInfo: Result? by mutableStateOf(null)
    var uiState by mutableStateOf(UIState.Loading)
    var imageBitmap: Bitmap? by mutableStateOf(null)

    val bangumiInfoScreenScrollState = ScrollState(0)

    fun getBangumiInfo(
        bangumiIdType: String = BANGUMI_ID_TYPE_EPID,
        bangumiId: Long
    ) {
        viewModelScope.launch {
            val response = BangumiInfo.getBangumiInfo(bangumiIdType, bangumiId)
            if (response.code != 0) {
                uiState = UIState.Failed
                return@launch
            }
            bangumiInfo = response.data?.result
            uiState = UIState.Success
            getImageBitmap(response.data?.result?.cover ?: "")
        }
    }

    private fun getImageBitmap(url: String) {
        viewModelScope.launch {
            try {
                val response: HttpResponse = ktorNetworkUtils.client.get(url) {
                    //accept(ContentType.Application.Json)
                }
                if (response.status == HttpStatusCode.OK) {
                    val bytes = response.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    if (bitmap != null) {
                        imageBitmap = bitmap
                    }
                } else {
                    MainScope().launch {
                        ToastUtils.showText("${response.status.value}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}