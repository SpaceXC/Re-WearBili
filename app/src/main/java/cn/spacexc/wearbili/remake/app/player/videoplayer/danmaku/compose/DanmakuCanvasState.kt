package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import bilibili.community.service.dm.v1.DanmakuElem
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.DanmakuSegment
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.DisplayDanmakuItem

/**
 * Created by XC-Qan on 2023/12/10.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class DanmakuType(vararg types: Int) {
    private val typeInts = types
    infix fun isEqualTo(other: Int): Boolean {
        return typeInts.contains(other)
    }

    infix fun isNotEqualTo(other: Int): Boolean {
        return !typeInts.contains(other)
    }
}

val DANMAKU_TYPE_NORM = DanmakuType(1, 2, 3)
val DANMAKU_TYPE_REVERSED = DanmakuType(6)
val DANMAKU_TYPE_TOP = DanmakuType(5)
val DANMAKU_TYPE_BOTTOM = DanmakuType(4)
val DANMAKU_TYPE_ADVANCE = DanmakuType(7)
val DANMAKU_TYPE_SCRIPT = DanmakuType(8)

@Composable
fun rememberDanmakuCanvasState(updateTimer: () -> Long) = remember {
    DanmakuCanvasState(updateTimer)
}

class DanmakuCanvasState(val updateTimer: () -> Long) {
    enum class DanmakuCanvasState {
        Idle,
        Playing,
        Paused
        //Completed
    }

    private var danmakuList = emptyList<DanmakuElem>()

    var displayingDanmakus by mutableStateOf(listOf<DisplayDanmakuItem>())

    var state = DanmakuCanvasState.Idle
    //var timer: MillisecondTimer = MillisecondTimer()
    //private var displayDanmakus = mutableListOf<DisplayDanmakuItem>()

    fun start() {
        //timer.start()
        state = DanmakuCanvasState.Playing
    }

    fun pause() {
        //timer.pause()
        state = DanmakuCanvasState.Paused
    }

    fun seekTo(time: Long) {
        //timer.elapsedTime = time
        dynamicDanmakuList = danmakuList.filter { it.progress >= time }.toMutableList()
        displayingDanmakus = emptyList()
        topDisplayRows = HashMap()
        bottomDisplayRows = HashMap()
    }

    private var topDisplayRows = HashMap<Int, List<DisplayDanmakuItem>>()
    private var bottomDisplayRows = HashMap<Int, List<DisplayDanmakuItem>>()

    //private var displayedDanmakuIds = mutableListOf<Long>()
    private var dynamicDanmakuList = mutableListOf<DanmakuElem>()

    /**
     * 在canvas上绘制弹幕
     */
    @OptIn(ExperimentalTextApi::class)
    fun updatedDanmaku(
        textMeasurer: TextMeasurer,
        drawScope: DrawScope,
        fontSize: Float,
        playSpeed: Float,
        framePerSecond: Int
    ) {
        if (state != DanmakuCanvasState.Playing) return
        drawScope.apply {
            var tempList = displayingDanmakus.toMutableList()
            //寻找新的可以被显示的弹幕，要求为 出现时间 > 当前计时器时间 且 不是高级弹幕/脚本弹幕
            val newVisibleDanmakus =
                dynamicDanmakuList.filter { danmakuItem ->
                    danmakuItem.progress <= updateTimer()
                            && DANMAKU_TYPE_ADVANCE isNotEqualTo danmakuItem.mode
                            && DANMAKU_TYPE_SCRIPT isNotEqualTo danmakuItem.mode
                            && danmakuItem.weight > 0   //屏蔽等级
                    //TODO 显示高级弹幕
                }
                    .map { danmakuItem ->
                        //用来获取文字的大小
                        val textLayoutResult: TextLayoutResult =
                            textMeasurer.measure(
                                text = AnnotatedString(danmakuItem.content),
                                style = TextStyle(fontSize = (danmakuItem.fontsize * 0.55 * fontSize).sp)
                            )
                        val textSize = textLayoutResult.size

                        val row = getRow(danmakuItem, textSize.width, drawScope)

                        val newItem = DisplayDanmakuItem(
                            appearTime = danmakuItem.progress.toDouble(),
                            content = danmakuItem.content,
                            x = when (parseDanmakuType(danmakuItem.mode)) {
                                DANMAKU_TYPE_NORM -> size.width
                                DANMAKU_TYPE_REVERSED -> 0f - textSize.width
                                DANMAKU_TYPE_TOP, DANMAKU_TYPE_BOTTOM -> (size.width / 2) - (textSize.width / 2)
                                else -> 0f
                            },
                            y = when (parseDanmakuType(danmakuItem.mode)) {
                                DANMAKU_TYPE_BOTTOM -> size.height - ((row + 1) * textSize.height.toFloat())
                                else -> row * textSize.height.toFloat()
                            },
                            color = Color(danmakuItem.color).copy(alpha = 1f),
                            fontSize = danmakuItem.fontsize,
                            type = danmakuItem.mode,
                            displayedFrames = 0,
                            danmakuId = danmakuItem.id,
                            displayRow = row,
                            textWidth = textSize.width,
                            textHeight = textSize.height
                        )
                        if (DANMAKU_TYPE_BOTTOM isEqualTo danmakuItem.mode) {
                            val danmakusInRow =
                                (bottomDisplayRows[row] ?: emptyList()).toMutableList()
                            if (!danmakusInRow.any { it.danmakuId == danmakuItem.id }) {
                                danmakusInRow.add(newItem)
                            }
                            bottomDisplayRows[row] = danmakusInRow
                        } else {
                            val danmakusInRow =
                                (topDisplayRows[row] ?: emptyList()).toMutableList()
                            if (!danmakusInRow.any { it.danmakuId == danmakuItem.id }) {
                                danmakusInRow.add(newItem)
                            }
                            topDisplayRows[row] = danmakusInRow
                        }
                        newItem
                    }
            //Log.d("TAG", "updateDanmaku: $newVisibleDanmakus")
            tempList.addAll(newVisibleDanmakus)
            dynamicDanmakuList.removeAll {
                newVisibleDanmakus.map { it.danmakuId }.contains(it.id)
            }
            //displayedDanmakuIds.addAll(newVisibleDanmakus.map { it.danmakuId })

            tempList = tempList.map {
                when (parseDanmakuType(it.type)) {
                    DANMAKU_TYPE_NORM -> it.copy(
                        x = it.x - (2.5f * playSpeed),
                        displayedFrames = it.displayedFrames + 1
                    )

                    DANMAKU_TYPE_REVERSED -> it.copy(
                        x = it.x + (2.5f * playSpeed),
                        displayedFrames = it.displayedFrames + 1
                    )

                    DANMAKU_TYPE_TOP, DANMAKU_TYPE_BOTTOM -> it.copy(displayedFrames = it.displayedFrames + 1)
                    else -> it
                }
            }.toMutableList()

            //新的不可见弹幕
            val newInvisibleDanmakus = displayingDanmakus.filter { displayDanmakuItem ->
                val textPaint = Paint().asFrameworkPaint().apply {
                    textSize = 20 * density
                }
                val textBounds = android.graphics.Rect()
                textPaint.getTextBounds(
                    displayDanmakuItem.content,
                    0,
                    displayDanmakuItem.content.length,
                    textBounds
                )

                val isOut = when (parseDanmakuType(displayDanmakuItem.type)) {
                    DANMAKU_TYPE_NORM -> displayDanmakuItem.x + textBounds.width() < 0
                    DANMAKU_TYPE_REVERSED -> displayDanmakuItem.x > size.width
                    DANMAKU_TYPE_TOP, DANMAKU_TYPE_BOTTOM -> displayDanmakuItem.displayedFrames.toFloat() > (300 / playSpeed)    //5秒
                    else -> false
                }
                //Log.d("TAG", "getUpdatedDanmaku: ${displayDanmakuItem.content}: $isOut")
                isOut
            }
            newInvisibleDanmakus.forEach { newInvisibleDanmaku ->
                tempList.removeAll { it.danmakuId == newInvisibleDanmaku.danmakuId }
                if (DANMAKU_TYPE_BOTTOM isEqualTo newInvisibleDanmaku.type) {
                    val list =
                        bottomDisplayRows[newInvisibleDanmaku.displayRow]?.toMutableList()
                    list?.removeAll { it.danmakuId == newInvisibleDanmaku.danmakuId }
                    bottomDisplayRows[newInvisibleDanmaku.displayRow] = list ?: emptyList()
                } else {
                    val list = topDisplayRows[newInvisibleDanmaku.displayRow]?.toMutableList()
                    list?.removeAll { it.danmakuId == newInvisibleDanmaku.danmakuId }
                    topDisplayRows[newInvisibleDanmaku.displayRow] = list ?: emptyList()
                }
            }
            //Log.d("TAG", "getUpdatedDanmaku: $tempList")
            displayingDanmakus = tempList
        }
    }

    private fun getRow(
        danmaku: DanmakuElem,
        textWidth: Int,
        drawScope: DrawScope
    ): Int {
        if (DANMAKU_TYPE_BOTTOM isNotEqualTo danmaku.mode) {    //如果不是底端弹幕
            topDisplayRows.values.forEachIndexed { row, danmakusInRow ->
                when (parseDanmakuType(danmaku.mode)) {
                    DANMAKU_TYPE_NORM -> {
                        val hasHitDanmaku = danmakusInRow.any {
                            it.x + it.textWidth - 50 > drawScope.size.width
                        } || danmakusInRow.any { DANMAKU_TYPE_REVERSED isEqualTo it.type }
                        if (!hasHitDanmaku) return row
                    }

                    DANMAKU_TYPE_REVERSED -> {
                        val hasHitDanmaku = danmakusInRow.any {
                            textWidth - 50 >= it.x
                        }   //文字宽度没有超过上一个弹幕的
                                || danmakusInRow.any { DANMAKU_TYPE_NORM isEqualTo it.type }  //同一行不能同时有从左到右或从右到左
                        if (!hasHitDanmaku) return row
                    }

                    DANMAKU_TYPE_TOP -> {
                        val hasHitDanmaku =
                            danmakusInRow.any { DANMAKU_TYPE_TOP isEqualTo it.type }   //同一行只要没有弹幕就行
                        if (!hasHitDanmaku) return row
                    }
                }
            }
            return topDisplayRows.keys.size
        } else {
            bottomDisplayRows.values.forEachIndexed { row, danmakusInRow ->
                val hasHitDanmaku = danmakusInRow.any { DANMAKU_TYPE_BOTTOM isEqualTo it.type }
                if (!hasHitDanmaku) return row
            }
            return bottomDisplayRows.keys.size
        }
    }

    fun setDanmakuList(newDanmakuList: List<DanmakuSegment>) {
        val newList = newDanmakuList.map { it.danmakuList }.flatten()
        val difference = newList - danmakuList.toSet()
        danmakuList = newList
        dynamicDanmakuList.addAll(difference)
    }

    private fun parseDanmakuType(type: Int): DanmakuType {
        return when (type) {
            1, 2, 3 -> DANMAKU_TYPE_NORM
            4 -> DANMAKU_TYPE_BOTTOM
            5 -> DANMAKU_TYPE_TOP
            6 -> DANMAKU_TYPE_REVERSED
            7 -> DANMAKU_TYPE_ADVANCE
            8 -> DANMAKU_TYPE_SCRIPT
            else -> DANMAKU_TYPE_NORM
        }
    }
}