package cn.spacexc.wearbili.remake.ui.search.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by XC-Qan on 2023/4/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val PARAM_DEFAULT_SEARCH_KEYWORD = "defaultSearchKeyword"

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHotSearch()
        val defaultSearchKeyword = intent.getStringExtra(PARAM_DEFAULT_SEARCH_KEYWORD) ?: ""
        setContent {

        }
    }
}