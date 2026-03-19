package com.example.armusic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.armusic.domain.usecase.PlaySongUseCase
import com.example.armusic.ui.state.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicPlayerVM @Inject constructor(
    private val playSong: PlaySongUseCase
): ViewModel() {
    private val _playerState = MutableLiveData<PlayerState<ExoPlayer>>(PlayerState.NoPlayer)
    val playerState: LiveData<PlayerState<ExoPlayer>> get() = _playerState
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying
    private val _duration = MutableLiveData<Long>(1L)
    val duration: LiveData<Long> get() = _duration

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> = _currentTime

    private val _buffer = MutableLiveData<Long>()
    val buffer: LiveData<Long> get() = _buffer

    private val _isSeeking= MutableLiveData<Boolean>()
    val isSeeking: LiveData<Boolean> get() = _isSeeking

    suspend fun playSong(songUrl: String) {
        ( _playerState.value as? PlayerState.Active )?.player?.clearMediaItems()
        _currentTime.value = 0L
        _duration.value = 1L


        val newPlayer = playSong.invoke(songUrl)
        _playerState.value = PlayerState.Active(newPlayer)
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                _duration.value = player.duration.coerceAtLeast(1L)
                _currentTime.value = player.currentPosition.coerceAtLeast(1L)
            }
            override fun onPlaybackStateChanged(state: Int) {

                if (state == Player.STATE_READY) {
                    when(val player = playerState.value){
                        is PlayerState.Active->{
                            _duration.value = player.player.duration
                        }
                        is PlayerState.NoPlayer->{
                            _duration.value = 1L
                        }
                        else->_duration.value
                    }

                }

            }

            override fun onIsPlayingChanged(playing: Boolean) {
                _isPlaying.value = playing

            }

        }

        newPlayer.addListener(listener)



    }


    fun previous(){
        ( _playerState.value as? PlayerState.Active )?.player?.seekBack()
    }
    fun pause() {
        ( _playerState.value as? PlayerState.Active )?.player?.pause()
    }
    fun next(){
        ( _playerState.value as? PlayerState.Active )?.player?.seekForward()
    }
    /** Reanudar la reproducción */
    fun resume() {
        ( _playerState.value as? PlayerState.Active )?.player?.play()
    }

    /** Detener y liberar el reproductor */
    fun stop() {
        (_playerState.value as? PlayerState.Active)?.player?.release()
        _isPlaying.value = false
    }
    //se cambia con la posicion del slider
    fun changeCurrentTime(time: Float){
        ( _playerState.value as? PlayerState.Active )?.player?.seekTo(time.toLong())

    }
    fun isSeeking(isSeeking: Boolean){
        _isSeeking.value = isSeeking
    }




    

}