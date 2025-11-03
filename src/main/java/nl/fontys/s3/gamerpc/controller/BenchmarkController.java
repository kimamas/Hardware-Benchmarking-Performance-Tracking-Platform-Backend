package nl.fontys.s3.gamerpc.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.BenchmarkService;
import nl.fontys.s3.gamerpc.dto.benchmark.BenchmarkResponse;
import nl.fontys.s3.gamerpc.dto.benchmark.BenchmarkSaveRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/benchmark")
@AllArgsConstructor
public class BenchmarkController {
    private BenchmarkService benchmarkService;
    @PermitAll
    @PostMapping("/save")
    public ResponseEntity<BenchmarkResponse> saveBenchmark(@Valid @RequestBody BenchmarkSaveRequest benchmark) {
        try{
            BenchmarkResponse response = benchmarkService.saveBenchmark(benchmark);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving benchmark", e);

        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<BenchmarkResponse>> getBenchmarksForUser(@PathVariable Long id) {
        try {
            List<BenchmarkResponse> benchmarks = benchmarkService.getBenchmarksByUserId(id);
            return ResponseEntity.ok(benchmarks);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BenchmarkResponse>> getBenchmarks() {
        List<BenchmarkResponse> benchmarks = benchmarkService.getBenchmarks();
        return ResponseEntity.ok(benchmarks);
    }
}