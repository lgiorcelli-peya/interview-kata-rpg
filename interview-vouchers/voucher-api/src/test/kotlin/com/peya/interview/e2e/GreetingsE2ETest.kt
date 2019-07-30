package com.peya.interview.e2e

import com.peya.interview.App
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient
import io.vertx.reactivex.ext.web.codec.BodyCodec
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.junit.platform.commons.util.StringUtils


@ExtendWith(VertxExtension::class)
class GreetingsE2ETest {

    // Deploy the verticle and execute the test methods when the verticle is successfully deployed
    @BeforeEach
    fun deployVerticle(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(App(), testContext.completing())
    }

    @Test
    fun checkGreetingsResponse(vertx: Vertx, testContext: VertxTestContext) {
        val client = WebClient.create(vertx)
        client.get(8080, "localhost", "/greetings")
                .`as`(BodyCodec.string())
                .send(testContext.succeeding { response ->
                    testContext.verify {
                        Assertions.assertAll(
                                Executable { Assertions.assertTrue(StringUtils.isNotBlank(response.body())) },
                                Executable { Assertions.assertEquals(200, response.statusCode()) }
                        )
                        testContext.completeNow()
                    }
                })
    }

    @Test
    fun notFoundRoute(vertx: Vertx, testContext: VertxTestContext) {
        val client = WebClient.create(vertx)
        client.get(8080, "localhost", "/bla")
                .`as`(BodyCodec.string())
                .send(testContext.succeeding { response ->
                    testContext.verify {
                        Assertions.assertEquals(404, response.statusCode())
                        testContext.completeNow()
                    }
                })
    }

}