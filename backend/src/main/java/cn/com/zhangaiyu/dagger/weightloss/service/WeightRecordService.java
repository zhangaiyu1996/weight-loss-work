package cn.com.zhangaiyu.dagger.weightloss.service;

import cn.com.zhangaiyu.dagger.weightloss.dto.request.WeightRecordRequest;
import cn.com.zhangaiyu.dagger.weightloss.dto.response.WeightRecordResponse;
import cn.com.zhangaiyu.dagger.weightloss.entity.User;
import cn.com.zhangaiyu.dagger.weightloss.entity.WeightRecord;
import cn.com.zhangaiyu.dagger.weightloss.exception.ResourceNotFoundException;
import cn.com.zhangaiyu.dagger.weightloss.repository.UserRepository;
import cn.com.zhangaiyu.dagger.weightloss.repository.WeightRecordRepository;
import cn.com.zhangaiyu.dagger.weightloss.util.BmiCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;
    private final UserRepository userRepository;

    @Transactional
    public WeightRecordResponse createWeightRecord(Long userId, WeightRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if record already exists for this date
        weightRecordRepository.findByUserIdAndRecordDate(userId, request.getRecordDate())
                .ifPresent(record -> {
                    throw new IllegalArgumentException("Weight record already exists for date: " + request.getRecordDate());
                });

        WeightRecord record = new WeightRecord();
        record.setUserId(userId);
        record.setWeight(request.getWeight());
        record.setRecordDate(request.getRecordDate());
        record.setNote(request.getNote());

        // Calculate BMI if height is available
        if (user.getHeight() != null) {
            record.setBmi(BmiCalculator.calculateBmi(request.getWeight(), user.getHeight()));
        }

        WeightRecord savedRecord = weightRecordRepository.save(record);
        return mapToResponse(savedRecord);
    }

    public List<WeightRecordResponse> getAllWeightRecords(Long userId) {
        return weightRecordRepository.findByUserIdOrderByRecordDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<WeightRecordResponse> getWeightRecordsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return weightRecordRepository.findByUserIdAndRecordDateBetweenOrderByRecordDateAsc(userId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WeightRecordResponse getWeightRecordById(Long userId, Long recordId) {
        WeightRecord record = weightRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Weight record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Weight record does not belong to user");
        }

        return mapToResponse(record);
    }

    @Transactional
    public WeightRecordResponse updateWeightRecord(Long userId, Long recordId, WeightRecordRequest request) {
        WeightRecord record = weightRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Weight record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Weight record does not belong to user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        record.setWeight(request.getWeight());
        record.setRecordDate(request.getRecordDate());
        record.setNote(request.getNote());

        // Recalculate BMI
        if (user.getHeight() != null) {
            record.setBmi(BmiCalculator.calculateBmi(request.getWeight(), user.getHeight()));
        }

        WeightRecord updatedRecord = weightRecordRepository.save(record);
        return mapToResponse(updatedRecord);
    }

    @Transactional
    public void deleteWeightRecord(Long userId, Long recordId) {
        WeightRecord record = weightRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Weight record not found with id: " + recordId));

        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Weight record does not belong to user");
        }

        weightRecordRepository.delete(record);
    }

    public WeightRecordResponse getLatestWeightRecord(Long userId) {
        return weightRecordRepository.findFirstByUserIdOrderByRecordDateDesc(userId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    private WeightRecordResponse mapToResponse(WeightRecord record) {
        WeightRecordResponse response = new WeightRecordResponse();
        response.setId(record.getId());
        response.setUserId(record.getUserId());
        response.setWeight(record.getWeight());
        response.setBmi(record.getBmi());
        response.setRecordDate(record.getRecordDate());
        response.setNote(record.getNote());
        response.setCreatedAt(record.getCreatedAt());
        response.setUpdatedAt(record.getUpdatedAt());
        return response;
    }
}
