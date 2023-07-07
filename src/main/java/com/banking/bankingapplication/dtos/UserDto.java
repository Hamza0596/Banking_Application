package com.banking.bankingapplication.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String job;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;
    private String roles;
    private String[] authorities;
    private String profileImageUrl;


    private Date creationDate;
    private boolean isActive;
    private boolean isNotLocked;

    private Date lastLoginDate;
    private Date lastLoginDateDisplay;

}
