package nl.fontys.s3.gamerpc.business;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.converter.UserConverter;
import nl.fontys.s3.gamerpc.business.security.AccessToken;
import nl.fontys.s3.gamerpc.business.security.AccessTokenEncoderDecoderImpl;
import nl.fontys.s3.gamerpc.business.security.AccessTokenImpl;
import nl.fontys.s3.gamerpc.dto.login.LoginRequest;
import nl.fontys.s3.gamerpc.dto.login.LoginResponse;
import nl.fontys.s3.gamerpc.dto.user.UserSaveRequest;
import nl.fontys.s3.gamerpc.dto.user.UserResponse;
import nl.fontys.s3.gamerpc.dto.user.UserUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.exception.WrongLoginCredentials;
import nl.fontys.s3.gamerpc.persistence.iinterface.UserRepository;
import nl.fontys.s3.gamerpc.persistence.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccessTokenEncoderDecoderImpl accessTokenEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(long id) {
        return userRepository.findById(id);
    }

    public UserResponse saveUser(UserSaveRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExists("A user with this email already exists.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExists("A user with this username already exists.");
        }
        UserEntity newUser = UserConverter.fromSaveRequestToEntity(user);

        String salt = generateSalt();
        newUser.setPasswordSalt(salt);

        String hashedPassword = hashPassword(user.getPassword(), salt);
        newUser.setPasswordHash(hashedPassword);

        UserEntity savedUser = userRepository.save(newUser);
        return UserConverter.fromEntityToResponse(savedUser);
    }

    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user don't exist.");
        }
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(UserUpdateRequest user) {
       UserEntity existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + user.getId() + " not found"));

        if (!Objects.equals(existingUser.getEmail(), user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExists("A user with this email already exists.");
        }
        if (!Objects.equals(existingUser.getUsername(), user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExists("A user with this username already exists.");
        }

        UserEntity userToUpdate = UserConverter.fromUpdateRequestToEntity(user);

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String newSalt = generateSalt();
            String hashedPassword = hashPassword(user.getPassword(), newSalt);
            userToUpdate.setPasswordHash(hashedPassword);
            userToUpdate.setPasswordSalt(newSalt);
        }

        UserEntity updatedUser = userRepository.save(userToUpdate);
        return UserConverter.fromEntityToResponse(updatedUser);
    }
    public LoginResponse login(LoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new WrongLoginCredentials("Wrong email or password"));

            if(!hashPassword(request.getPassword(), user.getPasswordSalt()).matches(user.getPasswordHash()))
            {
                throw new WrongLoginCredentials("Wrong email or password");
            }

                List<String> roles = new ArrayList<>();
                roles.add("ROLE_USER");
                if(Boolean.TRUE.equals(user.getAdmin())){
                    roles.add("ROLE_ADMIN");
                }
                AccessToken accessToken = new AccessTokenImpl("Login", (long) user.getId(), roles);
                return LoginResponse.builder()
                        .token(accessTokenEncoder.encode(accessToken))
                        .build();
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), 10000, 256);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("Error while hashing password", e);
        }
    }
}
