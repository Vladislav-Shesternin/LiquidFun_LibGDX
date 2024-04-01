package com.liquidfun.playground.game.box2d.bodiesGroup

import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.liquidfun.playground.game.box2d.AbstractBodyGroup
import com.liquidfun.playground.game.box2d.AbstractJoint
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodies.standart.BStatic
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen

class BGFlagsCircle(override val screenBox2d: AdvancedBox2dScreen, flagsCircle: BFlagsCircle.FlagsCircle) : AbstractBodyGroup() {

    override val standartW = 661f

    // Body
    private val bStatic      = BStatic(screenBox2d)
    private val bFlagsCircle = BFlagsCircle(screenBox2d, flagsCircle)

    // Joint
    private val jRevolute = AbstractJoint<RevoluteJoint, RevoluteJointDef>(screenBox2d)

    val checkedParticleTypeArr get() = bFlagsCircle.getActor().getCheckedParticleTypeArr()

    override fun create(x: Float, y: Float, w: Float, h: Float) {
        super.create(x, y, w, h)

        createB_Static()
        createB_FlagsCircle()
        createJ_Revolute()
    }


    // ---------------------------------------------------
    // Create Body
    // ---------------------------------------------------

    private fun createB_Static() {
        createBody(bStatic, 306f, 306f, 50f, 50f)
    }

    private fun createB_FlagsCircle() {
        createBody(bFlagsCircle, 0f, 0f, 661f, 661f)
    }

    // ---------------------------------------------------
    // Create Joint
    // ---------------------------------------------------

    private fun createJ_Revolute() {
        createJoint(jRevolute, RevoluteJointDef().apply {
            bodyA = bStatic.body
            bodyB = bFlagsCircle.body
        })
    }

}