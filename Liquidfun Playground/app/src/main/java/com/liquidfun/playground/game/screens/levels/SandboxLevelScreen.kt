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

class SandboxLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    companion object {
        const val PARTICLE_COUNT = 5_000
    }

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius = 10f.toB2
        }
    )

    // Actor
    private val aTeleportList = List(3) { Image(game.allAssets.black_circle) }

    // Body
    private val bBlockList = List(11) { BBlock(this) }
    private val bBall      = BBall(this)
    private val bFinish    = BFinish(this)
    private val bBlockRevolute = BBlock(this)

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.SP_EL_WL_BR)

    // Joint
    private val jRevolute = AbstractJoint<RevoluteJoint, RevoluteJointDef>(this)

    // Field
    private var isCreateParticle = false
    private var particleTime     = 0f
    private val particleDef      = ParticleDef()
    private val particleColors   = listOf(GameColor.blue, GameColor.pink)
    private val tmpParticlePos   = Vector2()
    private val particleSystem_0 = particleSystemList[0]

    override fun AdvancedStage.addActorsOnStage() {
        addTeleportList()
        createB_BlockList()
        createB_Ball()
        createB_Finish()
        createB_BlockRevolute()

        createJ_Revolute()

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

    private fun AdvancedStage.addTeleportList() {
        var nx = 185f
        aTeleportList.onEach { img ->
            addActor(img)
            img.setBounds(nx,946f,60f,60f)
            nx += 489 + 60
        }
    }

    // ---------------------------------------------------
    // create Body
    // ---------------------------------------------------

    private fun createB_BlockList() {
        val dataList = listOf(
            Static.BlockData(Vector2(514f, 830f), 90f),
            Static.BlockData(Vector2(1064f, 830f), 90f),
            Static.BlockData(Vector2(15f, 755f), -45f),
            Static.BlockData(Vector2(192f, 578f), 0f),
            Static.BlockData(Vector2(1087f, 578f), 0f),
            Static.BlockData(Vector2(1337f, 578f), 45f),
            Static.BlockData(Vector2(404f, 330f), -20f),
            Static.BlockData(Vector2(639f, 245f), 0f),
            Static.BlockData(Vector2(889f, 245f), 20f),

            Static.BlockData(Vector2(484f, -45f), 37f),
            Static.BlockData(Vector2(846f, 106f), -37f),
        )
        val size = Vector2(250f, 50f)

        bBlockList.onEachIndexed { index, bBlock ->
            bBlock.apply {
                bodyDef.angle = dataList[index].angle * DEGTORAD
                fixtureDef.apply {
                    restitution = 0.2f
                    friction = 0.2f
                }

                create(dataList[index].pos, size)
            }

        }
    }

    private fun createB_Ball() {
        bBall.apply {
            fixtureDef.apply {
                density     = 1f
                restitution = 0.3f
                friction    = 0.3f
            }

            create(702f, 355f, 125f, 125f)
        }
    }

    private fun createB_Finish() {
        bFinish.create(639f, 0f, 250f, 50f)

        bFinish.beginParticleContactBlockArray.add(AbstractBody.ParticleContactBlock { system, index ->
            runGDX { system.destroyParticle(index) }
        })
    }

    private fun createB_BlockRevolute() {
        bBlockRevolute.apply {
            bodyDef.apply {
                type  = BodyDef.BodyType.DynamicBody
                angle = 90f * DEGTORAD
            }
            fixtureDef.apply {
                density     = 1f
                restitution = 0.5f
                friction    = 0.4f
            }
            create(789f, 540f, 250f, 50f)
        }
    }

    // ---------------------------------------------------
    // Create Body Group
    // ---------------------------------------------------

    private fun createBG_FlagsCircle() {
        bgFlagsCircle.create(1589f, 207f, 661f, 661f)
    }

    // ---------------------------------------------------
    // Create Joint
    // ---------------------------------------------------

    private fun createJ_Revolute() {
        val bStatic = BStatic(this)
        bStatic.create(758f, 659f, 13f, 13f)

        jRevolute.create(RevoluteJointDef().apply {
            bodyA = bStatic.body
            bodyB = bBlockRevolute.body

            localAnchorB.set(Vector2(125f, 25f).toB2)

            enableMotor    = true
            motorSpeed     = 450f * DEGTORAD
            maxMotorTorque = 100f
        })
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
            // 0
            var firstX_0 = 185f
            repeat(4) {
                particleDef.position.set(tmpParticlePos.set(firstX_0, 971f).toB2)
                particleSystem_0.createParticle(particleDef)
                firstX_0 += 17f
            }
            // 1
            var firstX_1 = 734f
            repeat(4) {
                particleDef.position.set(tmpParticlePos.set(firstX_1, 971f).toB2)
                particleSystem_0.createParticle(particleDef)
                firstX_1 += 17f
            }
            // 2
            var firstX_2 = 1283f
            repeat(4) {
                particleDef.position.set(tmpParticlePos.set(firstX_2, 971f).toB2)
                particleSystem_0.createParticle(particleDef)
                firstX_2 += 17f
            }
        }

    }


//    private fun createPG_First() {
//        val pgDef = ParticleGroupDef().apply {
//            this.shape = worldUtil.bodyEditor.getShape("rect", 688f.toB2)
//            position.set(Vector2(1039f, 196f).toB2)
//            color.set(Color.RED)
//
//            flags.addAll(
//                ParticleType.b2_viscousParticle,
//            )
//            groupFlags.addAll(
//                ParticleGroupType.b2_solidParticleGroup,
//            )
//        }
//
//       // val pg = pSystem.createParticleGroup(pgDef)
//
//    }
//
//    val destroyShape by lazy { worldUtil.bodyEditor.getShape("circle", 88f.toB2) }
//    val pgDefCircle  by lazy { ParticleGroupDef().apply {
//        shape = destroyShape
//        color.set(Color.RED)
//
//        flags.addAll(
//            ParticleType.b2_elasticParticle,
//        )
//        groupFlags.addAll(
//            ParticleGroupType.b2_solidParticleGroup,
//        )
//    } }
//    var pgDestroy: ParticleGroup? = null
//
//    private var isFirst = true
//    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
////        if (isFirst) {
////            val pos = stageUI.viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())).toB2
////
////            pgDefCircle.position.set(pos)
////            pgDestroy?.destroyParticlesInGroup()
////            pgDestroy = pSystem.createParticleGroup(pgDefCircle)
////            isFirst = false
////        }
//        return true
//    }
//
//    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
//        val pos = stageUI.viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())).toB2
//
//        pgDestroy?.destroyParticlesInGroup()
//        pSystem.destroyParticleInShape(destroyShape, Transform(pos, 0f))
//        pgDefCircle.position.set(pos)
//        pgDestroy = pSystem.createParticleGroup(pgDefCircle)
//
//        return true
//    }
//
//    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
//        val pos = stageUI.viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())).toB2
//
//        pgDestroy?.let { pg ->
//            val direction = pos.sub(pg.position)
//
//            pg.applyLinearImpulse(direction.nor().scl(-400f))
//        }
//        return true
//    }

}