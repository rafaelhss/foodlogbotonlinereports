package com.foodlog.timeline.repository;

import com.foodlog.entity.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


/**
 * Spring Data JPA repository for the MealLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealLogRepository extends JpaRepository<MealLog,Long> {
    List<MealLog> findByMealDateTimeBetweenOrderByMealDateTimeDesc(Instant today, Instant tomorrow);
    List<MealLog> findByMealDateTimeAfterOrderByMealDateTimeDesc(Instant today);
}
