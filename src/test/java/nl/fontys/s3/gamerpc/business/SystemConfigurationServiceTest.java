package nl.fontys.s3.gamerpc.business;

import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationRequest;
import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationResponse;
import nl.fontys.s3.gamerpc.persistence.iinterface.CPURepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.GPURepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.RAMRepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.SystemConfigurationRepository;
import nl.fontys.s3.gamerpc.persistence.iinterface.UserRepository;
import nl.fontys.s3.gamerpc.persistence.entity.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
class SystemConfigurationServiceTest {
	@InjectMocks
	private SystemConfigurationService systemConfigurationService;

	@Mock
	private SystemConfigurationRepository systemConfigurationRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CPURepository cpuRepository;

	@Mock
	private GPURepository gpuRepository;

	@Mock
	private RAMRepository ramRepository;

	private CPUEntity cpu;
	private GPUEntity gpu;
	private RAMEntity ram;
	private UserEntity user;
	private SystemConfigurationEntity systemConfigurationEntity;
	@BeforeEach
    void setup() {
		user = new UserEntity(1, "User1", "user@gmail.com", "234", "123", true, "https://example.org/example1");
		cpu = new CPUEntity(1, "Core i7", "Intel", 16,  2.3, 3.0);
		gpu = new GPUEntity(1, "RTX 3060", "NVIDIA", 12, 2300, 2500);
		ram = new RAMEntity(1, "16 GB", "Corsair", 12, 3200, "DRR5", "CL16", 2);

		when(cpuRepository.findById(1L)).thenReturn(Optional.of(cpu));
		when(gpuRepository.findById(1L)).thenReturn(Optional.of(gpu));
		when(ramRepository.findById(1L)).thenReturn(Optional.of(ram));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		systemConfigurationEntity = new SystemConfigurationEntity();
		systemConfigurationEntity.setUser(user);
		systemConfigurationEntity.setCpu(cpu);
		systemConfigurationEntity.setGpu(gpu);
		systemConfigurationEntity.setRam(ram);
		when(systemConfigurationRepository.findByUserId(1)).thenReturn(Optional.of(systemConfigurationEntity));
		when(systemConfigurationRepository.save(Mockito.any(SystemConfigurationEntity.class)))
				.thenReturn(systemConfigurationEntity);
    }
	@Test
	void saveConfiguration_ShouldSaveAndReturnCorrectResponse() {
		SystemConfigurationRequest configuration = new SystemConfigurationRequest(1,1,1);
		int userId = 1;
		SystemConfigurationResponse expected = new SystemConfigurationResponse(1,1,1);

		SystemConfigurationResponse actual = systemConfigurationService.saveConfiguration(configuration, userId);

		assertEquals(expected, actual);
	}

	@Test
	void saveConfiguration_ShouldReturnUserNotFoundResponse() {
		SystemConfigurationRequest configuration = new SystemConfigurationRequest(1,1,1);
		int userId = 123;

		when(userRepository.findById((long) userId)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
				systemConfigurationService.saveConfiguration(configuration, userId)
		);
		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void saveConfiguration_ShouldReturnCpuNotFoundResponse() {
		SystemConfigurationRequest configuration = new SystemConfigurationRequest(1, 99, 1); // Invalid CPU ID
		int userId = 1;

		when(cpuRepository.findById(99L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
				systemConfigurationService.saveConfiguration(configuration, userId)
		);

		assertEquals("CPU not found", exception.getMessage());
	}

	@Test
	void saveConfiguration_ShouldReturnGpuNotFoundResponse() {
		SystemConfigurationRequest configuration = new SystemConfigurationRequest(1, 1, 99);
		int userId = 1;

		when(gpuRepository.findById(99L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
				systemConfigurationService.saveConfiguration(configuration, userId)
		);

		assertEquals("GPU not found", exception.getMessage());
	}

	@Test
	void saveConfiguration_ShouldReturnRamNotFoundResponse() {
		SystemConfigurationRequest configuration = new SystemConfigurationRequest(99, 1, 1);
		int userId = 1;

		when(ramRepository.findById(99L)).thenReturn(Optional.empty()); // RAM does not exist

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
				systemConfigurationService.saveConfiguration(configuration, userId)
		);

		assertEquals("RAM not found", exception.getMessage());
	}

	@Test
	void getConfigurationById_ShouldReturnCorrectResponse() {
		Long userId = 1L;
		SystemConfigurationResponse expected = new SystemConfigurationResponse(1, 1, 1);
		SystemConfigurationResponse actual = systemConfigurationService.getConfigurationById(userId);

		assertEquals(expected, actual);
	}

	@Test
	void getConfigurationById_ShouldReturnUserWithThisIdDoesNotExist() {
		Long userId = 99L;

		when(systemConfigurationRepository.findByUserId(99)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
				systemConfigurationService.getConfigurationById(userId)
		);

		assertEquals("Configuration not found for user ID " + userId, exception.getMessage());
	}
}
