package swyp.qampus.university.domain.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter

public class UniversityRankResponseDto {
    private Long university_id;
    private String university_name;
    private Integer ranking;
    private Long participant_count;
    private Integer rate;

    @QueryProjection
    @Builder
    public UniversityRankResponseDto(Long university_id,String university_name,Integer ranking,Long participant_count,Integer rate){
        this.university_name=university_name;
        this.ranking=ranking;
        this.university_id=university_id;
        this.rate=rate;
        this.participant_count=participant_count;
    }
}
