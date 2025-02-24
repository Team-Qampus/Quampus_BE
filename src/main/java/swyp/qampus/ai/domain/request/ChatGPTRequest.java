package swyp.qampus.ai.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import swyp.qampus.ai.domain.Message;
import swyp.qampus.ai.domain.TextMessage;


import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatGPTRequest {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("max_tokens")
    private int maxTokens;

    public static ChatGPTRequest createImageRequest(String model,int maxTokens,String role,String requestText, String imageUrl){
        TextContent textContent=new TextContent("text",requestText);
        ImageContent imageContent=new ImageContent("image_url",new ImageUrl(imageUrl));
        Message message=new ImageMessage(role,List.of(textContent,imageContent));
        return createGPTRequest(model,maxTokens, Collections.singletonList(message));
    }
    public static ChatGPTRequest createTextRequest(String model,int maxTokens,String role,String requestText){
        Message message=new TextMessage(role,requestText);
        return createGPTRequest(model,maxTokens,Collections.singletonList(message));
    }

    private static ChatGPTRequest createGPTRequest(String model, int maxTokens, List<Message> messages) {
        return ChatGPTRequest.builder()
                .model(model)
                .maxTokens(maxTokens)
                .messages(messages)
                .build();

    }

}
