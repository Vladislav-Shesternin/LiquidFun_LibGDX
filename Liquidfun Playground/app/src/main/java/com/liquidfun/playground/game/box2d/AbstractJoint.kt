package com.liquidfun.playground.game.box2d

import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.JointDef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.liquidfun.playground.game.utils.advanced.AdvancedBox2dScreen
import com.liquidfun.playground.util.cancelCoroutinesAll

class AbstractJoint<T : Joint, TD : JointDef>(val screenBox2d: AdvancedBox2dScreen): Destroyable {

    var coroutine: CoroutineScope? = null
        private set
    var joint: T? = null
        private set
    var jointDef: TD? = null
        private set

    fun create(jointDef: TD) {
        if (joint == null) {
            this.jointDef = jointDef
            joint         = screenBox2d.worldUtil.world.createJoint(jointDef).apply { userData = this@AbstractJoint } as T
            coroutine     = CoroutineScope(Dispatchers.Default)
        }
    }

    override fun destroy() {
        if (joint != null) {
            cancelCoroutinesAll(coroutine)
            coroutine = null
            jointDef  = null

            screenBox2d.worldUtil.world.destroyJoint(joint)
            joint = null
        }
    }

}