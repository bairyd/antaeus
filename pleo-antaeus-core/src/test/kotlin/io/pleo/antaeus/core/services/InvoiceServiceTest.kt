package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InvoiceServiceTest {

    private val validInvoiceOneId = 1
    private val validInvoiceTwoId = 2
    private val validInvoiceThreeId = 3
    private val validCustomerId = 1

    private val dal = mockk<AntaeusDal> {
        every { fetchInvoice(404) } returns null
        every { fetchInvoice(validInvoiceOneId) } returns GetValidInvoice()
        every { fetchInvoices() } returns GetValidInvoices()
    }

    private fun GetValidInvoice(): Invoice {
        return Invoice(validInvoiceOneId, validCustomerId, Money(BigDecimal.TEN, Currency.SEK), InvoiceStatus.PAID)
    }

    private fun GetValidInvoices(): List<Invoice> {
        return listOf(
            Invoice(validInvoiceTwoId, validCustomerId, Money(BigDecimal.ZERO, Currency.USD), InvoiceStatus.PENDING),
            Invoice(validInvoiceThreeId, validCustomerId, Money(BigDecimal.valueOf(150), Currency.EUR), InvoiceStatus.PAID))
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
        assert(invoice.customerId == validCustomerId)
        assert(invoice.amount.value == BigDecimal.TEN)
        assert(invoice.amount.currency == Currency.SEK)
        assert(invoice.status == InvoiceStatus.PAID)
    }

    @Test
    fun `will return a list of invoices if any records exist`() {
        val invoices = invoiceService.fetchAll()
        val invoiceTwo = invoices.find { i -> i.id == validInvoiceTwoId }

        assert(invoiceTwo?.id == validInvoiceTwoId)
        assert(invoiceTwo?.customerId == validCustomerId)
        assert(invoiceTwo?.amount?.value == BigDecimal.ZERO)
        assert(invoiceTwo?.amount?.currency == Currency.USD)
        assert(invoiceTwo?.status == InvoiceStatus.PENDING)

        val invoiceThree = invoices.find { i -> i.id == validInvoiceThreeId }

        assert(invoiceThree?.id == validInvoiceThreeId)
        assert(invoiceThree?.customerId == validCustomerId)
        assert(invoiceThree?.amount?.value == BigDecimal.valueOf(150))
        assert(invoiceThree?.amount?.currency == Currency.EUR)
        assert(invoiceThree?.status == InvoiceStatus.PAID)
    }
}
