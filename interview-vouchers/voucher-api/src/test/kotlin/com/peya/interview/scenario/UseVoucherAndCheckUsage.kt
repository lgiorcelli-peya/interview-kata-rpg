package com.peya.interview.scenario

import com.peya.interview.model.Voucher
import com.peya.interview.repository.VoucherRepository
import com.peya.interview.service.VoucherService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.util.concurrent.atomic.AtomicInteger

class UseVoucherAndCheckUsage {

    private val storage = mutableMapOf("AA1" to Voucher(3, "AA1"), "AA0" to Voucher(0, "AA0"))
    private val voucherService = VoucherService(VoucherRepository(storage))

    @Test
    fun `use once the voucher and the usage has to be n - 1`() {
        val code = "AA1"
        val used: Boolean = voucherService.canUse(code)
        voucherService.use(code)

        val returned = voucherService.getVoucher(code)!!

        Assertions.assertAll(
                Executable { Assertions.assertTrue(used) },
                Executable { Assertions.assertEquals(2, returned.usages) },
                Executable { Assertions.assertEquals(code, returned.code) }
        )
    }

    @Test
    fun `cannot use voucher with 0 usages`() {
        val code = "AA0"
        val used: Boolean = voucherService.canUse(code)
        voucherService.use(code)

        val returned = voucherService.getVoucher(code)!!



        Assertions.assertAll(
                Executable { Assertions.assertFalse(used) },
                Executable { Assertions.assertEquals(0, returned.usages) },
                Executable { Assertions.assertEquals(code, returned.code) }
        )
    }
}