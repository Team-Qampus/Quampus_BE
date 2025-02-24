package swyp.qampus.ai.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import swyp.qampus.ai.domain.Message;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ImageMessage extends Message {
    private List<Content> content;

    public ImageMessage(String role,List<Content> content){
        super(role);
        this.content=content;
    }
}
