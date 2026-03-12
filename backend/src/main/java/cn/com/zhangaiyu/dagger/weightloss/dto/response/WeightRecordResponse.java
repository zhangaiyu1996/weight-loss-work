package cn.com.zhangaiyu.dagger.weightloss.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordResponse {

    private Long id;

    private Long userId;

    private BigDecimal weight;

    private BigDecimal bmi;

    private LocalDate recordDate;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
