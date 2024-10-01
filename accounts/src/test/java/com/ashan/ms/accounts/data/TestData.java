package com.ashan.ms.accounts.data;

import com.ashan.ms.accounts.constants.AccountsConstants;
import com.ashan.ms.accounts.dto.CustomerDto;
import com.ashan.ms.accounts.entity.Accounts;
import com.ashan.ms.accounts.entity.Customer;
import com.ashan.ms.accounts.mapper.CustomerMapper;

import java.util.Random;

public class TestData {

    public CustomerDto getCustomerDto() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("abc");
        customerDto.setEmail("abc@test.com");
        customerDto.setMobileNumber("1234567890");
        return customerDto;
    }

    public Customer getCustomer() {
        Customer customer = CustomerMapper.mapToCustomer(getCustomerDto(), new Customer());
        customer.setCustomerId(1L);
        return customer;
    }

    public Accounts getAccount() {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(1L);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }
}
