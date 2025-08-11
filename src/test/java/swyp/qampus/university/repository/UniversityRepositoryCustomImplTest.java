package swyp.qampus.university.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import swyp.qampus.login.entity.User;
import swyp.qampus.login.repository.UserRepository;
import swyp.qampus.university.domain.University;
import swyp.qampus.university.domain.response.UniversityRankResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
class UniversityRepositoryCustomImplTest {
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;
    @Test
    @DisplayName("대학교명으로 이번 달 랭킹을 조회합니다.")
    void getThisMonthRankOfSchool() {
        //given
        University university1 = University
                .builder()
                .universityName("대학교1")
                .build();

        University university2 = University
                .builder()
                .universityName("대학교2")
                .build();

        University university3 = University
                .builder()
                .universityName("대학교3")
                .build();

        University university4 = University
                .builder()
                .universityName("대학교4")
                .build();

        university1.setMonthlyChoiceCnt(10L);
        university2.setMonthlyChoiceCnt(20L);
        university3.setMonthlyChoiceCnt(4L);
        university4.setMonthlyChoiceCnt(32L);

        university1.setLastMonthChoiceCnt(29L);
        university2.setLastMonthChoiceCnt(39L);
        university3.setLastMonthChoiceCnt(10L);
        university4.setLastMonthChoiceCnt(2L);

        universityRepository.saveAll(List.of(university1, university2, university3, university4));
        universityRepository.flush();

        /*
        * 이번 달 랭킹 예상 - 1:대학4  2:대학2   3:대학1  4:대학3
        * 저번 달 랭킹 예상 - 1:대학2  2:대학1   3:대학3  4:대학4
        * */
        //when&then
        assertThat(universityRepository
                .getThisMonthRankOfSchool(university1.getUniversityName())).isEqualTo(3);
        assertThat(universityRepository
                .getThisMonthRankOfSchool(university2.getUniversityName())).isEqualTo(2);
        assertThat(universityRepository
                .getThisMonthRankOfSchool(university3.getUniversityName())).isEqualTo(4);
        assertThat(universityRepository
                .getThisMonthRankOfSchool(university4.getUniversityName())).isEqualTo(1);


    }

    @Test
    @DisplayName("대학교명으로 저번 달 랭킹을 조회합니다.")
    void getLastMonthRankOfSchool() {
        //given
        University university1 = University
                .builder()
                .universityName("대학교1")
                .build();

        University university2 = University
                .builder()
                .universityName("대학교2")
                .build();

        University university3 = University
                .builder()
                .universityName("대학교3")
                .build();

        University university4 = University
                .builder()
                .universityName("대학교4")
                .build();

        university1.setLastMonthChoiceCnt(29L);
        university2.setLastMonthChoiceCnt(39L);
        university3.setLastMonthChoiceCnt(10L);
        university4.setLastMonthChoiceCnt(2L);

        universityRepository.saveAll(List.of(university1, university2, university3, university4));
        universityRepository.flush();

        /*
         * 저번 달 랭킹 예상 - 1:대학2  2:대학1   3:대학3  4:대학4
         * */
        //when&then
        assertThat(universityRepository
                .getLastMonthRankOfSchool(university1.getUniversityName())).isEqualTo(2);
        assertThat(universityRepository
                .getLastMonthRankOfSchool(university2.getUniversityName())).isEqualTo(1);
        assertThat(universityRepository
                .getLastMonthRankOfSchool(university3.getUniversityName())).isEqualTo(3);
        assertThat(universityRepository
                .getLastMonthRankOfSchool(university4.getUniversityName())).isEqualTo(4);


    }

}
