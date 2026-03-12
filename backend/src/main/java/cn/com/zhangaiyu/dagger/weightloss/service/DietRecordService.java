package cn.com.zhangaiyu.dagger.weightloss.service;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.DietRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.DietRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.entity.DietRecord;
import cn.com.zhangaiyu.dagger.weightloss.exception.ResourceNotFoundException;
import cn.com.zhangaiyu.dagger.weightloss.repository.DietRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DietRecordService {

    private final DietRecordRepository dietRecordRepository;

    @Transactional
    public DietRecordResponse createDietRecord(Long userId, DietRecordRequest request) {
        DietRecord record = new DietRecord();
        record.setUserId(userId);
        record.setMealType(request.getMealType());
        record.setFoodName(request.getFoodName());
        record.setCalories(request.getCalories());
        record.setProtein(request.getProtein());
        record.setCarbohydrate(request.getCarbohydrate());
        record.setFat(request.getFat());
        record.setPortion(request.getPortion());
        record.setRecordDate(request.getRecordDate());
        record.setRecordTime(request.getRecordTime());
        record.setNote(request.getNote());

        DietRecord savedRecord = dietRecordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    public List<DietRecordResponse> getAllDietRecords(Long userId) {
        return dietRecordRepository.findByUserIdOrderByRecordDateDescRecordTimeDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DietRecordResponse> getDietRecordsByDate(Long userId, LocalDate recordDate) {
        return dietRecordRepository.findByUserIdAndRecordDateOrderByRecordTimeAsc(userId, recordDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DietRecordResponse> getDietRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return dietRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateDescRecordTimeDesc(userId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DietRecordResponse getDietRecordById(Long userId, Long recordId) {
        DietRecord record = dietRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Diet record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Diet record does not belong to user");
        }

        return mapToResponse(record);
    }

    @Transactional
    public DietRecordResponse updateDietRecord(Long userId, Long recordId, DietRecordRequest request) {
        DietRecord record = dietRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Diet record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Diet record does not belong to user");
        }

        record.setMealType(request.getMealType());
        record.setFoodName(request.getFoodName());
        record.setCalories(request.getCalories());
        record.setProtein(request.getProtein());
        record.setCarbohydrate(request.getCarbohydrate());
        record.setFat(request.getFat());
        record.setPortion(request.getPortion());
        record.setRecordDate(request.getRecordDate());
        record.setRecordTime(request.getRecordTime());
        record.setNote(request.getNote());

        DietRecord updatedRecord = dietRecordRepository.save(record);
        return mapToResponse(updatedRecord);
    }

    @Transactional
    public void deleteDietRecord(Long userId, Long recordId) {
        DietRecord record = dietRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Diet record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Diet record does not belong to user");
        }

        dietRecordRepository.delete(record);
    }

    public BigDecimal getTotalCaloriesByDate(Long userId, LocalDate recordDate) {
        BigDecimal total = dietRecordRepository.sumCaloriesByUserIdAndRecordDate(userId, recordDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    private DietRecordResponse mapToResponse(DietRecord record) {
        DietRecordResponse response = new DietRecordResponse();
        response.setId(record.getId());
        response.setUserId(record.getUserId());
        response.setMealType(record.getMealType());
        response.setFoodName(record.getFoodName());
        response.setCalories(record.getCalories());
        response.setProtein(record.getProtein());
        response.setCarbohydrate(record.getCarbohydrate());
        response.setFat(record.getFat());
        response.setPortion(record.getPortion());
        response.setRecordDate(record.getRecordDate());
        response.setRecordTime(record.getRecordTime());
        response.setNote(record.getNote());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
