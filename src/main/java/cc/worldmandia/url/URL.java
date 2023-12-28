package cc.worldmandia.url;

import cc.worldmandia.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "urls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class URL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "end_at")
    private Timestamp endAt;
    @Column(name = "click_count")
    private int clickCount;
    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "full_url")
    private String fullUrl;
    private boolean enabled;

    @ManyToMany(mappedBy = "urls")
    private Set<User> users;

}