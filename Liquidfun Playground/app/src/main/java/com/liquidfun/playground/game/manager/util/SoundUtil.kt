package com.liquidfun.playground.game.manager.util

import com.badlogic.gdx.audio.Sound
import com.liquidfun.playground.game.manager.AudioManager
import com.liquidfun.playground.game.manager.SoundManager
import com.liquidfun.playground.game.utils.runGDX
class SoundUtil {

    val DROP = SoundManager.EnumSound.DROP.data.sound

    // 0..100
    var volumeLevel = AudioManager.volumeLevelPercent

    var isPause = (volumeLevel <= 0f)

    fun play(sound: Sound, volume: Float = volumeLevel) = runGDX {
        if (isPause.not()) sound.play(volume / 100f)
    }
}

