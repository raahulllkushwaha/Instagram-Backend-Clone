package com.rahul.instagram.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 150)
    private String bio;

    private String profilePicUrl;
}
