package com.foodlog.timeline.repository;

import com.foodlog.timeline.entity.MealLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


/**
 * Spring Data JPA repository for the MealLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealLogRepository extends JpaRepository<MealLog,Long> {
    List<MealLog> findByMealDateTimeBetweenOrderByMealDateTimeDesc(Instant today, Instant tomorrow);
}
