package com.momentz.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
    private String caption;
    private String imageUrl;
    private String username;
    private String userProfilePicture;
    private Integer likesCount;
    private Integer commentsCount;
    private LocalDateTime createdAt;
    private Boolean likedByCurrentUser;
}