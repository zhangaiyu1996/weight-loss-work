package cn.com.zhangaiyu.dagger.weightloss.service;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.ExerciseRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.ExerciseRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.entity.ExerciseRecord;
import cn.com.zhangaiyu.dagger.weightloss.exception.ResourceNotFoundException;
import cn.com.zhangaiyu.dagger.weightloss.repository.ExerciseRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {

    private final ExerciseRecordRepository exerciseRecordRepository;

    @Transactional
    public ExerciseRecordResponse createExerciseRecord(Long userId, ExerciseRecordRequest request) {
        ExerciseRecord record = new ExerciseRecord();
        record.setUserId(userId);
        record.setExerciseType(request.getExerciseType());
        record.setExerciseName(request.getExerciseName());
        record.setDuration(request.getDuration());
        record.setCaloriesBurned(request.getCaloriesBurned());
        record.setDistance(request.getDistance());
        record.setIntensity(request.getIntensity());
        record.setRecordDate(request.getRecordDate());
        record.setRecordTime(request.getRecordTime());
        record.setNote(request.getNote());

        ExerciseRecord savedRecord = exerciseRecordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    public List<ExerciseRecordResponse> getAllExerciseRecords(Long userId) {
        return exerciseRecordRepository.findByUserIdOrderByRecordDateDescRecordTimeDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ExerciseRecordResponse> getExerciseRecordsByDate(Long userId, LocalDate recordDate) {
        return exerciseRecordRepository.findByUserIdAndRecordDateOrderByRecordTimeAsc(userId, recordDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ExerciseRecordResponse> getExerciseRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return exerciseRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateDescRecordTimeDesc(userId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ExerciseRecordResponse getExerciseRecordById(Long userId, Long recordId) {
        ExerciseRecord record = exerciseRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Exercise record does not belong to user");
        }

        return mapToResponse(record);
    }

    @Transactional
    public ExerciseRecordResponse updateExerciseRecord(Long userId, Long recordId, ExerciseRecordRequest request) {
        ExerciseRecord record = exerciseRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Exercise record does not belong to user");
        }

        record.setExerciseType(request.getExerciseType());
        record.setExerciseName(request.getExerciseName());
        record.setDuration(request.getDuration());
        record.setCaloriesBurned(request.getCaloriesBurned());
        record.setDistance(request.getDistance());
        record.setIntensity(request.getIntensity());
        record.setRecordDate(request.getRecordDate());
        record.setRecordTime(request.getRecordTime());
        record.setNote(request.getNote());

        ExerciseRecord updatedRecord = exerciseRecordRepository.save(record);
        return mapToResponse(updatedRecord);
    }

    @Transactional
    public void deleteExerciseRecord(Long userId, Long recordId) {
        ExerciseRecord record = exerciseRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Exercise record does not belong to user");
        }

        exerciseRecordRepository.delete(record);
    }

    public BigDecimal getTotalCaloriesBurnedByDate(Long userId, LocalDate recordDate) {
        BigDecimal total = exerciseRecordRepository.sumCaloriesBurnedByUserIdAndRecordDate(userId, recordDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    private ExerciseRecordResponse mapToResponse(ExerciseRecord record) {
        ExerciseRecordResponse response = new ExerciseRecordResponse();
        response.setId(record.getId());
        response.setUserId(record.getUserId());
        response.setExerciseType(record.getExerciseType());
        response.setExerciseName(record.getExerciseName());
        response.setDuration(record.getDuration());
        response.setCaloriesBurned(record.getCaloriesBurned());
        response.setDistance(record.getDistance());
        response.setIntensity(record.getIntensity());
        response.setRecordDate(record.getRecordDate());
        response.setRecordTime(record.getRecordTime());
        response.setNote(record.getNote());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
