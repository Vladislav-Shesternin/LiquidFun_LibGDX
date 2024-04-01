package com.liquidfun.playground.game.manager

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class SpriteManager(var assetManager: AssetManager) {

    var loadableAtlasList   = mutableListOf<AtlasData>()
    var loadableTextureList = mutableListOf<TextureData>()

    fun loadAtlas() {
        loadableAtlasList.onEach { assetManager.load(it.path, TextureAtlas::class.java) }
    }

    fun loadTexture() {
        loadableTextureList.onEach {
            assetManager.load(it.path, Texture::class.java, TextureLoader.TextureParameter().apply {
                minFilter = Texture.TextureFilter.Linear
                magFilter = Texture.TextureFilter.Linear
                genMipMaps = true
            })
        }
    }

    fun initAtlas() {
        loadableAtlasList.onEach { it.atlas = assetManager[it.path, TextureAtlas::class.java] }
        loadableAtlasList.clear()
    }

    fun initTexture() {
        loadableTextureList.onEach { it.texture = assetManager[it.path, Texture::class.java] }
        loadableTextureList.clear()
    }

    fun initAtlasAndTexture() {
        initAtlas()
        initTexture()
    }


    enum class EnumAtlas(val data: AtlasData) {
        // All
        ALL(AtlasData("atlas/all.atlas")),
    }

    enum class EnumTexture(val data: TextureData) {
        // Loader
        LOGO_LIBGDX   (TextureData("textures/loader/logo_libgdx.png")),
        LOGO_LIQUIDFUN(TextureData("textures/loader/logo_liquidfun.png")),

        // All
        FLAGS_CIRCLE(TextureData("textures/flags_circle.png")),
        WM          (TextureData("textures/wm.png")),

        // Levels
        Sanbox          (TextureData("textures/levels/Sanbox.png")),
        ParticleDrawing (TextureData("textures/levels/ParticleDrawing.png")),
        DamBreak        (TextureData("textures/levels/DamBreak.png")),
        LiquidTimer     (TextureData("textures/levels/LiquidTimer.png")),
        WaveMachine     (TextureData("textures/levels/WaveMachine.png")),
        Faucet          (TextureData("textures/levels/Faucet.png")),
        Sparky          (TextureData("textures/levels/Sparky.png")),
        MultiplySystem  (TextureData("textures/levels/MultiplySystem.png")),
    }

    data class AtlasData(val path: String) {
        lateinit var atlas: TextureAtlas
    }

    data class TextureData(val path: String) {
        lateinit var texture: Texture
    }

}