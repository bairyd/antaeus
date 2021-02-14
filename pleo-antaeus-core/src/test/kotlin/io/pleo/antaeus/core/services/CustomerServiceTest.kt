package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerServiceTest {

    private val validCustomerOneId = 1
    private val validCustomerTwoId = 2
    private val validCustomerThreeId = 3

    private val dal = mockk<AntaeusDal> {
        every { fetchCustomer(404) } returns null
        every { fetchCustomer(1) } returns getValidCustomer()
        every { fetchCustomers() } returns getValidListOfCustomers()
    }

    private fun getValidCustomer(): Customer {
        return Customer(validCustomerOneId, Currency.EUR)
    }

    private fun getValidListOfCustomers(): List<Customer> {
        return listOf(Customer(validCustomerTwoId, Currency.DKK), Customer(validCustomerThreeId, Currency.GBP))
    }

    private val customerService = CustomerService(dal = dal)

    @Test
    fun `will throw if customer is not found`() {
        assertThrows<CustomerNotFoundException> {
            customerService.fetch(404)
        }
    }

    @Test
    fun `will return a valid customer when supplied with a matching ID`() {
        val customer = customerService.fetch(validCustomerOneId)
        assert(customer.id == 1)
        assert(customer.currency == Currency.EUR)
    }

    @Test
    fun `will return a list of customers if any exist`() {
        val customers = customerService.fetchAll()
        assert(customers.size == 2)
        assert(customers.find { c -> c.id == validCustomerTwoId }?.currency == Currency.DKK)
        assert(customers.find { c -> c.id == validCustomerThreeId }?.currency == Currency.GBP)
    }
}
