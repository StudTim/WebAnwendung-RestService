package sssIT.Bachelorarbeit.Tim.restService.persistence.repository;

import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findBypatientDoctorId(UUID id);

}
