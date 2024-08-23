package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PrayerRequestForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrayerRequestForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrayerRequestFormRepository extends JpaRepository<PrayerRequestForm, Long> {}
