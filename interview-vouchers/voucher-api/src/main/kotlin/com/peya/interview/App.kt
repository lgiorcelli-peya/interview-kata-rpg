package com.peya.interview

import com.peya.interview.rest.greeting
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.http.httpServerOptionsOf


fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(App())
}

class App: AbstractVerticle() {

    private val router = Router.router(vertx).apply {
        /**
         * First route.
         */
        get("/greetings").handler { ctx ->
            ctx.response().end(greeting())
        }

    }

    override fun start(startFuture: Promise<Void>?) {
        vertx.createHttpServer(
                httpServerOptionsOf(
                        port = 8080,
                        host = "localhost"
                ))
                .requestHandler(router)
                .listen { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                }
        print("Server started on 8080")
    }

}

