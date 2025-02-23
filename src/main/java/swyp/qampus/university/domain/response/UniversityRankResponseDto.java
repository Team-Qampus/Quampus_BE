package swyp.qampus.university.domain.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor
public class UniversityRankResponseDto {
    private Long university_id;
    private String university_name;
    //랭킹
    private Integer ranking;
    //함여자 수
    private Long participant_count;
    //차지율
    private Integer rate;
    //채택 수
    private Long choice_cnt;


    public UniversityRankResponseDto(Long university_id, String university_name, Integer ranking, Long participant_count, Integer rate, Long choice_cnt) {
        this.university_id = university_id;
        this.university_name = university_name;
        this.ranking = ranking;
        this.participant_count = participant_count;
        this.rate = rate;
        this.choice_cnt = choice_cnt;
    }
}
