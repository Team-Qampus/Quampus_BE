package swyp.qampus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long question_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean is_deleted = false;

    @Column(nullable = false)
    private int view_cnt = 0;

    @Column(nullable = false)
    private int curious_count = 0;

    @Column(nullable = false)
    private LocalDateTime create_date;

    @Column(nullable = false)
    private LocalDateTime modified_date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany
    private List<Image> images;

    // 조회수 증가 메서드
    public void increseViewCount() {
        this.view_cnt++;
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.modified_date = LocalDateTime.now();
    }

    public void delete() {
        this.is_deleted = true;
    }

}
