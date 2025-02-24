package swyp.qampus.university.domain.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * swyp.qampus.university.domain.response.QUniversityDetailResponseDto is a Querydsl Projection type for UniversityDetailResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QUniversityDetailResponseDto extends ConstructorExpression<UniversityDetailResponseDto> {

    private static final long serialVersionUID = 924982758L;

    public QUniversityDetailResponseDto(com.querydsl.core.types.Expression<Long> university_id, com.querydsl.core.types.Expression<String> university_name, com.querydsl.core.types.Expression<Integer> rate, com.querydsl.core.types.Expression<Long> participant_count, com.querydsl.core.types.Expression<Long> question_cnt, com.querydsl.core.types.Expression<Long> answer_cnt, com.querydsl.core.types.Expression<Long> choice_cnt) {
        super(UniversityDetailResponseDto.class, new Class<?>[]{long.class, String.class, int.class, long.class, long.class, long.class, long.class}, university_id, university_name, rate, participant_count, question_cnt, answer_cnt, choice_cnt);
    }

}

