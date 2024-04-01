package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Vector2
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.runGDX
import com.liquidfun.playground.game.utils.toB2
import com.liquidfun.playground.util.log
import finnstr.libgdx.liquidfun.ParticleGroup
import finnstr.libgdx.liquidfun.ParticleGroupDef
import finnstr.libgdx.liquidfun.ParticleSystemDef

class DamBreakLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius = 5f.toB2
            gravityScale = 0.3f
        }
    )

    // Body
    private val bBall = BBall(this)

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.BR)

    // Field
    private val particleSystem_0 = particleSystemList[0]
    private val shapeRect        by lazy { worldUtil.bodyEditor.getShape("rect", 400f.toB2) }
    private val particleGroupDef = ParticleGroupDef()
    private var particleGroupBot: ParticleGroup? = null
    private var particleGroupTop: ParticleGroup? = null

    private val bBallPos = Vector2(1318f, 869f)
    private val pBotPos  = Vector2(50f, 50f).toB2
    private val pTopPos  = Vector2(50f, 450f).toB2

    override fun AdvancedStage.addActorsOnStage() {
        createBG_FlagsCircle()
        addApplyBtn()
        createB_Ball()

        showBlock  = { createPG_Joined() }
        applyBlock = { createPG_Joined() }
    }

    override fun dispose() {
        super.dispose()
        listOf(bgFlagsCircle).destroyAll()
        shapeRect.dispose()
    }

    // ---------------------------------------------------
    // create Body
    // ---------------------------------------------------

    private fun createB_Ball() {
        bBall.apply {
            fixtureDef.apply {
                density = 1f
                restitution = 0.2f
                friction = 0.2f
            }

            create(bBallPos, Vector2(125f, 125f))
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

    private fun createPG_Joined() {
        particleGroupBot?.destroyParticlesInGroup()

        // Bot
        particleGroupDef.apply {
            shape = shapeRect
            color.set(GameColor.pink)
            position.set(pBotPos)

            flags.clear()
            flags.addAll(bgFlagsCircle.checkedParticleTypeArr)
        }
        particleGroupBot = particleSystem_0.createParticleGroup(particleGroupDef)

        // Top
        particleGroupDef.apply {
            color.set(GameColor.blue)
            position.set(pTopPos)
        }
        particleGroupTop = particleSystem_0.createParticleGroup(particleGroupDef)

        particleSystem_0.joinParticleGroups(particleGroupBot, particleGroupTop)
    }

}