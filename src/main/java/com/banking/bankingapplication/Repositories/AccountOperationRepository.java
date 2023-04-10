package com.banking.bankingapplication.Repositories;

import com.banking.bankingapplication.Entities.AccountOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperations,Long> {
    public List<AccountOperations> findAccountOperationsByBankAccountId(String id);
    Page<AccountOperations> findAccountOperationsByBankAccountId(String id , Pageable p);


}
