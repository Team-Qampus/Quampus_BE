package swyp.qampus.like.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import swyp.qampus.like.service.LikeService;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {LikeController.class})
class LikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LikeService likeService;

    @Test
    @DisplayName("[성공케이스]-좋아요 클릭 성공시 성공 메시지가 같이 반환됩니다.")
    void insertLike() throws Exception {
        //given
        Long answerId=1L;
        String token="Bearer token";

        doNothing().when(likeService).insert(any(String.class),eq(answerId));

        //when
        //then
        mockMvc.perform(post("/like")
                .param("answer",String.valueOf(answerId))
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("좋아요"));

        verify(likeService).insert(any(String.class),eq(answerId));
    }

    @Test
    @DisplayName("[성공케이스]-좋아요 취소 시 성공 메시지가 반환된다.")
    void deleteLike() throws Exception {
        //given
        Long answerId=1L;
        String token="Bearer token";

        doNothing().when(likeService).delete(any(String.class),eq(answerId));

        //when
        //then
        mockMvc.perform(delete("/like")
                .param("answer",String.valueOf(answerId))
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("좋아요 취소 성공"));

        verify(likeService).delete(any(String.class),eq(answerId));
    }
}