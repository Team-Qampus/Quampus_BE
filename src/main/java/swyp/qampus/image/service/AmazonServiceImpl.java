package swyp.qampus.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;

@RequiredArgsConstructor
@Service
public class AmazonServiceImpl implements AmazonS3Service{
    private final AmazonS3Client amazonS3Client;

    @Override
    public void putObject(PutObjectRequest request) {
        amazonS3Client.putObject(request);
    }

    @Override
    public URL getUrl(String bucketName, String fileName) {
        return amazonS3Client.getUrl(bucketName,fileName);
    }
}
