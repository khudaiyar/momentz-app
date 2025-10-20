package com.momentz.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profilePicture;
    private String website;
    private Long postsCount;
    private Long followersCount;
    private Long followingCount;
    private Boolean isPrivate;
    private Boolean isVerified;
}