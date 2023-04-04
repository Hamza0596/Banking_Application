package com.banking.bankingapplication.Repositories;

import com.banking.bankingapplication.Entities.AccountOperations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperations,Long> {
}
