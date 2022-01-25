package sssIT.Bachelorarbeit.Tim.restService.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.ConflictException;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Nurse;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.NursePatientRelationship;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.NursePatientRepository;

import java.util.UUID;

@Service
public class PatientNurseService {

    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private NursePatientRepository nursePatientRepository;

    public void combinePatientNurse(UUID patientId, UUID nurseId) {

        final Patient patient = this.patientService.findById(patientId);
        final Nurse nurse = this.nurseService.findById(nurseId);

        if (nursePatientRepository.findByPatientIdAndNurseId(patientId, nurseId).isPresent()) {
            throw new ConflictException("Connection existiert bereits");
        }

        final NursePatientRelationship nursePatientRelationship = new NursePatientRelationship();
        nursePatientRelationship.setPatient(patient);
        nursePatientRelationship.setNurse(nurse);

        nursePatientRepository.save(nursePatientRelationship);
    }



}
