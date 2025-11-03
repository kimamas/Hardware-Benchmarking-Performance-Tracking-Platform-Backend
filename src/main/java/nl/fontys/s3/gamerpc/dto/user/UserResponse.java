package nl.fontys.s3.gamerpc.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private String email;
    private String avatarUrl;
}
