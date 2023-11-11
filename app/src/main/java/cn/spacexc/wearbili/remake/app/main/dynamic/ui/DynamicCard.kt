package cn.spacexc.wearbili.remake.app.main.dynamic.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.common.domain.video.toShortChinese
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BANGUMI_ID_TYPE_EPID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.BangumiActivity
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID
import cn.spacexc.wearbili.remake.app.bangumi.info.ui.PARAM_BANGUMI_ID_TYPE
import cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list.DynamicItem
import cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list.ItemRichTextNode
import cn.spacexc.wearbili.remake.app.search.ui.PARAM_DEFAULT_SEARCH_KEYWORD
import cn.spacexc.wearbili.remake.app.search.ui.SearchActivity
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ui.BiliImage
import cn.spacexc.wearbili.remake.common.ui.BilibiliPink
import cn.spacexc.wearbili.remake.common.ui.Card
import cn.spacexc.wearbili.remake.common.ui.ClickableText
import cn.spacexc.wearbili.remake.common.ui.IconText
import cn.spacexc.wearbili.remake.common.ui.SmallUserCard
import cn.spacexc.wearbili.remake.common.ui.clickVfx
import cn.spacexc.wearbili.remake.common.ui.spx
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import cn.spacexc.wearbili.remake.common.ui.toOfficialVerify

/**
 * Created by XC-Qan on 2023/4/27.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

val supportedDynamicTypes =
    listOf("DYNAMIC_TYPE_FORWARD", "DYNAMIC_TYPE_DRAW", "DYNAMIC_TYPE_WORD", "DYNAMIC_TYPE_AV")

@Composable
fun DynamicRichText(
    modifier: Modifier = Modifier,
    textNodes: List<ItemRichTextNode>,
    textStyle: TextStyle,
    context: Context,
    leadingIcon: (@Composable () -> Unit)? = null,
    onGloballyClicked: () -> Unit
) {
    val inlineContentMap = hashMapOf(
        "leadingIcon" to InlineTextContent(
            Placeholder(
                width = textStyle.fontSize,
                height = textStyle.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            leadingIcon?.invoke()
        },
        "webLinkIcon" to InlineTextContent(
            Placeholder(
                width = textStyle.fontSize,
                height = textStyle.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextBottom
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Link,
                contentDescription = null,
                tint = cn.spacexc.wearbili.common.domain.color.parseColor("#178bcf"),
                modifier = Modifier.fillMaxSize()
            )
        },
        "lotteryIcon" to InlineTextContent(
            Placeholder(
                width = textStyle.fontSize,
                height = textStyle.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Redeem,
                contentDescription = null,
                tint = cn.spacexc.wearbili.common.domain.color.parseColor("#178bcf"),
                modifier = Modifier.fillMaxSize()
            )
        }
    )
    val annotatedString = buildAnnotatedString {
        leadingIcon?.let {
            appendInlineContent("leadingIcon")
            append(" ")
        }
        textNodes.forEach {
            when (it.type) {
                "RICH_TEXT_NODE_TYPE_TEXT" -> {
                    append(it.text)
                }

                "RICH_TEXT_NODE_TYPE_WEB" -> {
                    pushStringAnnotation(tag = "tagUrl", annotation = it.jumpUrl ?: "")
                    withStyle(
                        style = SpanStyle(
                            color = cn.spacexc.wearbili.common.domain.color.parseColor(
                                "#178bcf"
                            )
                        )
                    ) {
                        appendInlineContent("webLinkIcon")
                        append("网页链接")
                    }
                    pop()
                }

                "RICH_TEXT_NODE_TYPE_TOPIC" -> {
                    pushStringAnnotation(tag = "tagSearch", annotation = it.text)
                    withStyle(
                        style = SpanStyle(
                            color = cn.spacexc.wearbili.common.domain.color.parseColor(
                                "#178bcf"
                            )
                        )
                    ) {
                        append(it.text)
                    }
                    pop()
                }

                "RICH_TEXT_NODE_TYPE_AT" -> {
                    pushStringAnnotation(tag = "tagUser", annotation = it.rid ?: "")
                    withStyle(
                        style = SpanStyle(
                            color = cn.spacexc.wearbili.common.domain.color.parseColor(
                                "#178bcf"
                            )
                        )
                    ) {
                        append(it.text)
                    }
                    pop()
                }

                "RICH_TEXT_NODE_TYPE_LOTTERY" -> {
                    withStyle(
                        style = SpanStyle(
                            color = cn.spacexc.wearbili.common.domain.color.parseColor(
                                "#178bcf"
                            )
                        )
                    ) {
                        appendInlineContent("lotteryIcon")
                        append(it.text)
                    }
                }

                "RICH_TEXT_NODE_TYPE_EMOJI" -> {
                    appendInlineContent(it.emoji?.text ?: "")
                    inlineContentMap[it.emoji?.text ?: ""] = InlineTextContent(
                        Placeholder(
                            width = textStyle.fontSize.times(1.4f).times(it.emoji?.size ?: 1),
                            height = textStyle.fontSize.times(1.4f).times(it.emoji?.size ?: 1),
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) { _ ->
                        BiliImage(
                            url = it.emoji?.iconUrl ?: "",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                else -> {
                    append(it.text)
                }
            }
        }
    }
    ClickableText(
        text = annotatedString,
        inlineTextContent = inlineContentMap,
        style = textStyle,
        modifier = modifier
    ) { index ->
        /*annotatedString.getStringAnnotations(tag = "tagUser", start = index, end = index)
            .firstOrNull()?.let { annotation ->
                context.startActivity(Intent(context, SpaceProfileActivity::class.java).apply {
                    putExtra("userMid", annotation.item.toLong())
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
                return@ClickableText
            }*/
        annotatedString.getStringAnnotations(tag = "tagSearch", start = index, end = index)
            .firstOrNull()?.let { annotation ->
                val intent = Intent(context, SearchActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(PARAM_DEFAULT_SEARCH_KEYWORD, annotation.item)
                context.startActivity(intent)
                return@ClickableText
            }
        /*annotatedString.getStringAnnotations(tag = "tagUrl", start = index, end = index)
            .firstOrNull()?.let { annotation ->
                context.startActivity(Intent(context, LinkProcessActivity::class.java).apply {
                    putExtra("url", annotation.item)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
                return@ClickableText
            }*/
        onGloballyClicked()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DynamicContent(
    item: DynamicItem,
    textSizeScale: Float = 1.0f,
    context: Context/*?*/   //for compose preview   //f*** **u compose preview!
) {
    val localDensity = LocalDensity.current
    Column(modifier = Modifier.padding(4.dp)) {
        item.modules.moduleDynamic.desc?.let {
            Spacer(modifier = Modifier.height(2.dp))
            DynamicRichText(
                textNodes = it.richTextNodes,
                textStyle = AppTheme.typography.body1.copy(fontSize = AppTheme.typography.body1.fontSize * textSizeScale),
                context = context
            ) {
                //TODO 动态详情&评论
                /*if (supportedDynamicTypes.contains(item.type)) {
                    val intent = Intent(context, NewDynamicDetailActivity::class.java)
                    intent.putExtra("dyId", item.idStr)
                    intent.putExtra(
                        "oid", when (item.type) {
                            "DYNAMIC_TYPE_FORWARD", "DYNAMIC_TYPE_WORD" -> item.idStr
                            "DYNAMIC_TYPE_DRAW" -> item.modules.moduleDynamic.major?.draw?.id?.toString()
                            "DYNAMIC_TYPE_AV" -> item.modules.moduleDynamic.major?.archive?.aid
                            else -> ""
                        }
                    )
                    intent.putExtra("dyType", item.type)
                    intent.putExtra("upMid", item.modules.moduleAuthor.mid)
                    context.startActivity(intent)
                }*/
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
        when (item.type) {
            "DYNAMIC_TYPE_FORWARD" -> {
                if (item.orig != null) {
                    DynamicCard(item = item.orig, context = context, textSizeScale = 0.95f)
                }
            }

            "DYNAMIC_TYPE_DRAW" -> {
                val imageList = item.modules.moduleDynamic.major?.draw?.items
                if (!imageList.isNullOrEmpty()) {
                    //Spacer(modifier = Modifier.height(6.dp))
                    LazyVerticalGrid(
                        modifier = Modifier.requiredSizeIn(maxHeight = 4000.dp),
                        columns = GridCells.Fixed(
                            if (imageList.size == 1 || imageList.size % 2 == 0) 2 else 3
                        )
                    ) {
                        imageList.forEachIndexed { _, image ->
                            item {
                                BiliImage(
                                    url = image.src,
                                    contentDescription = null,
                                    modifier = when (imageList.size) {
                                        1 -> Modifier
                                            .fillMaxWidth()
                                            .clickVfx {
                                                /*val intent =
                                                    Intent(context, ImageViewerActivity::class.java)
                                                val arrayList = arrayListOf<DrawItem>()
                                                arrayList.addAll(imageList)
                                                intent.putParcelableArrayListExtra(
                                                    "imageList",
                                                    arrayList
                                                )
                                                intent.putExtra("currentPhotoItem", index)
                                                context.startActivity(intent)*/
                                            }
                                            .aspectRatio(image.width.toFloat() / image.height.toFloat())
                                            .clip(
                                                RoundedCornerShape(6.dp)
                                            )

                                        else -> Modifier
                                            .padding(2.dp)
                                            .aspectRatio(1f)
                                            .clickVfx {
                                                /*val intent =
                                                    Intent(context, ImageViewerActivity::class.java)
                                                val arrayList = arrayListOf<DrawItem>()
                                                arrayList.addAll(imageList)
                                                intent.putParcelableArrayListExtra(
                                                    "imageList",
                                                    arrayList
                                                )
                                                intent.putExtra("currentPhotoItem", index)
                                                context.startActivity(intent)*/
                                            }
                                            .clip(
                                                RoundedCornerShape(6.dp)
                                            )
                                    },
                                    contentScale = if (imageList.size == 1) ContentScale.FillBounds else ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            "DYNAMIC_TYPE_WORD" -> {

            }

            "DYNAMIC_TYPE_AV" -> {
                var infoHeight by remember {
                    mutableStateOf(0.dp)
                }

                Box(
                    modifier = Modifier
                        .clickVfx {
                            context.startActivity(
                                Intent(context, VideoInformationActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    putExtra(
                                        PARAM_VIDEO_ID,
                                        item.modules.moduleDynamic.major?.archive?.bvid
                                    )
                                }
                            )
                        }
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    BiliImage(
                        url = item.modules.moduleDynamic.major?.archive?.cover ?: "",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.FillWidth
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color(0, 0, 0, 204)
                                    )
                                )
                            )
                            .fillMaxWidth()
                            .height(infoHeight)
                            .align(Alignment.BottomCenter),
                    )   //阴影
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .onGloballyPositioned {
                                infoHeight = with(localDensity) {
                                    it.size.height
                                        .toDp()
                                }
                            }
                            .padding(6.dp)) {
                        Text(
                            text = item.modules.moduleDynamic.major?.archive?.title ?: "",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier,
                            fontSize = 10.spx * textSizeScale
                        )
                        Row(modifier = Modifier.alpha(0.6f)) {
                            IconText(
                                text = item.modules.moduleDynamic.major?.archive?.stat?.play ?: "",
                                color = Color.White,
                                fontSize = 9.spx * textSizeScale,
                                fontWeight = FontWeight.Medium,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_view_count),
                                    contentDescription = null,
                                    modifier = Modifier,
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconText(
                                text = item.modules.moduleDynamic.major?.archive?.stat?.danmaku
                                    ?: "",
                                color = Color.White,
                                fontSize = 9.spx * textSizeScale,
                                fontWeight = FontWeight.Medium,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_danmaku),
                                    contentDescription = null,
                                    modifier = Modifier,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            "DYNAMIC_TYPE_PGC", "DYNAMIC_TYPE_PGC_UNION" -> {
                /*Text(
                    text = "${item.modules.moduleDynamic.major?.pgc?.title} 更新了",
                    fontFamily = puhuiFamily,
                    color = Color.White,
                    fontSize = 10.spx
                )*/
                Column(modifier = Modifier.clickVfx {
                    Intent(
                        context,
                        BangumiActivity::class.java
                    ).apply {
                        putExtra(PARAM_BANGUMI_ID, item.modules.moduleDynamic.major?.pgc?.epid)
                        putExtra(PARAM_BANGUMI_ID_TYPE, BANGUMI_ID_TYPE_EPID)
                        context.startActivity(this)
                    }
                }) {
                    var infoHeight by remember {
                        mutableStateOf(0.dp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        BiliImage(
                            url = item.modules.moduleDynamic.major?.pgc?.cover ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentScale = ContentScale.FillWidth
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color(0, 0, 0, 204)
                                        )
                                    )
                                )
                                .fillMaxWidth()
                                .height(infoHeight)
                                .align(Alignment.BottomCenter),
                        )   //阴影
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .onGloballyPositioned {
                                    infoHeight = with(localDensity) {
                                        it.size.height
                                            .toDp()
                                    }
                                }
                                .padding(6.dp)) {
                            Text(
                                text = item.modules.moduleDynamic.major?.pgc?.title ?: "",
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier,
                                fontSize = 10.spx * textSizeScale
                            )
                            Row {
                                var textHeight by remember {
                                    mutableStateOf(0.dp)
                                }
                                Icon(
                                    imageVector = Icons.Outlined.PlayCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(textHeight),
                                    tint = Color.White
                                )
                                Text(
                                    text = item.modules.moduleDynamic.major?.pgc?.stat?.play ?: "",
                                    color = Color.White,
                                    fontSize = 9.spx * textSizeScale,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.onGloballyPositioned {
                                        with(localDensity) {
                                            textHeight =
                                                it.size.height.toDp()
                                        }
                                    })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            else -> {
                Text(
                    text = "不支持的动态类型",
                    color = BilibiliPink,
                    fontSize = 11.spx,
                    modifier = Modifier.alpha(0.7f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item.modules.moduleInteraction?.items?.forEach { item ->
            Spacer(modifier = Modifier.height(6.dp))
            DynamicRichText(
                textNodes = item.desc.richTextNodes,
                textStyle = TextStyle(
                    fontSize = 9.spx,
                    color = Color.White,
                    fontFamily = wearbiliFontFamily
                ),
                context = context,
                modifier = Modifier
                    .alpha(0.7f)
                    .padding(horizontal = 4.dp),
                leadingIcon = {
                    when (item.type) {
                        1 -> {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_comment),
                                contentDescription = null,
                                modifier = Modifier,
                                tint = Color.White
                            )
                        }

                        2 -> {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                modifier = Modifier,
                                tint = Color.White
                            )
                        }
                    }
                },
                onGloballyClicked = {}
            )
            /*IconText(text = item.desc.text, fontSize = 9.spx, modifier = Modifier.alpha(0.5f)) {

            }*/
        }
        item.modules.moduleStat?.let {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                IconText(
                    text = it.like?.count?.toShortChinese() ?: "null",
                    fontSize = 9.spx,
                    fontFamily = wearbiliFontFamily
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_thumb_up),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconText(
                    text = it.forward?.count?.toShortChinese() ?: "null",
                    fontSize = 9.spx,
                    fontFamily = wearbiliFontFamily
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_comment),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconText(
                    text = it.comment?.count?.toShortChinese() ?: "null",
                    fontSize = 9.spx,
                    fontFamily = wearbiliFontFamily
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_dynamic_forward),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

        }
    }
}

@Composable
fun DynamicCard(
    item: DynamicItem,
    context: Context,
    textSizeScale: Float = 1.0f
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        innerPaddingValues = PaddingValues(
            top = 6.dp, bottom = 4.dp,
            start = 4.dp, end = 4.dp
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            item.modules.moduleAuthor.also { author ->
                SmallUserCard(
                    avatar = author.face,
                    username = author.name,
                    pendant = author.pendant?.image,
                    userInfo = buildString {
                        append(author.pubTime)
                        if (author.pubTime.isNotEmpty()) append(" ")
                        append(author.pubAction)
                    },
                    usernameColor = author.vip?.nicknameColor,
                    textSizeScale = textSizeScale,
                    officialVerify = (author.officialVerify?.type ?: -1).toOfficialVerify()
                )
            }
            DynamicContent(item = item, context = context, textSizeScale = textSizeScale)
        }
    }
}