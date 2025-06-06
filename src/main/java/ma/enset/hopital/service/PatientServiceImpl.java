package ma.enset.hopital.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    
    @Override
    public Patient save(Patient patient) {
        log.debug("Saving patient: {}", patient.getName());
        
        // Validate email uniqueness
        if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            if (!isEmailUnique(patient.getEmail(), patient.getId())) {
                throw new IllegalArgumentException("Email already exists: " + patient.getEmail());
            }
        }
        
        return patientRepository.save(patient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }
    
    @Override
    public void deleteById(Long id) {
        log.debug("Deleting patient with id: {}", id);
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> searchByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return patientRepository.findAll(pageable);
        }
        return patientRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findBySickStatus(boolean isSick, Pageable pageable) {
        return patientRepository.findByIsSick(isSick, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByStatus(Patient.PatientStatus status, Pageable pageable) {
        return patientRepository.findByStatus(status, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> findByScoreRange(int minScore, int maxScore, Pageable pageable) {
        return patientRepository.findByScoreBetween(minScore, maxScore, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Patient> searchWithFilters(String name, Boolean isSick, 
                                          Patient.PatientStatus status, 
                                          Integer minScore, Integer maxScore, 
                                          Pageable pageable) {
        return patientRepository.findPatientsWithFilters(name, isSick, status, minScore, maxScore, pageable);
    }
    
    @Override
    public Patient updatePatientStatus(Long id, Patient.PatientStatus status) {
        Optional<Patient> patientOpt = patientRepository.findById(id);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setStatus(status);
            return patientRepository.save(patient);
        } else {
            throw new RuntimeException("Patient not found with id: " + id);
        }
    }
    
    @Override
    public Patient updatePatientScore(Long id, int score) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
        
        if (score < 0 || score > 1000) {
            throw new IllegalArgumentException("Score must be between 0 and 1000");
        }
        
        patient.setScore(score);
        log.debug("Updated patient {} score to {}", id, score);
        return patientRepository.save(patient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalPatients() {
        return patientRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getSickPatientsCount() {
        return patientRepository.countByIsSickTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getHealthyPatientsCount() {
        return patientRepository.countByIsSickFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageScore() {
        Double average = patientRepository.findAverageScore();
        return average != null ? average : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getPatientCountByStatus() {
        Object[][] results = patientRepository.findPatientCountByStatus();
        List<Object[]> statusCounts = new ArrayList<>();
        
        for (Object[] result : results) {
            statusCounts.add(result);
        }
        
        return statusCounts;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailUnique(String email, Long excludeId) {
        Patient existingPatient = patientRepository.findByEmail(email);
        return existingPatient == null || 
               (excludeId != null && existingPatient.getId().equals(excludeId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> findDuplicatesByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name, Pageable.unpaged()).getContent();
    }
}
