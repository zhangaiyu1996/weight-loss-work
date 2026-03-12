package cn.com.zhangaiyu.dagger.weightloss.repository;

import cn.com.zhangaiyu.dagger.weightloss.entity.DietRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DietRecordRepository extends JpaRepository<DietRecord, Long> {

    List<DietRecord> findByUserIdOrderByRecordDateDescRecordTimeDesc(Long userId);

    List<DietRecord> findByUserIdAndRecordDateOrderByRecordTimeAsc(Long userId, LocalDate recordDate);

    List<DietRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateDescRecordTimeDesc(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(d.calories) FROM DietRecord d WHERE d.userId = :userId AND d.recordDate = :recordDate")
    BigDecimal sumCaloriesByUserIdAndRecordDate(@Param("userId") Long userId, @Param("recordDate") LocalDate recordDate);
}
