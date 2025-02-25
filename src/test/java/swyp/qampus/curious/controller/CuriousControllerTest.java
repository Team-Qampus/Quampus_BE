/*
package swyp.qampus.curious.controller;

import org.apache.http.client.methods.RequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import swyp.qampus.curious.service.CuriousService;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.apache.http.client.methods.RequestBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = {CuriousController.class})
class CuriousControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CuriousService curiousService;

    @Test
    @DisplayName("[성공케이스]-나도 궁금해요 클릭 성공시 성공 메시지가 같이 반환합니다.")
    void insertCurious() throws Exception {
        //given
        Long questionId=1L;
        String token="test1";

        doNothing().when(curiousService).insert(any(String.class),eq(questionId));

        //when
        //then
        mockMvc.perform(post("/curious")
                .param("question",String.valueOf(questionId))
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("나도 궁금해요 성공"));

        verify(curiousService).insert(any(String.class),eq(questionId));
    }

    @Test
    @DisplayName("[성공케이스]-나도 궁금해요 취소 시 메시지가 반환된다.")
    void deleteCurious() throws Exception {
        //given
        Long questionId=1L;
        String token="test1";

        doNothing().when(curiousService).insert(any(String.class),eq(questionId));

        //when
        //then
        mockMvc.perform(delete("/curious")
                        .param("question",String.valueOf(questionId))
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("나도 궁금해요 취소"));

        verify(curiousService).delete(any(String.class),eq(questionId));
    }
}*/
