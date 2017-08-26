package com.foodlog.timeline.repository;

import com.foodlog.entity.MealLog;
import com.foodlog.entity.user.User;
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

    List<MealLog> findByUserAndMealDateTimeBetweenOrderByMealDateTimeDesc(User currentUser, Instant yesterday, Instant tomorrow);

    List<MealLog> findByUserAndMealDateTimeAfterOrderByMealDateTimeDesc(User currentUser, Instant minus);
}
