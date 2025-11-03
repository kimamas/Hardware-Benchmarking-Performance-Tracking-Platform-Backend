package nl.fontys.s3.gamerpc.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.GPUService;
import nl.fontys.s3.gamerpc.domain.GPU;
import nl.fontys.s3.gamerpc.dto.gpu.GPUSaveRequest;
import nl.fontys.s3.gamerpc.dto.gpu.GPUResponse;
import nl.fontys.s3.gamerpc.dto.gpu.GPUUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/gpu")
@AllArgsConstructor
public class GPUController {
    private final GPUService gpuService;

    @GetMapping
    public ResponseEntity<List<GPUResponse>> getAllGPUs() {
        List<GPUResponse> gpus = gpuService.getAllGPUs();
        return ResponseEntity.ok(gpus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GPUResponse> getGPUById(@PathVariable long id) {
        try {
            GPUResponse gpu = gpuService.getGPUById(id);
            return ResponseEntity.ok(gpu);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GPU not found with id: " + id, e);
        }
    }
    @PostMapping
    public ResponseEntity<GPUResponse> createGPU(@RequestBody @Valid GPUSaveRequest gpu) {
            GPUResponse savedGpu = gpuService.saveGPU(gpu);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGpu);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGPU(@PathVariable long id) {
        gpuService.deleteGPU(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<GPU> updateGPU(@PathVariable long id, @RequestBody GPUUpdateRequest gpu) {
        gpu.setId(id);
        gpuService.updateGPU(gpu);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
