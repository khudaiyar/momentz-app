package com.momentz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    private String caption;

    @NotBlank
    private String imageUrl;
}