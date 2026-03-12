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
public class DietRecordResponse {

    private Long id;

    private Long userId;

    private String mealType;

    private String foodName;

    private BigDecimal calories;

    private BigDecimal protein;

    private BigDecimal carbohydrate;

    private BigDecimal fat;

    private String portion;

    private LocalDate recordDate;

    private LocalTime recordTime;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
