package com.liquidfun.playground.game.actors.flags_circle

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.liquidfun.playground.game.actors.checkbox.ACheckBoxTouch
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType

abstract class AbstractFlagsCircle: AdvancedGroup() {

    // Actor
    abstract val aCBoxList: List<ACheckBoxTouch>
    abstract val aAreaList: List<Actor>

    // Field
    abstract val angleList: List<Float>

    abstract val particleTypeList  : List<ParticleType>
    abstract val tmpParticleTypeArr: Array<ParticleType>

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    abstract fun addCBoxList()
    abstract fun addAreaList()

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    fun getCheckedParticleTypeArr(): Array<ParticleType> {
        tmpParticleTypeArr.clear()

        aCBoxList.onEachIndexed { index, box ->
            if (box.checkFlow.value) tmpParticleTypeArr.add(particleTypeList[index])
        }

        return tmpParticleTypeArr
    }

}