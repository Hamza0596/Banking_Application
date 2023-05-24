package com.banking.bankingapplication.repositories;

import com.banking.bankingapplication.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

  /* @Query(value ="select c from Users c where lower(c.firstName) LIKE CONCAT ('%', lower(:query),'%')"+"OR lower(c.email) LIKE CONCAT ('%', lower(:query),'%')"+"OR lower(c.job) LIKE CONCAT ('%', lower(:query),'%')")
    Page<Users> findByQuery(String query, Pageable p);*/


    @Query(value = "SELECT c FROM Users c WHERE " +
            "LOWER(c.firstName) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR LOWER(c.email) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR LOWER(c.job) LIKE CONCAT('%', LOWER(:query), '%') " +
            "OR CAST(c.creationDate AS string) LIKE CONCAT('%', LOWER(:query), '%')")
    Page<Users> findByQuery(@Param("query") String query, Pageable p);


    Users findByEmail(String email);
    Users findByUserName(String email);



}
