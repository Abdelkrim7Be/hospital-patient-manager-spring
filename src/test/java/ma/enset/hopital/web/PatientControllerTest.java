package ma.enset.hopital.web;

import ma.enset.hopital.config.TestConfig;
import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class, useDefaultFilters = false)
@Import({PatientControllerTest.TestSecurityConfig.class})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

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
    @WithMockUser(roles = "USER")
    void testIndexPage() throws Exception {
        // Given
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(testPatient));
        when(patientService.searchByName(anyString(), any(PageRequest.class))).thenReturn(patientPage);

        // When & Then
        mockMvc.perform(get("/user/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("Patients"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attributeExists("pages"))
                .andExpect(model().attributeExists("currentPage"));

        verify(patientService).searchByName(anyString(), any(PageRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testIndexPageWithSearch() throws Exception {
        // Given
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(testPatient));
        when(patientService.searchByName(eq("Test"), any(PageRequest.class))).thenReturn(patientPage);

        // When & Then
        mockMvc.perform(get("/user/index")
                        .param("keyword", "Test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("Patients"))
                .andExpect(model().attribute("keyword", "Test"));

        verify(patientService).searchByName(eq("Test"), any(PageRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeletePatient() throws Exception {
        // Given
        doNothing().when(patientService).deleteById(1L);

        // When & Then
        mockMvc.perform(get("/deletePatient")
                        .param("id", "1")
                        .param("keyword", "")
                        .param("page", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/index?page=0&keyword="));

        verify(patientService).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeletePatientAccessDenied() throws Exception {
        // When & Then
        mockMvc.perform(get("/deletePatient")
                        .param("id", "1"))
                .andExpect(status().isForbidden());

        verify(patientService, never()).deleteById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFormPatients() throws Exception {
        // When & Then
        mockMvc.perform(get("/admin/formPatients"))
                .andExpect(status().isOk())
                .andExpect(view().name("formPatients"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSavePatient() throws Exception {
        // Given
        when(patientService.save(any(Patient.class))).thenReturn(testPatient);

        // When & Then
        mockMvc.perform(post("/admin/save")
                        .with(csrf())
                        .param("name", "Test Patient")
                        .param("birthday", "2000-01-01")
                        .param("score", "750")
                        .param("email", "test@example.com")
                        .param("phone", "+212612345678")
                        .param("status", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/index?page=0&keyword="));

        verify(patientService).save(any(Patient.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSavePatientValidationError() throws Exception {
        // When & Then
        mockMvc.perform(post("/admin/save")
                        .with(csrf())
                        .param("name", "") // Invalid: empty name
                        .param("birthday", "2000-01-01")
                        .param("score", "750"))
                .andExpect(status().isOk())
                .andExpect(view().name("formPatients"))
                .andExpect(model().attributeHasFieldErrors("patient", "name"));

        verify(patientService, never()).save(any(Patient.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEditPatients() throws Exception {
        // Given
        when(patientService.findById(1L)).thenReturn(Optional.of(testPatient));

        // When & Then
        mockMvc.perform(get("/admin/editPatients")
                        .param("id", "1")
                        .param("keyword", "")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("editPatients"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("statuses"));

        verify(patientService).findById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDashboard() throws Exception {
        // Given
        when(patientService.getTotalPatients()).thenReturn(100L);
        when(patientService.getSickPatientsCount()).thenReturn(30L);
        when(patientService.getHealthyPatientsCount()).thenReturn(70L);
        when(patientService.getAverageScore()).thenReturn(650.5);

        // When & Then
        mockMvc.perform(get("/user/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("totalPatients"))
                .andExpect(model().attributeExists("sickPatients"))
                .andExpect(model().attributeExists("healthyPatients"))
                .andExpect(model().attributeExists("averageScore"));

        verify(patientService).getTotalPatients();
        verify(patientService).getSickPatientsCount();
        verify(patientService).getHealthyPatientsCount();
        verify(patientService).getAverageScore();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testPatientDetails() throws Exception {
        // Given
        when(patientService.findById(1L)).thenReturn(Optional.of(testPatient));

        // When & Then
        mockMvc.perform(get("/user/patient/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patientDetails"))
                .andExpect(model().attributeExists("patient"));

        verify(patientService).findById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testPatientDetailsNotFound() throws Exception {
        // Given
        when(patientService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/user/patient/999"))
                .andExpect(status().isInternalServerError());

        verify(patientService).findById(999L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdvancedSearch() throws Exception {
        // Given
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(testPatient));
        when(patientService.searchWithFilters(anyString(), any(), any(), any(), any(), any(PageRequest.class)))
                .thenReturn(patientPage);

        // When & Then
        mockMvc.perform(get("/user/search")
                        .param("name", "Test")
                        .param("isSick", "false")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(view().name("advancedSearch"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attributeExists("statuses"));

        verify(patientService).searchWithFilters(anyString(), any(), any(), any(), any(), any(PageRequest.class));
    }

    @Test
    void testHomeRedirect() throws Exception {
        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/index"));
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfig {
        
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
            String encodedPassword = passwordEncoder.encode("1234");
            return new InMemoryUserDetailsManager(
                    User.withUsername("admin").password(encodedPassword).roles("USER", "ADMIN").build(),
                    User.withUsername("user").password(encodedPassword).roles("USER").build()
            );
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(ar -> ar
                            .requestMatchers("/deletePatient/**").hasRole("ADMIN")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/user/**").hasRole("USER")
                            .anyRequest().authenticated())
                    .csrf(csrf -> csrf.disable())
                    .build();
        }
    }
}
