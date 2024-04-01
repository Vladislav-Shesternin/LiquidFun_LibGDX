package com.liquidfun.playground.game.actors.button

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.liquidfun.playground.game.utils.actor.disable
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen

class AButtonText(
    override val screen: AdvancedScreen,
    text: String, type: Static.Type? = null, labelStyle: Label.LabelStyle, val alignment: Int = Align.center
): AButton(screen, type) {

    val label = Label(text, labelStyle)
    override fun addActorsOnGroup() {
        super.addActorsOnGroup()
        addLabel()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun addLabel() {
        addAndFillActor(label)
        label.also {
            it.disable()
            it.setAlignment(alignment)
            it.wrap = true
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    fun setText(text: String) {
        label.setText(text)
    }

}