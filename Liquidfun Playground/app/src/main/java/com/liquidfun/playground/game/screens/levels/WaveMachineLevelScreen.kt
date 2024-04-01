package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.box2d.bodies.BBall
import com.liquidfun.playground.game.box2d.bodies.flags_circle.BFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGFlagsCircle
import com.liquidfun.playground.game.box2d.bodiesGroup.BGWaveMachine
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.font.FontParameter
import com.liquidfun.playground.game.utils.runGDX
import com.liquidfun.playground.game.utils.toB2
import finnstr.libgdx.liquidfun.ParticleGroup
import finnstr.libgdx.liquidfun.ParticleGroupDef
import finnstr.libgdx.liquidfun.ParticleSystemDef

class WaveMachineLevelScreen(override val game: LibGDXGame): AbstractLevelScreen() {

    private val fontParameter = FontParameter()
    private val font_Bold_40  = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(40))

    override val particleSystemDefList = listOf(
        // ParticleSystem [0]
        ParticleSystemDef().apply {
            radius  = 10f.toB2
        }
    )

    // Actor
    private val aBagLbl = Label("BAG: Leaking", Label.LabelStyle(font_Bold_40, GameColor.brown))

    // Body
    private val bBall = BBall(this)

    // BodyGroup
    private val bgWaveMachine = BGWaveMachine(this)
    private val bgFlagsCircle = BGFlagsCircle(this, BFlagsCircle.FlagsCircle.BR)

    // Field
    private val particleSystem_0 = particleSystemList[0]
    private val shapeRect        by lazy { worldUtil.bodyEditor.getShape("rect", 400f.toB2) }
    private val particleGroupDef = ParticleGroupDef()
    private var particleGroup1: ParticleGroup? = null
    private var particleGroup2: ParticleGroup? = null

    private val bBallPos = Vector2(720f+44f, 757f+44f).toB2
    private val pPos1    = Vector2(365f, 333f).toB2
    private val pPos2    = Vector2(765f, 333f).toB2

    override fun show() {
        isCreateBG_Borders = false
        super.show()
    }

    override fun AdvancedStage.addActorsOnStage() {
        createBG_FlagsCircle()
        addApplyBtn()

        createBG_WaveMachine()
        createB_Ball()

        addActor(aBagLbl)
        aBagLbl.setBounds(26f, 982f, 261f, 48f)

        showBlock  = {
            createPG_Joined()
            bgWaveMachine.launchWaveMachine()
        }
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

    private fun createB_Ball() {
        bBall.apply {
            fixtureDef.apply {
                density = 1f
                restitution = 0.2f
                friction    = 0.2f
            }

            create(720f, 757f, 88f, 88f)
        }
    }

    // ---------------------------------------------------
    // Create Body Group
    // ---------------------------------------------------

    private fun createBG_WaveMachine() {
        bgWaveMachine.create(189f, 159f, 1150f, 750f)
    }

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
            color.set(GameColor.blue)
            position.set(pPos1)

            flags.clear()
            flags.addAll(bgFlagsCircle.checkedParticleTypeArr)
        }
        particleGroup1 = particleSystem_0.createParticleGroup(particleGroupDef)

        // 2
        particleGroupDef.apply {
            color.set(GameColor.pink)
            position.set(pPos2)
        }
        particleGroup2 = particleSystem_0.createParticleGroup(particleGroupDef)

        particleSystem_0.joinParticleGroups(particleGroup1, particleGroup2)
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