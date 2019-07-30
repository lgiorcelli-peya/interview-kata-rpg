package com.peya.interview.scenario

import com.peya.interview.model.Voucher
import com.peya.interview.repository.VoucherRepository
import com.peya.interview.service.VoucherService
import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.util.*
import java.util.concurrent.CompletableFuture.runAsync
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream
import kotlin.coroutines.coroutineContext
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import kotlin.system.measureTimeMillis

class UseVouchersConcurrently {

    private val storage = mutableMapOf("AA1" to Voucher(3, "AA1"), "AA0" to Voucher(0, "AA0"), "AA2" to Voucher(2, "AA2"))
    private val voucherService = VoucherService(VoucherRepository(storage))

    @Test
    fun `check concurrent usages`() {
        val code = "AA1"

        val nThreads = 100
        val ctxCoordinator = Executors.newFixedThreadPool(nThreads)
        for (i in 1..nThreads) {
            ctxCoordinator.submit { voucherService.use(code) }
        }

        runBlocking {
            kotlinx.coroutines.delay(5000L)
        }

        Assertions.assertAll(
                Executable {
                    storage.filter { it.value.usages < 0 }
                            .forEach { (s, voucher) -> Assertions.fail() }
                }
        )
    }

    @Test
    fun `check concurrent usages for multiple voucher`() {
        val code = listOf( "AA1", "AA2", "AA3", "AA4", "AA5")

        val nThreads = 100
        val ctxCoordinator = Executors.newFixedThreadPool(nThreads)
        for (i in 1..nThreads) {
            ctxCoordinator.submit { voucherService.use(code.random()) }
        }


        Assertions.assertAll(
                Executable {
                    storage.filter { it.value.usages < 0 }
                            .forEach { (s, voucher) -> Assertions.fail() }
                }
        )

        ctxCoordinator.awaitTermination(10000, TimeUnit.MILLISECONDS)
    }
//
//    suspend fun massiveRun(action: suspend () -> Unit) {
//        val n = 1000  // number of coroutines to launch
//        val time = measureTimeMillis {
//            coroutineScope {
//                val jobs = List(n) {
//                    newFixedThreadPoolContext(n, "Concurrency").use { ctx ->
//                        async(ctx) {
//                            println("INIT (${Thread.currentThread().name})")
//                            action()
//                            println("FINISH (${Thread.currentThread().name})")
//                        }
//                    }
//                }
//            }
//        }
//        println("Completed ${n} actions in $time ms")
//    }

}