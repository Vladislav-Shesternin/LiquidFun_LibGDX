package com.liquidfun.playground.game.box2d

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.liquidfun.playground.util.log
import finnstr.libgdx.liquidfun.ParticleBodyContact
import finnstr.libgdx.liquidfun.ParticleContact
import finnstr.libgdx.liquidfun.ParticleSystem

class WorldContactListener: ContactListener {

    private val Contact.abstractBodyA get() = (fixtureA.body.userData as AbstractBody)
    private val Contact.abstractBodyB get() = (fixtureB.body.userData as AbstractBody)

    override fun beginContact(contact: Contact) {
        with(contact) {
            abstractBodyA.beginContact(abstractBodyB, contact)
            abstractBodyB.beginContact(abstractBodyA, contact)
        }
    }

    override fun endContact(contact: Contact) {
        with(contact) {
            abstractBodyA.endContact(abstractBodyB, contact)
            abstractBodyB.endContact(abstractBodyA, contact)
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {}
    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {}

    override fun beginParticleBodyContact(system: ParticleSystem?, contact: ParticleBodyContact?) {
        contact?.let {
            (contact.body.userData as? AbstractBody)?.beginParticleContact(it.particleSystem, it.index)
        }
    }
    override fun endParticleBodyContact(p0: Fixture?, p1: ParticleSystem?, p2: Int) {
        //log("endParticleBodyContact")
    }
    override fun beginParticleContact(p0: ParticleSystem?, p1: ParticleContact?) {
        //log("beginParticleContact")

    }
    override fun endParticleContact(p0: ParticleSystem?, p1: Int, p2: Int) {
        //log("endParticleContact")
    }

    // ---------------------------------------------------
    // Logic
    // ---------------------------------------------------

//    private fun playSound(bodyA: AbstractBody, bodyB: AbstractBody) {
//        tmpArray[0] = bodyA
//        tmpArray[1] = bodyB
//
//        soundUtil = bodyA.screenBox2d.game.soundUtil
//
//        if (tmpArray.all { it.fixtureDef.isSensor.not() }) {
//            when {
//                tmpArray.any { it.id == BodyId.BORDERS }
//                -> if (currentTimeMinus(timeContactBorders) >= 100) {
//                    soundUtil.apply { play(plasmablaster_udar, 100f) }
//                    timeContactBorders = System.currentTimeMillis()
//                }
//
//                tmpArray.any { it.bodyDef.type == BodyDef.BodyType.DynamicBody }
//                -> if (currentTimeMinus(timeContactDynamic) >= 100) {
//                    soundUtil.apply { play(plasmablaster_udar, 100f/*volumeLevel * 0.2f*/) }
//                    timeContactDynamic = System.currentTimeMillis()
//                }
//            }
//        }
//    }
}