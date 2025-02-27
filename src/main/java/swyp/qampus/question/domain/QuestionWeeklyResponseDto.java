package swyp.qampus.question.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "주별 질문 목록")
public class QuestionWeeklyResponseDto {
    @Schema(description = "질문 ID", example = "1")
    private Long question_id;

    @Schema(description = "질문 제목", example = "전공과 관련된 질문입니다.")
    private String title;

    @Schema(description = "대학교명", example = "서울대학교")
    private String university_name;

    @Schema(description = "전공", example = "컴퓨터공학과")
    private String major;

    @Schema(description = "궁금해요 수", example = "10")
    private int curious_count;

    @Schema(description = "조회수", example = "100")
    private int view_count;

    @Schema(description = "총 점수", example = "80")
    private int total_score;

    @Builder
    private QuestionWeeklyResponseDto(Long question_id, String title, String university_name, String major, int curious_count, int view_count, int total_score) {
        this.question_id = question_id;
        this.title = title;
        this.university_name = university_name;
        this.major = major;
        this.curious_count = curious_count;
        this.view_count = view_count;
        this.total_score = total_score;
    }

    public static QuestionWeeklyResponseDto of(Question question) {
        return QuestionWeeklyResponseDto.builder()
                .question_id(question.getQuestionId())
                .title(question.getTitle())
                .university_name(question.getUser().getUniversity().getUniversityName())
                .major(question.getUser().getMajor())
                .curious_count(question.getCuriousCount())
                .view_count(question.getViewCnt())
                .total_score(question.getCuriousCount() + question.getViewCnt())
                .build();
    }
}