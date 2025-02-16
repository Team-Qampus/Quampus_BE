package swyp.qampus.answer.domain;

import jakarta.persistence.*;
import lombok.*;
import swyp.qampus.image.domain.Image;
import swyp.qampus.question.domain.Question;
import swyp.qampus.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long answer_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime create_date = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modified_date = LocalDateTime.now();

    @OneToMany
    private List<Image> images;

    @Column(nullable = false)
    private int like_count = 0;

    public void increaseLike() {
        this.like_count++;
    }

    public void update(String content) {
        this.content = content;
        this.modified_date = LocalDateTime.now();
    }

    public void decreaseLike() {
        if (this.like_count > 0) {
            this.like_count--;
        }
    }

}
