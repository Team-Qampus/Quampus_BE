package swyp.qampus.image.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String putFileToBucket(MultipartFile file,String type);
}
