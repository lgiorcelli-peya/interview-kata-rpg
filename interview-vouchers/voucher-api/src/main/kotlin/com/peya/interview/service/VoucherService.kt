package com.peya.interview.service

import com.peya.interview.model.Voucher
import com.peya.interview.repository.VoucherRepository

class VoucherService(private val repository: VoucherRepository) {

    fun use(code: String) {

        repository.subtractUsages(code, this::canUse)

    }

    fun canUse(code: String): Boolean {
        return getVoucher(code)?.let {
            it.usages > 0
        } ?: false
    }

    fun getVoucher(code: String): Voucher? {
        return repository.get(code)
    }

}
