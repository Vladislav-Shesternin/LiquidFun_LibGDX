package com.liquidfun.playground.game.screens.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.liquidfun.playground.game.actors.button.AButton
import com.liquidfun.playground.game.actors.button.AButtonText
import com.liquidfun.playground.game.box2d.bodiesGroup.BGBorders
import com.liquidfun.playground.game.box2d.destroyAll
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.METER_UI
import com.liquidfun.playground.game.utils.TIME_ANIM
import com.liquidfun.playground.game.utils.actor.animHide
import com.liquidfun.playground.game.utils.actor.animShow
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.advanced.AdvancedUserScreen
import com.liquidfun.playground.game.utils.font.FontParameter
import com.liquidfun.playground.game.utils.particle.ParticleColorRenderer
import finnstr.libgdx.liquidfun.ParticleSystem
import finnstr.libgdx.liquidfun.ParticleSystemDef

abstract class AbstractLevelScreen: AdvancedUserScreen() {

    abstract val particleSystemDefList: List<ParticleSystemDef>

    private val fontParameter = FontParameter()
    private val font_Bold_60  = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(60))
    private val font_Bold_40  = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(40))
    private val font_Bold_30  = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(30))

    // Particle
    protected val particleSystemList    by lazy { List(particleSystemDefList.size) { ParticleSystem(worldUtil.world, particleSystemDefList[it]) } }
    protected val particleColorRenderer = ParticleColorRenderer(0)

    // Actor
    private val aMenuBtn  by lazy { AButtonText(this, "Menu", AButton.Static.Type.DEFAULT, Label.LabelStyle(font_Bold_60, GameColor.brown)) }
    private val aApplyBtn by lazy { AButtonText(this, "Apply", AButton.Static.Type.DEFAULT, Label.LabelStyle(font_Bold_30, GameColor.brown)) }
    private val aEraseBtn by lazy { AButtonText(this, "Erase", AButton.Static.Type.DEFAULT, Label.LabelStyle(font_Bold_30, GameColor.brown)) }
    private val aFpsLbl       = Label("", Label.LabelStyle(font_Bold_40, GameColor.brown))
    private val aParticlesLbl = Label("", Label.LabelStyle(font_Bold_40, GameColor.brown))

    // Body Group
    private val bgBorders by lazy { BGBorders(this) }

    // Field
    protected var applyBlock: () -> Unit = { }
    protected var showBlock : () -> Unit = { }

    protected var isCreateBG_Borders = true

    protected var isErase = false
        private set

    override fun show() {
        stageUI.root.animHide()
        super.show()
        stageUI.root.animShow(TIME_ANIM) { showBlock() }
    }

    override fun render(delta: Float) {
        super.render(delta)

        aFpsLbl.setText("FPS: ${Gdx.graphics.framesPerSecond}")
        aParticlesLbl.setText("Particles: ${particleSystemList.sumOf { it.particleCount }}")
        renderParticle()
    }

    override fun dispose() {
        particleColorRenderer.dispose()
        listOf(bgBorders).destroyAll()

        super.dispose()
    }

    final override fun AdvancedStage.addActorsOnStageUI() {
        addMenuBtn()
        addFPSLbl()
        addParticlesLbl()

        if (isCreateBG_Borders) createBG_Borders()

        addActorsOnStage()
    }

    abstract fun AdvancedStage.addActorsOnStage()

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun AdvancedStage.addFPSLbl() {
        addActor(aFpsLbl)
        aFpsLbl.setBounds(1556f, 986f, 333f, 48f)
    }

    private fun AdvancedStage.addParticlesLbl() {
        addActor(aParticlesLbl)
        aParticlesLbl.setBounds(1556f, 918f, 333f, 48f)
    }

    private fun AdvancedStage.addMenuBtn() {
        addActor(aMenuBtn)
        aMenuBtn.apply {
            setBounds(1598f, 33f, 271f, 123f)
            setOnClickListener {
                disable()
                stageUI.root.animHide(TIME_ANIM) { game.navigationManager.back() }
            }
        }
    }

    protected fun AdvancedStage.addApplyBtn() {
        addActor(aApplyBtn)
        aApplyBtn.apply {
            setBounds(1549f, 182f, 156f, 71f)
            setOnClickListener { applyBlock() }
        }
    }

    protected fun AdvancedStage.addEraseBtn() {
        addActor(aEraseBtn)
        aEraseBtn.apply {
            setBounds(1549f, 182f, 156f, 71f)

            setOnClickListener {
                isErase = !isErase
                if (isErase) press() else unpress()
            }
        }
    }

    // ---------------------------------------------------
    // create Body Group
    // ---------------------------------------------------

    private fun createBG_Borders() {
        bgBorders.create(0f, 0f, 1529f, 1080f)
    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    private fun renderParticle() {
        particleColorRenderer.apply {
            maxParticleNumber = particleSystemList.sumOf { it.particleCount }
            particleSystemList.onEach { system -> render(system, METER_UI, viewportBox2d.camera.combined, stageUI.root.color.a) }
        }
    }

    // ---------------------------------------------------
    // Class
    // ---------------------------------------------------

    object Static {
        data class BlockData(
            val pos  : Vector2,
            val angle: Float,
        )
    }

}