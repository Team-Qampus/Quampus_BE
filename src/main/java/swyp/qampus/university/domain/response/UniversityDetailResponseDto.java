package swyp.qampus.university.domain.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@Schema(name = "학교 상세보기 조회Dto")
public class UniversityDetailResponseDto {
    //학교 인덱스
    private Long university_id;
    //학교 이름
    private String university_name;
    //차지율
    private Integer rate;
    //참여자수
    private Long participant_count;
    //질문 수
    private Long question_cnt;
    //답변 수
    private Long answer_cnt;
    //채택 수
    private Long choice_cnt;
    //랭킹
    private Long rank;

    @QueryProjection
    public UniversityDetailResponseDto(Long university_id, String university_name, Integer rate, Long participant_count, Long question_cnt, Long answer_cnt, Long choice_cnt,Long rank) {
        this.university_id = university_id;
        this.university_name = university_name;
        this.rate = rate;
        this.participant_count = participant_count;
        this.question_cnt = question_cnt;
        this.answer_cnt = answer_cnt;
        this.choice_cnt = choice_cnt;
        this.rank=rank;
    }
}
