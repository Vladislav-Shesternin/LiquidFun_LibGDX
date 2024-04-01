package com.liquidfun.playground.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.actors.ALevelFrame
import com.liquidfun.playground.game.actors.button.AButton
import com.liquidfun.playground.game.actors.button.AButtonText
import com.liquidfun.playground.game.actors.scroll.AHorizontalGroup
import com.liquidfun.playground.game.screens.levels.DamBreakLevelScreen
import com.liquidfun.playground.game.screens.levels.FaucetLevelScreen
import com.liquidfun.playground.game.screens.levels.LiquidTimerLevelScreen
import com.liquidfun.playground.game.screens.levels.MultipleSystemLevelScreen
import com.liquidfun.playground.game.screens.levels.ParticleDrawingLevelScreen
import com.liquidfun.playground.game.screens.levels.SandboxLevelScreen
import com.liquidfun.playground.game.screens.levels.SparkyLevelScreen
import com.liquidfun.playground.game.screens.levels.WaveMachineLevelScreen
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.METER_UI
import com.liquidfun.playground.game.utils.TIME_ANIM
import com.liquidfun.playground.game.utils.actor.animHide
import com.liquidfun.playground.game.utils.actor.animShow
import com.liquidfun.playground.game.utils.actor.setOnClickListener
import com.liquidfun.playground.game.utils.actor.setOnTouchListener
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.font.FontParameter
import com.liquidfun.playground.game.utils.region

class MenuScreen(override val game: LibGDXGame): AdvancedScreen() {

    private val fontParameter    = FontParameter()
    private val font_SemiBold_50 = fontGenerator_Inter_SemiBold.generateFont(fontParameter.setCharacters(FontParameter.CharType.LATIN).setSize(50))
    private val font_Bold_60     = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(60))

    private val horizontalGroup = AHorizontalGroup(this, 50f)
    private val scrollPane      = ScrollPane(horizontalGroup)
    private val fpsLbl          = Label("", LabelStyle(font_Bold_60, GameColor.brown))
    private val exitBtn         = AButtonText(this, "Exit", AButton.Static.Type.DEFAULT, LabelStyle(font_Bold_60, GameColor.brown))

    override fun show() {
        stageUI.root.animHide()
        super.show()
        stageUI.root.animShow(TIME_ANIM)
    }

    override fun render(delta: Float) {
        super.render(delta)
        fpsLbl.setText("FPS: ${Gdx.graphics.framesPerSecond}")
    }

    override fun dispose() {
        horizontalGroup.dispose()
        super.dispose()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        horizontalGroup.addHorizontalGroup()
        addScrollPane()
        addFPSLbl()
        addExitBtn()
    }

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun AdvancedGroup.addHorizontalGroup() {
        val titles = listOf(
            "Sandbox", "Particle Drawing", "DamBreak", "Liquid Timer",
            "Wave Machine", "Faucet", "Sparky", "Multiple System",
        )
        val textures = listOf(
            game.allAssets.Sanbox,
            game.allAssets.ParticleDrawing,
            game.allAssets.DamBreak,
            game.allAssets.LiquidTimer,
            game.allAssets.WaveMachine,
            game.allAssets.Faucet,
            game.allAssets.Sparky,
            game.allAssets.MultiplySystem,
        )
        val labelStyle = LabelStyle(font_SemiBold_50, GameColor.brown)

        val levelScreenNameList = listOf(
            SandboxLevelScreen::class.java.name,
            ParticleDrawingLevelScreen::class.java.name,
            DamBreakLevelScreen::class.java.name,
            LiquidTimerLevelScreen::class.java.name,
            WaveMachineLevelScreen::class.java.name,
            FaucetLevelScreen::class.java.name,
            SparkyLevelScreen::class.java.name,
            MultipleSystemLevelScreen::class.java.name,
        )

        repeat(8) { index ->
            addActor(
                ALevelFrame(this@MenuScreen, titles[index], textures[index], labelStyle,
                    isTopBlue = (index % 2) == 0,
                    delay     = if((index % 2) == 0) ALevelFrame.TIME_BLUR_SCALE else 0f
                ).apply {
                    setSize(626f, 687f)
                    setOnTouchListener(game.soundUtil) {
                        stageUI.root.animHide(TIME_ANIM) { game.navigationManager.navigate(levelScreenNameList[index], MenuScreen::class.java.name) }
                    }
                }
            )
        }

    }

    private fun AdvancedStage.addScrollPane() {
        addActor(scrollPane)
        scrollPane.setBounds(0f, 227f, 1920f, 687f)
    }

    private fun AdvancedStage.addFPSLbl() {
        addActor(fpsLbl)
        fpsLbl.setBounds(1598f, 957f, 254f, 93f)
    }

    private fun AdvancedStage.addExitBtn() {
        addActor(exitBtn)
        exitBtn.apply {
            setBounds(1598f, 33f, 271f, 123f)
            setOnClickListener { stageUI.root.animHide(TIME_ANIM) { game.navigationManager.exit() } }
        }
    }

}