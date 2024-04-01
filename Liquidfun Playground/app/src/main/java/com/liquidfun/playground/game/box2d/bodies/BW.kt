package com.liquidfun.playground.game.box2d.bodies

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.liquidfun.playground.game.actors.image.AImage
import com.liquidfun.playground.game.box2d.AbstractBody
import com.liquidfun.playground.game.box2d.BodyId
import com.liquidfun.playground.game.utils.GameColor
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen
import com.liquidfun.playground.game.utils.advanced.AdvancedGroup

class BW(override val screenBox2d: AdvancedBox2dScreen): AbstractBody() {
    override val name       = "wm"
    override val bodyDef    = BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
    }
    override val fixtureDef = FixtureDef().apply {
        density = 1f
    }

    override var actor: AdvancedGroup? = AImage(screenBox2d, screenBox2d.game.allAssets.WM)

    override var id = BodyId.DYNAMIC
    override val collisionList = mutableListOf(BodyId.BORDERS, BodyId.DYNAMIC)

}