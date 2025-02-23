package swyp.qampus.test;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TestDto {
    private String name;
    private String major;
    private String email;
    private String universityName;
}
