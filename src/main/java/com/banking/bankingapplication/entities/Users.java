package com.banking.bankingapplication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users   {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String job;
    private Date creationDate;
    private String password;
    private String profileImageUrl;
    private Date LastLoginDate;
    private Date LastLoginDateDisplay;
    private String roles;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    @OneToMany(mappedBy ="user")
    @JsonIgnore()
    private List<BankAccount> bankAccount;

}
