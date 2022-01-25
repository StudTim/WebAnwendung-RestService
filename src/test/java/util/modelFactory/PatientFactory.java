package util.modelFactory;

import com.github.javafaker.Faker;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PatientFactory {

    private static final Faker faker = new Faker();

    public static List<Patient> generatePatients(int amount) {
        List<Patient> patients = new LinkedList<>();
        for (int round = 0; round < amount; round++) {
            Patient patient = new Patient();
            patient.setCareConcept(faker.animal().name());
            patient.setFirstname(faker.name().firstName());
            patient.setLastname(faker.name().lastName());
            patient.setIdentifier(faker.name().lastName());
            patient.setPatComment(faker.starTrek().character());
            patient.setId(UUID.randomUUID());
            patients.add(patient);
        }
        return patients;
    }


}
