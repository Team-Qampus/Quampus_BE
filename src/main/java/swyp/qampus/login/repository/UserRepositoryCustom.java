package swyp.qampus.login.repository;

public interface UserRepositoryCustom {
    void resetMonthlyChoiceCnt();
    void resetWeeklyChoiceCnt();
    int getThisMonthRankOfMajor(String major);
    int getLastMonthRankOfMajor(String major);
}
