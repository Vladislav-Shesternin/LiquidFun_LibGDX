package com.liquidfun.playground.game.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Disposable
import com.liquidfun.playground.util.cancelCoroutinesAll
import com.liquidfun.playground.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WorldUtil: Disposable {

    companion object {
        const val GRAVITY_X = 0f
        const val GRAVITY_Y = -9.8f
        private const val TIME_STEP: Float = 1f / 60f

        var isDebug = false
    }

    private var accumulatorTime = 0f
    private val coroutine       = CoroutineScope(Dispatchers.Default)

    val world         by lazy { World(Vector2(GRAVITY_X, GRAVITY_Y), true) }
    val debugRenderer by lazy { Box2DDebugRenderer(true, true, true, true, true, true) }
    val bodyEditor    by lazy { BodyEditorLoader(Gdx.files.internal("physics/PhysicsData")) }

    val contactFilter   = WorldContactFilter()
    val contactListener = WorldContactListener()

    init {
        world.setContactFilter(contactFilter)
        world.setContactListener(contactListener)
    }

    override fun dispose() {
        try {
            log("WorldUtil dispose")
            cancelCoroutinesAll(coroutine)
            world.bodies().map { it.userData as AbstractBody }.destroyAll()
            world.dispose()
            debugRenderer.dispose()
        } catch (e: Exception) {
            log("dispose Exception Kotlin: ${e.message}")
        } catch (e: java.lang.Exception) {
            log("dispose Exception Java: ${e.message}")
        }
    }

    fun update(deltaTime: Float) {
        if (world.isLocked) return

        accumulatorTime += deltaTime

        while (accumulatorTime >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2, 2)
            accumulatorTime -= TIME_STEP
        }

        world.bodies().onEach { (it.userData as AbstractBody).render(deltaTime) }
    }

    fun debug(matrix4: Matrix4) {
         if (isDebug) debugRenderer.render(world, matrix4)
    }

}




