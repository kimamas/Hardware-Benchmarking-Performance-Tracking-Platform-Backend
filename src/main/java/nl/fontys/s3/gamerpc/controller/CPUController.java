package nl.fontys.s3.gamerpc.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.CPUService;
import nl.fontys.s3.gamerpc.dto.cpu.CPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.cpu.CPUResponse;
import nl.fontys.s3.gamerpc.dto.cpu.CPUUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cpu")
@AllArgsConstructor
public class CPUController {
    private final CPUService cpuService;

    @GetMapping
    public ResponseEntity<List<CPUResponse>> getAllCPUs() {
        List<CPUResponse> cpus = cpuService.getAllCPUs();
        return ResponseEntity.ok(cpus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CPUResponse> getCPUById(@PathVariable long id) {
        try {
            CPUResponse cpu = cpuService.getCPUById(id);
            return ResponseEntity.ok(cpu);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPU not found", e);
        }
    }
    @PostMapping
    public ResponseEntity<CPUResponse> createCPU(@RequestBody @Valid CPUSaveRequest cpu) {
        CPUResponse savedCpu = cpuService.saveCPU(cpu);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCpu);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCPU(@PathVariable long id) {
        cpuService.deleteCPU(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CPUResponse> updateCPU(@PathVariable long id, @RequestBody CPUUpdateRequest cpu) {
        cpu.setId(id);
        cpuService.updateCPU(cpu);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
