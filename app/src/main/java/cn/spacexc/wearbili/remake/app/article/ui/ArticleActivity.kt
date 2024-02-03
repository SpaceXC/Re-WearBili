package cn.spacexc.wearbili.remake.app.article.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

const val PARAM_CVID = "cvid"

@AndroidEntryPoint
class ArticleActivity : ComponentActivity() {
    val viewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cvid = intent.getLongExtra(PARAM_CVID, 0L)
        if (cvid != 0L) {
            viewModel.getArticle(cvid)
        }
        setContent {
            ArticleScreen(viewModel = viewModel)
        }
    }
}