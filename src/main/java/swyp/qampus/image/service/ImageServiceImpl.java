package swyp.qampus.image.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.exception.ImageErrorCode;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String BUCKET_NAME="quampus";
    private static final String DIRECTORY_OF_QUESTION="/question";
    private static final String DIRECTORY_OF_ANSWER="/answer";
    private final AmazonS3Client objectStorageClient;
    @Override
    public String putFileToBucket(MultipartFile file,String type) {

        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String FILE_DIRECTORY = "";
        String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename();

        //질문하기 디렉토리
        if(type.equals("QUESTION")){
            FILE_DIRECTORY=BUCKET_NAME+DIRECTORY_OF_QUESTION;
        //답변하기 디렉토리
        } else if (type.equals("ANSWER")) {
            FILE_DIRECTORY=BUCKET_NAME+DIRECTORY_OF_ANSWER;
        }

        //사진 업로드
        try {
            PutObjectRequest request=new PutObjectRequest(FILE_DIRECTORY,fileName,file.getInputStream(),objectMetadata);
            objectStorageClient.putObject(request);
        } catch (IOException e) {
            throw new RestApiException(ImageErrorCode.FAILED_UPLOAD);
        }
        return objectStorageClient.getUrl(BUCKET_NAME,fileName).toString();
    }
}
