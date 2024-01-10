package cn.spacexc.wearbili.remake.ui.player.videoplayer.danmaku

import bilibili.community.service.dm.v1.DmSegMobileReply
import bilibili.community.service.dm.v1.DmWebViewReply
import cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils

/**
 * Created by XC-Qan on 2023/12/24.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class DanmakuGetter(
    private val networkUtils: KtorNetworkUtils
) {
    suspend fun getDanmaku(
        videoCid: Long,
        segment: Int
    ): DmSegMobileReply {
        try {
            val responseBytes =
                networkUtils.getBytes("https://api.bilibili.com/x/v2/dm/web/seg.so?type=1&oid=$videoCid&segment_index=$segment")
            return DmSegMobileReply.parseFrom(responseBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getSpecialDanmakus(
        videoCid: Long
    ): DmWebViewReply {
        try {
            val responseBytes =
                networkUtils.getBytes("https://api.bilibili.com/x/v2/dm/web/view?type=1&oid=$videoCid")
            return DmWebViewReply.parseFrom(responseBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}