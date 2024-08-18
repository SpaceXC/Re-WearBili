package cn.spacexc.wearbili.remake.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cn.spacexc.wearbili.common.ifNullOrEmpty
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.space.ui.UserSpaceScreen
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily
import coil.compose.AsyncImage

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

fun Int?.toOfficialVerify(): OfficialVerify {
    return when (this) {
        -1 -> OfficialVerify.NONE
        0 -> OfficialVerify.PERSONAL
        1 -> OfficialVerify.ORG
        null -> OfficialVerify.NONE
        else -> OfficialVerify.NONE
    }
}

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    //imageModifier: Modifier = Modifier,
    avatar: String,
    pendant: String? = null,
    useBiliImage: Boolean = true,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    size: DpSize,
) {
    val localDensity = LocalDensity.current
    var avatarBoxSize by remember {
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
                avatarBoxSize = with(localDensity) {
                    it.height
                        .toDp()
                }
            }
    ) {
        if (pendant.isNullOrEmpty()) {
            if(useBiliImage) {
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
            }
            else {
                AsyncImage(
                    model = avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.82f)
                        .clip(
                            CircleShape
                        )
                )
            }
        } else {
            if(useBiliImage) {
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
            else {
                AsyncImage(
                    model = avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.6f)
                        .clip(
                            CircleShape
                        )
                )
                AsyncImage(
                    model = pendant,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        if (officialVerify != OfficialVerify.NONE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        end = avatarBoxSize * (if (pendant.isNullOrEmpty()) .1f else .2f),
                        bottom = avatarBoxSize * (if (pendant.isNullOrEmpty()) .1f else .2f)
                    )
            ) {
                Image(
                    painter = painterResource(
                        id = when (officialVerify) {
                            //OfficialVerify.NONE -> 0
                            OfficialVerify.PERSONAL -> R.drawable.icon_official_verify_personal
                            OfficialVerify.ORG -> R.drawable.icon_official_verify_org
                            else -> 0
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(avatarBoxSize * (if (pendant.isNullOrEmpty()) .25f else .25f))
                    //.padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }
    }
}


@Composable
fun LargeUserCard(
    modifier: Modifier = Modifier,
    avatar: String,
    pendant: String? = null,
    username: String,
    textSizeScale: Float = 1.0f,
    usernameColor: String? = null,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    userInfo: String? = null,
    isInfoAdaptive: Boolean = false,
    mid: Long,
    isFillMaxWidth: Boolean = true,
    navController: NavController
) {
    Card(
        modifier = modifier.apply { if (isFillMaxWidth) fillMaxWidth() },
        shape = CircleShape,
        innerPaddingValues = if (isFillMaxWidth) PaddingValues(8.dp) else PaddingValues(
            top = 8.dp,
            bottom = 8.dp,
            start = 8.dp,
            end = 12.dp
        ),
        onClick = {
            navController.navigate(UserSpaceScreen(mid))
        }
    ) {
        val localDensity = LocalDensity.current
        var avatarHeight by remember {
            mutableStateOf(0.dp)
        }
        Row(
            modifier = Modifier
                .apply { if (isFillMaxWidth) fillMaxWidth() },
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
            Column(modifier = Modifier/*.padding(vertical = 6.dp)*/) {
                AutoResizedText(
                    text = username,
                    fontWeight = Bold,
                    color = cn.spacexc.wearbili.common.domain.color.parseColor(
                        (usernameColor ?: "#FFFFFF").ifEmpty { "#FFFFFF" }),
                    modifier = Modifier
                        .onSizeChanged {
                            avatarHeight = with(localDensity) { it.height.toDp() }
                        },
                    style = AppTheme.typography.h2.copy(fontWeight = Bold)
                )
                if (!userInfo.isNullOrEmpty()) {
                    if (isInfoAdaptive) {
                        AutoResizedText(
                            text = userInfo,
                            style = TextStyle(
                                fontSize = 9.sp * textSizeScale,
                                fontWeight = FontWeight.Medium,
                                fontFamily = wearbiliFontFamily
                            ),
                            color = Color.White,
                            modifier = Modifier.alpha(0.7f),
                            maxLines = 1,
                            //overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = userInfo,
                            style = TextStyle(
                                fontSize = 9.sp * textSizeScale,
                                fontWeight = FontWeight.Medium,
                                fontFamily = wearbiliFontFamily
                            ),
                            color = Color.White,
                            modifier = Modifier.alpha(0.7f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
            }

        }
    }
}

/**
 * @param mid 设置为0 => 不可点击
 */
@Composable
fun SmallUserCard(
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    usernameModifier: Modifier = Modifier,
    avatar: String,
    pendant: String? = null,
    textSizeScale: Float = 1.0f,
    officialVerify: OfficialVerify = OfficialVerify.NONE,
    username: String,
    usernameColor: String? = null,
    userInfo: String? = null,
    userLabel: AnnotatedString = buildAnnotatedString { },
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    navController: NavController,
    useBiliImage: Boolean = true,
    mid: Long
) {
    val localDensity = LocalDensity.current
    var avatarHeight by remember {
        mutableStateOf(0.dp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickVfx(enabled = mid != 0L, onClick = {
            if (mid != 0L) {
                /*context.startActivity(Intent(context, UserSpaceActivity::class.java).apply {
                    putExtra(PARAM_MID, mid)
                })*/
                navController.navigate(UserSpaceScreen(mid))
            }
        })
    ) {
        UserAvatar(
            avatar = avatar,
            pendant = pendant,
            officialVerify = officialVerify,
            modifier = imageModifier.size(avatarHeight.times(1.2f)),
            size = DpSize.Unspecified,
            useBiliImage = useBiliImage
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
                            color = cn.spacexc.wearbili.common.domain.color.parseColor(
                                usernameColor.ifNullOrEmpty { "#FFFFFF" }
                            ),
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(username)
                    }
                },
                //color = parseColor(usernameColor ?: "#FFFFFF"),
                style = TextStyle(
                    fontSize = 12.5.sp * textSizeScale,
                    fontWeight = FontWeight.Medium,
                    fontFamily = wearbiliFontFamily
                ),
                fontWeight = FontWeight.Medium,
                inlineContent = inlineContent,
                modifier = usernameModifier
            )
            if (!userInfo.isNullOrEmpty()) {
                AutoResizedText(
                    text = userInfo,
                    style = TextStyle(
                        fontSize = 10.5.sp * textSizeScale,
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

@Composable
fun TinyUserCard(
    avatar: String,
    username: String,
    mid: Long,
    navController: NavController
) {
    val localDensity = LocalDensity.current
    var textHeight by remember {
        mutableStateOf(0.dp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickVfx {
                navController.navigate(UserSpaceScreen(mid))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        BiliImage(
            url = avatar,
            contentDescription = null,
            modifier = Modifier
                .size(textHeight * 1.8f)
                .clip(
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = username,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.onSizeChanged {
                textHeight = with(localDensity) { it.height.toDp() }
            },
            fontSize = 12.5.sp
        )
    }
}