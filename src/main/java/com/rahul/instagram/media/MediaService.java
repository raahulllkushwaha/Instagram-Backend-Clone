package com.rahul.instagram.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;

    public record UploadResult(String url, String publicId) {}

    public UploadResult uploadFile(MultipartFile file){
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")  // auto will handle both img and video
            );
            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

             return new UploadResult(url, publicId);

        } catch (IOException e){
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }


    public void deleteFile(String publicId){
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
        catch (IOException e){
            throw new RuntimeException("File delete failed: " + e.getMessage());
        }
    }
}
