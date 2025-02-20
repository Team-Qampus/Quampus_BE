package swyp.qampus.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.exception.RestApiException;
import swyp.qampus.like.domain.Like;
import swyp.qampus.like.exception.LikeErrorCode;
import swyp.qampus.like.repository.LikeRepository;
import swyp.qampus.user.domain.User;
import swyp.qampus.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    @Override
    @Transactional
    public void insert(String token, Long answerId) {
        /*
        TODO: jwt로 교체 해야함
        */
        Result result = getResult(token, answerId);

        //이미 좋아요가 눌러져있으면 에러 반환
        if(likeRepository.findLikesByAnswerAndUser(answerId,token).isPresent()){
            throw new RestApiException(LikeErrorCode.DUPLICATED_LIKE_REQUEST);
        }

        Like like=Like.of(result.user(), result.answer());
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void delete(String token, Long answerId) {
        /*
        TODO: jwt로 교체 해야함
        */
        Result result=getResult(token,answerId);

        Like like=likeRepository.findLikesByAnswerAndUser(answerId,result.user.getUserId()).orElseThrow(
                ()->new RestApiException(LikeErrorCode.DUPLICATED_LIKE_REQUEST)
        );
        result.answer.decreaseLike(like);
        likeRepository.delete(like);
    }

    private Result getResult(String token, Long answerId) {
        //유저 예외처리
        User user=userRepository.findById(token)
                .orElseThrow(()->new RestApiException(CommonErrorCode.USER_NOT_FOUND));
        //답변 예외처리
        Answer answer=answerRepository.findById(answerId)
                .orElseThrow(()->new RestApiException(LikeErrorCode.NOT_EXISTED_ANSWERED));
        Result result = new Result(user, answer);
        return result;
    }

    private record Result(User user, Answer answer) {

    }

}
