package com.liquidfun.playground.game.box2d.bodies.flags_circle

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.liquidfun.playground.game.actors.flags_circle.AFlagsCircle
import com.liquidfun.playground.game.actors.flags_circle.AFlagsCircle_Disable_BR
import com.liquidfun.playground.game.actors.flags_circle.AFlagsCircle_Disable_SP_EL_WL_BR
import com.liquidfun.playground.game.actors.flags_circle.AFlagsCircle_Disable_WL_BR
import com.liquidfun.playground.game.box2d.AbstractBody
import com.liquidfun.playground.game.box2d.BodyId
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup

class BFlagsCircle(override val screenBox2d: AdvancedBox2dScreen, private val flagsCircle: FlagsCircle): AbstractBody() {
    override val name       = "circle"
    override val bodyDef    = BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
    }
    override val fixtureDef = FixtureDef().apply {
        density = 1f
    }

    override var actor: AdvancedGroup? = getActorFlagsCircle()

    override var id = BodyId.DYNAMIC

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    fun getActor() = when (flagsCircle) {
        FlagsCircle.DEFAULT     -> actor as AFlagsCircle
        FlagsCircle.BR       -> actor as AFlagsCircle_Disable_BR
        FlagsCircle.WL_BR       -> actor as AFlagsCircle_Disable_WL_BR
        FlagsCircle.SP_EL_WL_BR -> actor as AFlagsCircle_Disable_SP_EL_WL_BR
    }

    private fun getActorFlagsCircle() = when (flagsCircle) {
        FlagsCircle.DEFAULT     -> AFlagsCircle(screenBox2d)
        FlagsCircle.BR          -> AFlagsCircle_Disable_BR(screenBox2d)
        FlagsCircle.WL_BR       -> AFlagsCircle_Disable_WL_BR(screenBox2d)
        FlagsCircle.SP_EL_WL_BR -> AFlagsCircle_Disable_SP_EL_WL_BR(screenBox2d)
    }

    enum class FlagsCircle {
        DEFAULT    , // All
        BR         , // Wall_Barrier
        WL_BR      , // Wall_Barrier
        SP_EL_WL_BR, // Spring_Elastic_Wall_Barrier
    }

}