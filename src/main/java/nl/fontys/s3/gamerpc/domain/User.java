package nl.fontys.s3.gamerpc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {
    private long id;
    private String username;
    private String email;
    private String passwordHash;
    private String passwordSalt;
    private Boolean admin;
    private String avatarUrl;
}
