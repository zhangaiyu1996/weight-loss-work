package cn.com.zhangaiyu.dagger.weightloss.controller;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.DietRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ApiResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.DietRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.UserResponse;
import cn.com.zhangaiyu.dagger.weightloss.service.DietRecordService;
import cn.com.zhangaiyu.dagger.weightloss.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diet-records")
@RequiredArgsConstructor
public class DietRecordController {

    private final DietRecordService dietRecordService;
    private final UserService userService;

    @PostMapping
    public ApiResponse<DietRecordResponse> createDietRecord(
            Authentication authentication,
            @Valid @RequestBody DietRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        DietRecordResponse record = dietRecordService.createDietRecord(user.getId(), request);
        return ApiResponse.success("Diet record created successfully", record);
    }

    @GetMapping
    public ApiResponse<List<DietRecordResponse>> getAllDietRecords(Authentication authentication) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<DietRecordResponse> records = dietRecordService.getAllDietRecords(user.getId());
        return ApiResponse.success(records);
    }

    @GetMapping("/date/{date}")
    public ApiResponse<List<DietRecordResponse>> getDietRecordsByDate(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<DietRecordResponse> records = dietRecordService.getDietRecordsByDate(user.getId(), date);
        return ApiResponse.success(records);
    }

    @GetMapping("/statistics")
    public ApiResponse<BigDecimal> getDailyCalories(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        BigDecimal totalCalories = dietRecordService.getTotalCaloriesByDate(user.getId(), date);
        return ApiResponse.success(totalCalories);
    }

    @GetMapping("/{id}")
    public ApiResponse<DietRecordResponse> getDietRecordById(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        DietRecordResponse record = dietRecordService.getDietRecordById(user.getId(), id);
        return ApiResponse.success(record);
    }

    @PutMapping("/{id}")
    public ApiResponse<DietRecordResponse> updateDietRecord(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody DietRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        DietRecordResponse record = dietRecordService.updateDietRecord(user.getId(), id, request);
        return ApiResponse.success("Diet record updated successfully", record);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDietRecord(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        dietRecordService.deleteDietRecord(user.getId(), id);
        return ApiResponse.success("Diet record deleted successfully", null);
    }
}
