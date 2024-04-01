package com.liquidfun.playground.game.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable

val Float.toMS: Long get() = (this * 1000).toLong()
val Texture.region: TextureRegion get() = TextureRegion(this)
val TextureEmpty: Texture get() = Texture(1,1,Pixmap.Format.Alpha)

fun disposeAll(vararg disposable: Disposable) {
    disposable.forEach { it.dispose() }
}

fun currentTimeMinus(time: Long) = System.currentTimeMillis().minus(time)
fun Collection<Disposable>.disposeAll() {
    onEach { it.dispose() }
}

fun InputMultiplexer.addProcessors(vararg processor: InputProcessor) {
    processor.onEach { addProcessor(it) }
}

fun interface Block {
    fun invoke()
}

fun runGDX(block: Block) {
    Gdx.app.postRunnable { block.invoke() }
}

fun Float.divOr0(num: Float): Float = try { this / num } catch (e: Exception) { 0f }

fun Vector2.divOr0(scalar: Float): Vector2 {
    x = x.divOr0(scalar)
    y = y.divOr0(scalar)
    return this
}