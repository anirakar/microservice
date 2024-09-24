package com.ashan.ms.accounts.service.impl;

import com.ashan.ms.accounts.constants.AccountsConstants;
import com.ashan.ms.accounts.dto.AccountsDto;
import com.ashan.ms.accounts.dto.CustomerDto;
import com.ashan.ms.accounts.entity.Accounts;
import com.ashan.ms.accounts.entity.Customer;
import com.ashan.ms.accounts.exception.CustomerAlreadyExistsException;
import com.ashan.ms.accounts.exception.ResourceNotFoundException;
import com.ashan.ms.accounts.mapper.AccountsMapper;
import com.ashan.ms.accounts.mapper.CustomerMapper;
import com.ashan.ms.accounts.repository.AccountsRepository;
import com.ashan.ms.accounts.repository.CustomerRepository;
import com.ashan.ms.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     *
     * @param customerDto - CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if( optionalCustomer.isPresent() ) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number "+customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                 new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() ->
                new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if( accountsDto != null ) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(() ->
                    new ResourceNotFoundException("Accounts", "accountNumber", accountsDto.getAccountNumber().toString()));

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                    new ResourceNotFoundException("Customer", "customerId", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
