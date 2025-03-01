package swyp.qampus.login.service;

import org.springframework.data.domain.Pageable;
import swyp.qampus.login.dto.MyPageResponseDto;
import swyp.qampus.question.domain.MyQuestionResponseDto;

import java.util.List;


public interface UserService {
    MyPageResponseDto getMyPageData(String token, Long categoryId, String sort, Pageable pageable);
    String testUser(String userName,String universityName,String major);
}
