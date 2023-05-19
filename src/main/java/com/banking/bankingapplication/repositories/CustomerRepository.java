package com.banking.bankingapplication.repositories;

import com.banking.bankingapplication.dtos.CustomerDto;
import com.banking.bankingapplication.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

  /* @Query(value ="select c from Customer c where lower(c.firstName) LIKE CONCAT ('%', lower(:query),'%')"+"OR lower(c.email) LIKE CONCAT ('%', lower(:query),'%')"+"OR lower(c.job) LIKE CONCAT ('%', lower(:query),'%')")
    Page<Customer> findByQuery(String query, Pageable p);*/


    @Query(value = "SELECT c FROM Customer c WHERE " +
            "LOWER(c.firstName) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR LOWER(c.email) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR LOWER(c.job) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR CAST(c.creationDate AS string) LIKE CONCAT('%', LOWER(:query), '%')")
    Page<Customer> findByQuery(@Param("query") String query, Pageable p);


    Optional<Customer> findByEmail(String email);

    Boolean existsByEmail(String email);
}
