package com.liquidfun.playground.game.screens

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.manager.MusicManager
import com.liquidfun.playground.game.manager.SoundManager
import com.liquidfun.playground.game.manager.SpriteManager
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedStage
import com.liquidfun.playground.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoaderScreen(override val game: LibGDXGame) : AdvancedScreen() {

    private val progressFlow     = MutableStateFlow(0f)
    private var isFinishLoading  = false
    private var isFinishProgress = false

    private val logoLibgdxImg    by lazy { Image(game.loaderAssets.LOGO_LIBGDX) }
    private val logoLiquidfunImg by lazy { Image(game.loaderAssets.LOGO_LIQUIDFUN) }

    override fun show() {
        loadSplashAssets()
        super.show()
        loadAssets()
        collectProgress()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
        isFinish()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addLogos()
    }

    // ------------------------------------------------------------------------
    // Add Actors
    // ------------------------------------------------------------------------

    private fun AdvancedStage.addLogos() {
        addActors(logoLibgdxImg, logoLiquidfunImg)
        logoLibgdxImg.setBounds(0f, 910f, 520f, 170f)
        logoLiquidfunImg.setBounds(1400f, 0f, 520f, 520f)
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(game.spriteManager) {
//            loadableAtlasList = SpriteManager.EnumAtlas.values().map { it.data }.toMutableList()
//            loadAtlas()
            loadableTextureList = mutableListOf(
                SpriteManager.EnumTexture.LOGO_LIBGDX.data,
                SpriteManager.EnumTexture.LOGO_LIQUIDFUN.data,
            )
            loadTexture()
        }
        game.assetManager.finishLoading()
        game.spriteManager.initTexture()
    }

    private fun loadAssets() {
        with(game.spriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.values().map { it.data }.toMutableList()
            loadAtlas()
            loadableTextureList = SpriteManager.EnumTexture.values().map { it.data }.toMutableList()
            loadTexture()
        }
        with(game.musicManager) {
            loadableMusicList = MusicManager.EnumMusic.values().map { it.data }.toMutableList()
            load()
        }
        with(game.soundManager) {
            loadableSoundList = SoundManager.EnumSound.values().map { it.data }.toMutableList()
            load()
        }
    }

    private fun initAssets() {
        game.spriteManager.initAtlasAndTexture()
        game.musicManager.init()
        game.soundManager.init()
    }

    private fun loadingAssets() {
        if (isFinishLoading.not()) {
            if (game.assetManager.update(16)) {
                isFinishLoading = true
                initAssets()
            }
            progressFlow.value = game.assetManager.progress
        }
    }

    private fun collectProgress() {
        coroutine?.launch {
            var progress = 0
            progressFlow.collect { p ->
                while (progress < (p * 100)) {
                    progress += 1
                    if (progress % 50 == 0) log("progress = $progress%")
                    if (progress == 100) isFinishProgress = true

                    delay((10..15).shuffled().first().toLong())
                }
            }
        }
    }

    private fun isFinish() {
        if (isFinishProgress) {
            isFinishProgress = false

            game.activity.lottie.hideLoader()

            game.musicUtil.apply {
                coff  = 0.4f
                music = JAPANESE.apply { isLooping = true }
            }

            game.navigationManager.navigate(DescriptionScreen::class.java.name)
        }
    }


}