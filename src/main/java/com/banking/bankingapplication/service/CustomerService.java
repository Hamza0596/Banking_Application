package com.banking.bankingapplication.service;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    public CustomerDto createCustomer(CustomerDto customerDto);
      public Page<CustomerDto> getAllCustomers(int pageNumber,int size);
    public CustomerDto customer(Long id) ;


    Page<CustomerDto> filter(String query, int pageNumber, int size);
}
