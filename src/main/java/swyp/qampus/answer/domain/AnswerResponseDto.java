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
    private final List<String> imageUrls;

    @Builder
    private AnswerResponseDto(Long answerId, Long userId, String content, String userName, LocalDateTime createdDate,
                              int likeCnt, Boolean isChosen, List<String> imageUrls) {
        this.answerId = answerId;
        this.userId = userId;
        this.content = content;
        this.userName = userName;
        this.createdDate = createdDate;
        this.likeCnt = likeCnt;
        this.isChosen = isChosen;
        this.imageUrls = imageUrls;
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
                .imageUrls(imageUrls)
                .build();
    }
}
