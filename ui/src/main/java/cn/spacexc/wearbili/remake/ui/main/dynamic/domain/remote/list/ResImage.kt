package cn.spacexc.wearbili.remake.app.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ResImage(
    @SerializedName("image_src")
    val imageSrc: ImageSrc
)