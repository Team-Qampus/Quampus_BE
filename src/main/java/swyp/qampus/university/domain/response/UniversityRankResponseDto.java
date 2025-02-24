package swyp.qampus.university.domain.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class UniversityRankResponseDto {
    private String university_name;
    private Integer ranking;
    private Integer choice_count;
    private Integer rate;

    @QueryProjection
    public UniversityRankResponseDto(String university_name,Integer ranking,Integer choice_count,Integer rate){
        this.ranking=ranking;
        this.university_name=university_name;
        this.choice_count=choice_count;
        this.rate=rate;
    }
}
