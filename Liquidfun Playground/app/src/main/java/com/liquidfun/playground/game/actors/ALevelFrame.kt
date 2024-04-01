package com.liquidfun.playground.game.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.liquidfun.playground.game.actors.label.ASpinningLabel
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import com.liquidfun.playground.util.log

class ALevelFrame constructor(
    override val screen: AdvancedScreen,
    title       : String,
    textureLevel: Texture,
    style       : LabelStyle,
    isTopBlue   : Boolean,
    private val delay: Float
): AdvancedGroup() {

    companion object {
        private const val BLUR_SCALE_MIN  = 0.3f
        private const val BLUR_SCALE_MAX  = 1.0f
        const val TIME_BLUR_SCALE = 1f
    }

    private val titleLbl = ASpinningLabel(screen, title, style)
    private val frameImg = Image(screen.game.allAssets.frame_level)
    private val levelImg = Image(textureLevel)
    private val topBlurImg = Image(if (isTopBlue) screen.game.allAssets.blur_blue_top else screen.game.allAssets.blur_pink_top)
    private val botBlurImg = Image(if (isTopBlue) screen.game.allAssets.blur_pink_bot else screen.game.allAssets.blur_blue_bot)

    override fun addActorsOnGroup() {
        addActors(titleLbl, frameImg, topBlurImg, botBlurImg, levelImg)
        titleLbl.setBounds(0f, 626f, 626f, 61f)
        frameImg.setBounds(0f, 0f, 626f, 626f)
        levelImg.setBounds(4f, 140f, 618f, 346f)
        topBlurImg.setBounds(4f, 486f, 618f, 136f)
        botBlurImg.setBounds(4f, 4f, 618f, 136f)

        animBlurImg()
    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

    private fun animBlurImg() {
        topBlurImg.setOrigin(309f, 136f)
        botBlurImg.setOrigin(309f, 0f)

        addAction(Actions.sequence(
            Actions.delay(delay),
            Actions.run {
                topBlurImg.addAction(Actions.forever(Actions.sequence(
                    Actions.scaleTo(BLUR_SCALE_MIN, BLUR_SCALE_MIN, TIME_BLUR_SCALE, Interpolation.sineOut),
                    Actions.scaleTo(BLUR_SCALE_MAX, BLUR_SCALE_MAX, TIME_BLUR_SCALE, Interpolation.pow2),
                )))
                botBlurImg.addAction(Actions.forever(Actions.sequence(
                    Actions.scaleTo(BLUR_SCALE_MAX, BLUR_SCALE_MAX, TIME_BLUR_SCALE, Interpolation.pow2),
                    Actions.scaleTo(BLUR_SCALE_MIN, BLUR_SCALE_MIN, TIME_BLUR_SCALE, Interpolation.sineOut),
                )))
            }
        ))
    }

}