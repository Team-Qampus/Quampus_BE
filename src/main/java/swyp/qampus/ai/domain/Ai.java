package swyp.qampus.ai.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.qampus.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ai extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "ai_id")
    private Long aiId;

    @Lob
    @Column(nullable = true,columnDefinition = "LONGTEXT")
    private String content;

    @Builder
    public Ai(String content){
        this.content=content;
    }
}

