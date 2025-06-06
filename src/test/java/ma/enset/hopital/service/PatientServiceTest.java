package ma.enset.hopital.service;

import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1L)
                .name("Test Patient")
                .birthday(new Date())
                .isSick(false)
                .score(750)
                .email("test@example.com")
                .phone("+212612345678")
                .status(Patient.PatientStatus.ACTIVE)
                .build();
    }

    @Test
    void testSavePatient() {
        // Given
        when(patientRepository.findByEmail(testPatient.getEmail())).thenReturn(null);
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient savedPatient = patientService.save(testPatient);

        // Then
        assertNotNull(savedPatient);
        assertEquals(testPatient.getName(), savedPatient.getName());
        assertEquals(testPatient.getEmail(), savedPatient.getEmail());
        verify(patientRepository).save(testPatient);
    }

    @Test
    void testSavePatientWithDuplicateEmail() {
        // Given
        Patient existingPatient = Patient.builder()
                .id(2L)
                .email(testPatient.getEmail())
                .build();
        when(patientRepository.findByEmail(testPatient.getEmail())).thenReturn(existingPatient);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.save(testPatient));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testFindById() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When
        Optional<Patient> foundPatient = patientService.findById(1L);

        // Then
        assertTrue(foundPatient.isPresent());
        assertEquals(testPatient.getName(), foundPatient.get().getName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Patient> foundPatient = patientService.findById(999L);

        // Then
        assertFalse(foundPatient.isPresent());
        verify(patientRepository).findById(999L);
    }

    @Test
    void testDeleteById() {
        // Given
        when(patientRepository.existsById(1L)).thenReturn(true);

        // When
        patientService.deleteById(1L);

        // Then
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        // Given
        when(patientRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.deleteById(999L));
        verify(patientRepository, never()).deleteById(any());
    }

    @Test
    void testSearchByName() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(testPatient));
        when(patientRepository.findByNameContainingIgnoreCase("Test", pageable)).thenReturn(patientPage);

        // When
        Page<Patient> result = patientService.searchByName("Test", pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testPatient.getName(), result.getContent().get(0).getName());
        verify(patientRepository).findByNameContainingIgnoreCase("Test", pageable);
    }

    @Test
    void testUpdatePatientStatus() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient updatedPatient = patientService.updatePatientStatus(1L, Patient.PatientStatus.INACTIVE);

        // Then
        assertNotNull(updatedPatient);
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void testUpdatePatientScore() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        Patient updatedPatient = patientService.updatePatientScore(1L, 800);

        // Then
        assertNotNull(updatedPatient);
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void testUpdatePatientScoreInvalidRange() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> patientService.updatePatientScore(1L, 1500));
        assertThrows(IllegalArgumentException.class, () -> patientService.updatePatientScore(1L, -100));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testGetStatistics() {
        // Given
        when(patientRepository.count()).thenReturn(100L);
        when(patientRepository.countByIsSickTrue()).thenReturn(30L);
        when(patientRepository.countByIsSickFalse()).thenReturn(70L);
        when(patientRepository.findAverageScore()).thenReturn(650.5);

        // When
        long totalPatients = patientService.getTotalPatients();
        long sickPatients = patientService.getSickPatientsCount();
        long healthyPatients = patientService.getHealthyPatientsCount();
        Double averageScore = patientService.getAverageScore();

        // Then
        assertEquals(100L, totalPatients);
        assertEquals(30L, sickPatients);
        assertEquals(70L, healthyPatients);
        assertEquals(650.5, averageScore);
    }

    @Test
    void testIsEmailUnique() {
        // Given
        when(patientRepository.findByEmail("unique@example.com")).thenReturn(null);
        when(patientRepository.findByEmail("existing@example.com")).thenReturn(testPatient);

        // When & Then
        assertTrue(patientService.isEmailUnique("unique@example.com", null));
        assertFalse(patientService.isEmailUnique("existing@example.com", null));
        assertTrue(patientService.isEmailUnique("existing@example.com", testPatient.getId()));
    }
}
