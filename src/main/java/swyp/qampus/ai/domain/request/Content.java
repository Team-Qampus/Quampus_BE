package swyp.qampus.ai.domain.request;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class Content {
    private String type;
}
