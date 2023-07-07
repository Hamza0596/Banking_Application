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

    public static UserDto fromCustomer(Users user){
        if(user !=null){
            UserDto userDto =new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }else{
            return null;

        }
    }

    public  static Users fromCustomerDto(UserDto userDto){
        Users user = new Users();
        BeanUtils.copyProperties(userDto, user);

        return user;
    }

    public List<UserDto>  fromCustomerListToCustomerListDto(List<Users> users){
        return users.stream().map(customer -> {
            UserDto userDto = new UserDto();
            userDto.setEmail(customer.getEmail());
            userDto.setFirstName(customer.getFirstName());
            userDto.setId(customer.getId());
            userDto.setLastName(customer.getLastName());
            userDto.setJob(customer.getJob());
            userDto.setCreationDate(customer.getCreationDate());
            userDto.setPassword(customer.getPassword());
            userDto.setRoles(customer.getRoles());
            userDto.setUserName(customer.getUserName());

            return userDto;
        }).collect(Collectors.toList());

    }

    public  Page<UserDto> fromCustomerListToCustomerPageDto(Page<Users> customers){
        return  customers.map(customer -> {
            UserDto userDto = new UserDto();
            userDto.setEmail(customer.getEmail());
            userDto.setFirstName(customer.getFirstName());
            userDto.setId(customer.getId());
            userDto.setLastName(customer.getLastName());
            userDto.setJob(customer.getJob());
            userDto.setCreationDate(customer.getCreationDate());
            userDto.setPassword(customer.getPassword());
            userDto.setRoles(customer.getRoles());
            userDto.setUserName(customer.getUserName());
            return userDto;
        });

    }


    public List<Users>  fromCustomerDtoListToCustomerList(List<UserDto> customersDto){
         return customersDto.stream().map(customerDto -> {
            Users user = new Users();
            user.setEmail(customerDto.getEmail());
            user.setFirstName(customerDto.getFirstName());
            user.setId(customerDto.getId());
             user.setLastName(customerDto.getLastName());
             user.setJob(customerDto.getJob());
             user.setCreationDate(customerDto.getCreationDate());
             user.setPassword(customerDto.getPassword());
            return user;
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
