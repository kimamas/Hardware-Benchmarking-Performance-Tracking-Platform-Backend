package nl.fontys.s3.gamerpc.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import nl.fontys.s3.gamerpc.business.benchmark.*;
import nl.fontys.s3.gamerpc.business.converter.BenchmarkConverter;
import nl.fontys.s3.gamerpc.dto.benchmark.*;
import nl.fontys.s3.gamerpc.persistence.iinterface.*;
import nl.fontys.s3.gamerpc.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BenchmarkServiceTest {
    @Mock
    private BenchmarkConverter benchmarkConverter;

    @Mock
    private BenchmarkRepository benchmarkRepository;

    @Mock
    private BenchmarkResultRepository benchmarkResultRepository;

    @Mock
    private BenchmarkResultFactory benchmarkResultFactory;

    @InjectMocks
    private BenchmarkService benchmarkService;

    @Mock
    private CpuBenchmarkResult cpuBenchmarkResult;
    @Mock
    private GpuBenchmarkResult gpuBenchmarkResult;
    @Mock
    private RamBenchmarkResult ramBenchmarkResult;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BenchmarkResult benchmarkResult;
    @Mock
    private CPURepository cpuRepository;
    @Mock
    private GPURepository gpuRepository;
    @Mock
    private RAMRepository ramRepository;

    private UserEntity user;
    private CPUEntity cpu;
    private RAMEntity ram;
    private GPUEntity gpu;
    private SystemConfigurationEntity configuration;
    private BenchmarkSaveRequest request;
    private List<Object[]> results;
    private BenchmarkEntity benchmarkEntity;
    private BenchmarkResponse benchmarkResponse;
    private List<BenchmarkResultEntity> benchmarkResultEntity;
    @BeforeEach
    void setUp() {
        user = new UserEntity(1, "User1", "user@gmail.com", "234", "123", true, "https://example.org/example1");
        cpu = new CPUEntity(1, "I7 12500k", "Intel", 12, 2.5, 3.1);
        gpu = new GPUEntity(1, "Model X", "NVIDIA", 12, 1500, 1750);
        ram = new RAMEntity(1, "Model A", "Crucial", 16, 3200, "DDR5", "CL16", 2);
        configuration = new SystemConfigurationEntity(1, user, cpu, gpu, ram, Instant.now());
        request = new BenchmarkSaveRequest(1, Instant.now(), List.of(
                new HardwareBenchmarkResultRequest("GPU", 1L, Map.of("gamingTest", 100.0)),
                new HardwareBenchmarkResultRequest("GPU", 1L, Map.of("shaderTest", 50.0)),
                new HardwareBenchmarkResultRequest("GPU", 1L, Map.of("memoryTest", 750.0)),
                new HardwareBenchmarkResultRequest("CPU", 1L, Map.of("singleCoreTest", 850.0)),
                new HardwareBenchmarkResultRequest("CPU", 1L, Map.of("multiCoreTest", 100.0)),
                new HardwareBenchmarkResultRequest("RAM", 1L, Map.of("memorySpeedTest", 200.0))));
        results = Arrays.asList(
                new Object[]{"Gaming Test", 30},
                new Object[]{"Shader Test", 20},
                new Object[]{"Memory Test", 500},
                new Object[]{"Single-Core Test", 600},
                new Object[]{"Multi-Core Test", 50},
                new Object[]{"Memory-Speed Test", 130}
        );

        CpuBenchmarkResponse cpuResponse = new CpuBenchmarkResponse(1L, "i7-12700H", 690.5, 1234.8);
        GpuBenchmarkResponse gpuResponse = new GpuBenchmarkResponse(2L, "Geforce RTX 3060", 80.5, 95.0, 75.3);
        RamBenchmarkResponse ramResponse = new RamBenchmarkResponse(3L, "Vengeance DDR5", 4800.0);
        List<HardwareBenchmarkResponse> hardwareResults = Arrays.asList(cpuResponse, gpuResponse, ramResponse);
        benchmarkResponse = new BenchmarkResponse(1L, 69.69, "Comprehensive benchmark results", Instant.now(), hardwareResults);
        benchmarkEntity = new BenchmarkEntity(1, user, 69.69, benchmarkResultEntity, Instant.now(), "", configuration);
        benchmarkResultEntity = List.of(
                new BenchmarkResultEntity(1, benchmarkEntity, 1, "GPU", "Gaming Test", 100.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(2, benchmarkEntity, 1, "GPU", "Shader Test", 50.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(3, benchmarkEntity, 1, "GPU", "Memory Test", 750.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(4, benchmarkEntity, 1, "CPU", "Single-Core Test", 850.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(5, benchmarkEntity, 1, "CPU", "Multi-Core Test", 100.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(6, benchmarkEntity, 1, "RAM", "Memory-Speed Test", 200.0, benchmarkEntity.getCreatedAt()));
        benchmarkEntity.setResults(benchmarkResultEntity);

        when(userRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(user));
        when(benchmarkResultRepository.findMinValueForEachTestType()).thenReturn(results);
        when(benchmarkRepository.save(isA(BenchmarkEntity.class))).thenReturn(benchmarkEntity);
        when(benchmarkResultFactory.getProcessor("CPU")).thenReturn(cpuBenchmarkResult);
        when(benchmarkResultFactory.getProcessor("GPU")).thenReturn(gpuBenchmarkResult);
        when(benchmarkResultFactory.getProcessor("RAM")).thenReturn(ramBenchmarkResult);
        when(cpuBenchmarkResult.getType()).thenReturn("CPU");
        when(gpuBenchmarkResult.getType()).thenReturn("GPU");
        when(ramBenchmarkResult.getType()).thenReturn("RAM");
        when(cpuRepository.findById(1L)).thenReturn(Optional.of(cpu));
        when(gpuRepository.findById(1L)).thenReturn(Optional.of(gpu));
        when(ramRepository.findById(1L)).thenReturn(Optional.of(ram));
        when(benchmarkConverter.mapToBenchmarkResponse(isA(BenchmarkEntity.class)))
                .thenReturn(benchmarkResponse);
    }
    @Test
    void testSaveBenchmark_success_overallScoreHighest() {
        BenchmarkResponse actual =  benchmarkService.saveBenchmark(request);

        when(benchmarkConverter.mapToBenchmarkResponse(isA(BenchmarkEntity.class)))
                .thenReturn(benchmarkResponse);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(benchmarkResponse);
    }

    @Test
    @DisplayName("Test saveBenchmark(BenchmarkSaveRequest); then return Description is 'The characteristics of someone or something'")
    void testSaveBenchmark_thenReturnDescriptionIsTheCharacteristicsOfSomeoneOrSomething() {
        benchmarkEntity.setConfiguration(configuration);
        benchmarkEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        benchmarkEntity.setDescription("The characteristics of someone or something");
        benchmarkEntity.setId(1);
        benchmarkEntity.setOverallScore(10.0d);
        benchmarkEntity.setResults(new ArrayList<>());
        benchmarkEntity.setUser(user);
        when(benchmarkRepository.save(Mockito.any())).thenReturn(benchmarkEntity);


        BenchmarkResponse.BenchmarkResponseBuilder descriptionResult = BenchmarkResponse.builder()
                .description("The characteristics of someone or something");
        BenchmarkResponse.BenchmarkResponseBuilder overallScoreResult = descriptionResult.hardwareResults(new ArrayList<>())
                .id(1L)
                .overallScore(10.0d);
        BenchmarkResponse buildResult = overallScoreResult
                .timestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant())
                .build();
        when(benchmarkConverter.mapToBenchmarkResponse(Mockito.any())).thenReturn(buildResult);
        when(benchmarkResultRepository.findMinValueForEachTestType()).thenReturn(new ArrayList<>());

        BenchmarkResponse actualSaveBenchmarkResult = benchmarkService.saveBenchmark(request);

        verify(benchmarkConverter).mapToBenchmarkResponse(isA(BenchmarkEntity.class));
        verify(benchmarkResultRepository).findMinValueForEachTestType();
        verify(userRepository).findById(1L);
        verify(benchmarkRepository).save(isA(BenchmarkEntity.class));
        assertEquals("The characteristics of someone or something", actualSaveBenchmarkResult.getDescription());
        Instant timestamp = actualSaveBenchmarkResult.getTimestamp();
        assertEquals(0, timestamp.getNano());
        assertEquals(0L, timestamp.getEpochSecond());
        assertEquals(10.0d, actualSaveBenchmarkResult.getOverallScore().doubleValue());
        assertEquals(1L, actualSaveBenchmarkResult.getId().longValue());
        assertTrue(actualSaveBenchmarkResult.getHardwareResults().isEmpty());
    }


    @Test
    @DisplayName("Test getBenchmarksByUserId(Long); given CPUEntity() ClockSpeed is ten; then return size is one")
    void testGetBenchmarksByUserId_givenCPUEntityClockSpeedIsTen_thenReturnSizeIsOne() {
        benchmarkEntity = new BenchmarkEntity(1, user, 69.69, benchmarkResultEntity, Instant.now(), "", configuration);
        benchmarkResultEntity = List.of(
                new BenchmarkResultEntity(1, benchmarkEntity, 1, "GPU", "Gaming Test", 100.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(2, benchmarkEntity, 1, "GPU", "Shader Test", 50.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(3, benchmarkEntity, 1, "GPU", "Memory Test", 750.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(4, benchmarkEntity, 1, "CPU", "Single-Core Test", 850.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(5, benchmarkEntity, 1, "CPU", "Multi-Core Test", 100.0, benchmarkEntity.getCreatedAt()),
                new BenchmarkResultEntity(6, benchmarkEntity, 1, "RAM", "Memory-Speed Test", 200.0, benchmarkEntity.getCreatedAt()));
        benchmarkEntity.setResults(benchmarkResultEntity);

        ArrayList<BenchmarkEntity> benchmarkEntityList = new ArrayList<>();
        benchmarkEntityList.add(benchmarkEntity);
        when(benchmarkRepository.findByUserId(anyInt())).thenReturn(benchmarkEntityList);
        BenchmarkResponse.BenchmarkResponseBuilder descriptionResult = BenchmarkResponse.builder()
                .description("The characteristics of someone or something");
        BenchmarkResponse.BenchmarkResponseBuilder overallScoreResult = descriptionResult.hardwareResults(new ArrayList<>())
                .id(1L)
                .overallScore(10.0d);
        BenchmarkResponse buildResult = overallScoreResult
                .timestamp(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant())
                .build();
        when(benchmarkConverter.mapToBenchmarkResponse(Mockito.any())).thenReturn(buildResult);

        List<BenchmarkResponse> actualBenchmarksByUserId = benchmarkService.getBenchmarksByUserId(1L);

        verify(benchmarkConverter).mapToBenchmarkResponse(isA(BenchmarkEntity.class));
        verify(benchmarkRepository).findByUserId(1);
        assertEquals(1, actualBenchmarksByUserId.size());
        BenchmarkResponse getResult = actualBenchmarksByUserId.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        Instant timestamp = getResult.getTimestamp();
        assertEquals(0, timestamp.getNano());
        assertEquals(0L, timestamp.getEpochSecond());
        assertEquals(10.0d, getResult.getOverallScore().doubleValue());
        assertEquals(1L, getResult.getId().longValue());
        assertTrue(getResult.getHardwareResults().isEmpty());
    }


    @Test
    @DisplayName("Test getBenchmarksByUserId(Long); then throw IllegalArgumentException")
    void testGetBenchmarksByUserId_thenThrowIllegalArgumentException() {
        when(benchmarkRepository.findByUserId(anyInt())).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> benchmarkService.getBenchmarksByUserId(1L));
        verify(benchmarkRepository).findByUserId(1);
    }


    @Test
    @DisplayName("Test getBenchmarks(); given BenchmarkConverter; then return Empty")
    void testGetBenchmarks_givenBenchmarkConverter_thenReturnEmpty() {
        when(benchmarkRepository.findAllBenchmarks()).thenReturn(new ArrayList<>());

        List<BenchmarkResponse> actualBenchmarks = benchmarkService.getBenchmarks();

        verify(benchmarkRepository).findAllBenchmarks();
        assertTrue(actualBenchmarks.isEmpty());
    }


    @Test
    @DisplayName("Test getBenchmarks(); then throw IllegalArgumentException")
    void testGetBenchmarks_thenThrowIllegalArgumentException() {
        UserEntity user2 = new UserEntity();
        user2.setAdmin(true);
        user2.setAvatarUrl("https://example.org/example");
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setPasswordHash("Password Hash");
        user2.setPasswordSalt("Password Salt");
        user2.setUsername("janedoe");

        ArrayList<BenchmarkEntity> benchmarkEntityList = new ArrayList<>();
        benchmarkEntityList.add(benchmarkEntity);
        when(benchmarkRepository.findAllBenchmarks()).thenReturn(benchmarkEntityList);
        when(benchmarkConverter.mapToBenchmarkResponse(Mockito.any()))
                .thenThrow(new IllegalArgumentException("foo"));

        assertThrows(IllegalArgumentException.class, () -> benchmarkService.getBenchmarks());
        verify(benchmarkConverter).mapToBenchmarkResponse(isA(BenchmarkEntity.class));
        verify(benchmarkRepository).findAllBenchmarks();
    }
}
