package cn.spacexc.wearbili.remake.app.article.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cn.spacexc.wearbili.common.domain.color.parseColor
import cn.spacexc.wearbili.common.ifEmptyThenNull
import cn.spacexc.wearbili.remake.app.TAG
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

val colorMap = mapOf(
    "color-default" to "ffffff80",
    "color-blue-01" to "56c1fe",
    "color-lblue-01" to "73fdea",
    "color-green-01" to "89fa4e",
    "color-yellow-01" to "fff359",
    "color-pink-01" to "ff968d",
    "color-purple-01" to "ff8cc6",
    "color-blue-02" to "02a2ff",
    "color-lblue-02" to "18e7cf",
    "color-green-02" to "60d837",
    "color-yellow-02" to "fbe231",
    "color-pink-02" to "ff654e",
    "color-purple-02" to "ef5fa8",
    "color-blue-03" to "0176ba",
    "color-lblue-03" to "068f86",
    "color-green-03" to "1db100",
    "color-yellow-03" to "f8ba00",
    "color-pink-03" to "ee230d",
    "color-purple-03" to "cb297a",
    "color-blue-04" to "004e80",
    "color-lblue-04" to "017c76",
    "color-green-04" to "017001",
    "color-yellow-04" to "ff9201",
    "color-pink-04" to "b41700",
    "color-purple-04" to "99195e",
    "color-gray-01" to "d6d5d5",
    "color-gray-02" to "929292",
    "color-gray-03" to "5f5f5f",
)

const val TYPE_PARAGRAPH = "p"
const val TYPE_BREAK = "br"
const val TYPE_FIGURE = "figure"
const val TYPE_IMAGE = "img"
const val TYPE_IMAGE_DESCRIPTION = "figcaption"
const val TYPE_QUOTE = "blockquote"
const val TYPE_SPAN = "span"

data class HtmlNode(
    val text: HtmlText? = null,
    val image: HtmlImage? = null
)

data class HtmlText(
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isCenter: Boolean = false,
    val color: Color = Color.White,
    val type: String,
    val content: AnnotatedString
)

data class HtmlImage(
    val imageUrl: String,
    val width: Float,
    val height: Float,
    val imageCaption: String
)

fun MutableList<HtmlNode>.parseElement(element: Element) {
    Log.d(TAG, "parseElement: $element")
    when (element.tag().name) {
        TYPE_PARAGRAPH -> {
            val text = HtmlText(
                isCenter = element.attributes().get("style").contains("text-align: center"),
                color = element.getColorFromClass(),
                content = buildAnnotatedString {
                    append(element.buildAnnotatedString())
                },
                type = element.tagName()
            )
            add(HtmlNode(text = text))
        }

        TYPE_QUOTE -> {
            if (element.childNodes().isNotEmpty()) {
                val text = HtmlText(
                    isCenter = element.attributes().get("style").contains("text-align: center"),
                    color = element.getColorFromClass(),
                    content = buildAnnotatedString {
                        append(element.buildAnnotatedString())
                    },
                    type = element.tagName()
                )
                add(HtmlNode(text = text))
            } else {
                addAll(element.children().map {
                    val text = HtmlText(
                        element.attributes().get("style").contains("text-align: center"),
                        color = element.getColorFromClass(),
                        content = AnnotatedString(it.text()),
                        type = element.tagName()
                    )
                    HtmlNode(text = text)
                })
            }
        }

        TYPE_BREAK -> {
            val text = HtmlText(
                content = AnnotatedString("\n"),
                type = element.tagName()
            )
            add(HtmlNode(text = text))
        }

        TYPE_FIGURE -> {
            val imageElement = element.children().find { it.tagName() == TYPE_IMAGE }
            val captionElement = element.children().find { it.tagName() == TYPE_IMAGE_DESCRIPTION }
            val url = imageElement?.attr("data-src")?.ifEmptyThenNull()?.let { "https:$it" } ?: ""
            val width = imageElement?.attr("width")?.ifEmptyThenNull()?.toFloat() ?: 1f
            val height = imageElement?.attr("height")?.ifEmptyThenNull()?.toFloat() ?: 1f
            val description = captionElement?.text()
            val image = HtmlImage(
                imageUrl = url,
                imageCaption = description ?: "",
                width = width,
                height = height
            )
            add(HtmlNode(image = image))
        }

        TYPE_IMAGE -> {
            val url = element.attr("data-src").ifEmptyThenNull()?.let { "https:$it" } ?: ""
            val width = element.attr("width").ifEmptyThenNull()?.toFloat() ?: 1f
            val height = element.attr("height").ifEmptyThenNull()?.toFloat() ?: 1f
            val image = HtmlImage(
                imageUrl = url,
                imageCaption = "",
                width = width,
                height = height
            )
            add(HtmlNode(image = image))
        }


        else -> {
            if (element.children().isNotEmpty()) {
                element.children().forEach {
                    parseElement(it)
                }
            } else {
                val text = HtmlText(
                    content = AnnotatedString(element.text()),
                    type = element.tagName()
                )
                add(HtmlNode(text = text))
            }
        }
    }
}

fun Element.buildAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        childNodes().forEach {
            Log.d(TAG, "buildAnnotatedString: $it")
            if (it is TextNode) {
                append(it.text())
            } else if (it is Element) {
                if (it.tagName() == "br") {
                    append("\n")
                } else {
                    withStyle(
                        style = SpanStyle(
                            color = it.getColorFromClass(),
                            fontWeight = if (it.children()
                                    .find { element -> element.tagName() == "b" || element.tagName() == "strong" } != null
                            ) FontWeight.Bold else FontWeight.Normal,

                            )
                    ) {
                        var currentNode = it
                        while (currentNode.childNodes().isNotEmpty()) {
                            currentNode = currentNode.childNode(0)
                        }
                        if (currentNode is Element) {
                            append(currentNode.text())
                        }
                        if (currentNode is TextNode) {
                            append(currentNode.text())
                        }
                    }
                }
            }
        }
    }
}

private fun Element.getColorFromClass(): Color {
    val classes = this.className().split(" ")
    for (clazz in classes) {
        if (colorMap.containsKey(clazz)) {
            return parseColor("#" + colorMap[clazz])
        }
    }
    return Color.White // Default color if class not found
}
