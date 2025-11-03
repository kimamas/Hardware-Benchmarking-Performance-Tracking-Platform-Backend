package nl.fontys.s3.gamerpc.controller;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.SystemConfigurationService;
import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationRequest;
import nl.fontys.s3.gamerpc.dto.system_configuration.SystemConfigurationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
@AllArgsConstructor
public class SystemConfigurationController {
    private SystemConfigurationService systemConfigurationService;
    @PostMapping("/user/{id}")
    public ResponseEntity<SystemConfigurationResponse> saveSystemConfiguration(@RequestBody SystemConfigurationRequest configuration, @PathVariable int id) {
        SystemConfigurationResponse response = systemConfigurationService.saveConfiguration(configuration, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<SystemConfigurationResponse> getBenchmarksForUser(@PathVariable Long id) {
        SystemConfigurationResponse response = systemConfigurationService.getConfigurationById(id);
        return ResponseEntity.ok(response);
    }
}
