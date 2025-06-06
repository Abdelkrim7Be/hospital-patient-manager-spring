package ma.enset.hopital.repository;

import ma.enset.hopital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Basic search methods
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find by specific criteria
    Page<Patient> findByIsSick(boolean isSick, Pageable pageable);
    Page<Patient> findByStatus(Patient.PatientStatus status, Pageable pageable);
    Page<Patient> findByScoreBetween(int minScore, int maxScore, Pageable pageable);
    
    // Find by email
    Patient findByEmail(String email);
    
    // Count methods for statistics
    long countByIsSickTrue();
    long countByIsSickFalse();
    long countByStatus(Patient.PatientStatus status);
    
    // Advanced search with multiple criteria
    @Query("SELECT p FROM Patient p WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:isSick IS NULL OR p.isSick = :isSick) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:minScore IS NULL OR p.score >= :minScore) AND " +
           "(:maxScore IS NULL OR p.score <= :maxScore)")
    Page<Patient> findPatientsWithFilters(@Param("name") String name,
                                         @Param("isSick") Boolean isSick,
                                         @Param("status") Patient.PatientStatus status,
                                         @Param("minScore") Integer minScore,
                                         @Param("maxScore") Integer maxScore,
                                         Pageable pageable);
    
    // Statistics queries
    @Query("SELECT AVG(p.score) FROM Patient p")
    Double findAverageScore();
    
    @Query("SELECT p.status, COUNT(p) FROM Patient p GROUP BY p.status")
    Object[][] findPatientCountByStatus();
}
