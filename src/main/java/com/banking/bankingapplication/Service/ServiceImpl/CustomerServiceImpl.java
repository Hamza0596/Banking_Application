package com.banking.bankingapplication.Service.ServiceImpl;

import com.banking.bankingapplication.Dtos.CustomerDto;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Exceptions.UserNotFoundException;
import com.banking.bankingapplication.Mappers.BankingMapper;
import com.banking.bankingapplication.Repositories.CustomerRepository;
import com.banking.bankingapplication.Service.CustomerService;
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
    public CustomerDto createCustomer(Customer customer) {

        return bankingMapper.fromCustomer(customerRepository.save(customer));
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
