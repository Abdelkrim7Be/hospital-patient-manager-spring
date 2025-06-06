package ma.enset.hopital.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

    @GetMapping("/user/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "10") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword,
                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                        @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Page<Patient> patients = patientService.searchByName(keyword,
                                                           PageRequest.of(page, size, sort));

        model.addAttribute("listPatients", patients.getContent());
        model.addAttribute("patients", patients);  // Add the full Page object
        model.addAttribute("pages", new int[patients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("totalElements", patients.getTotalElements());

        return "Patients";
    }

    @GetMapping("/deletePatient")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam(name = "id") Long id,
                         @RequestParam(name="keyword", defaultValue = "") String keyword,
                         @RequestParam(name="page", defaultValue = "0") int page,
                         RedirectAttributes redirectAttributes) {
        try {
            patientService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient supprimé avec succès");
        } catch (Exception e) {
            log.error("Error deleting patient with id: {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du patient");
        }
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ADMIN')")
    public String formPatients(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("statuses", Patient.PatientStatus.values());
        return "formPatients";
    }

    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", Patient.PatientStatus.values());
            return "formPatients";
        }

        try {
            patientService.save(patient);
            redirectAttributes.addFlashAttribute("successMessage",
                patient.getId() == null ? "Patient ajouté avec succès" : "Patient modifié avec succès");
        } catch (Exception e) {
            log.error("Error saving patient: {}", patient.getName(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la sauvegarde: " + e.getMessage());
        }

        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/editPatients")
    @PreAuthorize("hasRole('ADMIN')")
    public String editPatients(Model model, @RequestParam Long id,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        Patient patient = patientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statuses", Patient.PatientStatus.values());
        return "editPatients";
    }

    // Dashboard with statistics
    @GetMapping("/user/dashboard")
    public String dashboard(Model model) {
        try {
            // Get basic statistics
            long totalPatients = patientService.getTotalPatients();
            long sickPatients = patientService.getSickPatientsCount();
            long healthyPatients = patientService.getHealthyPatientsCount();
            Double averageScore = patientService.getAverageScore();
            
            // Get status counts
            List<Object[]> statusCounts = patientService.getPatientCountByStatus();
            
            // Add to model
            model.addAttribute("totalPatients", totalPatients);
            model.addAttribute("sickPatients", sickPatients);
            model.addAttribute("healthyPatients", healthyPatients);
            model.addAttribute("averageScore", averageScore != null ? Math.round(averageScore * 100.0) / 100.0 : 0.0);
            model.addAttribute("statusCounts", statusCounts);
            
            log.info("Dashboard loaded - Total: {}, Sick: {}, Healthy: {}, Avg Score: {}", 
                    totalPatients, sickPatients, healthyPatients, averageScore);
            
            return "dashboard";
        } catch (Exception e) {
            log.error("Error loading dashboard", e);
            model.addAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
            return "dashboard";
        }
    }

    // Advanced search
    @GetMapping("/user/search")
    public String advancedSearch(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "10") int size,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) Boolean isSick,
                                @RequestParam(required = false) Patient.PatientStatus status,
                                @RequestParam(required = false) Integer minScore,
                                @RequestParam(required = false) Integer maxScore) {

        Page<Patient> patients = patientService.searchWithFilters(name, isSick, status,
                                                                 minScore, maxScore,
                                                                 PageRequest.of(page, size));

        model.addAttribute("patients", patients.getContent());
        model.addAttribute("pages", new int[patients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("statuses", Patient.PatientStatus.values());
        model.addAttribute("searchParams", model.asMap());

        return "advancedSearch";
    }

    // Quick status update (AJAX endpoint)
    @PostMapping("/admin/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String updateStatus(@RequestParam Long id, @RequestParam Patient.PatientStatus status) {
        try {
            patientService.updatePatientStatus(id, status);
            return "success";
        } catch (Exception e) {
            log.error("Error updating patient status", e);
            return "error: " + e.getMessage();
        }
    }

    // Patient details view
    @GetMapping("/user/patient/{id}")
    public String patientDetails(@PathVariable Long id, Model model) {
        Patient patient = patientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        model.addAttribute("patient", patient);
        return "patientDetails";
    }

    // Test info page showing security and data status
    @GetMapping("/user/test-info")
    public String testInfo() {
        return "test-info";
    }

}