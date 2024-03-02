package cn.spacexc.wearbili.remake.app.article.ui

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.spacexc.bilibilisdk.sdk.article.info.ArticleInfo
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
import cn.spacexc.wearbili.remake.app.TAG
import cn.spacexc.wearbili.remake.app.article.util.HtmlNode
import cn.spacexc.wearbili.remake.app.article.util.parseElement
import cn.spacexc.wearbili.remake.common.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    val ktorNetworkUtils: KtorNetworkUtils
) : ViewModel() {
    var uiState: UIState by mutableStateOf(UIState.Loading)
    var articleNodes by mutableStateOf(emptyList<HtmlNode>())
    var articleInfo: cn.spacexc.bilibilisdk.sdk.article.info.remote.content.ArticleInfo? by mutableStateOf(
        null
    )

    val readerScrollState = ScrollState(0)

    fun getArticle(cvid: Long) {
        viewModelScope.launch {
            val article = ArticleInfo.getArticle(cvid)
            Log.d(TAG, "initApp: $article")
            if (article.data == null) {
                uiState = UIState.Failed(article.code)
                return@launch
            }
            val info = article.data!!.articleInfo
            articleInfo = info
            val content = article.data!!.decodedContent
            val nodes = buildList {
                content.forEach {
                    parseElement(it)
                }
            }
            articleNodes = nodes
            uiState = UIState.Success
        }
    }
}