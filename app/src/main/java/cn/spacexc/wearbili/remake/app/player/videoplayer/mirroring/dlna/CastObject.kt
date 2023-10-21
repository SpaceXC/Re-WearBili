package cn.spacexc.wearbili.remake.app.player.videoplayer.mirroring.dlna

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
        return MyCastVideo(url, id, name)
    }
}


class MyCastVideo(
    private val mUrl: String,
    private val mId: String,
    private val mName: String
) : ICast.ICastVideo {
    private val durationMillis = 0L
    private val size = 0L
    private val bitrate = 0L
    override fun getId(): String {
        return mId
    }

    override fun getUri(): String {
        return mUrl
    }

    override fun getName(): String {
        return mName
    }

    /**
     * @return video duration, ms
     */
    override fun getDurationMillSeconds(): Long {
        return durationMillis
    }

    override fun getSize(): Long {
        return size
    }

    override fun getBitrate(): Long {
        return bitrate
    }
}