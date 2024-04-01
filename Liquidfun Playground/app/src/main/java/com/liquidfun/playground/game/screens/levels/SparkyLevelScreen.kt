package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.AbstractBody
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.disposeAll
import com.liquidfun.playground.game.utils.runGDX
import com.liquidfun.playground.game.utils.toB2
import com.liquidfun.playground.game.utils.toUI
import com.liquidfun.playground.util.log
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType
import finnstr.libgdx.liquidfun.ParticleGroupDef
import finnstr.libgdx.liquidfun.ParticleSystemDef

class SparkyLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius  = 8f.toB2
            density = 0.7f
        }
    )

    // Body
    private val bBallList = List(6) { BBall(this) }

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.WL_BR)

    // Field
    private val particleColors   = listOf(GameColor.blue, GameColor.pink)
    private val particleSystem_0 = particleSystemList[0]
    private val circleShape      = CircleShape()
    private val particleGroupDefList = List(6) { ParticleGroupDef() }

    private val randomRadius get() = (25..50).random().toFloat().toB2

    override fun AdvancedStage.addActorsOnStage() {
        createB_BallList()

        createBG_FlagsCircle()
    }

    override fun dispose() {
        listOf(bgFlagsCircle).destroyAll()
        circleShape.dispose()
        super.dispose()
    }

    // ---------------------------------------------------
    // create Body
    // ---------------------------------------------------

    private fun createB_BallList() {
        val posList = listOf(
            Vector2(501f, 331f),
            Vector2(702f, 331f),
            Vector2(903f, 331f),

            Vector2(601f, 501f),
            Vector2(802f, 501f),

            Vector2(702f, 671f),

        )
        val size = Vector2(125f, 125f)

        bBallList.onEachIndexed { index, bBall ->
            bBall.apply {
                bodyDef.gravityScale = 0.15f

                fixtureDef.apply {
                    density     = 1f
                    restitution = 0.2f
                    friction    = 0.2f
                }

                create(posList[index], size)

                beginContactBlockArray.add(AbstractBody.ContactBlock { _, contact ->
                    createPG_Collide(particleGroupDefList[index], contact.worldManifold.points.first())
                })
            }

        }
    }

    // ---------------------------------------------------
    // Create Body Group
    // ---------------------------------------------------

    private fun createBG_FlagsCircle() {
        bgFlagsCircle.create(1589f, 207f, 661f, 661f)
    }

    // ---------------------------------------------------
    // Create Particle
    // ---------------------------------------------------

    private fun createPG_Collide(particleGroupDef: ParticleGroupDef, pos: Vector2) {
        runGDX {
            particleGroupDef.apply {
                shape = circleShape.apply { radius = randomRadius }
                color.set(particleColors.random())
                position.set(pos)
                lifetime = 1f

                flags.clear()
                flags.addAll(bgFlagsCircle.checkedParticleTypeArr,)

                particleSystem_0.createParticleGroup(this)
            }
        }
    }

//    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        val pos = stageUI.viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())).toB2
//
//        particleGroupDefList.first().apply {
//            shape = circleShape.apply { radius = 50f.toB2 }
//            color.set(particleColors.random())
//            position.set(pos)
//            lifetime = 1f
//
//            flags.addAll(
//                bgFlagsCircle.checkedParticleTypeArr,
//            )
//            groupFlags.addAll(
//                ParticleGroupDef.ParticleGroupType.b2_solidParticleGroup,
//            )
//
//            particleSystem_0.createParticleGroup(this)
//        }
//
//        return true
//    }

}