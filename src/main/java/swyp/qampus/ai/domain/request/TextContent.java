package swyp.qampus.ai.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class TextContent extends Content{
    private String text;
    public TextContent(String type,String text){
        super(type);
        this.text=text;
    }
}
