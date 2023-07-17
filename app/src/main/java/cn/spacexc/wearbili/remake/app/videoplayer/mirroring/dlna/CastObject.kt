package cn.spacexc.wearbili.remake.app.videoplayer.mirroring.dlna

import com.android.cast.dlna.core.ICast


// ---------------------------------------------
// ---- URL
// ---------------------------------------------
const val CAST_VIDEO_MP4 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val CAST_VIDEO_M3U8 =
    "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8"
const val CAST_IMAGE_JPG = "https://seopic.699pic.com/photo/40011/2135.jpg_wh1200.jpg"

// ---------------------------------------------
// ---- Cast Object
// ---------------------------------------------
object CastObject {

    // demo
    fun newInstance(url: String, id: String, name: String): ICast {
        return CastVideo(url, id, name)
    }
}

data class CastImage @JvmOverloads constructor(
    override val uri: String,
    override val id: String,
    override val name: String,
    override val size: Long = 0L
) : ICast.ICastImage

data class CastVideo @JvmOverloads constructor(
    override val uri: String,
    override val id: String,
    override val name: String,
    override val size: Long = 0L,
    override val bitrate: Long = 0
) : ICast.ICastVideo {

    var duration: Long = 0

    override val durationMillSeconds: Long
        get() = duration
}