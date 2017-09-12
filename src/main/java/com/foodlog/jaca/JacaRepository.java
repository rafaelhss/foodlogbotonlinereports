package com.foodlog.jaca;

import com.foodlog.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Jaca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JacaRepository extends JpaRepository<Jaca,Long> {

    List<Jaca> findTop30ByUserOrderByJacaDateTime(User user);
    
}
