package cc.worldmandia.user;

import cc.worldmandia.url.Url;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String token;

    @ManyToMany
    @JoinTable(name = "users_urls",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id"))
    private Set<Url> urls;

}