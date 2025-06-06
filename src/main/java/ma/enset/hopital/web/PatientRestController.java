package ma.enset.hopital.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PatientRestController {

    private final PatientService patientService;

    // Get all patients with pagination
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                       Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Page<Patient> patients = search != null && !search.isEmpty() ?
                    patientService.searchByName(search, PageRequest.of(page, size, sort)) :
                    patientService.findAll(PageRequest.of(page, size, sort));

            Map<String, Object> response = new HashMap<>();
            response.put("patients", patients.getContent());
            response.put("currentPage", patients.getNumber());
            response.put("totalItems", patients.getTotalElements());
            response.put("totalPages", patients.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching patients", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.findById(id);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Create new patient
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (Exception e) {
            log.error("Error creating patient", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update patient
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                               @Valid @RequestBody Patient patient) {
        try {
            if (!patientService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            patient.setId(id);
            Patient updatedPatient = patientService.save(patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            log.error("Error updating patient", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete patient
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            if (!patientService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            patientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting patient", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalPatients", patientService.getTotalPatients());
            stats.put("sickPatients", patientService.getSickPatientsCount());
            stats.put("healthyPatients", patientService.getHealthyPatientsCount());
            stats.put("averageScore", patientService.getAverageScore());
            stats.put("statusCounts", patientService.getPatientCountByStatus());

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update patient status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Patient> updatePatientStatus(@PathVariable Long id,
                                                      @RequestParam Patient.PatientStatus status) {
        try {
            Patient updatedPatient = patientService.updatePatientStatus(id, status);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            log.error("Error updating patient status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Search with filters
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isSick,
            @RequestParam(required = false) Patient.PatientStatus status,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore) {

        try {
            Page<Patient> patients = patientService.searchWithFilters(
                name, isSick, status, minScore, maxScore,
                PageRequest.of(page, size)
            );

            Map<String, Object> response = new HashMap<>();
            response.put("patients", patients.getContent());
            response.put("currentPage", patients.getNumber());
            response.put("totalItems", patients.getTotalElements());
            response.put("totalPages", patients.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching patients", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
