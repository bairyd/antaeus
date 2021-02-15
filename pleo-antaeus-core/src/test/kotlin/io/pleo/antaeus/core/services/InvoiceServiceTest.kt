package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.core.testhelpers.CustomerHelper.Companion.validCustomerOneId
import io.pleo.antaeus.core.testhelpers.InvoiceHelper
import io.pleo.antaeus.core.testhelpers.InvoiceHelper.Companion.validInvoiceOneId
import io.pleo.antaeus.core.testhelpers.InvoiceHelper.Companion.validInvoiceThreeId
import io.pleo.antaeus.core.testhelpers.InvoiceHelper.Companion.validInvoiceTwoId
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InvoiceServiceTest {


    private val dal = mockk<AntaeusDal> {
        every { fetchInvoice(404) } returns null
        every { fetchInvoice(validInvoiceOneId) } returns InvoiceHelper.getSingleValidInvoice()
        every { fetchInvoices() } returns InvoiceHelper.getListOfValidInvoices()
    }

    private val invoiceService = InvoiceService(dal = dal)

    @Test
    fun `will throw if invoice is not found`() {
        assertThrows<InvoiceNotFoundException> {
            invoiceService.fetch(404)
        }
    }

    @Test
    fun `will return an invoice if an Id is supplied and matches a record`() {
        val invoice = invoiceService.fetch(validInvoiceOneId)
        assert(invoice.id == validInvoiceOneId)
        assert(invoice.customerId == validCustomerOneId)
        assert(invoice.amount.value == BigDecimal.TEN)
        assert(invoice.amount.currency == Currency.SEK)
        assert(invoice.status == InvoiceStatus.PAID)
    }

    @Test
    fun `will return a list of invoices if any records exist`() {
        val invoices = invoiceService.fetchAll()
        assert(invoices.size == 8)

        val invoiceTwo = invoices.find { i -> i.id == validInvoiceTwoId }
        assert(invoiceTwo?.id == validInvoiceTwoId)
        assert(invoiceTwo?.customerId == validInvoiceThreeId)
        assert(invoiceTwo?.amount?.value == BigDecimal.valueOf(150))
        assert(invoiceTwo?.amount?.currency == Currency.USD)
        assert(invoiceTwo?.status == InvoiceStatus.PENDING)

        val invoiceThree = invoices.find { i -> i.id == validInvoiceThreeId }
        assert(invoiceThree?.id == validInvoiceThreeId)
        assert(invoiceThree?.customerId == validCustomerOneId)
        assert(invoiceThree?.amount?.value == BigDecimal.valueOf(99))
        assert(invoiceThree?.amount?.currency == Currency.SEK)
        assert(invoiceThree?.status == InvoiceStatus.PAID)
    }
}
