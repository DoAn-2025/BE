package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.SearchRangeTopicAndScoreScaleDto;
import com.doan2025.webtoeic.dto.request.ScoreScaleRequest;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.service.ScoreScaleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/score-scale")
public class ScoreScaleController {
    private final ScoreScaleService scoreScaleService;

    @GetMapping()
    public ApiResponse<?> getRangeTopic(HttpServletRequest request, Integer id) {
        return ApiResponse.of(
                ResponseCode.GET_SUCCESS,
                ResponseObject.RANGE_TOPIC,
                scoreScaleService.getScoreScale(request, id)
        );
    }

    @PostMapping("/filter")
    public ApiResponse<?> filter(HttpServletRequest request, SearchRangeTopicAndScoreScaleDto dto, Pageable pageable) {
        return ApiResponse.of(
                ResponseCode.GET_SUCCESS,
                ResponseObject.RANGE_TOPIC,
                scoreScaleService.getScoreScales(request, dto, pageable)
        );
    }

    @PostMapping("/create")
    public ApiResponse<?> createRangeTopic(HttpServletRequest request, ScoreScaleRequest dto) {
        return ApiResponse.of(
                ResponseCode.CREATE_SUCCESS,
                ResponseObject.RANGE_TOPIC,
                scoreScaleService.createScoreScale(request, dto)
        );
    }

    @PostMapping("/update")
    public ApiResponse<?> updateRangeTopic(HttpServletRequest request, ScoreScaleRequest dto) {
        return ApiResponse.of(
                ResponseCode.UPDATE_SUCCESS,
                ResponseObject.RANGE_TOPIC,
                scoreScaleService.updateScoreScale(dto, request)
        );
    }
}
