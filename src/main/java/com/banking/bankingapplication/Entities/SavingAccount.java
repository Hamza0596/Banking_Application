package com.banking.bankingapplication.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("SAVE")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SavingAccount extends BankAccount{
    private double intersetRating;
}
