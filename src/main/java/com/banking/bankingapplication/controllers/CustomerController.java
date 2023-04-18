package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    BankingMapper bankingMapper;
    @GetMapping("/customers")
    public List<CustomerDto> customers(){
        return customerService.getAllCustomers();
    }


    @GetMapping("/customers/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id)  {
        return customerService.customer(id);
    }

    @PostMapping("customers")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);
    }
}

