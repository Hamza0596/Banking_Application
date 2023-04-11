package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.entities.Customer;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.CustomerRepository;
import com.banking.bankingapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional

public class CustomerServiceImpl  implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BankingMapper bankingMapper;
    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
       Customer cus=bankingMapper.fromCustomerDto(customerDto);
        return bankingMapper.fromCustomer(customerRepository.save(cus));
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return bankingMapper.fromCustomerListToCustomerListDto(customerRepository.findAll());
    }

    @Override
    public CustomerDto customer(Long id) throws UserNotFoundException {
        return bankingMapper.fromCustomer(customerRepository.findById(id).orElseThrow(()->new UserNotFoundException("No customer was found with this id")));
    }


}
