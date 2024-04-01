package com.liquidfun.playground.game.actors.flags_circle

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.liquidfun.playground.game.actors.checkbox.ACheckBox
import com.liquidfun.playground.game.actors.checkbox.ACheckBoxTouch
import com.liquidfun.playground.game.utils.actor.setBounds
import com.liquidfun.playground.game.utils.actor.setOnTouchListener
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import finnstr.libgdx.liquidfun.ParticleDef.ParticleType

class AFlagsCircle_Disable_SP_EL_WL_BR(override val screen: AdvancedScreen): AbstractFlagsCircle() {

    private val blackCircleCount = 4

    // Actor
    override val aCBoxList = List(12-blackCircleCount) { ACheckBoxTouch(screen, ACheckBox.Static.Type.DEFAULT) }
    override val aAreaList = List(12-blackCircleCount) { Actor() }
    private val blackList  = List(blackCircleCount) { Image(screen.game.allAssets.black_circle) }

    // Field
    override val angleList = listOf(
        0f, -30f, -60f, /*-90f, -120f,*/ -150f,
        -180f, -210f, -240f, -270f/*, -300f, -330f,*/
    )


    override val particleTypeList   = listOf(
        ParticleType.b2_waterParticle,
        ParticleType.b2_viscousParticle,
        ParticleType.b2_tensileParticle,
        //ParticleType.b2_springParticle,
        //ParticleType.b2_elasticParticle,
        ParticleType.b2_colorMixingParticle,
        ParticleType.b2_powderParticle,
        ParticleType.b2_staticPressureParticle,
        ParticleType.b2_repulsiveParticle,
        ParticleType.b2_reactiveParticle,
        //ParticleType.b2_wallParticle,
        //ParticleType.b2_barrierParticle,
    )
    override val tmpParticleTypeArr = Array<ParticleType>()

    override fun addActorsOnGroup() {
        addAndFillActor(Image(screen.game.allAssets.FLAGS_CIRCLE))
        addBlackList()
        addCBoxList()
        addAreaList()
    }

    // ---------------------------------------------------
    // Add Actor
    // ---------------------------------------------------

    private fun addBlackList() {
        val posList = listOf(
            //Vector2(237f, 317f),
            //Vector2(247f, 357f),
            //Vector2(277f, 386f),
            Vector2(317f, 398f),
            Vector2(357f, 386f),
            //Vector2(387f, 357f),
            //Vector2(398f, 317f),
            //Vector2(387f, 276f),
            //Vector2(357f, 247f),
            //Vector2(317f, 236f),
            Vector2(277f, 247f),
            Vector2(247f, 276f),
        )
        val size = Vector2(26f, 26f)
        blackList.onEachIndexed { index, img ->
            addActor(img)
            img.setBounds(posList[index], size)
        }
    }

    override fun addCBoxList() {
        addActors(aCBoxList)

        val posList = listOf(
            Vector2(237f, 317f),
            Vector2(247f, 357f),
            Vector2(277f, 386f),
//            Vector2(317f, 398f),
//            Vector2(357f, 386f),
            Vector2(387f, 357f),
            Vector2(398f, 317f),
            Vector2(387f, 276f),
            Vector2(357f, 247f),
            Vector2(317f, 236f),
//            Vector2(277f, 247f),
//            Vector2(247f, 276f),
        )
        val size = Vector2(26f, 26f)

        aCBoxList.onEachIndexed { index, box ->
            box.setBounds(posList[index], size)
            box.setOrigin(Align.center)
            box.rotation = angleList[index]

            if (index == 0) box.check()

            box.setOnCheckListener {
                if (aCBoxList.all { !it.checkFlow.value }) {
                    box.check()
                }
            }
        }
    }

    override fun addAreaList() {
        addActors(aAreaList)

        val posList = listOf(
            Vector2(0f, 312f),
            Vector2(28f, 419f),
            Vector2(107f, 498f),
//            Vector2(215f, 527f),
//            Vector2(322f, 498f),
            Vector2(401f, 419f),
            Vector2(430f, 312f),
            Vector2(401f, 204f),
            Vector2(322f, 125f),
            Vector2(215f, 97f),
//            Vector2(107f, 125f),
//            Vector2(28f, 204f),
        )
        val size = Vector2(231f, 36f)

        aAreaList.onEachIndexed { index, area ->
            area.setBounds(posList[index], size)
            area.setOrigin(Align.center)
            area.rotation = angleList[index]

            area.setOnTouchListener(screen.game.soundUtil) {
                aCBoxList[index].also { box ->
                    if (box.checkFlow.value) box.uncheck() else box.check()
                }
            }
        }
    }

}