package swyp.qampus.answer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import swyp.qampus.answer.service.AnswerService;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("성공시 ResponseDto를 반환합니다.")
    void choice_SUCEESS() throws Exception {
        //given
        Long answerId=10L;
        Long questionId=20L;
        String token="Bearer token";

        //when
        doNothing().when(answerService).choice(answerId,questionId,token);

        //then
        mockMvc.perform(post("/answers/{answer_id}/choice",answerId)
                .param("question_id",String.valueOf(questionId))
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("채택 성공"));

        verify(answerService).choice(answerId,questionId,token);
    }
}