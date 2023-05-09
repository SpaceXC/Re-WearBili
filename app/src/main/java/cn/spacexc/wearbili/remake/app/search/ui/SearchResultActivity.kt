package cn.spacexc.wearbili.remake.app.search.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/5/3.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_KEYWORD = "keyword"

@AndroidEntryPoint
class SearchResultActivity : ComponentActivity() {
    private val viewModel by viewModels<SearchResultViewModel>()

    private fun getKeyword(): String? {
        return if (intent.getStringExtra(PARAM_KEYWORD).isNullOrEmpty()) {
            intent.data?.getQueryParameter(PARAM_KEYWORD)
        } else {
            intent.getStringExtra(PARAM_KEYWORD)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keyword = getKeyword() ?: ""
        val flow = viewModel.getSearchResultFlow(keyword)
        setContent {
            SearchResultScreen(onBack = ::finish, flow = flow)
        }
    }
}