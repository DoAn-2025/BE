package com.doan2025.webtoeic.controller;

import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.dto.response.ApiResponse;
import com.doan2025.webtoeic.dto.response.NotiResponse;
import com.doan2025.webtoeic.service.NotiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/noti")
@Tag(name = "Notification Controller", description = "APIs for managing user notifications")
public class NotiController {
    private final NotiService notiService;

    @Operation(
            summary = "Count unread notifications",
            description = "Returns the count of unread notifications for the current authenticated user. " +
                    "Requires authentication via JWT token in the Authorization header."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved notification count",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/count")
    public ApiResponse<Long> countNoti(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS,
                ResponseObject.NOTIFICATION,
                notiService.countNoti(request));
    }

    @Operation(
            summary = "Get paginated list of notifications",
            description = "Retrieves a paginated list of notifications for the current authenticated user. " +
                    "Supports pagination parameters (page, size, sort). " +
                    "Returns notifications with details including title, content, type, creation date, and read status."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved notifications list",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/list")
    public ApiResponse<Page<?>> filter(
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(description = "Pagination parameters (page, size, sort). Example: page=0&size=10&sort=createdAt,desc")
            Pageable pageable
    ) {
        return ApiResponse.of(ResponseCode.GET_SUCCESS,
                ResponseObject.NOTIFICATION,
                notiService.listNoti(request, pageable));
    }

    @Operation(
            summary = "Mark notifications as read",
            description = "Updates the read status of one or more notifications to 'read' for the current authenticated user. " +
                    "Accepts a list of notification IDs in the request body. " +
                    "Only notifications belonging to the authenticated user can be marked as read."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated notification status",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User or notification not found"
            )
    })
    @PostMapping("/update")
    public ApiResponse<Void> update(
            @Parameter(hidden = true) HttpServletRequest request,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of notification IDs to mark as read",
                    required = true
            )
            @RequestBody List<Long> notiIds
    ) {
        notiService.updateNoti(request, notiIds);
        return ApiResponse.of(ResponseCode.GET_SUCCESS,
                ResponseObject.NOTIFICATION,
                null);
    }
}
