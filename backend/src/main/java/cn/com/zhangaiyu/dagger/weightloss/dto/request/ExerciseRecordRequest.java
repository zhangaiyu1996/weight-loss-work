package cn.com.zhangaiyu.dagger.weightloss.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordRequest {

    @NotBlank(message = "Exercise type is required")
    private String exerciseType;

    @NotBlank(message = "Exercise name is required")
    private String exerciseName;

    private Integer duration;

    private BigDecimal caloriesBurned;

    private BigDecimal distance;

    private String intensity;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    private LocalTime recordTime;

    private String note;
}
