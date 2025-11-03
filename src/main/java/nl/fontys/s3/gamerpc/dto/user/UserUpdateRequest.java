package nl.fontys.s3.gamerpc.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserUpdateRequest {
    @NotNull
    private long id;

    @Size(max = 64, message = "Username cannot exceed 64 characters")
    private String username;

    @Email(message = "Email should be valid")
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    private String email;

    @Size(max = 64, message = "Password cannot exceed 64 characters")
    private String password;

    private Boolean admin;

    private String avatarUrl;
}
