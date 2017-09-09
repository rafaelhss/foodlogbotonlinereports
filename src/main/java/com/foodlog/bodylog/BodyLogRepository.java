package com.foodlog.bodylog;

import com.foodlog.entity.BodyLog;
import com.foodlog.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the BodyLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyLogRepository extends JpaRepository<BodyLog,Long> {

    List<BodyLog> findByUser(User user);

    List<BodyLog> findByUserOrderByBodyLogDatetime(User user);
}
