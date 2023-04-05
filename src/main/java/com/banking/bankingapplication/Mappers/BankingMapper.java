package com.banking.bankingapplication.Mappers;

import com.banking.bankingapplication.Dtos.BankAccountDto;
import com.banking.bankingapplication.Dtos.CurrentAcountDto;
import com.banking.bankingapplication.Dtos.CustomerDto;
import com.banking.bankingapplication.Dtos.SavingAccountDto;
import com.banking.bankingapplication.Entities.BankAccount;
import com.banking.bankingapplication.Entities.CurrentAccount;
import com.banking.bankingapplication.Entities.Customer;
import com.banking.bankingapplication.Entities.SavingAccount;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingMapper {

    public CustomerDto fromCustomer(Customer customer){
        CustomerDto customerDto=new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
        return customerDto;
    }

    public Customer fromCustomerDto(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setId(customerDto.getId());
        customer.setEmail(customerDto.getEmail());
        return customer;
    }

    public List<CustomerDto>  fromCustomerListToCustomerListDto(List<Customer> customers){
        List<CustomerDto> customersDto=customers.stream().map(customer -> {
            CustomerDto customerDto= new CustomerDto();
            customerDto.setEmail(customer.getEmail());
            customerDto.setName(customer.getName());
            customerDto.setId(customer.getId());
            return customerDto;
        }).collect(Collectors.toList());
        return customersDto;
    }


    public List<Customer>  fromCustomerDtoListToCustomerList(List<CustomerDto> customersDto){
        List<Customer> customers=customersDto.stream().map(customerDto -> {
            Customer customer= new Customer();
            customer.setEmail(customerDto.getEmail());
            customer.setName(customerDto.getName());
            customer.setId(customerDto.getId());
            return customer;
        }).collect(Collectors.toList());
        return customers;
    }






    public List<BankAccountDto>  fromBankAccountListToBankAccountDto(List<BankAccount> bankAccouts){
        List<BankAccountDto> bankAccountDtos=bankAccouts.stream().map(bankAcount -> {
            BankAccountDto bankAccountDto= new BankAccountDto();
            bankAccountDto.setId(bankAcount.getId());
            bankAccountDto.setBalnce(bankAcount.getBalnce());
            bankAccountDto.setStatus(bankAcount.getStatus());
            bankAccountDto.setCustomer(bankAcount.getCustomer());
            bankAccountDto.setCreatedAt(bankAcount.getCreatedAt());

            return bankAccountDto;
        }).collect(Collectors.toList());
        return bankAccountDtos;
    }



    public BankAccountDto fromBankAccount(BankAccount bankAccount){

        if(bankAccount instanceof CurrentAccount){
            CurrentAcountDto currentAcountDto=new CurrentAcountDto();
            BeanUtils.copyProperties((CurrentAccount) bankAccount,currentAcountDto);
            System.out.println(currentAcountDto);

            return currentAcountDto;
        }else {
            SavingAccountDto savingAccountDto=new SavingAccountDto();
            BeanUtils.copyProperties((SavingAccount)bankAccount,savingAccountDto);
            System.out.println(savingAccountDto);

            return savingAccountDto;
        }

    }

    public BankAccount fromBankAcountDto(BankAccountDto bankAccountDto){
        BankAccount bankAccount=new BankAccount();
        BeanUtils.copyProperties(bankAccountDto,bankAccount);
        return bankAccount;
    }




}
