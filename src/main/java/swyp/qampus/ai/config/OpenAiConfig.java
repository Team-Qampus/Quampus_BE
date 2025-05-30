package swyp.qampus.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class OpenAiConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean(name = "openAiRestTemplate")
    public RestTemplate template(){
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add("Authorization","Bearer "+openAiKey);
            return execution.execute(request,body);
        }));
        return restTemplate;
    }
}
