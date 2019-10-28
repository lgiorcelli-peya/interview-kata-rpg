package com.pedidosya

import org.junit.jupiter.api.Test

class CharacterKotlinTest {

    @Test
    internal fun characterAttackOtherCharacter() {
        val character1 = RPGCharacter(range = 10)
        val character2 = RPGCharacter(range = 10)
        val attackedCharacter = character1.attack(character2, 200, 10)
        assert(attackedCharacter.health == 800)
        assert(attackedCharacter.isAlive())
    }

    @Test
    internal fun characterKillsOtherCharacter() {
        val character1 = RPGCharacter(range = 10)
        val character2 = RPGCharacter(range = 10)
        val attackedCharacter = character1.attack(character2, 2000, 1)
        assert(attackedCharacter.health == 0)
        assert(!attackedCharacter.isAlive())
    }
}