package cn.com.zhangaiyu.dagger.weightloss.controller;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.ExerciseRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ApiResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ExerciseRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.UserResponse;
import cn.com.zhangaiyu.dagger.weightloss.service.ExerciseRecordService;
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
@RequestMapping("/exercise-records")
@RequiredArgsConstructor
public class ExerciseRecordController {

    private final ExerciseRecordService exerciseRecordService;
    private final UserService userService;

    @PostMapping
    public ApiResponse<ExerciseRecordResponse> createExerciseRecord(
            Authentication authentication,
            @Valid @RequestBody ExerciseRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        ExerciseRecordResponse record = exerciseRecordService.createExerciseRecord(user.getId(), request);
        return ApiResponse.success("Exercise record created successfully", record);
    }

    @GetMapping
    public ApiResponse<List<ExerciseRecordResponse>> getAllExerciseRecords(Authentication authentication) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<ExerciseRecordResponse> records = exerciseRecordService.getAllExerciseRecords(user.getId());
        return ApiResponse.success(records);
    }

    @GetMapping("/date/{date}")
    public ApiResponse<List<ExerciseRecordResponse>> getExerciseRecordsByDate(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<ExerciseRecordResponse> records = exerciseRecordService.getExerciseRecordsByDate(user.getId(), date);
        return ApiResponse.success(records);
    }

    @GetMapping("/statistics")
    public ApiResponse<BigDecimal> getDailyCaloriesBurned(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        BigDecimal totalCalories = exerciseRecordService.getTotalCaloriesBurnedByDate(user.getId(), date);
        return ApiResponse.success(totalCalories);
    }

    @GetMapping("/{id}")
    public ApiResponse<ExerciseRecordResponse> getExerciseRecordById(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        ExerciseRecordResponse record = exerciseRecordService.getExerciseRecordById(user.getId(), id);
        return ApiResponse.success(record);
    }

    @PutMapping("/{id}")
    public ApiResponse<ExerciseRecordResponse> updateExerciseRecord(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ExerciseRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        ExerciseRecordResponse record = exerciseRecordService.updateExerciseRecord(user.getId(), id, request);
        return ApiResponse.success("Exercise record updated successfully", record);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteExerciseRecord(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        exerciseRecordService.deleteExerciseRecord(user.getId(), id);
        return ApiResponse.success("Exercise record deleted successfully", null);
    }
}
