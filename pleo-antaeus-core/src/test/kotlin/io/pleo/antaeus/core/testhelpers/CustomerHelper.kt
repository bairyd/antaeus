package io.pleo.antaeus.core.testhelpers

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer

internal class CustomerHelper {
    companion object {
        private const val validCustomerOneId = 1
        private const val validCustomerTwoId = 2
        private const val validCustomerThreeId = 3

        internal fun getListOfValidCustomers(): List<Customer> {
            return listOf(Customer(validCustomerTwoId, Currency.DKK), Customer(validCustomerThreeId, Currency.GBP))
        }

        internal fun getSingleValidCustomer(): Customer {
            return Customer(validCustomerOneId, Currency.EUR)
        }
    }
}