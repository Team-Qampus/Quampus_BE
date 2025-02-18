package swyp.qampus.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.answer.domain.Answer;
import swyp.qampus.answer.domain.AnswerRequestDto;
import swyp.qampus.answer.domain.AnswerResponseDto;
import swyp.qampus.answer.domain.AnswerUpdateRequestDto;
import swyp.qampus.answer.repository.AnswerRepository;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.repository.ImageRepository;
import swyp.qampus.image.service.ImageService;
import swyp.qampus.question.domain.MessageResponseDto;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.exception.ErrorCode;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Transactional
    public AnswerResponseDto createAnswer(AnswerRequestDto requestDto, List<MultipartFile> images) {
        User user = userRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.USER_NOT_FOUND));

        Question question = questionRepository.findById(requestDto.getQuestion_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        Answer answer = Answer.builder()
                .question(question)
                .content(requestDto.getContent())
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        //사진을 올린 경우 -> 사진업로드
        if(images!=null){
            List<String>urls=imageService.putFileToBucket(images,"ANSWER");
            for (String url:urls){
                Image newImage=Image.builder()
                        .pictureUrl(url)
                        .answer(answer)
                        .build();
                imageRepository.save(newImage);
            }
        }
        return new AnswerResponseDto(savedAnswer.getAnswerId(), "답변 생성 성공");
    }

    @Transactional
    public MessageResponseDto updateAnswer(Long answer_id, AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        answer.update(requestDto.getContent());
        return new MessageResponseDto("답변 수정 성공");
    }

    @Transactional
    public MessageResponseDto deleteAnswer(Long answer_id) {
        Answer answer = answerRepository.findById(answer_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.QUESTION_NOT_FOUND));

        answerRepository.delete(answer);
        return new MessageResponseDto("답변 삭제 성공");
    }

}
