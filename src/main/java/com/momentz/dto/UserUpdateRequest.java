package com.momentz.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String bio;
    private String profilePicture;
    private String website;
}