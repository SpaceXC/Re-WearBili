package cn.spacexc.wearbili.remake.common.ui

import android.util.Log
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.app.TAG

/**
 * Created by XC-Qan on 2023/4/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun ListenableLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    onScroll: (isScrollingUp: Boolean) -> Unit,
    content: LazyListScope.() -> Unit
) {
    if (state.isScrollInProgress) {
        //进入组合后只会启动一次，
        DisposableEffect(Unit) {
            Log.d(TAG, "start scroll")
            onDispose {
                Log.d(TAG, "stop scroll")
            }
        }

        //记录上一次第一个可见item的滑动偏移
        var lastFirstVisibleItemScrollOffset by remember {
            mutableStateOf(state.firstVisibleItemScrollOffset)
        }

        //记录上一次第一个可见item下标
        var lastFirstVisibleItemIndex by remember {
            mutableStateOf(state.firstVisibleItemIndex)
        }
        run {
            val currentFirstVisibleItemIndex = state.firstVisibleItemIndex
            //手指向上滑动（即正方向）时offset会变大，向下时变小
            val currentFirstVisibleItemScrollOffset = state.firstVisibleItemScrollOffset

            //第一个可见的item改变了
            if (currentFirstVisibleItemIndex != lastFirstVisibleItemIndex) {
                if (currentFirstVisibleItemIndex < lastFirstVisibleItemIndex) {
                    Log.d(TAG, "向下滑动↓ ")
                } else if (currentFirstVisibleItemIndex > lastFirstVisibleItemIndex) {
                    Log.d(TAG, "向上滑动↑")
                }
                //更新记录的值，退出run代码块
                lastFirstVisibleItemIndex = currentFirstVisibleItemIndex
                lastFirstVisibleItemScrollOffset = currentFirstVisibleItemScrollOffset
                return@run
            }

            //第一个可见item当前的offset - 上一次记录的offset
            val offset =
                currentFirstVisibleItemScrollOffset - lastFirstVisibleItemScrollOffset
            onScroll(offset < 0)
            if (offset < 0) {
                Log.d(TAG, "手指向下滑动↓ $offset")
            } else if (offset > 0) {
                Log.d(TAG, "手指向上滑动↑ $offset")
            }
            //记录第一个可见item当前的offset
            lastFirstVisibleItemScrollOffset = currentFirstVisibleItemScrollOffset
        }
    }

    //将lazyListState赋值给LazyColumn
    LazyColumn(
        modifier,
        state,
        contentPadding,
        reverseLayout,
        verticalArrangement,
        horizontalAlignment,
        flingBehavior,
        userScrollEnabled,
        content
    )
}