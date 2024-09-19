package com.ashan.ms.accounts.mapper;

import com.ashan.ms.accounts.dto.CustomerDto;
import com.ashan.ms.accounts.entity.Customer;

public class CustomerMapper {

    private CustomerMapper() {
        //restrict instantiation
    }

    public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }
}
