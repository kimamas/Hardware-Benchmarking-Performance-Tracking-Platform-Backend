package nl.fontys.s3.gamerpc.business;

import java.util.ArrayList;
import java.util.List;
import nl.fontys.s3.gamerpc.business.security.AccessTokenEncoderDecoderImpl;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.UserRepository;
import nl.fontys.s3.gamerpc.persistence.entity.UserEntity;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import nl.fontys.s3.gamerpc.dto.user.UserSaveRequest;
import nl.fontys.s3.gamerpc.dto.user.UserResponse;
import nl.fontys.s3.gamerpc.dto.user.UserUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
    private AccessTokenEncoderDecoderImpl accessTokenEncoderDecoderImpl;
	@InjectMocks
	private UserService userService;

	private UserEntity user;
	@BeforeEach
	void setup() {
		user = new UserEntity(1, "User1", "user@gmail.com", "234", "123", true, "https://example.org/example1");
		UserEntity user2 = new UserEntity(2, "User2", "user@gmail.com", "234", "123", false, "https://example.org/example1");
		List<UserEntity> users = List.of(user, user2);

		Mockito.when(userRepository.findAll()).thenReturn(users);
		Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		Mockito.when(userRepository.existsById(1L)).thenReturn(true);
	}

	@Test
	void saveUser_ShouldSaveAndReturnCorrectResponse() {
		UserSaveRequest userSaveRequest = new UserSaveRequest("user","user@gmail.com","123", true,"https://example.org/example1");
		UserResponse expected = new UserResponse(0, "user", "user@gmail.com", "https://example.org/example1");
		UserResponse actual = userService.saveUser(userSaveRequest);

		assertNotNull(actual);
		assertThat(actual)
				.usingRecursiveComparison()
				.isEqualTo(expected);
	}

	@Test
	void saveUser_ShouldReturnUsernameAlreadyExists() {
		UserSaveRequest userRequest = new UserSaveRequest("user","user@gmail.com","123", true,"https://example.org/example1");

		when(userRepository.existsByUsername(Mockito.any())).thenReturn(true);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
				userService.saveUser(userRequest)
		);
		assertEquals("A user with this username already exists.", exception.getReason());
	}

	@Test
	void saveUser_ShouldReturnEmailAlreadyExists() {
		UserSaveRequest userRequest = new UserSaveRequest("user","user@gmail.com","123", true,"https://example.org/example1");

		when(userRepository.existsByEmail(Mockito.any())).thenReturn(true);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
				userService.saveUser(userRequest)
		);
		assertEquals("A user with this email already exists.", exception.getReason());
	}

	@Test
	void getAllUsers_ShouldReturnTwoUsers() {
		List<UserEntity> expected = new ArrayList<>();
		UserEntity user1 = new UserEntity(1, "User1", "user@gmail.com", "234", "123", true, "https://example.org/example1");
		UserEntity user2 = new UserEntity(2, "User2", "user@gmail.com", "234", "123", false, "https://example.org/example1");
		expected.add(user1);
		expected.add(user2);

		List<UserEntity> actual = userService.getAllUsers();

		assertThat(actual)
				.usingRecursiveFieldByFieldElementComparatorIgnoringFields("password", "salt")
				.containsExactlyElementsOf(expected);
	}

	@Test
	void getUserById_ShouldReturnUserWithProperId() {
		long id = 1L;
		Optional<UserEntity> expected = Optional.ofNullable(user);

		Optional<UserEntity> actual = userService.getUserById(id);

		assertEquals(expected, actual);
	}

	@Test
	void getUserById_ShouldReturnUserNotFound() {
		long id = 123L;
		Optional<UserEntity> expected = Optional.empty();

		Optional<UserEntity> actual = userService.getUserById(id);

		assertEquals(expected, actual);
	}

	@Test
	void deleteUser_ShouldDeleteUser() {
		long id = 1L;
		userService.deleteUser(id);

		Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);
	}

	@Test
	void deleteUser_ShouldReturnUserNotFound() {
		long id = 123L;

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
				userService.deleteUser(id)
		);
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
		assertEquals("A user don't exist.", exception.getReason());
	}
	@Test
	void updateUser_ShouldReturnUpdatedUser() {
		UserUpdateRequest userRequest = new UserUpdateRequest(1, "User1", "user@gmail.com", "1234", true, "https://example.org/example1");
		UserResponse expected = new UserResponse(1, "User1", "user@gmail.com", "https://example.org/example1");

		UserResponse actual = userService.updateUser(userRequest);

		assertThat(actual)
				.usingRecursiveComparison()
				.isEqualTo(expected);
	}

	@Test
	void updateUser_ShouldThrowEmailAlreadyExists() {
		UserUpdateRequest userRequest = new UserUpdateRequest(1, "User1", "existing_email@gmail.com", "1234", true, "https://example.org/example1");

		Mockito.when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);

		ResponseStatusException exception = assertThrows(AlreadyExists.class, () ->
				userService.updateUser(userRequest)
		);

		assertEquals("A user with this email already exists.", exception.getReason());
	}

	@Test
	void updateUser_ShouldThrowUsernameAlreadyExists() {
		UserUpdateRequest userRequest = new UserUpdateRequest(1, "ExistingUsername", "user@gmail.com", "1234", true, "https://example.org/example1");

		Mockito.when(userRepository.existsByUsername(userRequest.getUsername())).thenReturn(true);

		ResponseStatusException exception = assertThrows(AlreadyExists.class, () ->
				userService.updateUser(userRequest)
		);

		assertEquals("A user with this username already exists.", exception.getReason());
	}

}
