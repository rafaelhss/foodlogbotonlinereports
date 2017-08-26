package com.foodlog.timeline.repository;

import com.foodlog.entity.ScheduledMeal;
import com.foodlog.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ScheduledMeal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledMealRepository extends JpaRepository<ScheduledMeal,Long> {

    List<ScheduledMeal> findByUserOrderByTargetTime(User currentUser);

    List<ScheduledMeal> findByUserOrderByTargetTimeDesc(User currentUser);
}
