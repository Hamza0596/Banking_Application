package com.banking.bankingapplication.mappers;

import com.banking.bankingapplication.dtos.*;
import com.banking.bankingapplication.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingMapper {

    public CustomerDto fromCustomer(Customer customer){
        if(customer!=null){
            CustomerDto customerDto=new CustomerDto();
            BeanUtils.copyProperties(customer,customerDto);
            return customerDto;
        }else{
            return null;

        }
    }

    public Customer fromCustomerDto(CustomerDto customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDto,customer);

        return customer;
    }

    public List<CustomerDto>  fromCustomerListToCustomerListDto(List<Customer> customers){
        return customers.stream().map(customer -> {
            CustomerDto customerDto= new CustomerDto();
            customerDto.setEmail(customer.getEmail());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setId(customer.getId());
            customerDto.setLastName(customer.getLastName());
            customerDto.setJob(customer.getJob());
            customerDto.setCreationDate(customer.getCreationDate());
            customerDto.setPassword(customer.getPassword());


            return customerDto;
        }).collect(Collectors.toList());

    }

    public Page<CustomerDto> fromCustomerListToCustomerPageDto(Page<Customer> customers){
        return  customers.map(customer -> {
            CustomerDto customerDto= new CustomerDto();
            customerDto.setEmail(customer.getEmail());
            customerDto.setFirstName(customer.getFirstName());
            customerDto.setId(customer.getId());
            customerDto.setLastName(customer.getLastName());
            customerDto.setJob(customer.getJob());
            customerDto.setCreationDate(customer.getCreationDate());
            customerDto.setPassword(customer.getPassword());
            return customerDto;
        });

    }


    public List<Customer>  fromCustomerDtoListToCustomerList(List<CustomerDto> customersDto){
         return customersDto.stream().map(customerDto -> {
            Customer customer= new Customer();
            customer.setEmail(customerDto.getEmail());
            customer.setFirstName(customerDto.getFirstName());
            customer.setId(customerDto.getId());
             customer.setLastName(customerDto.getLastName());
             customer.setJob(customerDto.getJob());
             customer.setCreationDate(customerDto.getCreationDate());
             customer.setPassword(customerDto.getPassword());
            return customer;
        }).collect(Collectors.toList());
    }






    public List<BankAccountDto>  fromBankAccountListToBankAccountDto(List<BankAccount> bankAccouts){

       return bankAccouts.stream().map(bankAcount -> {

            if(bankAcount instanceof CurrentAccount){
              CurrentAcountDto currentAcountDto= new CurrentAcountDto();
              BeanUtils.copyProperties((CurrentAccount) bankAcount,currentAcountDto);
                currentAcountDto.setType(((CurrentAccount) bankAcount).getClass().getSimpleName());
                return currentAcountDto;

            }
            else{
                SavingAccountDto savingAccountDto= new SavingAccountDto();
                BeanUtils.copyProperties((SavingAccount) bankAcount,savingAccountDto);
                savingAccountDto.setType(((SavingAccount) bankAcount).getClass().getSimpleName());

                return savingAccountDto;
            }

        }).collect(Collectors.toList());
    }



    public BankAccountDto fromBankAccount(BankAccount bankAccount){

        if(bankAccount instanceof CurrentAccount){
            CurrentAcountDto currentAcountDto=new CurrentAcountDto();
            BeanUtils.copyProperties((CurrentAccount) bankAccount,currentAcountDto);
            currentAcountDto.setType(((CurrentAccount) bankAccount).getClass().getSimpleName());

            return currentAcountDto;
        }else {
            SavingAccountDto savingAccountDto=new SavingAccountDto();
            BeanUtils.copyProperties((SavingAccount)bankAccount,savingAccountDto);
            savingAccountDto.setType(((SavingAccount) bankAccount).getClass().getSimpleName());

            return savingAccountDto;
        }

    }

    public BankAccount fromBankAcountDto(BankAccountDto bankAccountDto){
        if(bankAccountDto instanceof CurrentAcountDto){
         CurrentAccount  currentAccount =new CurrentAccount();
        BeanUtils.copyProperties((CurrentAcountDto)bankAccountDto,currentAccount);
           return currentAccount;
        }
        else {
            SavingAccount savingAccount= new SavingAccount();
            BeanUtils.copyProperties((SavingAccountDto) bankAccountDto , savingAccount);
            return savingAccount;

        }
    }

    public List<AccountOperationDto>  fromAccountOperationListToAccountOperationDtoList(List<AccountOperations> accountOperations){

       return accountOperations.stream().map(operation -> {
            AccountOperationDto accountOperationDto= new AccountOperationDto();

                BeanUtils.copyProperties(operation,accountOperationDto);

                return accountOperationDto;
        }).collect(Collectors.toList());
    }





}
