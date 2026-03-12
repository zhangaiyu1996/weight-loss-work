package cn.com.zhangaiyu.dagger.weightloss.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightRecordRequest {

    @NotNull(message = "Weight is required")
    private BigDecimal weight;

    @NotNull(message = "Record date is required")
    private LocalDate recordDate;

    private String note;
}
