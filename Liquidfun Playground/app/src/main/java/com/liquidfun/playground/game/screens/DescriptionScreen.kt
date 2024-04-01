package com.liquidfun.playground.game.screens

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.actors.TmpGroup
import com.liquidfun.playground.game.actors.button.AButton
import com.liquidfun.playground.game.actors.button.AButtonText
import com.liquidfun.playground.game.actors.checkbox.ACheckBox
import com.liquidfun.playground.game.actors.checkbox.ACheckBoxGroup
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.TIME_ANIM
import com.liquidfun.playground.game.utils.actor.animHide
import com.liquidfun.playground.game.utils.actor.animShow
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.game.utils.font.FontParameter

class DescriptionScreen(override val game: LibGDXGame): AdvancedScreen() {

    private val textEN = "This game was created to demonstrate the capabilities of the LiquidFun extension for LibGDX.\n" +
            "This library has many bugs and is already outdated.\n" +
            "\n" +
            "I hope this game will be opened by developers who will update LiquidFun with an extension for LibGDX."
    private val textUA = "Цю гру створено для демонстрації можливостей LiquidFun розширення для LibGDX. \n" +
            "Ця бібліотека має багато багів і вона вже застаріла.\n" +
            "\n" +
            "Cподіваюсь цю гру відкриють Розробники які займуться оновленням LiquidFun розширенням для LibGDX.  "

    private val fontParameter    = FontParameter()
    private val font_SemiBold_50 = fontGenerator_Inter_SemiBold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(50))
    private val font_Bold_60     = fontGenerator_Inter_Bold.generateFont(fontParameter.setCharacters(FontParameter.CharType.ALL).setSize(60))

    private val tmpGroup         = TmpGroup(this).apply { color.a = 0f }
    private val logoLibgdxImg    = Image(game.loaderAssets.LOGO_LIBGDX)
    private val logoLiquidfunImg = Image(game.loaderAssets.LOGO_LIQUIDFUN)
    private val descriptionLbl   = Label(textEN, LabelStyle(font_SemiBold_50, GameColor.brown))
    private val playBtn          = AButtonText(this, "Play", AButton.Static.Type.DEFAULT, LabelStyle(font_Bold_60, GameColor.brown))
    private val languagesImg     = Image(game.allAssets.languages)

    override fun AdvancedStage.addActorsOnStageUI() {
        addLogos()
        addAndFillActor(tmpGroup)

        tmpGroup.apply {

            addDescriptionLbl()
            addPlayBtn()
            addLanguages()

        }.animShow(TIME_ANIM)
    }

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun AdvancedStage.addLogos() {
        addActors(logoLibgdxImg, logoLiquidfunImg)
        logoLibgdxImg.setBounds(0f, 910f, 520f, 170f)
        logoLiquidfunImg.setBounds(1400f, 0f, 520f, 520f)
    }

    private fun AdvancedGroup.addDescriptionLbl() {
        addActor(descriptionLbl)
        descriptionLbl.apply {
            wrap = true
            setBounds(35f, 357f, 1401f, 366f)
            setAlignment(Align.center)
        }
    }

    private fun AdvancedGroup.addPlayBtn() {
        addActor(playBtn)
        playBtn.apply {
            setBounds(600f, 100f, 271f, 123f)
            setOnClickListener { stageUI.root.animHide(TIME_ANIM) {
                game.navigationManager.navigate(MenuScreen::class.java.name)
            } }
        }
    }

    private fun AdvancedGroup.addLanguages() {
        addActor(languagesImg)
        languagesImg.setBounds(1532f, 811f, 288f, 198f)

        val cbEN = ACheckBox(this@DescriptionScreen, ACheckBox.Static.Type.DEFAULT)
        val cbUA = ACheckBox(this@DescriptionScreen, ACheckBox.Static.Type.DEFAULT)
        addActors(cbEN, cbUA)

        val cbg = ACheckBoxGroup()

        cbEN.apply {
            setBounds(1540f, 949f, 44f, 44f)
            checkBoxGroup = cbg
            setOnCheckListener { if (it) descriptionLbl.setText(textEN) }
            check(false)
        }
        cbUA.apply {
            setBounds(1540f, 831f, 44f, 44f)
            checkBoxGroup = cbg
            setOnCheckListener { if (it) descriptionLbl.setText(textUA) }
        }
    }

}