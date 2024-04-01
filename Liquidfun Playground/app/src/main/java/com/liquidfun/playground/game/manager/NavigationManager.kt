package com.liquidfun.playground.game.manager

import com.badlogic.gdx.Gdx
import com.liquidfun.playground.game.LibGDXGame
import com.liquidfun.playground.game.screens.DescriptionScreen
import com.liquidfun.playground.game.screens.LoaderScreen
import com.liquidfun.playground.game.screens.MenuScreen
import com.liquidfun.playground.game.screens.levels.AbstractLevelScreen
import com.liquidfun.playground.game.screens.levels.DamBreakLevelScreen
import com.liquidfun.playground.game.screens.levels.FaucetLevelScreen
import com.liquidfun.playground.game.screens.levels.LiquidTimerLevelScreen
import com.liquidfun.playground.game.screens.levels.MultipleSystemLevelScreen
import com.liquidfun.playground.game.screens.levels.ParticleDrawingLevelScreen
import com.liquidfun.playground.game.screens.levels.SandboxLevelScreen
import com.liquidfun.playground.game.screens.levels.SparkyLevelScreen
import com.liquidfun.playground.game.screens.levels.WaveMachineLevelScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedScreen
import com.liquidfun.playground.game.utils.runGDX

class NavigationManager(val game: LibGDXGame) {

    private val backStack = mutableListOf<String>()
    var key: Int? = null
        private set

    fun navigate(toScreenName: String, fromScreenName: String? = null, key: Int? = null) = runGDX {
        this.key = key

        game.updateScreen(getScreenByName(toScreenName))
        backStack.filter { name -> name == toScreenName }.onEach { name -> backStack.remove(name) }
        fromScreenName?.let { fromName ->
            backStack.filter { name -> name == fromName }.onEach { name -> backStack.remove(name) }
            backStack.add(fromName)
        }
    }

    fun back(key: Int? = null) = runGDX {
        this.key = key

        if (isBackStackEmpty()) exit() else game.updateScreen(getScreenByName(backStack.removeLast()))
    }


    fun exit() = runGDX { Gdx.app.exit() }


    fun isBackStackEmpty() = backStack.isEmpty()

    private fun getScreenByName(name: String): AdvancedScreen = when(name) {
        LoaderScreen     ::class.java.name -> LoaderScreen(game)
        DescriptionScreen::class.java.name -> DescriptionScreen(game)
        MenuScreen       ::class.java.name -> MenuScreen(game)

        // Levels
        SandboxLevelScreen    ::class.java.name -> SandboxLevelScreen(game)
        SparkyLevelScreen     ::class.java.name -> SparkyLevelScreen(game)
        DamBreakLevelScreen   ::class.java.name -> DamBreakLevelScreen(game)
        LiquidTimerLevelScreen::class.java.name -> LiquidTimerLevelScreen(game)
        WaveMachineLevelScreen::class.java.name -> WaveMachineLevelScreen(game)
        FaucetLevelScreen     ::class.java.name -> FaucetLevelScreen(game)
        MultipleSystemLevelScreen ::class.java.name -> MultipleSystemLevelScreen(game)
        ParticleDrawingLevelScreen::class.java.name -> ParticleDrawingLevelScreen(game)

        else -> MenuScreen(game)
    }

}


