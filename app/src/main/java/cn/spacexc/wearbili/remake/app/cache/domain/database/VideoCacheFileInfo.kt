package cn.spacexc.wearbili.remake.app.cache.domain.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Created by XC-Qan on 2023/9/5.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val STATE_IDLE = "idle"
const val STATE_FETCHING = "fetching"   //获取视频中
const val STATE_DOWNLOADING = "downloading"
const val STATE_COMPLETED = "completed"
const val STATE_FAILED = "failed"

@Entity(tableName = "video_caches")
@TypeConverters(MapTypeConverter::class)
data class VideoCacheFileInfo(
    @PrimaryKey @ColumnInfo(name = "cid") val videoCid: Long,
    @ColumnInfo(name = "state") val state: String = STATE_IDLE,
    @ColumnInfo(name = "bvid") val videoBvid: String,
    @ColumnInfo(name = "name") val videoName: String,
    @ColumnInfo(name = "part_name") val videoPartName: String,
    @ColumnInfo(name = "uploader_name") val videoUploaderName: String,
    @ColumnInfo(name = "cover_url") val videoCover: String,
    @ColumnInfo(name = "file_size") val downloadFileSize: String = "0B",
    @ColumnInfo(name = "duration") val videoDurationMillis: Long = 0L,
    @ColumnInfo(name = "download_progress") val downloadProgress: Int = 0,
    @ColumnInfo(name = "subtitle_filenames") val downloadedSubtitleFileNames: Map<String, String> = emptyMap()
)