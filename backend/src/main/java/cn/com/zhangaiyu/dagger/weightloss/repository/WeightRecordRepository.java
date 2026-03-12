package cn.com.zhangaiyu.dagger.weightloss.repository;

import cn.com.zhangaiyu.dagger.weightloss.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

    List<WeightRecord> findByUserIdOrderByRecordDateDesc(Long userId);

    List<WeightRecord> findByUserIdAndRecordDateBetweenOrderByRecordDateAsc(Long userId, LocalDate startDate, LocalDate endDate);

    Optional<WeightRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);

    Optional<WeightRecord> findFirstByUserIdOrderByRecordDateDesc(Long userId);
}
