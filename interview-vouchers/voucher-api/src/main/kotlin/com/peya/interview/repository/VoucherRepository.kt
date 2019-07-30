package com.peya.interview.repository

import com.peya.interview.model.Voucher
import kotlin.reflect.KFunction1

class VoucherRepository(private val vouchers: MutableMap<String, Voucher>) {

    private val lock = Any()

    fun subtractUsages(code: String, validation: KFunction1<String, Boolean>): Voucher? {

        synchronized(code) {
            execute(validation, code)
        }


        return get(code)
    }

    private fun execute(validation: KFunction1<String, Boolean>, code: String) {
        if (validation(code)) {
            val currentVoucher = vouchers[code]
            currentVoucher?.let {
                val newVoucher = it.copy(usages = it.usages - 1)
                // to simulate concurrent in asynchronous requests

                vouchers[code] = newVoucher
                println("(${Thread.currentThread().name}) current = $newVoucher")
            }
        }
    }


    fun get(code: String): Voucher? {
        Thread.sleep(2000)
        return vouchers[code]
    }

}
