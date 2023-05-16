package com.banking.bankingapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDto {
    private Long id;
    private String firstName;
    private String email;
    private String lastName;
    private String job;
    private Date creationDate;
}
