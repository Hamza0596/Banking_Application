package com.banking.bankingapplication.Service;

import com.banking.bankingapplication.Dtos.CustomerDto;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;

import java.util.List;

public interface CustomerService {

    public CustomerDto createCustomer(CustomerDto customerDto);
    public List<CustomerDto> getAllCustomers();
    public CustomerDto customer(Long id) throws UserNotFoundException;


}
