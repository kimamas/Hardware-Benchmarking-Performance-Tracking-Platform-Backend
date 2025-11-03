package nl.fontys.s3.gamerpc.business.converter;

import nl.fontys.s3.gamerpc.dto.user.UserSaveRequest;
import nl.fontys.s3.gamerpc.dto.user.UserResponse;
import nl.fontys.s3.gamerpc.dto.user.UserUpdateRequest;
import nl.fontys.s3.gamerpc.persistence.entity.UserEntity;

public class UserConverter {
    private UserConverter() {throw new IllegalStateException("Utility class");}
    public static UserEntity fromSaveRequestToEntity(UserSaveRequest request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .admin(request.getAdmin())
                .avatarUrl(request.getAvatarUrl())
                .build();
    }

    public static UserResponse fromEntityToResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }

    public static UserEntity fromUpdateRequestToEntity(UserUpdateRequest request) {
        return UserEntity.builder()
                .id((int) request.getId())
                .username(request.getUsername())
                .email(request.getEmail())
                .admin(request.getAdmin())
                .avatarUrl(request.getAvatarUrl())
                .build();
    }
}
