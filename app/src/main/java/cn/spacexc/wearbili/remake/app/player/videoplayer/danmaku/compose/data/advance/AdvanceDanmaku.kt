package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.advance

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import bilibili.community.service.dm.v1.DanmakuElem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlin.math.abs
import kotlin.math.sqrt

data class AdvanceDanmaku(
    var text: String = "",
    //var originalContent: String,
    var beginX: Float = 0f,
    var beginY: Float = 0f,
    var endX: Float = 0f,
    var endY: Float = 0f,
    var deltaX: Float = 0f,
    var deltaY: Float = 0f,
    var translationDuration: Long = 0,
    var translationStartDelay: Long = 0,
    var beginAlpha: Float = 0f,
    var endAlpha: Float = 0f,
    var deltaAlpha: Float = 0f,
    var alphaDuration: Long = 0,
    var rotateX: Float = 0f,
    var rotateY: Float = 0f,
    var rotateZ: Float = 0f,
    var pivotX: Float = 0f,
    var pivotY: Float = 0f,
    var duration: Long = 5000,
    var textShadowColor: Color = Color.Transparent,
    var linePaths: List<LinePath> = emptyList()
)

const val BILI_PLAYER_WIDTH = 682f
const val BILI_PLAYER_HEIGHT = 438f
const val MAX_ALPHA = 255

object AdvanceDanmakuParser {
    val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type
    fun parseAdvanceDanmaku(
        danmakuItem: DanmakuElem,
        displaySize: Size
    ): AdvanceDanmaku? {
        try {
            val displayScaleX: Float = displaySize.width / BILI_PLAYER_WIDTH
            val displayScaleY: Float = displaySize.height / BILI_PLAYER_HEIGHT


            val item = AdvanceDanmaku(/*originalContent = danmakuItem.content*/)
            if (danmakuItem.mode != 7 || !danmakuItem.content.startsWith("[") || !danmakuItem.content.endsWith(
                    "]"
                )
            ) return null
            val textArr: List<String> = gson.fromJson(danmakuItem.content, listType)
            if (textArr.isEmpty() || textArr.size < 5) {
                return null
            }
            item.text = textArr[4]
            val beginX = textArr[0].toFloat()
            val beginY = textArr[1].toFloat()
            var endX = beginX
            var endY = beginY
            val alphaArr = textArr[2].split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val beginAlpha = (MAX_ALPHA * alphaArr[0].toFloat()).toInt()
            var endAlpha = beginAlpha
            if (alphaArr.size > 1) {
                endAlpha = (MAX_ALPHA * alphaArr[1].toFloat()).toInt()
            }
            val alphaDuration = (textArr[3].toFloat() * 1000).toLong()
            var translationDuration = alphaDuration
            var translationStartDelay: Long = 0
            var rotateY = 0f
            var rotateZ = 0f
            if (textArr.size >= 7) {
                rotateZ = textArr[5].toFloat()
                rotateY = textArr[6].toFloat()
            }
            if (textArr.size >= 11) {
                endX = textArr[7].toFloat()
                endY = textArr[8].toFloat()
                if ("" != textArr[9]) {
                    translationDuration = textArr[9].toInt().toLong()
                }
                if ("" != textArr[10]) {
                    translationStartDelay = textArr[10].toFloat().toLong()
                }
            }

            item.duration = alphaDuration
            item.rotateZ = rotateZ
            item.rotateY = rotateY

            item.apply {
                this.beginX = beginX * displayScaleX
                this.beginY = beginY * displayScaleY
                this.endX = endX * displayScaleX
                this.endY = endY * displayScaleY
                this.translationDuration = translationDuration
                this.translationStartDelay = translationStartDelay
                this.alphaDuration = duration
                this.beginAlpha = beginAlpha.toFloat()
                this.endAlpha = endAlpha.toFloat()
            }

            if (textArr.size >= 12) {
                // 是否有描边
                if (textArr[11].isNotEmpty() && textArr[11] == "true") {
                    item.textShadowColor = Color.Transparent
                }
            }
            /*if (textArr.size >= 13) {
                //TODO 字体 textArr[12]
            }
            if (textArr.size >= 14) {
                //TODO 是否有加速
            }*/
            if (textArr.size >= 15) {
                // 路径数据
                if ("" != textArr[14]) {
                    val motionPathString = textArr[14].substring(1)
                    val pointStrArray =
                        motionPathString.split("L".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    if (pointStrArray.isNotEmpty()) {
                        val points = Array(pointStrArray.size) {
                            FloatArray(
                                2
                            )
                        }
                        for (i in pointStrArray.indices) {
                            val pointArray =
                                pointStrArray[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                            points[i][0] = pointArray[0].toFloat()
                            points[i][1] = pointArray[1].toFloat()
                        }
                        for (i in points.indices) {
                            points[i][0] *= displayScaleX
                            points[i][1] *= displayScaleY
                        }
                        item.setLinePathData(points)
                    }
                }
            }
            return item
        } catch (e: Exception) {
            return null
        }
    }

    private fun AdvanceDanmaku.setLinePathData(points: Array<FloatArray>) {
        val length = points.size
        this.beginX = points[0][0]
        this.beginY = points[0][1]
        this.endX = points[length - 1][0]
        this.endY = points[length - 1][1]
        if (points.size > 1) {
            linePaths = buildList {
                repeat(points.size - 1) { index ->
                    val linePath = LinePath()
                    linePath.setPoints(
                        Point(
                            points[index][0], points[index][1]
                        ), Point(
                            points[index + 1][0], points[index + 1][1]
                        )
                    )
                    add(linePath)
                }
            }

            var totalDistance = 0f
            for (line in linePaths) {
                totalDistance += line.distance
            }
            var lastLine: LinePath? = null
            for (line in linePaths) {
                line.duration = (line.distance / totalDistance * translationDuration).toLong()
                line.beginTime = lastLine?.endTime ?: 0
                line.endTime = line.beginTime + line.duration
                lastLine = line
            }
        }
    }
}


class LinePath {
    private lateinit var pBegin: Point
    private lateinit var pEnd: Point
    var duration: Long = 0
    var beginTime: Long = 0
    var endTime: Long = 0
    private var deltaX: Float = 0f
    private var deltaY: Float = 0f
    fun setPoints(pBegin: Point, pEnd: Point) {
        this.pBegin = pBegin
        this.pEnd = pEnd
        deltaX = pEnd.x - pBegin.x
        deltaY = pEnd.y - pBegin.y
    }

    val distance: Float
        get() = pEnd.getDistance(pBegin)
}


class Point(var x: Float, var y: Float) {
    fun getDistance(p: Point): Float {
        val mX = abs(x - p.x)
        val mY = abs(y - p.y)
        return sqrt((mX * mX + mY * mY).toDouble()).toFloat()
    }
}
