package sssIT.Bachelorarbeit.Tim.restService.application.service;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.NotFoundException;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Nurse;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.DoctorRepository;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.NursePatientRepository;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.PatientRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Dieser Service kümmert sich um die fachliche Abwicklung aller Patienten im System.
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private NursePatientRepository nursePatientRepository;

    /**
     * @return Gibt alle Patienten zurück.
     */
    public Collection<Patient> all() {
        return this.patientRepository.findAll();
    }
    /**
     * Sucht einen Patienten anhand der übergebenen Id. Löst eine Exception aus, falls der
     * Patient nicht existiert.
     * @param id Die Id des Patienten nachdem gesucht werden soll.
     * @return Der Patient mit der angegeben Id.
     * @throws NotFoundException falls der Patient nicht gefunden werden konnte.
     */
    public Patient findById(UUID id) {
        Optional<Patient> patient = this.patientRepository.findById(id);
        if (patient.isEmpty()) {
            throw new NotFoundException("Der Patient mit der Id " + id.toString() + " konnte nicht gefunden werden");
        }
        return patient.get();
    }
    /**
     * Legt einen neuen Patienten im System an und speichert ihn in der Datenbank.
     * @param patient Der anzulegende Patient.
     * @return Der angelegte Patient.
     */
    public Patient create(Patient patient) {
        if (doctorRepository.existsById(patient.getPatientDoctorId())){
            return this.patientRepository.save(patient);
        } else {
            throw new NotFoundException("Der Doctor mit der Id " + patient.getPatientDoctorId().toString() + " konnte nicht gefunden werden");
        }
    }

    /**
     * Löscht einen Patienten im System, Abfrage ob Patient existiert wird im Controller gehandhabt
     * @param patient Der zu löschende Patient
     */
    public void delete(Patient patient) {
        this.patientRepository.delete(patient);
    }


    public List<Nurse> getAllNurses (UUID patientId) {

        this.findById(patientId);

        return this.nursePatientRepository.findByPatientId(patientId).stream().map((npr) -> npr.getNurse()).collect(Collectors.toList());
    }

}
