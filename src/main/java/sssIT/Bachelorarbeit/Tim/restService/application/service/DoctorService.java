package sssIT.Bachelorarbeit.Tim.restService.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.NotFoundException;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Doctor;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.DoctorRepository;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.PatientRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    /**
     * @return Gibt alle Doctoren zurück.
     */
    public Collection<Doctor> all() {
        return this.doctorRepository.findAll();
    }
    /**
     * Sucht einen Doctor anhand der übergebenen Id. Löst eine Exception aus, falls der
     * Doctor nicht existiert.
     * @param id Die Id des Doctors nachdem gesucht werden soll.
     * @return Der Doctor mit der angegeben Id.
     * @throws NotFoundException falls der Doctor nicht gefunden werden konnte.
     */

    public Doctor findById(UUID id) {
        Optional<Doctor> doctor = this.doctorRepository.findById(id);
        if (doctor.isEmpty()) {
            throw new NotFoundException("Der Doctor mit der Id " + id.toString() + " konnte nicht gefunden werden");
        }
        return doctor.get();
    }
    /**
     * Legt einen neuen Doctoren im System an und speichert ihn in der Datenbank.
     * @param doctor Der anzulegende Doctor.
     * @return Der angelegte Doctor.
     */
    public Doctor create(Doctor doctor) {
        return this.doctorRepository.save(doctor);
    }

    /**
     * Löscht einen Doctor im System, Abfrage ob Doctor existiert wird im Controller gehandhabt
     * @param doctor Der zu löschende Doctor
     */
    public void delete(Doctor doctor) {
        this.doctorRepository.delete(doctor);
    }

    /**
     * @return Liefert alle Patienten mit gleicher DoktorId zurück
     */
    public Collection<Patient> allPatientsWithDoctorId(UUID patientDoctorId) {
        this.findById(patientDoctorId);
        return this.patientRepository.findBypatientDoctorId(patientDoctorId);
    }
}
