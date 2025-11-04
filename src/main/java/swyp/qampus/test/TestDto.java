package swyp.qampus.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TestDto {
    private String name;
    private String major;
    private String email;
    private String universityName;
}
