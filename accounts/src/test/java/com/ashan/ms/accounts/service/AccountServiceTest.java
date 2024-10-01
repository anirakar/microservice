package com.ashan.ms.accounts.service;

import com.ashan.ms.accounts.data.TestData;
import com.ashan.ms.accounts.dto.CustomerDto;
import com.ashan.ms.accounts.exception.CustomerAlreadyExistsException;
import com.ashan.ms.accounts.repository.AccountsRepository;
import com.ashan.ms.accounts.repository.CustomerRepository;
import com.ashan.ms.accounts.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    AccountServiceImpl accountsService;

    TestData testData = new TestData();

    @Test
    void testCreateAccount() {
        CustomerDto customerDto = testData.getCustomerDto();
        when(customerRepository.findByMobileNumber(customerDto.getMobileNumber())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(testData.getCustomer());
        when(accountsRepository.save(any())).thenReturn(testData.getAccount());

        accountsService.createAccount(customerDto);

        verify(customerRepository, times(1)).findByMobileNumber(customerDto.getMobileNumber());
        verify(customerRepository, times(1)).save(any());
        verify(accountsRepository, times(1)).save(any());
    }

    @Test
    void testCustomerAlreadyExists() {
        CustomerDto customerDto = testData.getCustomerDto();
        when(customerRepository.findByMobileNumber(customerDto.getMobileNumber())).thenReturn(Optional.ofNullable(testData.getCustomer()));

        assertThrows(CustomerAlreadyExistsException.class, () -> accountsService.createAccount(customerDto));

        verify(customerRepository, times(1)).findByMobileNumber(customerDto.getMobileNumber());
    }

}
