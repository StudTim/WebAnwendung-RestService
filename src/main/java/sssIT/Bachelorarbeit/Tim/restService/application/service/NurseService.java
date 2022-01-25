package sssIT.Bachelorarbeit.Tim.restService.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.NotFoundException;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Nurse;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.NursePatientRepository;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.NurseRepository;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.PatientRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private NursePatientRepository nursePatientRepository;

    /**
     * @return Gibt alle Pfleger zurück.
     */
    public Collection<Nurse> all() {
        return this.nurseRepository.findAll();
    }


    /**
     * Sucht einen Pfleger anhand der übergebenen Id. Löst eine Exception aus, falls der
     * Pfleger nicht existiert.
     * @param id Die Id des Pfleger nachdem gesucht werden soll.
     * @return Der Pfleger mit der angegeben Id.
     * @throws NotFoundException falls der Pfleger nicht gefunden werden konnte.
     */
    public Nurse findById(UUID id) {
        Optional<Nurse> nurse = this.nurseRepository.findById(id);
        if (nurse.isEmpty()) {
            throw new NotFoundException("Der Pfleger mit der Id " + id.toString() + " konnte nicht gefunden werden");
        }
        return nurse.get();
    }


    /**
     * Legt einen neuen Pfleger im System an und speichert ihn in der Datenbank.
     * @param nurse Des anzulegenden Pflegers.
     * @return Der angelegte Pfleger.
     */
    public Nurse create(Nurse nurse) {

        return this.nurseRepository.save(nurse);

    }

    /**
     * Löscht einen Pfleger im System, Abfrage ob Pfleger existiert wird im Controller gehandhabt
     * @param nurse Der zu löschende Pfleger
     */
    public void delete(Nurse nurse) {
        this.nurseRepository.delete(nurse);
    }

    /**
     * @return Liefert alle Patienten zum zugewiesenen Pfleger mit PflegerId zurück
     */
    public Collection<Patient> allPatientsWithNurseId(UUID nursePatientId) {
        this.findById(nursePatientId);

        return nursePatientRepository.findByNurseId(nursePatientId).stream().map((npr) -> npr.getPatient()).collect(Collectors.toList());
    }

}
