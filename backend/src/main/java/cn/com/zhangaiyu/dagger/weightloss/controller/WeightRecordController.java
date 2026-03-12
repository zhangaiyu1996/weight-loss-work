package cn.com.zhangaiyu.dagger.weightloss.controller;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.WeightRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ApiResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.UserResponse;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.WeightRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.service.UserService;
import cn.com.zhangaiyu.dagger.weightloss.service.WeightRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/weight-records")
@RequiredArgsConstructor
public class WeightRecordController {

    private final WeightRecordService weightRecordService;
    private final UserService userService;

    @PostMapping
    public ApiResponse<WeightRecordResponse> createWeightRecord(
            Authentication authentication,
            @Valid @RequestBody WeightRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        WeightRecordResponse record = weightRecordService.createWeightRecord(user.getId(), request);
        return ApiResponse.success("Weight record created successfully", record);
    }

    @GetMapping
    public ApiResponse<List<WeightRecordResponse>> getAllWeightRecords(Authentication authentication) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<WeightRecordResponse> records = weightRecordService.getAllWeightRecords(user.getId());
        return ApiResponse.success(records);
    }

    @GetMapping("/trend")
    public ApiResponse<List<WeightRecordResponse>> getWeightTrend(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        List<WeightRecordResponse> records = weightRecordService.getWeightRecordsByDateRange(user.getId(), startDate, endDate);
        return ApiResponse.success(records);
    }

    @GetMapping("/latest")
    public ApiResponse<WeightRecordResponse> getLatestWeightRecord(Authentication authentication) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        WeightRecordResponse record = weightRecordService.getLatestWeightRecord(user.getId());
        return ApiResponse.success(record);
    }

    @GetMapping("/{id}")
    public ApiResponse<WeightRecordResponse> getWeightRecordById(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        WeightRecordResponse record = weightRecordService.getWeightRecordById(user.getId(), id);
        return ApiResponse.success(record);
    }

    @PutMapping("/{id}")
    public ApiResponse<WeightRecordResponse> updateWeightRecord(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody WeightRecordRequest request) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        WeightRecordResponse record = weightRecordService.updateWeightRecord(user.getId(), id, request);
        return ApiResponse.success("Weight record updated successfully", record);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteWeightRecord(
            Authentication authentication,
            @PathVariable Long id) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        weightRecordService.deleteWeightRecord(user.getId(), id);
        return ApiResponse.success("Weight record deleted successfully", null);
    }
}
