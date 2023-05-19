package com.banking.bankingapplication.service.serviceimpl;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.entities.Customer;
import com.banking.bankingapplication.exceptions.UserNotFoundException;
import com.banking.bankingapplication.mappers.BankingMapper;
import com.banking.bankingapplication.repositories.CustomerRepository;
import com.banking.bankingapplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional

public class CustomerServiceImpl  implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BankingMapper bankingMapper;



    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        customerDto.setCreationDate(new Date());
       Customer cus=bankingMapper.fromCustomerDto(customerDto);
        return bankingMapper.fromCustomer(customerRepository.save(cus));
    }

    @Override
    public Page<CustomerDto> getAllCustomers(int pageNumber,int size) {
        return bankingMapper.fromCustomerListToCustomerPageDto(customerRepository.findAll(PageRequest.of(pageNumber,size)));
    }

    @Override
    public CustomerDto customer(Long id)   {
        return bankingMapper.fromCustomer(customerRepository.findById(id).orElseThrow(()->new UserNotFoundException("No customer was found with this id")));
    }

    @Override
    public Page<CustomerDto> filter(String query, int pageNumber,int size) {
        return bankingMapper.fromCustomerListToCustomerPageDto( customerRepository.findByQuery(query, PageRequest.of(pageNumber,size)));
    }

    @Override
    public CustomerDto getCustomerByMail(String email) {
        return bankingMapper.fromCustomer(customerRepository.findByEmail(email).orElse(null));
    }

    @Override
    public void deleteUser(Long userId) {
         customerRepository.deleteById(userId);
    }


}
