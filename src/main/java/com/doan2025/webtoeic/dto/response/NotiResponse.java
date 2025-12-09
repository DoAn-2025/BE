package com.doan2025.webtoeic.dto.response;

import com.doan2025.webtoeic.constants.enums.ENotiType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Notification response object containing notification details")
public class NotiResponse {
    @Schema(description = "Notification ID", example = "1")
    private Long id;

    @Schema(description = "Notification title", example = "New Course Available")
    private String title;

    @Schema(description = "Notification content/message", example = "A new TOEIC course has been added to the platform")
    private String content;

    @Schema(description = "Related object ID (e.g., course ID, class ID, quiz ID)", example = "5")
    private Long objectId;

    @Schema(description = "Type of notification", example = "NEW_COURSE")
    private ENotiType notiType;

    @Schema(description = "Notification creation timestamp", example = "2025-12-09T10:30:00.000Z")
    private Date createdAt;

    @Schema(description = "Whether the notification has been read", example = "false")
    private Boolean isRead;


}
