package nl.fontys.s3.gamerpc.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Size(max = 64)
    @NotNull
    @Column(name = "username", nullable = false, length = 64)
    private String username;

    @Size(max = 64)
    @NotNull
    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @Size(max = 64)
    @NotNull
    @Column(name = "password_hash", nullable = false, length = 64)
    private String passwordHash;

    @Size(max = 64)
    @NotNull
    @Column(name = "password_salt", nullable = false, length = 64)
    private String passwordSalt;

    @NotNull
    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    @Size(max = 64)
    @NotNull
    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;
}