package com.banking.bankingapplication.controllers;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.entities.Customer;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    BankingMapper bankingMapper;

    @GetMapping("/customers/{pageNumber}/{size}")
    public Page<CustomerDto> customers(@PathVariable int pageNumber , @PathVariable int size){
        return customerService.getAllCustomers(pageNumber, size);
    }


    @GetMapping("/customers/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id)  {
        return customerService.customer(id);
    }

    @PostMapping("customers")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto){
        return customerService.createCustomer(customerDto);}

    @PostMapping("customers/update/{id}")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto,@PathVariable Long id){
        customerDto.setId(id);
        return customerService.createCustomer(customerDto);}


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/search/{pageNumber}/{size}")
    public Page<CustomerDto> customerFilter(@RequestParam String query , @PathVariable int pageNumber, @PathVariable int size ){
        return customerService.filter(query,pageNumber,size);

    }

    @DeleteMapping ("/delete/{userId}")
    public void deleteCustomerById(@PathVariable Long userId) {
        customerService.deleteUser(userId);

    }
}

