package swyp.qampus.answer.domain;

import lombok.Builder;
import lombok.Getter;
import swyp.qampus.image.domain.Image;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AnswerResponseDto {
    private final Long answerId;
    private final Long userId;
    private final String content;
    private final String userName;
    private final LocalDateTime createdDate;
    private final int likeCnt;
    private final Boolean isChosen;
    private String universityName;
    private final List<String> imageUrls;
    //좋아요 유무 추가
    private final Boolean isLikeChosen;

    @Builder
    private AnswerResponseDto(Long answerId, Long userId, String content, String userName, LocalDateTime createdDate,
                              int likeCnt, Boolean isChosen, String universityName, List<String> imageUrls,Boolean isLikeChosen) {
        this.answerId = answerId;
        this.userId = userId;
        this.content = content;
        this.userName = userName;
        this.createdDate = createdDate;
        this.likeCnt = likeCnt;
        this.isChosen = isChosen;
        this.universityName = universityName;
        this.imageUrls = imageUrls;
        this.isLikeChosen=isLikeChosen;
    }

    public static AnswerResponseDto of(Answer answer, List<Image> images) {
        List<String> imageUrls = images.stream()
                .map(Image::getPictureUrl)
                .collect(Collectors.toList());

        return AnswerResponseDto.builder()
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .userName(answer.getUser().getName())
                .createdDate(answer.getCreateDate())
                .likeCnt(answer.getLikeCnt())
                .isChosen(answer.getIsChosen())
                .universityName(answer.getUser().getUniversity().getUniversityName())
                .imageUrls(imageUrls)
                .isLikeChosen(answer.getIsLikeChosen())
                .build();
    }
}
