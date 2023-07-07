/*
 * Copyright (C) 2013 Chen Hui <calmer91@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.spacexc.wearbili.remake.app.videoplayer.danmaku

import android.graphics.Color
import android.text.TextUtils
import master.flame.danmaku.danmaku.model.AlphaValue
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.Duration
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.SpecialDanmaku
import master.flame.danmaku.danmaku.model.android.DanmakuFactory
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource
import master.flame.danmaku.danmaku.util.DanmakuUtils
import org.json.JSONArray
import org.json.JSONException
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.helpers.XMLReaderFactory
import java.io.IOException
import java.util.Locale

open class BiliDanmukuParser : BaseDanmakuParser() {
    protected var mDispScaleX = 0f
    protected var mDispScaleY = 0f
    public override fun parse(): Danmakus? {
        if (mDataSource != null) {
            val source = mDataSource as AndroidFileSource
            try {
                val xmlReader = XMLReaderFactory.createXMLReader()
                val contentHandler: XmlContentHandler = XmlContentHandler()
                xmlReader.contentHandler = contentHandler
                xmlReader.parse(InputSource(source.data()))
                return contentHandler.result!!
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    inner class XmlContentHandler : DefaultHandler() {
        var result: Danmakus? = null
        var item: BaseDanmaku? = null
        var completed = false
        var index = 0

        @Throws(SAXException::class)
        override fun startDocument() {
            result = Danmakus(IDanmakus.ST_BY_TIME, false, mContext.baseComparator)
        }

        @Throws(SAXException::class)
        override fun endDocument() {
            completed = true
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String,
            localName: String,
            qName: String,
            attributes: Attributes
        ) {
            var tagName = if (localName.length != 0) localName else qName
            tagName = tagName.lowercase(Locale.getDefault()).trim { it <= ' ' }
            if (tagName == "d") {
                // <d p="23.826000213623,1,25,16777215,1422201084,0,057075e9,757076900">我从未见过如此厚颜无耻之猴</d>
                // 0:时间(弹幕出现时间)
                // 1:类型(1从右至左滚动弹幕|6从左至右滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
                // 2:字号
                // 3:颜色
                // 4:时间戳 ?
                // 5:弹幕池id
                // 6:用户hash
                // 7:弹幕id
                val pValue = attributes.getValue("p")
                // parse p value to danmaku
                val values = pValue.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                if (values.size > 0) {
                    val time = (parseFloat(values[0]) * 1000).toLong() // 出现时间
                    val type = parseInteger(values[1]) // 弹幕类型
                    val textSize = parseFloat(values[2]) // 字体大小
                    val color =
                        (0x00000000ff000000L or parseLong(values[3]) and 0x00000000ffffffffL).toInt() // 颜色
                    // int poolType = parseInteger(values[5]); // 弹幕池类型（忽略
                    item = mContext.mDanmakuFactory.createDanmaku(type, mContext)
                    if (item != null) {
                        item!!.time = time
                        item!!.textSize = textSize * (mDispDensity - 0.6f)
                        item!!.textColor = color
                        item!!.textShadowColor =
                            if (color <= Color.BLACK) Color.WHITE else Color.BLACK
                    }
                }
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String, localName: String, qName: String) {
            if (item != null && item!!.text != null) {
                if (item!!.duration != null) {
                    val tagName = if (localName.length != 0) localName else qName
                    if (tagName.equals("d", ignoreCase = true)) {
                        item!!.timer = mTimer
                        item!!.flags = mContext.mGlobalFlagValues
                        val lock = result!!.obtainSynchronizer()
                        synchronized(lock) { result!!.addItem(item) }
                    }
                }
                item = null
            }
        }

        override fun characters(ch: CharArray, start: Int, length: Int) {
            if (item != null) {
                DanmakuUtils.fillText(item, decodeXmlString(String(ch, start, length)))
                item!!.index = index++

                // initial specail danmaku data
                val text = item!!.text.toString().trim { it <= ' ' }
                if (item!!.type == BaseDanmaku.TYPE_SPECIAL && text.startsWith("[")
                    && text.endsWith("]")
                ) {
                    //text = text.substring(1, text.length() - 1);
                    var textArr: Array<String?>? = null //text.split(",", -1);
                    try {
                        val jsonArray = JSONArray(text)
                        textArr = arrayOfNulls(jsonArray.length())
                        for (i in textArr.indices) {
                            textArr[i] = jsonArray.getString(i)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (textArr == null || textArr.size < 5 || TextUtils.isEmpty(
                            textArr[4]
                        )
                    ) {
                        item = null
                        return
                    }
                    DanmakuUtils.fillText(item, textArr[4])
                    var beginX = parseFloat(textArr[0])
                    var beginY = parseFloat(textArr[1])
                    var endX = beginX
                    var endY = beginY
                    val alphaArr = textArr[2]!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val beginAlpha = (AlphaValue.MAX * parseFloat(alphaArr[0])).toInt()
                    var endAlpha = beginAlpha
                    if (alphaArr.size > 1) {
                        endAlpha = (AlphaValue.MAX * parseFloat(alphaArr[1])).toInt()
                    }
                    val alphaDuraion = (parseFloat(textArr[3]) * 1000).toLong()
                    var translationDuration = alphaDuraion
                    var translationStartDelay: Long = 0
                    var rotateY = 0f
                    var rotateZ = 0f
                    if (textArr.size >= 7) {
                        rotateZ = parseFloat(textArr[5])
                        rotateY = parseFloat(textArr[6])
                    }
                    if (textArr.size >= 11) {
                        endX = parseFloat(textArr[7])
                        endY = parseFloat(textArr[8])
                        if ("" != textArr.get(9)) {
                            translationDuration = parseInteger(textArr[9]).toLong()
                        }
                        if ("" != textArr.get(10)) {
                            translationStartDelay = parseFloat(textArr[10]).toLong()
                        }
                    }
                    if (isPercentageNumber(textArr[0])) {
                        beginX *= DanmakuFactory.BILI_PLAYER_WIDTH
                    }
                    if (isPercentageNumber(textArr[1])) {
                        beginY *= DanmakuFactory.BILI_PLAYER_HEIGHT
                    }
                    if (textArr.size >= 8 && isPercentageNumber(textArr[7])) {
                        endX *= DanmakuFactory.BILI_PLAYER_WIDTH
                    }
                    if (textArr.size >= 9 && isPercentageNumber(textArr[8])) {
                        endY *= DanmakuFactory.BILI_PLAYER_HEIGHT
                    }
                    item!!.duration = Duration(alphaDuraion)
                    item!!.rotationZ = rotateZ
                    item!!.rotationY = rotateY
                    mContext.mDanmakuFactory.fillTranslationData(
                        item,
                        beginX,
                        beginY,
                        endX,
                        endY,
                        translationDuration,
                        translationStartDelay,
                        mDispScaleX,
                        mDispScaleY
                    )
                    mContext.mDanmakuFactory.fillAlphaData(item, beginAlpha, endAlpha, alphaDuraion)
                    if (textArr.size >= 12) {
                        // 是否有描边
                        if (!TextUtils.isEmpty(textArr[11]) && "true".equals(
                                textArr[11], ignoreCase = true
                            )
                        ) {
                            item!!.textShadowColor = Color.TRANSPARENT
                        }
                    }
                    if (textArr.size >= 13) {
                        //TODO 字体 textArr[12]
                    }
                    if (textArr.size >= 14) {
                        // Linear.easeIn or Quadratic.easeOut
                        (item as SpecialDanmaku).isQuadraticEaseOut = "0" == textArr[13]
                    }
                    if (textArr.size >= 15) {
                        // 路径数据
                        if ("" != textArr.get(14)) {
                            val motionPathString = textArr[14]!!.substring(1)
                            if (!TextUtils.isEmpty(motionPathString)) {
                                val pointStrArray = motionPathString.split("L".toRegex())
                                    .dropLastWhile { it.isEmpty() }
                                    .toTypedArray()
                                if (pointStrArray.size > 0) {
                                    val points = Array(pointStrArray.size) {
                                        FloatArray(
                                            2
                                        )
                                    }
                                    for (i in pointStrArray.indices) {
                                        val pointArray = pointStrArray[i].split(",".toRegex())
                                            .dropLastWhile { it.isEmpty() }
                                            .toTypedArray()
                                        if (pointArray.size >= 2) {
                                            points[i][0] = parseFloat(pointArray[0])
                                            points[i][1] = parseFloat(pointArray[1])
                                        }
                                    }
                                    DanmakuFactory.fillLinePathData(
                                        item, points, mDispScaleX,
                                        mDispScaleY
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        private fun decodeXmlString(title: String): String {
            var temp = title
            if (temp.contains("&amp;")) {
                temp = temp.replace("&amp;", "&")
            }
            if (temp.contains("&quot;")) {
                temp = temp.replace("&quot;", "\"")
            }
            if (temp.contains("&gt;")) {
                temp = temp.replace("&gt;", ">")
            }
            if (temp.contains("&lt;")) {
                temp = temp.replace("&lt;", "<")
            }
            return temp
        }

        /*companion object {
            private const val TRUE_STRING = "true"
        }*/
    }

    private fun isPercentageNumber(number: String?): Boolean {
        //return number >= 0f && number <= 1f;
        return number != null && number.contains(".")
    }

    private fun parseFloat(floatStr: String?): Float {
        return try {
            floatStr!!.toFloat()
        } catch (e: NumberFormatException) {
            0.0f
        }
    }

    private fun parseInteger(intStr: String?): Int {
        return try {
            intStr!!.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    private fun parseLong(longStr: String): Long {
        return try {
            longStr.toLong()
        } catch (e: NumberFormatException) {
            0
        }
    }

    override fun setDisplayer(disp: IDisplayer): BaseDanmakuParser {
        super.setDisplayer(disp)
        mDispScaleX = mDispWidth / DanmakuFactory.BILI_PLAYER_WIDTH
        mDispScaleY = mDispHeight / DanmakuFactory.BILI_PLAYER_HEIGHT
        return this
    }

    companion object {
        init {
            System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver")
        }
    }
}