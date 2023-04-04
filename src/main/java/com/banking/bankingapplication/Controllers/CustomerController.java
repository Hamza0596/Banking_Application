package com.banking.bankingapplication.Controllers;

import com.banking.bankingapplication.Dtos.CustomerDto;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;
import com.banking.bankingapplication.Mappers.BankingMapper;
import com.banking.bankingapplication.Repositories.CustomerRepository;
import com.banking.bankingapplication.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("Api/Customer")
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
    public CustomerDto getCustomerById(@PathVariable Long id) throws UserNotFoundException {
        return customerService.customer(id);
    }
}

