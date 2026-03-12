package cn.com.zhangaiyu.dagger.weightloss.repository;

import cn.com.zhangaiyu.dagger.weightloss.entity.ExerciseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, Long> {

    List<ExerciseRecord> findByUserIdOrderByRecordDateDescRecordTimeDesc(Long userId);

    List<ExerciseRecord> findByUserIdAndRecordDateOrderByRecordTimeAsc(Long userId, LocalDate recordDate);

    List<ExerciseRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateDescRecordTimeDesc(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(e.caloriesBurned) FROM ExerciseRecord e WHERE e.userId = :userId AND e.recordDate = :recordDate")
    BigDecimal sumCaloriesBurnedByUserIdAndRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);
}
