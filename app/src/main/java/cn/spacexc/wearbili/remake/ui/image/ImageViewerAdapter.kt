package cn.spacexc.wearbili.remake.ui.image

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.github.chrisbanes.photoview.PhotoView

/**
 * Created by XC-Qan on 2022/7/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class ImageViewerAdapter(val context: Context) :
    ListAdapter<String, ImageViewerAdapter.ImageViewerViewHolder>(object :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewerViewHolder {
        return ImageViewerViewHolder(
            PhotoView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            }
        )
    }

    override fun onBindViewHolder(holder: ImageViewerViewHolder, position: Int) {
        try {
            val imageLoader = ImageLoader.Builder(holder.itemView.context)
                .crossfade(true)
                .components {
                    if (SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
            val request = ImageRequest.Builder(holder.itemView.context)
                .data(getItem(position))
                .crossfade(true)
                .target(holder.itemView as PhotoView)
                .build()
            imageLoader.enqueue(request)
        } catch (_: OutOfMemoryError) {

        }
    }

    class ImageViewerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}