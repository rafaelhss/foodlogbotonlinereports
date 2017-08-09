package com.foodlog.timeline.repository;

import com.foodlog.timeline.entity.ScheduledMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduledMeal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledMealRepository extends JpaRepository<ScheduledMeal,Long> {
    List<ScheduledMeal> findByName(String name);
    List<ScheduledMeal> findByOrderByTargetTime();
    List<ScheduledMeal> findByOrderByTargetTimeDesc();
}
