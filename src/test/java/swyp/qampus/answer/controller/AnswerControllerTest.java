package swyp.qampus.answer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.domain.ChoiceRequestDto;
import swyp.qampus.answer.service.AnswerService;
import swyp.qampus.image.service.ImageService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnswerController.class)
class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnswerService answerService;
    @MockitoBean
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("성공시 ResponseDto를 반환합니다.")
    void choice_SUCEESS() throws Exception {
        //given
        Long answerId = 10L;
        Long questionId = 20L;
        String token = "Bearer token";
        ChoiceRequestDto requestDto = ChoiceRequestDto.builder()
                .answer_id(answerId)
                .question_id(questionId)
                .is_chosen(true)
                .build();

        //when
        doNothing().when(answerService).choice(requestDto, token);

        //then
        mockMvc.perform(post("/answers/choice")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("채택 성공"));

    }


    @Test
    @DisplayName("답변 생성 성공 (이미지 없음)")
    void createAnswer_SUCCESS_without_image() throws Exception {
        //given
        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .user_id("1234")
                .question_id(20L)
                .content("테스트 답변입니다")
                .build();

        MockMultipartFile jsonFile = new MockMultipartFile("requestDto", "", "application/json",
                objectMapper.writeValueAsBytes(requestDto));

        //when
        doNothing().when(answerService).createAnswer(any(AnswerRequestDto.class), eq(null));

        //then
        mockMvc.perform(multipart("/answers")
                        .file(jsonFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("답변 생성 성공"));
    }

    @Test
    @DisplayName("답변 생성 성공 (이미지 포함)")
    void createAnswer_SUCCESS_with_image() throws Exception {
        //give
        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .user_id("1234")
                .question_id(20L)
                .content("테스트 답변입니다")
                .build();

        MockMultipartFile jsonFile = new MockMultipartFile("requestDto", "", "application/json",
                objectMapper.writeValueAsBytes(requestDto));

        MockMultipartFile imageFile1 = new MockMultipartFile("images", "test1.jpg", "image/jpeg",
                "test image content1".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile("images", "test2.jpg", "image/jpeg",
                "test image content2".getBytes());

        List<String> mockImageUrls = List.of(
                "https://s3.amazonaws.com/quampus/answer/test1.jpg",
                "https://s3.amazonaws.com/quampus/answer/test2.jpg"
        );

        //when
        when(imageService.putFileToBucket(anyList(), eq("ANSWER"))).thenReturn(mockImageUrls);
        doNothing().when(answerService).createAnswer(any(AnswerRequestDto.class), anyList());

        //then
        mockMvc.perform(multipart("/answers")
                        .file(jsonFile)
                        .file(imageFile1)
                        .file(imageFile2)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("답변 생성 성공"));
    }

    @Test
    @DisplayName("답변 수정 성공")
    void updateAnswer_SUCCESS() throws Exception {
        // given
        Long answerId = 10L;
        AnswerUpdateRequestDto requestDto = new AnswerUpdateRequestDto("수정된 답변 내용");

        //when
        doNothing().when(answerService).updateAnswer(answerId, requestDto);

        //then
        mockMvc.perform(put("/answers/{answer_id}", answerId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("답변 수정 성공"));
    }

    @Test
    @DisplayName("답변 삭제 성공")
    void deleteAnswer_SUCCESS() throws Exception {
        // given
        Long answerId = 10L;

        //when
        doNothing().when(answerService).deleteAnswer(answerId);

        //then
        mockMvc.perform(delete("/answers/{answer_id}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("답변 삭제 성공"));
    }

}