package io.pleo.antaeus.core.testhelpers

import io.pleo.antaeus.core.testhelpers.CustomerHelper.Companion.validCustomerOneId
import io.pleo.antaeus.core.testhelpers.CustomerHelper.Companion.validCustomerThreeId
import io.pleo.antaeus.core.testhelpers.CustomerHelper.Companion.validCustomerTwoId
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import java.math.BigDecimal

internal class InvoiceHelper {
    companion object {
    internal const val validInvoiceOneId = 1
    internal const val validInvoiceTwoId = 2
    internal const val validInvoiceThreeId = 3
    internal const val validInvoiceFourId = 4
    internal const val validInvoiceFiveId = 5
    internal const val validInvoiceSixId = 6
    internal const val validInvoiceSevenId = 7
    internal const val validInvoiceEightId = 8

        internal fun getSingleValidInvoice(): Invoice {
            return Invoice(validInvoiceOneId, validCustomerOneId, Money(BigDecimal.TEN, Currency.SEK),
                InvoiceStatus.PAID)
        }

        internal fun getListOfValidInvoices(): List<Invoice> {
            return listOf (
                Invoice(validInvoiceOneId, validCustomerTwoId, Money(BigDecimal.valueOf(100), Currency.USD),
                    InvoiceStatus.PENDING),
                Invoice(validInvoiceTwoId, validCustomerThreeId, Money(BigDecimal.valueOf(150), Currency.USD),
                    InvoiceStatus.PENDING),
                Invoice(validInvoiceThreeId, validCustomerOneId, Money(BigDecimal.valueOf(99), Currency.SEK),
                    InvoiceStatus.PAID),
                Invoice(validInvoiceFourId, validCustomerTwoId, Money(BigDecimal.valueOf(89), Currency.USD),
                    InvoiceStatus.PENDING),
                Invoice(validInvoiceFiveId, validCustomerThreeId, Money(BigDecimal.valueOf(50), Currency.USD),
                    InvoiceStatus.PENDING),
                Invoice(validInvoiceSixId, validCustomerOneId, Money(BigDecimal.valueOf(199), Currency.SEK),
                    InvoiceStatus.PAID),
                Invoice(validInvoiceSevenId, validCustomerThreeId, Money(BigDecimal.valueOf(120), Currency.EUR),
                    InvoiceStatus.PENDING),
                Invoice(validInvoiceEightId, validCustomerThreeId, Money(BigDecimal.valueOf(10), Currency.EUR),
                    InvoiceStatus.PENDING)
            )
        }
    }
}