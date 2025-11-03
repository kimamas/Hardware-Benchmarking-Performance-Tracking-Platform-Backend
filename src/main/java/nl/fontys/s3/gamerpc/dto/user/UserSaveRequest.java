package nl.fontys.s3.gamerpc.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserSaveRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 64, message = "Username cannot exceed 64 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 64, message = "Email cannot exceed 64 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 64, message = "Password cannot exceed 64 characters")
    private String password;

    @NotNull(message = "Admin flag cannot be null")
    private Boolean admin;

    @NotNull
    private String avatarUrl;
}
