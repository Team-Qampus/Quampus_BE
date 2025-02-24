package swyp.qampus.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import swyp.qampus.exception.CommonErrorCode;
import swyp.qampus.image.domain.Image;
import swyp.qampus.image.repository.ImageRepository;
import swyp.qampus.image.service.ImageService;
import swyp.qampus.question.domain.QuestionRequestDto;
import swyp.qampus.question.domain.QuestionUpdateRequestDto;
import swyp.qampus.category.domain.Category;
import swyp.qampus.question.domain.Question;
import swyp.qampus.question.exception.QuestionErrorCode;
import swyp.qampus.user.domain.User;
import swyp.qampus.exception.CustomException;
import swyp.qampus.question.repository.QuestionRepository;
import swyp.qampus.category.repository.CategoryRepository;
import swyp.qampus.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Transactional
    @Override
    public void createQuestion(Long user_id, QuestionRequestDto requestDto, List<MultipartFile> images) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException(CommonErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.CATEGORY_NOT_FOUND));

        Question question = Question.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(category)
                .build();

        Question savedQuestion = questionRepository.save(question);

        //사진을 올린 경우 -> 사진 업로드
        if(images!=null){
            List<String> urls=imageService.putFileToBucket(images,"QUESTION");
            for (String url:urls){
                Image newImage=Image.builder()
                        .pictureUrl(url)
                        .question(question)
                        .build();
                imageRepository.save(newImage);
            }
        }
    }

    @Transactional
    @Override
    public void updateQuestion(Long question_id, QuestionUpdateRequestDto requestDto) {
        Question question = questionRepository.findById(question_id)
                .orElseThrow(() -> new CustomException(QuestionErrorCode.NOT_EXIST_QUESTION));

        Category category = categoryRepository.findById(requestDto.getCategory_id())
                .orElseThrow(() -> new CustomException(CommonErrorCode.CATEGORY_NOT_FOUND));

        question.update(requestDto.getTitle(), requestDto.getContent(), category);
    }

    @Transactional
    @Override
    public void deleteQuestion(Long question_id) {
        Question question = questionRepository.findById(question_id)
                .orElseThrow(() -> new CustomException(QuestionErrorCode.NOT_EXIST_QUESTION));

        question.delete();
    }

}
