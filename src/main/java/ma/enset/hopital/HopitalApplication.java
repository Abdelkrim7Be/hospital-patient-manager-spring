package ma.enset.hopital;

import ma.enset.hopital.entities.Patient;
import ma.enset.hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {
    @Autowired
    private PatientRepository patientRepository;

    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample data if database is empty
        if (patientRepository.count() == 0) {
            initializeCompleteTestData();
        }
    }

    private void initializeCompleteTestData() {
        System.out.println("🏥 Initializing complete hospital test data...");

        // Generate comprehensive patient data
        generatePatientData();
        
        System.out.println("✅ Complete hospital test data initialized successfully!");
        System.out.println("📊 Total patients created: " + patientRepository.count());
        printDataSummary();
    }

    private void generatePatientData() {
        System.out.println("👥 Creating patient records...");

        // Predefined patient data with realistic information
        createPredefinedPatients();
        
        // Generate additional realistic patients
        generateRandomPatients(100); // Create 100 additional patients
    }

    private void createPredefinedPatients() {
        // VIP Patients with detailed profiles
        Patient[] vipPatients = {
            Patient.builder()
                .name("Dr. Soufiane Elboubkari")
                .birthday(generateRandomDate(1980, 1995))
                .isSick(false)
                .score(950)
                .email("soufiane.elboubkari@hopital.ma")
                .phone("+212661234567")
                .notes("Médecin généraliste, excellent état de santé. Suivi préventif annuel.")
                .status(Patient.PatientStatus.ACTIVE)
                .build(),

            Patient.builder()
                .name("Mme. Fatima Zahra Bennani")
                .birthday(generateRandomDate(1970, 1985))
                .isSick(true)
                .score(400)
                .email("fatima.bennani@email.ma")
                .phone("+212662345678")
                .notes("Diabète type 2, hypertension artérielle. Suivi médical régulier nécessaire.")
                .status(Patient.PatientStatus.ACTIVE)
                .build(),

            Patient.builder()
                .name("M. Ahmed Alaoui")
                .birthday(generateRandomDate(1990, 2000))
                .isSick(false)
                .score(800)
                .email("ahmed.alaoui@gmail.com")
                .phone("+212663456789")
                .notes("Jeune adulte en excellente forme physique. Sportif professionnel.")
                .status(Patient.PatientStatus.ACTIVE)
                .build(),

            Patient.builder()
                .name("Mme. Aicha Tazi")
                .birthday(generateRandomDate(1960, 1975))
                .isSick(true)
                .score(300)
                .email("aicha.tazi@hotmail.com")
                .phone("+212664567890")
                .notes("Arthrite rhumatoïde, fibromyalgie. Traitement de longue durée.")
                .status(Patient.PatientStatus.ACTIVE)
                .build(),

            Patient.builder()
                .name("M. Omar Fassi")
                .birthday(generateRandomDate(1985, 1995))
                .isSick(false)
                .score(720)
                .email("omar.fassi@yahoo.com")
                .phone("+212665678901")
                .notes("Cadre supérieur, stress professionnel léger. Bilan de santé normal.")
                .status(Patient.PatientStatus.ACTIVE)
                .build()
        };

        for (Patient patient : vipPatients) {
            patientRepository.save(patient);
        }
    }

    private void generateRandomPatients(int count) {
        String[] prenoms = {
            "Mohamed", "Fatima", "Hassan", "Aicha", "Omar", "Khadija", "Youssef", "Zineb", 
            "Karim", "Salma", "Abdelaziz", "Nadia", "Rachid", "Laila", "Mustapha", "Samira",
            "Khalid", "Malika", "Said", "Souad", "Brahim", "Naima", "Driss", "Zakia",
            "Hamid", "Rajae", "Noureddine", "Habiba", "Abdellah", "Latifa"
        };

        String[] noms = {
            "Alami", "Benali", "Tazi", "Fassi", "Idrissi", "Bennani", "Cherkaoui", "Lahlou",
            "Berrada", "Kettani", "Skalli", "Chraibi", "Filali", "Hajji", "Squalli", "Amrani",
            "Sefrioui", "Belkadi", "Lamrani", "Benabdellah", "Alaoui", "Benjelloun", "Regragui",
            "Benabbes", "Charef", "Belghiti", "Naciri", "Benomar", "Ghazi", "Benkirane"
        };

        String[] specialites = {
            "Cardiologie", "Neurologie", "Oncologie", "Pédiatrie", "Gynécologie", "Orthopédie",
            "Dermatologie", "Psychiatrie", "Radiologie", "Chirurgie", "Médecine générale"
        };

        String[] maladies = {
            "Hypertension artérielle", "Diabète type 2", "Asthme", "Arthrite", "Migraine chronique",
            "Dépression", "Anxiété", "Cholestérol élevé", "Gastrite", "Insomnie", "Allergies",
            "Bronchite chronique", "Ostéoporose", "Thyroïde", "Anémie"
        };

        for (int i = 0; i < count; i++) {
            String prenom = prenoms[random.nextInt(prenoms.length)];
            String nom = noms[random.nextInt(noms.length)];
            String fullName = prenom + " " + nom;
            
            boolean isSick = random.nextDouble() < 0.3; // 30% de patients malades
            int score = generateRealisticScore(isSick);
            
            String notes = generateRealisticNotes(isSick, maladies, specialites);
            
            Patient patient = Patient.builder()
                .name(fullName)
                .birthday(generateRandomDate(1940, 2005))
                .isSick(isSick)
                .score(score)
                .email(generateEmail(prenom, nom, i))
                .phone(generateMoroccanPhone())
                .notes(notes)
                .status(generateRandomStatus())
                .build();

            patientRepository.save(patient);
        }
    }

    private Date generateRandomDate(int startYear, int endYear) {
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private int generateRealisticScore(boolean isSick) {
        if (isSick) {
            return 100 + random.nextInt(500); // 100-600 pour malades
        } else {
            return 500 + random.nextInt(500); // 500-1000 pour sains
        }
    }

    private String generateEmail(String prenom, String nom, int index) {
        String[] domaines = {"gmail.com", "hotmail.com", "yahoo.com", "outlook.com", "email.ma", "hopital.ma"};
        String domaine = domaines[random.nextInt(domaines.length)];
        return prenom.toLowerCase() + "." + nom.toLowerCase() + "." + (index + 1000) + "@" + domaine;
    }

    private String generateMoroccanPhone() {
        String[] prefixes = {"61", "62", "63", "64", "65", "66", "67", "68", "69"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        return "+2126" + prefix + String.format("%06d", random.nextInt(1000000));
    }

    private Patient.PatientStatus generateRandomStatus() {
        Patient.PatientStatus[] statuses = Patient.PatientStatus.values();
        if (random.nextDouble() < 0.7) {
            return Patient.PatientStatus.ACTIVE;
        }
        return statuses[random.nextInt(statuses.length)];
    }

    private String generateRealisticNotes(boolean isSick, String[] maladies, String[] specialites) {
        if (isSick) {
            String maladie = maladies[random.nextInt(maladies.length)];
            String specialite = specialites[random.nextInt(specialites.length)];
            String[] traitements = {
                "Traitement médicamenteux en cours",
                "Suivi médical régulier nécessaire",
                "Hospitalisation récente",
                "Rééducation en cours",
                "Surveillance médicale renforcée"
            };
            String traitement = traitements[random.nextInt(traitements.length)];
            return String.format("Diagnostic: %s. Spécialité: %s. %s. Dernière consultation: %s.",
                    maladie, specialite, traitement, generateRandomDateString());
        } else {
            String[] notesPositives = {
                "Bilan de santé excellent",
                "Aucun antécédent médical significatif",
                "Contrôle préventif annuel",
                "Excellente condition physique",
                "Suivi de routine, aucun problème détecté",
                "Vaccination à jour, état de santé optimal"
            };
            return notesPositives[random.nextInt(notesPositives.length)] + 
                   ". Dernière visite: " + generateRandomDateString() + ".";
        }
    }

    private String generateRandomDateString() {
        String[] mois = {"janvier", "février", "mars", "avril", "mai", "juin",
                        "juillet", "août", "septembre", "octobre", "novembre", "décembre"};
        int jour = 1 + random.nextInt(28);
        String moisStr = mois[random.nextInt(mois.length)];
        int annee = 2020 + random.nextInt(4);
        return jour + " " + moisStr + " " + annee;
    }

    private void printDataSummary() {
        System.out.println("\n📋 RÉSUMÉ DES DONNÉES GÉNÉRÉES:");
        System.out.println("================================");
        
        long totalPatients = patientRepository.count();
        long sickPatients = patientRepository.countByIsSickTrue();
        long healthyPatients = patientRepository.countByIsSickFalse();
        
        System.out.println("👥 Total des patients: " + totalPatients);
        System.out.println("🤒 Patients malades: " + sickPatients + " (" + 
                          String.format("%.1f", (sickPatients * 100.0 / totalPatients)) + "%)");
        System.out.println("💚 Patients en bonne santé: " + healthyPatients + " (" + 
                          String.format("%.1f", (healthyPatients * 100.0 / totalPatients)) + "%)");
        
        // Statistiques par statut
        for (Patient.PatientStatus status : Patient.PatientStatus.values()) {
            long count = patientRepository.countByStatus(status);
            System.out.println("📊 Statut " + status.name() + ": " + count + " patients");
        }
        
        System.out.println("\n🎯 L'application est prête avec des données réalistes!");
        System.out.println("🌐 Accès: http://localhost:8080");
        System.out.println("🗄️  Console H2: http://localhost:8080/h2-console");
        System.out.println("================================\n");
    }
}