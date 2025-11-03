package nl.fontys.s3.gamerpc.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.RAMService;
import nl.fontys.s3.gamerpc.dto.ram.RAMResponse;
import nl.fontys.s3.gamerpc.dto.ram.RAMSaveRequest;
import nl.fontys.s3.gamerpc.dto.ram.RAMUpdateRequest;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/ram")
@AllArgsConstructor
public class RAMController {
    private final RAMService ramService;

    @GetMapping
    public ResponseEntity<List<RAMEntity>> getAllRAMs() {
        List<RAMEntity> rams = ramService.getAllRAMs();
        return ResponseEntity.ok(rams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RAMResponse> getRAMById(@PathVariable long id) {
        try {
            RAMResponse ram = ramService.getRAMById(id);
            return ResponseEntity.ok(ram);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RAM not found with id: " + id, e);
        }
    }

    @PostMapping
    public ResponseEntity<RAMResponse> createRAM(@RequestBody @Valid RAMSaveRequest ram) {
        RAMResponse savedRam = ramService.saveRAM(ram);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRAM(@PathVariable long id) {
        ramService.deleteRAM(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RAMResponse> updateRAM(@PathVariable long id, @RequestBody @Valid RAMUpdateRequest ram) {
        ram.setId(id);
        ramService.updateRAM(ram);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
