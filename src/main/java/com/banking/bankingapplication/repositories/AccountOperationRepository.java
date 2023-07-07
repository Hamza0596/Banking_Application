package com.banking.bankingapplication.repositories;

import com.banking.bankingapplication.entities.AccountOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperations,Long> {
    public List<AccountOperations> findAccountOperationsByBankAccountIdOrderByOperationDateDesc(String id);
    Page<AccountOperations> findAccountOperationsByBankAccountIdOrderByOperationDateDesc(String id , Pageable p);


}
