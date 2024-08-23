package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ContactRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, Long> {}
