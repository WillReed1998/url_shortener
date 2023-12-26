package cc.worldmandia.features.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String token;


    public static UserDto from(User user){
        return UserDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .token(user.getToken())
                .build();
    }

        public static List<UserDto> from(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(from(user));
        }

        return result;
    }

}

