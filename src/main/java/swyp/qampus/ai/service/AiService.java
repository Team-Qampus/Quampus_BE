package swyp.qampus.ai.service;

import swyp.qampus.ai.domain.response.AiResponseDto;

import java.io.IOException;

public interface AiService {
    AiResponseDto getAiAnswer(String token,Long questionId) throws IOException;
}
