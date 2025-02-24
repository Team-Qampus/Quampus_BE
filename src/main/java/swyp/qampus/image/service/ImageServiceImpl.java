package swyp.qampus.image.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.exception.ImageErrorCode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String BUCKET_NAME="quampus";
    private static final String DIRECTORY_OF_QUESTION="/question";
    private static final String DIRECTORY_OF_ANSWER="/answer";
    private final AmazonS3Service objectStorageClient;
    @Override
    public List<String> putFileToBucket(List<MultipartFile> files, String type) {
        //질문하기 디렉토리
        String FILE_DIRECTORY="";
        List<String> urls=new ArrayList<>();
        //TODO:testCode작성
        if(type.equals("QUESTION")){
            FILE_DIRECTORY=BUCKET_NAME+DIRECTORY_OF_QUESTION;
            //답변하기 디렉토리
        } else if (type.equals("ANSWER")) {
            FILE_DIRECTORY=BUCKET_NAME+DIRECTORY_OF_ANSWER;
        }
        for (MultipartFile file:files){
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename();
            try {
                //사진 업로드 및 url저장
                PutObjectRequest request=new PutObjectRequest(FILE_DIRECTORY,fileName,file.getInputStream(),objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                objectStorageClient.putObject(request);
                String string = "https://kr.object.ncloudstorage.com"+"/"
                        +FILE_DIRECTORY+"/"+fileName;
                urls.add(string);


            }catch (IOException e){
                throw new RestApiException(ImageErrorCode.FAILED_UPLOAD);
            }

        }
        return urls;
    }
}
