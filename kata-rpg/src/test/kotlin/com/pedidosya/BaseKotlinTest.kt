package com.pedidosya

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BaseKotlinTest {

    @Test
    internal fun name() {
        Assertions.assertEquals(2, 1+1)
    }
}