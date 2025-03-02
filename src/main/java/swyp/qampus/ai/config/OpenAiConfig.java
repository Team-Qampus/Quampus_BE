package swyp.qampus.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean(name = "openAiRestTemplate")
    public RestTemplate template(){
        SimpleClientHttpRequestFactory factory=new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate=new RestTemplate(factory);
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().add("Authorization","Bearer "+openAiKey);
            return execution.execute(request,body);
        }));
        return restTemplate;
    }
}
