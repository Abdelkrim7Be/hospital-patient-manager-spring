package com.abdelkrim.hospital;

import com.abdelkrim.hospital.entities.Patient;
import com.abdelkrim.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class HospitalApplication implements CommandLineRunner {
    @Autowired
    public PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //First method
        Patient patient1 = new Patient();
        patient1.setId(null);
        patient1.setName("Bellagnech");
        patient1.setBirthDate(new Date());
        patient1.setSick(false);
        patient1.setScore(15);

        //Second method
        Patient patient2 = new Patient(null, "Elboubkari", new Date(), true, 20);

        //Third Method (Using Builder)
        Patient patient3 = Patient.builder()
                .name("Boukhari")
                .birthDate(new Date())
                .sick(false)
                .score(18)
                .build();
    }

}
