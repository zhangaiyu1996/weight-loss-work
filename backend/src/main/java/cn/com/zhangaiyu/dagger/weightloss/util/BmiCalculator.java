package cn.com.zhangaiyu.dagger.weightloss.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BmiCalculator {

    public static BigDecimal calculateBmi(BigDecimal weight, BigDecimal height) {
        if (weight == null || height == null || height.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        // BMI = weight(kg) / (height(m))^2
        // height is in cm, convert to meters
        BigDecimal heightInMeters = height.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);

        return weight.divide(heightSquared, 2, RoundingMode.HALF_UP);
    }
}
