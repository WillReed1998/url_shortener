package cc.worldmandia.url;

import cc.worldmandia.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "urls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    //private Timestamp createdDate;

    @Column(name = "end_at")
    @Builder.Default
    private LocalDateTime endAt = LocalDateTime.now().plusDays(30);
    //private Timestamp endAt;

    @Column(name = "click_count")
    private int clickCount;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "full_url")
    private String fullUrl;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(mappedBy = "urls")
    private Set<User> users;

}