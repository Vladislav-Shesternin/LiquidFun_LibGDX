package com.liquidfun.playground.game.manager.util

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.liquidfun.playground.game.manager.SpriteManager

class SpriteUtil {

     class LoaderAssets {
          val LOGO_LIBGDX    = SpriteManager.EnumTexture.LOGO_LIBGDX.data.texture
          val LOGO_LIQUIDFUN = SpriteManager.EnumTexture.LOGO_LIQUIDFUN.data.texture
     }

     class AllAssets {
          private fun getRegion(name: String): TextureRegion = SpriteManager.EnumAtlas.ALL.data.atlas.findRegion(name)
          private fun getPath(name: String): NinePatch = SpriteManager.EnumAtlas.ALL.data.atlas.createPatch(name)

          val ball         = getRegion("ball")
          val btn_def      = getRegion("btn_def")
          val btn_press    = getRegion("btn_press")
          val black_circle = getRegion("black_circle")
          val check        = getRegion("check")
          val languages    = getRegion("languages")
          val faucet       = getRegion("faucet")
          val user         = getRegion("user")

          val blur_pink_top    = getRegion("blur_pink")
          val blur_pink_bot    = TextureRegion(blur_pink_top).apply { flip(false, true) }
          val blur_blue_bot    = getRegion("blur_blue")
          val blur_blue_top    = TextureRegion(blur_blue_bot).apply { flip(false, true) }

          // 9.path
          val frame_level = getPath("frame_level")

//          val bottles = List(32) { getRegion("${it.inc()}") }

          val FLAGS_CIRCLE = SpriteManager.EnumTexture.FLAGS_CIRCLE.data.texture
          val WM           = SpriteManager.EnumTexture.WM.data.texture

          // Levels
          val Sanbox          = SpriteManager.EnumTexture.Sanbox.data.texture
          val ParticleDrawing = SpriteManager.EnumTexture.ParticleDrawing.data.texture
          val DamBreak        = SpriteManager.EnumTexture.DamBreak.data.texture
          val LiquidTimer     = SpriteManager.EnumTexture.LiquidTimer.data.texture
          val WaveMachine     = SpriteManager.EnumTexture.WaveMachine.data.texture
          val Faucet          = SpriteManager.EnumTexture.Faucet.data.texture
          val Sparky          = SpriteManager.EnumTexture.Sparky.data.texture
          val MultiplySystem  = SpriteManager.EnumTexture.MultiplySystem.data.texture
     }

}