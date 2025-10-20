package com.momentz.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private String userProfilePicture;
    private LocalDateTime createdAt;
}