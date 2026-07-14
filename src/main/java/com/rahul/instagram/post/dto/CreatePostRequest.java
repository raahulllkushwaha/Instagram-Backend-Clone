package com.rahul.instagram.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class CreatePostRequest {

    @NotBlank
    @Size(max = 2200)
    private String caption;

    private String mediaUrl;

    private String mediaPublicId;   // To delete Cloudinary data
}
