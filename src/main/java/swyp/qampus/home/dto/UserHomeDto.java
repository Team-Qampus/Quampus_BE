package swyp.qampus.home.dto;

import lombok.Builder;
import lombok.Getter;
import swyp.qampus.login.entity.User;

@Getter
@Builder
public class UserHomeDto {
    private final String name;
    private final String university_name;
    private final String major;
    //전체 랭킹-이번 달
    private final Integer this_month_ranking;
    //전체 랭킹-저번 달
    private final Integer last_month_ranking;
    //학과 랭킹-이번 달
    private final Integer this_month_major_ranking;
    //학과 랭킹-저번 달
    private final Integer last_month_major_ranking;
}
