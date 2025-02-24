package swyp.qampus.image.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<String> putFileToBucket(List<MultipartFile> files, String type);
}
