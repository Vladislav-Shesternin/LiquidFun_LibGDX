package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Transform
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.actor.animHide
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.toB2
import finnstr.libgdx.liquidfun.ParticleGroup
import finnstr.libgdx.liquidfun.ParticleGroupDef
import finnstr.libgdx.liquidfun.ParticleSystemDef

class ParticleDrawingLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius = 10f.toB2
            strictContactCheck = true
            pressureStrength = 0.01f
            viscousStrength  = 0.5f
            springStrength   = 0.5f
            elasticStrength  = 0.5f
            powderStrength   = 0.1f
            ejectionStrength = 1f
        }
    )

    // Actor
    private val aDrawingArea = Actor()
    private val aUserImg     = Image(game.allAssets.user)

    // Body
    private val bBall = BBall(this)

    // BodyGroup
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.BR)

    // Field
    private val particleSystem_0 = particleSystemList[0]
    private val shapeCircle      = CircleShape().apply { radius = 50f.toB2 }
    private val particleColors   = listOf(GameColor.blue, GameColor.pink)
    private val particleGroupDef = ParticleGroupDef()
    private val tmpTransform     = Transform()

    private val bBallPos = Vector2(702f, 478f)

    override fun AdvancedStage.addActorsOnStage() {
        createBG_FlagsCircle()
        addEraseBtn()

        addUserImg()
        addActor(aDrawingArea)
        aDrawingArea.apply {
            setBounds(50f, 50f, 1429f, 980f)
            addListener(getDrawingListener())
        }

        createB_Ball()

        showBlock = {
            aUserImg.addAction(Actions.forever(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions.moveTo(919f, 652f, 1.25f, Interpolation.sine),
                Actions.fadeOut(0.5f),
                Actions.moveTo(474f, 652f)
            )))
        }
    }

    override fun dispose() {
        super.dispose()
        listOf(bgFlagsCircle).destroyAll()
        shapeCircle.dispose()
    }

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun AdvancedStage.addUserImg() {
        addActor(aUserImg)
        aUserImg.setBounds(474f, 652f, 136f, 168f)
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
    // Logic
    // ---------------------------------------------------

    private fun getDrawingListener() = object: InputListener() {
        private val tmpVector = Vector2()
        private var pColor    = particleColors.random()
        private var pg1: ParticleGroup? = null
        private var pg2: ParticleGroup? = null

        private var isFirstTouch = true

        override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            if (isFirstTouch) {
                isFirstTouch = false
                aUserImg.apply {
                    clearActions()
                    animHide(0.25f)
                }
            }

            val pos = tmpVector.set(x, y).add(50f, 50f).toB2

            pColor = particleColors.random()

            particleGroupDef.apply {
                position.set(pos)
                color.set(pColor)
                shape = shapeCircle

                flags.clear()
                flags.addAll(bgFlagsCircle.checkedParticleTypeArr)
            }
            tmpTransform.apply {
                setPosition(pos)
                setRotation(0f)
            }
            particleSystem_0.destroyParticleInShape(shapeCircle, tmpTransform)
            if (isErase.not()) pg1 = particleSystem_0.createParticleGroup(particleGroupDef)

            return true

        }

        override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            if (isErase.not()) if (x.toInt() !in 100..1350 || y.toInt() !in 50..950) return

            val pos = tmpVector.set(x, y).add(50f, 50f).toB2

            particleGroupDef.position.set(pos)

            tmpTransform.apply {
                setPosition(pos)
                setRotation(0f)
            }
            particleSystem_0.destroyParticleInShape(shapeCircle, tmpTransform)
            if (isErase.not()) {
                pg2 = particleSystem_0.createParticleGroup(particleGroupDef)
                particleSystem_0.joinParticleGroups(pg1, pg2)
            }

        }

    }

}