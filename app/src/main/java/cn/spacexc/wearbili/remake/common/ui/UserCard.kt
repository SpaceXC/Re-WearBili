package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.common.domain.color.parseColor
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by XC-Qan on 2023/4/12.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

enum class OfficialVerify {
    NONE,
    PERSONAL,
    ORG
}

fun Int.toOfficialVerify(): OfficialVerify {
    return when (this) {
        -1 -> OfficialVerify.NONE
        0 -> OfficialVerify.PERSONAL
        1 -> OfficialVerify.ORG
        else -> OfficialVerify.NONE
    }
}

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    avatar: String,
    pendant: String? = null,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    size: DpSize,
) {
    val localDensity = LocalDensity.current
    var officialVerifyIconSize by remember {
        mutableStateOf(0.dp)
    }
    Box(
        modifier = modifier
            .apply {
                if (size != DpSize.Unspecified) size(
                    size
                )
            }
            .aspectRatio(1f)
            .onSizeChanged {
                officialVerifyIconSize = with(localDensity) {
                    it.height
                        .toDp()
                        .times(0.2f)
                }
            }
    ) {
        if (pendant.isNullOrEmpty()) {
            BiliImage(
                url = avatar,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(0.82f)
                    .clip(
                        CircleShape
                    )
            )
        } else {
            BiliImage(
                url = avatar,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(0.6f)
                    .clip(
                        CircleShape
                    )
            )
            BiliImage(
                url = pendant,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                placeholderEnabled = false
            )
        }
        if (officialVerify != OfficialVerify.NONE) {
            Icon(
                painter = painterResource(
                    id = when (officialVerify) {
                        OfficialVerify.ORG -> R.drawable.icon_official_verify_org
                        OfficialVerify.PERSONAL -> R.drawable.icon_official_verify_personal
                        else -> 0
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(officialVerifyIconSize)
                    .padding(end = 16.dp, bottom = 16.dp)
            )
        }
    }
}


@Composable
fun LargeUserCard(
    avatar: String,
    pendant: String? = null,
    username: String,
    usernameColor: String? = null,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    mid: Long
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        val localDensity = LocalDensity.current
        var avatarHeight by remember {
            mutableStateOf(0.dp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            //.height(IntrinsicSize.Min)
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                avatar = avatar,
                officialVerify = officialVerify,
                pendant = pendant,
                size = DpSize.Unspecified,
                modifier = Modifier.size(avatarHeight * 2f)
            )
            Spacer(modifier = Modifier.width(2.dp))
            AutoResizedText(
                text = username,
                color = parseColor((usernameColor ?: "#FFFFFF").ifEmpty { "#FFFFFF" }),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .onSizeChanged {
                        avatarHeight = with(localDensity) { it.height.toDp() }
                    },
                style = AppTheme.typography.h2
            )
        }
    }
}

@Composable
fun SmallUserCard(
    modifier: Modifier = Modifier,
    avatar: String,
    pendant: String? = null,
    textSizeScale: Float = 1.0f,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    username: String,
    usernameColor: String? = null,
    userInfo: String? = null,
    userLabel: AnnotatedString = buildAnnotatedString { },
    inlineContent: Map<String, InlineTextContent> = mapOf()
) {
    val localDensity = LocalDensity.current
    var avatarHeight by remember {
        mutableStateOf(0.dp)
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        UserAvatar(
            avatar = avatar,
            pendant = pendant,
            officialVerify = officialVerify,
            modifier = Modifier.size(avatarHeight.times(1.1f)),
            size = DpSize.Unspecified
        )
        Spacer(modifier = Modifier.width(2.dp))
        Column(modifier = Modifier.onSizeChanged {
            avatarHeight = with(localDensity) { it.height.toDp() }
        }) {
            AutoResizedText(
                text = buildAnnotatedString {
                    append(userLabel)
                    withStyle(
                        style = SpanStyle(
                            color = parseColor(usernameColor ?: "#FFFFFF"),
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(username)
                    }
                },
                //color = parseColor(usernameColor ?: "#FFFFFF"),
                style = TextStyle(
                    fontSize = 12.5.spx * textSizeScale,
                    fontWeight = FontWeight.Medium,
                    fontFamily = wearbiliFontFamily
                ),
                fontWeight = FontWeight.Medium,
                inlineContent = inlineContent
            )
            if (!userInfo.isNullOrEmpty()) {
                AutoResizedText(
                    text = userInfo,
                    style = TextStyle(
                        fontSize = 10.5.spx * textSizeScale,
                        fontWeight = FontWeight.Medium,
                        fontFamily = wearbiliFontFamily
                    ),
                    color = Color.White,
                    modifier = Modifier.alpha(0.7f)
                )
            }
        }
    }
}