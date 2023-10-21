package cn.spacexc.wearbili.remake.livestreamplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import cn.spacexc.wearbili.remake.livestreamplayer.databinding.ActivityLiveStreamPlayerBinding
import kotlinx.coroutines.launch

const val PARAM_LIVE_ROOM_ID = "liveStreamRoomId"

class LiveStreamPlayerActivity : ComponentActivity() {
    private val viewModel by viewModels<LiveStreamPlayerViewModel>()


    private lateinit var binding: ActivityLiveStreamPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveStreamPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.player.player = viewModel.player

        lifecycleScope.launch {
            launch {
                viewModel.playerState.collect { state ->
                    binding.progressBar2.isVisible =
                        state != LiveStreamPlayerViewModel.StreamPlayerState.Playing
                    binding.textView.isVisible =
                        state != LiveStreamPlayerViewModel.StreamPlayerState.Playing
                }
            }
            launch {
                viewModel.playerMessage.collect {
                    binding.textView.text = it
                    viewModel.player.stop()
                    viewModel.player.release()
                }
            }
        }

        binding.button.setOnClickListener {
            finish()
        }


        val roomId = intent.getLongExtra(PARAM_LIVE_ROOM_ID, 0)
        if (roomId != 0L) {
            viewModel.playLiveStreamFromRoomId(roomId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.player.stop()
        viewModel.player.release()
    }
}