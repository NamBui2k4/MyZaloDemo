package org.example.repository;

import org.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    @Query("""
        SELECT DISTINCT c
        FROM Contact c
        WHERE c.user.userId = :userId
    """)
    List<Contact> findContactById(@Param("userId") Integer userId);

}
