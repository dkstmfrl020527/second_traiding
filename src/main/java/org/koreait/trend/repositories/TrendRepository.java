package org.koreait.trend.repositories;

import org.koreait.trend.entities.Trend;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrendRepository extends ListCrudRepository<Trend, Long> {
    @Query("SELECT * FROM TREND WHERE category=:category ORDER BY createdAt DESC LIMIT 1")
    Optional<Trend> getLatest(@Param("category") String category);

    @Query("SELECT * FROM TREND WHERE category = :category " +
            "AND createdAt >= :startDateTime AND createdAt < :endDateTime " +  // ← startDateTime, endDateTime 사용
            "ORDER BY createdAt DESC")
    List<Trend> findByCategoryAndDateRange(
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,    // ← startDate, endDate 파라미터
            @Param("endDate") LocalDate endDate
    );
}
