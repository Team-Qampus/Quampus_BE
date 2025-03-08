package swyp.qampus.openApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class GetLocationUtil {
    @Value("${openApi.serviceKey}")
    private String AUTH_ENCODING_KEY;

    @Value("${openApi.baseUrl}")
    private String API_URL;

    private final ObjectMapper objectMapper=new ObjectMapper();
    private final RestTemplate restTemplate=new RestTemplate();
    private final static int PAGE_SIZE=10;

    //데이터 가공
    public LocationDto findLocationByCompanyName(String universityName) throws URISyntaxException {
        int page=1;

        while (true){
            String url=API_URL+"?serviceKey="+AUTH_ENCODING_KEY+"&page="+page+"&perPage=10";
            URI uri=new URI(url);
            try {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
                String responseJson = responseEntity.getBody();

                UniversityLocationResponse response = objectMapper.readValue(responseJson, UniversityLocationResponse.class);

                if (response.getData() == null || response.getData().isEmpty()) {
                    return null;
                }

                for (LocationDto university : response.getData()) {
                    if (university.get학교명().contains(universityName)) {
                        return university;
                    }
                }

                page++;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }


}
