package com.pedidosya

import kotlin.math.max
import kotlin.math.min

const val MAX_HEALTH = 1000

data class RPGCharacter(val health: Int = MAX_HEALTH, val range: Int)  {

    fun isAlive() = health > 0
    fun attack(victim: RPGCharacter, damage: Int, distance: Int): RPGCharacter {
        if (distance > range) return victim
        return victim.receiveDamage(damage)
    }

    fun healCharacter(character: RPGCharacter, healing: Int): RPGCharacter {
        return character.receiveHealing(healing)
    }

    private fun receiveHealing(healing: Int): RPGCharacter {
        if (!isAlive())
            return this
        return modifyHealth(health + healing)
    }

    private fun receiveDamage(damage: Int): RPGCharacter {
        return modifyHealth(health - damage)
    }

    private fun modifyHealth(newHealth: Int) : RPGCharacter {
        val finalHealth = max(min(newHealth, MAX_HEALTH), 0)
        return this.copy(health = finalHealth)
    }
}
