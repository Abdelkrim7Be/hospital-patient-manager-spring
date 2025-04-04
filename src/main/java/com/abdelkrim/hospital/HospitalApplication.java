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
        patientRepository.save(new Patient(null, "Bellagnech", new Date(), false, 10));
        patientRepository.save(new Patient(null, "Boukhari", new Date(), true, 20));
        patientRepository.save(new Patient(null, "Zirouda", new Date(), true, 40));
    }

}
