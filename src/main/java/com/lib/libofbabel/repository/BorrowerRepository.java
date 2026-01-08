package com.lib.libofbabel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lib.libofbabel.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    boolean existsByEmail(String email);
    
}
