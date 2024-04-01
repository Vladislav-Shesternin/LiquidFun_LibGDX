package com.liquidfun.playground.game.box2d.bodiesGroup

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.liquidfun.playground.game.box2d.AbstractBody
import com.liquidfun.playground.game.box2d.AbstractBodyGroup
import com.liquidfun.playground.game.box2d.AbstractJoint
import com.liquidfun.playground.game.box2d.bodies.BBlock
import com.liquidfun.playground.game.box2d.bodies.BHorizontal
import com.liquidfun.playground.game.box2d.bodies.BVertical
import com.liquidfun.playground.game.box2d.bodies.BW
import com.liquidfun.playground.game.box2d.bodies.standart.BStatic
import com.liquidfun.playground.game.screens.levels.AbstractLevelScreen
import com.liquidfun.playground.game.utils.DEGTORAD
import com.liquidfun.playground.game.utils.RADTODEG
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen

class BGWaveMachine(override val screenBox2d: AdvancedBox2dScreen) : AbstractBodyGroup() {

    override val standartW = 1200f

    // Body
    private val bStatic     = BStatic(screenBox2d).apply {
        fixtureDef.isSensor = true
    }
    private val bWaveMachine = BW(screenBox2d)

    // Joint
    private val jRevolute = AbstractJoint<RevoluteJoint, RevoluteJointDef>(screenBox2d)

    override fun create(x: Float, y: Float, w: Float, h: Float) {
        super.create(x, y, w, h)

        createB_Static()
        createB_WaveMachine()

        createJ_Revolute()
    }

    // ---------------------------------------------------
    // Create Body
    // ---------------------------------------------------

    private fun createB_Static() {
        createBody(bStatic, 570f, 327f, 60f, 60f)
    }

    private fun createB_WaveMachine() {
        createBody(bWaveMachine, 0f, 0f, 1200f, 715f)
    }

    // ---------------------------------------------------
    // Create Joint
    // ---------------------------------------------------

    private fun createJ_Revolute() {
        createJoint(jRevolute, RevoluteJointDef().apply {
            bodyA = bStatic.body
            bodyB = bWaveMachine.body

            localAnchorB.set(Vector2(600f, 357f).subCenter(bodyB))

            lowerAngle  = -25f * DEGTORAD
            upperAngle  = 25f * DEGTORAD
            enableLimit = true
        })

    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    fun launchWaveMachine() {
        var timer    = 0f

        bStatic.renderBlockArray.add(AbstractBody.RenderBlock {
            timer    += it
            if (timer >= 0.050) {
                timer = 0f

                jRevolute.joint?.apply {
                    if ((jointAngle * RADTODEG) >= 25f) {
                        motorSpeed = -25f * DEGTORAD
                    }
                    if ((jointAngle * RADTODEG) <= -25f) {
                        motorSpeed = 25f * DEGTORAD
                    }
                }
            }
        })

        jRevolute.joint?.apply {
            motorSpeed     = 25f * DEGTORAD
            maxMotorTorque = 1000f
            enableMotor(true)
        }
    }


}