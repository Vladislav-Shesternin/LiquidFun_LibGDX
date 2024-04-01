package com.liquidfun.playground.game.box2d.bodiesGroup

import com.liquidfun.playground.game.box2d.AbstractBodyGroup
import com.liquidfun.playground.game.box2d.BodyId
import com.liquidfun.playground.game.box2d.bodies.BHorizontal
import com.liquidfun.playground.game.box2d.bodies.BVertical
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen

class BGBorders(override val screenBox2d: AdvancedBox2dScreen) : AbstractBodyGroup() {

    override val standartW = 1529f

    val bTop   = BHorizontal(screenBox2d)
    val bDown  = BHorizontal(screenBox2d)
    val bLeft  = BVertical(screenBox2d)
    val bRight = BVertical(screenBox2d)

    override fun create(x: Float, y: Float, w: Float, h: Float) {
        super.create(x, y, w, h)

        initB_Borders()

        createHorizontal()
        createVertical()
    }


    // ---------------------------------------------------
    // Init
    // ---------------------------------------------------

    private fun initB_Borders() {
        arrayOf(bTop, bDown, bLeft, bRight).onEach { it.apply {
            id = BodyId.BORDERS
            collisionList.addAll(arrayOf(
                BodyId.DYNAMIC,
            ))

            fixtureDef.apply {
                density     = 1f
                restitution = 0.2f
                friction    = 0.2f
            }
        } }
    }

    // ---------------------------------------------------
    // Create Body
    // ---------------------------------------------------

    private fun createHorizontal() {
        createBody(bTop, 0f, 1030f, 1529f, 50f)
        createBody(bDown, 0f, 0f, 1529f, 50f)
    }

    private fun createVertical() {
        createBody(bLeft, 0f, 0f, 50f, 1080f)
        createBody(bRight, 1479f, 0f, 50f, 1080f)
    }

}