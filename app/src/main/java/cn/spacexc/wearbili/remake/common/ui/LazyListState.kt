package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState

/**
 * Created by XC-Qan on 2023/8/11.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

suspend fun LazyListState.animateScrollAndCentralizeItem(index: Int) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    if (itemInfo != null) {
        val center = this@animateScrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
        val childCenter = itemInfo.offset + itemInfo.size / 2
        this@animateScrollAndCentralizeItem.animateScrollBy((childCenter - center).toFloat())
    } else {
        this@animateScrollAndCentralizeItem.animateScrollToItem(index)
    }
}

suspend fun LazyListState.scrollAndCentralizeItem(index: Int) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    if (itemInfo != null) {
        val center = this@scrollAndCentralizeItem.layoutInfo.viewportEndOffset / 2
        val childCenter = itemInfo.offset + itemInfo.size / 2
        this@scrollAndCentralizeItem.scrollBy((childCenter - center).toFloat())
    } else {
        this@scrollAndCentralizeItem.scrollToItem(index)
    }
}