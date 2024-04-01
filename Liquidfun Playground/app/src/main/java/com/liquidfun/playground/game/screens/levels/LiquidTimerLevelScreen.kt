package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Vector2
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.BBlock
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.DEGTORAD
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.runGDX
import com.liquidfun.playground.game.utils.toB2
import com.liquidfun.playground.util.log
import finnstr.libgdx.liquidfun.ParticleGroup
import finnstr.libgdx.liquidfun.ParticleGroupDef
import finnstr.libgdx.liquidfun.ParticleSystemDef

class LiquidTimerLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius  = 5f.toB2
            density = 3f
            strictContactCheck = true
            powderStrength  = 1f
            elasticStrength = 1f
            springStrength  = 0.1f
            viscousStrength = 0.8f
        }
    )

    // Body
    private val bBlockList = List(12) { BBlock(this) }
    private val bBall      = BBall(this)

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.BR)

    // Field
    private val particleSystem_0 = particleSystemList[0]
    private val shapeRect        by lazy { worldUtil.bodyEditor.getShape("rect", 342f.toB2) }
    private val particleGroupDef = ParticleGroupDef()
    private var particleGroup1: ParticleGroup? = null
    private var particleGroup2: ParticleGroup? = null
    private var particleGroup3: ParticleGroup? = null
    private var particleGroup4: ParticleGroup? = null

    private val bBallPos = Vector2(1116f+62.5f, 329f+62.5f).toB2
    private val pPos1  = Vector2(80f, 680f).toB2
    private val pPos2  = Vector2(422f, 680f).toB2
    private val pPos3  = Vector2(764f, 680f).toB2
    private val pPos4  = Vector2(1106f, 680f).toB2

    override fun AdvancedStage.addActorsOnStage() {
        createBG_FlagsCircle()
        addApplyBtn()

        createB_BlockList()
        createB_Ball()

        showBlock  = { createPG_Joined() }
        applyBlock = { restart() }
    }

    override fun dispose() {
        listOf(bgFlagsCircle).destroyAll()
        shapeRect.dispose()
        super.dispose()
    }

    // ---------------------------------------------------
    // create Body
    // ---------------------------------------------------

    private fun createB_BlockList() {
        val dataList = listOf(
            Static.BlockData(Vector2(0f, 623f), 0f),
            Static.BlockData(Vector2(268f, 623f), 0f),
            Static.BlockData(Vector2(513f, 623f), 0f),
            Static.BlockData(Vector2(758f, 623f), 0f),
            Static.BlockData(Vector2(1003f, 623f), 0f),
            Static.BlockData(Vector2(1248f, 623f), 0f),

            Static.BlockData(Vector2(152f, 498f), -5f),
            Static.BlockData(Vector2(401f, 476f), -5f),
            Static.BlockData(Vector2(650f, 454f), -5f),

            Static.BlockData(Vector2(375f, 50f), 90f),
            Static.BlockData(Vector2(789f, 50f), 90f),
            Static.BlockData(Vector2(1203f, 50f), 90f),
        )
        val size = Vector2(250f, 50f)

        bBlockList.onEachIndexed { index, bBlock ->
            bBlock.apply {
                bodyDef.angle = dataList[index].angle * DEGTORAD
                fixtureDef.apply {
                    restitution = 0.2f
                    friction    = 0.2f
                }

                create(dataList[index].pos, size)
            }

        }
    }

    private fun createB_Ball() {
        bBall.apply {
            fixtureDef.apply {
                density = 1f
                restitution = 0.2f
                friction    = 0.2f
            }

            create(1116f, 329f, 125f, 125f)
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
        particleGroup1?.destroyParticlesInGroup()

        // 1
        particleGroupDef.apply {
            shape = shapeRect
            color.set(GameColor.pink)
            position.set(pPos1)

            flags.clear()
            flags.addAll(bgFlagsCircle.checkedParticleTypeArr)
        }
        particleGroup1 = particleSystem_0.createParticleGroup(particleGroupDef)

        // 2
        particleGroupDef.apply {
            color.set(GameColor.blue)
            position.set(pPos2)
        }
        particleGroup2 = particleSystem_0.createParticleGroup(particleGroupDef)

        // 3
        particleGroupDef.apply {
            color.set(GameColor.pink)
            position.set(pPos3)
        }
        particleGroup3 = particleSystem_0.createParticleGroup(particleGroupDef)

        // 4
        particleGroupDef.apply {
            color.set(GameColor.blue)
            position.set(pPos4)
        }
        particleGroup4 = particleSystem_0.createParticleGroup(particleGroupDef)


        particleSystem_0.joinParticleGroups(particleGroup1, particleGroup2)
        particleSystem_0.joinParticleGroups(particleGroup1, particleGroup3)
        particleSystem_0.joinParticleGroups(particleGroup1, particleGroup4)
    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    private fun restart() {
        runGDX {
            bBall.body?.apply {
                setLinearVelocity(0f, 0f)
                isAwake = false
                setTransform(bBallPos, 0f)
                applyForceToCenter(0f, 1f, true)
            }
            createPG_Joined()
        }
    }

}