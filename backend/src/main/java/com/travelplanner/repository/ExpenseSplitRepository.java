package com.travelplanner.repository;

import com.travelplanner.entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    List<ExpenseSplit> findByExpenseId(Long expenseId);

    @Query("SELECT s FROM ExpenseSplit s WHERE s.expense.trip.id = :tripId")
    List<ExpenseSplit> findAllByTripId(@Param("tripId") Long tripId);
}
