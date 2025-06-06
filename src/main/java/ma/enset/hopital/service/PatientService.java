package ma.enset.hopital.service;

import ma.enset.hopital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    
    // Basic CRUD operations
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Page<Patient> findAll(Pageable pageable);
    void deleteById(Long id);
    boolean existsById(Long id);
    
    // Search operations
    Page<Patient> searchByName(String name, Pageable pageable);
    Page<Patient> findBySickStatus(boolean isSick, Pageable pageable);
    Page<Patient> findByStatus(Patient.PatientStatus status, Pageable pageable);
    Page<Patient> findByScoreRange(int minScore, int maxScore, Pageable pageable);
    
    // Advanced search
    Page<Patient> searchWithFilters(String name, Boolean isSick, 
                                   Patient.PatientStatus status, 
                                   Integer minScore, Integer maxScore, 
                                   Pageable pageable);
    
    // Business logic
    Patient updatePatientStatus(Long id, Patient.PatientStatus status);
    Patient updatePatientScore(Long id, int score);
    
    // Statistics
    long getTotalPatients();
    long getSickPatientsCount();
    long getHealthyPatientsCount();
    Double getAverageScore();
    List<Object[]> getPatientCountByStatus();
    
    // Validation
    boolean isEmailUnique(String email, Long excludeId);
    List<Patient> findDuplicatesByName(String name);
}
