package swyp.qampus.curious.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.curious.domain.Curious;
import swyp.qampus.curious.exception.CuriousErrorCode;
import swyp.qampus.curious.repository.CuriousRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.login.util.JWTUtil;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.login.entity.User;

@Service
@RequiredArgsConstructor
public class CuriousServiceImpl implements CuriousService{
    private final CuriousRepository curiousRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final JWTUtil jwtUtil;
    @Override
    @Transactional
    public void insert(String token, Long questionId) {
        /*
        TODO:JWT로 교체
         */
        Result result=getResult(token,questionId);
        Long userId= jwtUtil.getUserIdFromToken(token);

        //이미 궁금해요 눌러져 있으면 에러 반환
        if(curiousRepository.find_CuriousCustomByQuestionAndUser(questionId,userId).isPresent()){
            throw new RestApiException(CuriousErrorCode.DUPLICATED_CURIOUS_REQUEST);
        }

        //자신의 질문 나도 궁금해요 클릭 시 예외처리
        if(result.question().getUser().getUserId()
                .equals(jwtUtil.getUserIdFromToken(token))){
            throw new RestApiException(CuriousErrorCode.CAN_NOT_CLICK_MINE);
        }

        Curious curious=Curious.of(result.user(),result.question());
        curiousRepository.save(curious);
    }

    @Override
    @Transactional
    public void delete(String token, Long questionId) {
        /*
        TODO:JWT교체
         */
        Result result=getResult(token,questionId);

        Curious curious=curiousRepository.find_CuriousCustomByQuestionAndUser(questionId,result.user().getUserId()).orElseThrow(
                ()->new RestApiException(CuriousErrorCode.DUPLICATED_CURIOUS_REQUEST)
        );

        //자신의 질문 나도 궁금해요 클릭 시 예외처리
        if(result.question.getUser().getUserId()
                .equals(jwtUtil.getUserIdFromToken(token))){
            throw new RestApiException(CuriousErrorCode.CAN_NOT_CLICK_MINE);
        }

        result.question.decreaseCurious(curious);
        result.user.deleteCurious(curious);
        curiousRepository.delete(curious);
    }
    private Result getResult(String token,Long questionId){

        //유저 예외처리
        User user=userRepository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(()->new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        //질문 찾기 예외
        Question question=questionRepository.findById(questionId)
                .orElseThrow(()->new RestApiException(QuestionErrorCode.NOT_EXIST_QUESTION));
        return new Result(user,question);
    }

    private record Result(User user, Question question){

    }
}
