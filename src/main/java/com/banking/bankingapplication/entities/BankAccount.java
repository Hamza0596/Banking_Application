package com.banking.bankingapplication.entities;

import com.banking.bankingapplication.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Inheritance(strategy =InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE" )
public class BankAccount implements Serializable {
    @Id
    private String id;
    private double balnce;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Users user;
    @OneToMany(mappedBy = "bankAccount" , cascade= CascadeType.ALL)
    private List<AccountOperations> operations;
}
