package swyp.qampus.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    ALL("전체"),
    NATURAL_SCIENCES("자연계"),
    HUMANITIES("인문계"),
    ARTS_AND_SPORTS("예체능"),
    PRACTICAL("실무");

    private final String categoryName;
}