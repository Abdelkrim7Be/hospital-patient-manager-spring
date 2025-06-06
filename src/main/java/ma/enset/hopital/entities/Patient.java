package ma.enset.hopital.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date birthday;

    @Column(name = "is_sick")
    private boolean isSick;

    @Min(value = 0, message = "Le score ne peut pas être négatif")
    @Max(value = 1000, message = "Le score ne peut pas dépasser 1000")
    @Column(nullable = false)
    private int score;

    @Email(message = "Format d'email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\.\\(\\)]{8,20}$", message = "Format de téléphone invalide")
    private String phone;

    @Size(max = 1000, message = "Les notes ne peuvent pas dépasser 1000 caractères")
    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le statut est obligatoire")
    @Builder.Default
    private PatientStatus status = PatientStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    // Calculate age from birthday
    @Transient
    public int getAge() {
        if (birthday == null) return 0;
        LocalDate birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Get status display name
    @Transient
    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "";
    }

    // Health status display
    @Transient
    public String getHealthStatus() {
        return isSick ? "Malade" : "En bonne santé";
    }

    // Score category for display
    @Transient
    public String getScoreCategory() {
        if (score >= 800) return "Excellent";
        if (score >= 600) return "Bon";
        if (score >= 400) return "Moyen";
        return "Faible";
    }

    public enum PatientStatus {
        ACTIVE("Actif"),
        INACTIVE("Inactif"), 
        DISCHARGED("Sorti"),
        SUSPENDED("Suspendu");
        
        private final String displayName;
        
        PatientStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
