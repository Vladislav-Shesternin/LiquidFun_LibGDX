package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.AbstractBody
import com.liquidfun.playground.game.box2d.AbstractJoint
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.BBlock
import com.liquidfun.playground.game.box2d.bodies.BFinish
import com.liquidfun.playground.game.box2d.bodies.BFinishSide
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodies.standart.BStatic
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.DEGTORAD
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.runGDX
import com.liquidfun.playground.game.utils.toB2
import com.liquidfun.playground.util.log
import finnstr.libgdx.liquidfun.ParticleDef
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType
import finnstr.libgdx.liquidfun.ParticleSystemDef

class FaucetLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    companion object {
        const val PARTICLE_COUNT = 5_000
    }

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius = 5f.toB2
        }
    )

    // Actor
    private val aFaucetImg = Image(game.allAssets.faucet)

    // Body
    private val bBallList    = List(3) { BBall(this) }
    private val bFinishLeft  = BFinishSide(this)
    private val bFinishRight = BFinishSide(this)

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.WL_BR)

    // Field
    private var isCreateParticle = false
    private var particleTime     = 0f
    private val particleDef      = ParticleDef()
    private val particleColors   = listOf(GameColor.blue, GameColor.pink)
    private val tmpParticlePos   = Vector2()
    private val particleSystem_0 = particleSystemList[0]

    override fun AdvancedStage.addActorsOnStage() {
        addFaucetImg()

        createB_Ball()
        createB_Finish()

        createBG_FlagsCircle()

        isCreateParticle = true
    }

    override fun render(delta: Float) {
        super.render(delta)

        if (isCreateParticle && particleSystem_0.particleCount < PARTICLE_COUNT) {
            createP_Paticles(delta)
        }
    }

    override fun dispose() {
        listOf(bgFlagsCircle).destroyAll()
        super.dispose()
    }

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun AdvancedStage.addFaucetImg() {
        addActor(aFaucetImg)
        aFaucetImg.setBounds(754f, 841f, 172f, 189f)
    }

    // ---------------------------------------------------
    // create Body
    // ---------------------------------------------------

    private fun createB_Ball() {
        var nx = 183f
        bBallList.onEach {
            it.apply {
                fixtureDef.apply {
                    density     = 10f
                    restitution = 0.3f
                    friction    = 0.3f
                }

                create(nx, 478f, 125f, 125f)
            }
            nx += 394f + 125f
        }
    }

    private fun createB_Finish() {
        bFinishLeft.create(0f, 50f, 50f, 100f)
        bFinishRight.create(1479f, 50f, 50f, 100f)

        bFinishLeft.beginParticleContactBlockArray.add(AbstractBody.ParticleContactBlock { system, index ->
            runGDX { system.destroyParticle(index) }
        })
        bFinishRight.beginParticleContactBlockArray.add(AbstractBody.ParticleContactBlock { system, index ->
            runGDX { system.destroyParticle(index) }
        })
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

    private fun createP_Paticles(delta: Float) {
        particleTime += delta

        if (particleTime >= 0.050) {
            particleTime = 0f
            particleDef.apply {
                flags.clear()
                flags.add(ParticleType.b2_fixtureContactListenerParticle)
                flags.addAll(bgFlagsCircle.checkedParticleTypeArr)

                particleDef.color.set(particleColors.random())
            }
            var firstX = 760f
            repeat(3) {
                particleDef.position.set(tmpParticlePos.set(firstX, 834f).toB2)
                particleSystem_0.createParticle(particleDef)
                firstX += 12f
            }
        }

    }

}