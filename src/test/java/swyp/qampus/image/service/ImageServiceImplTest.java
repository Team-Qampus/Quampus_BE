package swyp.qampus.image.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.exception.RestApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class ImageServiceImplTest {

    @Autowired
    private ImageService imageService;

    @MockitoBean
    private AmazonS3Service amazonS3Service;

    @Mock
    private MultipartFile multipartFile;
    //버킷 이름
    private static final String FILE_BUCKET_NAME="quampus";

    //파일 이름 설정
    private static final String FILE_URL="https://quampus.kr.object.ncloudstorage.com/test.jpg";
    private static final String FILE_NAME="test.jpg";
    @Test
    @DisplayName("[성공케이스]-이미지 업로드 성공시 Url를 반한합니다.")
    void imageUpload_SUCCESS() throws IOException {
        //given
        byte[] fileContent="fakeFile".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getSize()).thenReturn((long)fileContent.length);
        when(multipartFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        doNothing().when(amazonS3Service).putObject(any(PutObjectRequest.class));
        when(amazonS3Service.getUrl(eq(FILE_BUCKET_NAME),any())).thenReturn(new URL(FILE_URL));

        //when
        List<String> resultUrls=imageService.putFileToBucket(List.of(multipartFile),"ANSWER");

        //then
        assertNotNull(resultUrls);
        assertThat(resultUrls).hasSize(1);
        assertThat(resultUrls.get(0)).isEqualTo(FILE_URL);

        verify(amazonS3Service,times(1)).putObject(any(PutObjectRequest.class));
        verify(amazonS3Service,times(1)).getUrl(eq(FILE_BUCKET_NAME),any(String.class));
    }
    @Test
    @DisplayName("[실패케이스]-이미지 업로드 실패시 예외를 반한합니다.")
    void imageUpload_FAILED() throws IOException {
        //given
        byte[] fileContent="fakeFile".getBytes();
        InputStream inputStream = new ByteArrayInputStream(fileContent);

        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getSize()).thenReturn((long)fileContent.length);
        when(multipartFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        //실패 상황 예외던지기
        when(multipartFile.getInputStream())
                .thenThrow(new IOException());

        //when then
        RestApiException exception = assertThrows(RestApiException.class, () ->
                imageService.putFileToBucket(List.of(multipartFile), "ANSWER"));

        assertThat(exception.getMessage()).isEqualTo("이미지 업로드를 실패했습니다.");
    }
}