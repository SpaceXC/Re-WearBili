package cn.spacexc.wearbili.remake.ui.main.dynamic.domain.remote.list


import com.google.gson.annotations.SerializedName

data class ResImage(
    @SerializedName("image_src")
    val imageSrc: ImageSrc
)