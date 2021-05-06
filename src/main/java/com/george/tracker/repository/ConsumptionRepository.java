package com.george.tracker.repository;

import com.george.tracker.model.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    Optional<Consumption> findByTimestamp(LocalDate timestamp);
}
