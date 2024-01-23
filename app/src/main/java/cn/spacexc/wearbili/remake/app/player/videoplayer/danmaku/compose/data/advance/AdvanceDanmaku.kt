package cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.data.advance

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import bilibili.community.service.dm.v1.DanmakuElem
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.app.player.videoplayer.danmaku.compose.MAX_ALPHA
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlin.math.abs
import kotlin.math.sqrt

data class AdvanceDanmaku(
    var text: String = "",
    var progress: Long,
    var danmakuId: Long,
    var color: Color,
    var textSize: Float,
    var beginX: Float = 0f,
    var beginY: Float = 0f,
    var currentPosition: Offset = Offset(beginX, beginY),
    var endX: Float = 0f,
    var endY: Float = 0f,
    var deltaX: Float = 0f,
    var deltaY: Float = 0f,
    var translationDuration: Long = 0,
    var translationStartDelay: Long = 0,
    var beginAlpha: Float = 0f,
    var endAlpha: Float = 0f,
    var currentAlpha: Float = beginAlpha,
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

fun String.secureToFloat(ifEmpty: Float): Float = try {
    toFloat()
} catch (_: Exception) {
    ifEmpty
}

object AdvanceDanmakuParser {
    val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type
    fun parseAdvanceDanmaku(
        danmakuItem: DanmakuElem,
        displaySize: Size = Size(1f, 1f)
    ): AdvanceDanmaku? {
        try {
            val item = AdvanceDanmaku(
                progress = danmakuItem.progress,
                textSize = danmakuItem.fontsize.toFloat(),
                danmakuId = danmakuItem.id,
                color = parseColor(danmakuItem.color)
            )
            if (danmakuItem.mode != 7 || !danmakuItem.content.startsWith("[") || !danmakuItem.content.endsWith(
                    "]"
                )
            ) return null
            val textArr: List<String> = gson.fromJson(danmakuItem.content, listType)
            if (textArr.isEmpty() || textArr.size < 5) {
                return null
            }
            item.text = textArr[4]

            val beginX = textArr[0].secureToFloat(0f)
            val beginY = textArr[1].secureToFloat(0f)
            var endX = beginX
            var endY = beginY
            val alphaArr = textArr[2].split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var translationDuration = 0L
            var endAlpha = 1
            var alphaDuration = 0L
            if (alphaArr.isNotEmpty()) {
                val beginAlpha = (MAX_ALPHA * alphaArr[0].secureToFloat(1f)).toInt()
                endAlpha = beginAlpha
                if (alphaArr.size > 1) {
                    endAlpha = (MAX_ALPHA * alphaArr[1].secureToFloat(beginAlpha.toFloat())).toInt()
                }
                alphaDuration = (textArr[3].secureToFloat(3000f) * 1000).toLong()
                translationDuration = alphaDuration
            }
            var translationStartDelay: Long = 0
            var rotateY = 0f
            var rotateZ = 0f
            if (textArr.size >= 7) {
                if (textArr[5].isNotEmpty()) {
                    rotateZ = textArr[5].secureToFloat(0f)
                }
                if (textArr[6].isNotEmpty()) {
                    rotateY = textArr[6].secureToFloat(0f)
                }
            }
            if (textArr.size >= 11) {
                if (textArr[7].isNotEmpty() && textArr[8].isNotEmpty()) {
                    endX = textArr[7].secureToFloat(beginX)
                    endY = textArr[8].secureToFloat(beginY)
                }
                if (textArr[9].isNotEmpty()) {
                    translationDuration = textArr[9].secureToFloat(5000f).toLong()
                }
                if (textArr[10].isNotEmpty()) {
                    translationStartDelay = textArr[10].secureToFloat(0f).toLong()
                }
            }

            item.duration = alphaDuration
            item.rotateZ = rotateZ
            item.rotateY = rotateY

            val scaleX = displaySize.width / if (beginX < 1) 1f else BILI_PLAYER_WIDTH
            val scaleY = displaySize.height / if (beginY < 1) 1f else BILI_PLAYER_HEIGHT

            item.apply {
                this.beginX = beginX * scaleX
                this.beginY = beginY * scaleY
                this.endX = endX * scaleX
                this.endY = endY * scaleY
                this.translationDuration = translationDuration
                this.translationStartDelay = translationStartDelay
                this.alphaDuration = duration
                this.beginAlpha = beginAlpha.toFloat()
                this.endAlpha = endAlpha.toFloat()
                this.deltaX = this.endX - this.beginX
                this.deltaY = this.endY - this.beginY
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
                            points[i][0] = pointArray[0].secureToFloat(0f)
                            points[i][1] = pointArray[1].secureToFloat(0f)
                        }
                        for (i in points.indices) {
                            points[i][0] *= scaleX
                            points[i][1] *= scaleY
                        }
                        item.setLinePathData(points)
                    }
                }
            }
            return item
        } catch (e: Exception) {
            /*Log.w(TAG, "parseAdvanceDanmaku: here is an error ${danmakuItem.content}")
            e.printStackTrace()*/
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

fun AdvanceDanmaku.asUpdated(currentTime: Long): AdvanceDanmaku {
    val deltaTime: Long = currentTime - progress
    val deltaAlpha = endAlpha - beginAlpha
    // calculate alpha
    val updateAlpha = if (alphaDuration > 0 && deltaAlpha != 0f) {
        if (deltaTime >= alphaDuration) {
            endAlpha
        } else {
            val alphaProgress: Float = deltaTime / alphaDuration.toFloat()
            val vectorAlpha: Int = (deltaAlpha * alphaProgress).toInt()
            beginAlpha + vectorAlpha
        }
    } else beginAlpha

    // calculate x y
    var currentX: Float = beginX
    var currentY: Float = beginY
    val delayedDeltaTime: Long = deltaTime - translationStartDelay
    if (translationDuration > 0 && delayedDeltaTime >= 0 && delayedDeltaTime <= translationDuration) {
        var translationProgress: Float = delayedDeltaTime / translationDuration.toFloat()
        if (linePaths.isNotEmpty()) {
            var currentLinePath: LinePath? = null
            for (line in linePaths) {
                if (delayedDeltaTime >= line.beginTime && delayedDeltaTime < line.endTime) {
                    currentLinePath = line
                    break
                } else {
                    currentX = line.pEnd.x
                    currentY = line.pEnd.y
                }
            }
            if (currentLinePath != null) {
                val deltaX: Float = currentLinePath.deltaX
                val deltaY = currentLinePath.deltaY
                translationProgress = ((deltaTime - currentLinePath.beginTime)
                        / currentLinePath.duration.toFloat())
                val beginX = currentLinePath.pBegin.x
                val beginY = currentLinePath.pBegin.y
                if (deltaX != 0f) {
                    val vectorX = deltaX * translationProgress
                    currentX = beginX + vectorX
                }
                if (deltaY != 0f) {
                    val vectorY = deltaY * translationProgress
                    currentY = beginY + vectorY
                }
            }
        } else {
            if (deltaX != 0f) {
                val vectorX: Float = deltaX * translationProgress
                currentX = beginX + vectorX
            }
            if (deltaY != 0f) {
                val vectorY: Float = deltaY * translationProgress
                currentY = beginY + vectorY
            }
            /*
            val deltaX = endX - beginX.ifNullOrZero { 1f }.secureToFloat()  //这里的delta有点不一样，advanceDanmaku.deltaX应该是通过路径算出来的，但是我看了一圈也没有所谓的路径返回，但是官方代码有，只好先抄了（
            val deltaY = endY - beginY.ifNullOrZero { 1f }.secureToFloat()
            if(deltaTime > translationStartDelay) {
                currentX += (deltaX / translationDuration * deltaTime)
                currentY += (deltaY / translationDuration * deltaTime)
            }*/

            /*val targetX = advanceDanmaku.beginX + (deltaX / advanceDanmaku.translationDuration.ifNullOrZero { 1f }.secureToFloat() * displayedTime.secureToFloat())
            val targetY = advanceDanmaku.beginY + (deltaY / advanceDanmaku.translationDuration.ifNullOrZero { 1f }.secureToFloat() * displayedTime.secureToFloat())
            val targetAlpha = advanceDanmaku.beginAlpha + (deltaAlpha / advanceDanmaku.translationDuration.secureToFloat() * displayedTime.secureToFloat())
*/
        }
    } else if (delayedDeltaTime > translationDuration) {
        currentX = endX
        currentY = endY
    }


    return this.copy(
        currentPosition = Offset(currentX, currentY),
        currentAlpha = updateAlpha
    )
    /*currStateValues.get(0) = currX
    currStateValues.get(1) = currY
    currStateValues.get(2) = currX + paintWidth
    currStateValues.get(3) = currY + paintHeight*/
}


class LinePath {
    lateinit var pBegin: Point
    lateinit var pEnd: Point
    var duration: Long = 0
    var beginTime: Long = 0
    var endTime: Long = 0
    var deltaX: Float = 0f
    var deltaY: Float = 0f
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
