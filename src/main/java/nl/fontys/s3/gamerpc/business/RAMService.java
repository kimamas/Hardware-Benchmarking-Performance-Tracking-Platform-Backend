package nl.fontys.s3.gamerpc.business;

import lombok.AllArgsConstructor;
import nl.fontys.s3.gamerpc.business.converter.RAMConverter;
import nl.fontys.s3.gamerpc.dto.ram.RAMSaveRequest;
import nl.fontys.s3.gamerpc.dto.ram.RAMResponse;
import nl.fontys.s3.gamerpc.dto.ram.RAMUpdateRequest;
import nl.fontys.s3.gamerpc.exception.AlreadyExists;
import nl.fontys.s3.gamerpc.persistence.iinterface.RAMRepository;
import nl.fontys.s3.gamerpc.persistence.entity.RAMEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RAMService {
    private final RAMRepository ramRepository;

    public List<RAMEntity> getAllRAMs() {
        return ramRepository.findAll();
    }

    public RAMResponse getRAMById(long id) {
        RAMEntity ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RAM not found"));
        return RAMConverter.fromEntityToResponse(ram);
    }

    public RAMResponse saveRAM(RAMSaveRequest ram) {
        if (ramRepository.existsByModel(ram.getModel())) {
            throw new AlreadyExists("A RAM with this model already exists.");
        }
        RAMEntity savedRAM = ramRepository.save(RAMConverter.fromSaveRequestToEntity(ram));
        return RAMConverter.fromEntityToResponse(savedRAM);
    }

    public void deleteRAM(long id) {
        if (!ramRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RAM with ID " + id + " not found");
        }
        ramRepository.deleteById(id);
    }

    public RAMResponse updateRAM(RAMUpdateRequest ram) {
        RAMEntity existingRam = ramRepository.findById(ram.getId())
                .orElseThrow(() -> new AlreadyExists("Ram is not found."));

        if (!Objects.equals(existingRam.getModel(), ram.getModel()) && ramRepository.existsByModel(ram.getModel())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "RAM with this model already exists.");
        }

        RAMEntity updatedRAM = ramRepository.save(RAMConverter.fromUpdateRequestToEntity(ram));
        return RAMConverter.fromEntityToResponse(updatedRAM);
    }
}
