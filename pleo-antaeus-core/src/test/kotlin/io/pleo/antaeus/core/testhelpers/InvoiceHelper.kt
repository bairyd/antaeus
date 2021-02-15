package io.pleo.antaeus.core.testhelpers

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import java.math.BigDecimal

internal class InvoiceHelper {
    companion object {
    private const val validCustomerId = 1
    private const val validInvoiceOneId = 1
    private const val validInvoiceTwoId = 2
    private const val validInvoiceThreeId = 3

        internal fun getSingleValidInvoice(): Invoice {
            return Invoice(validInvoiceOneId, validCustomerId, Money(BigDecimal.TEN, Currency.SEK), InvoiceStatus.PAID)
        }

        internal fun getListOfValidInvoices(): List<Invoice> {
            return listOf(
                Invoice(validInvoiceTwoId, validCustomerId, Money(BigDecimal.ZERO, Currency.USD), InvoiceStatus.PENDING),
                Invoice(validInvoiceThreeId, validCustomerId, Money(BigDecimal.valueOf(150), Currency.EUR), InvoiceStatus.PAID)
            )
        }
    }
}