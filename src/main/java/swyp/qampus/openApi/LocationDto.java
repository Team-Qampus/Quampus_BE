package swyp.qampus.openApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LocationDto {
    private String 학교명;
    private String 위도;
    private String 경도;
}
