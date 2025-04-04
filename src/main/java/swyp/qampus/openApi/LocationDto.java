package swyp.qampus.openApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LocationDto {
    private String 학교명;
    private String 위도;
    private String 경도;

    @Builder
    public LocationDto(String 학교명,String 위도,String 경도){
        this.위도=위도;
        this.경도=경도;
        this.학교명=학교명;
    }
}
