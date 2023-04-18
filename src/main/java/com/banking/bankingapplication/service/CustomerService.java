package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.exceptions.UserNotFoundException;

import java.util.List;

public interface CustomerService {

    public CustomerDto createCustomer(CustomerDto customerDto);
    public List<CustomerDto> getAllCustomers();
    public CustomerDto customer(Long id) ;


}
