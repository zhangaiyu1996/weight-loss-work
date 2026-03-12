package cn.com.zhangaiyu.dagger.weightloss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String email;

    private String nickname;

    private String gender;

    private LocalDate birthDate;

    private BigDecimal height;

    private BigDecimal targetWeight;

    private String avatarUrl;
}
