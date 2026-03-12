package cn.com.zhangaiyu.dagger.weightloss.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordResponse {

    private Long id;

    private Long userId;

    private String exerciseType;

    private String exerciseName;

    private Integer duration;

    private BigDecimal caloriesBurned;

    private BigDecimal distance;

    private String intensity;

    private LocalDate recordDate;

    private LocalTime recordTime;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
