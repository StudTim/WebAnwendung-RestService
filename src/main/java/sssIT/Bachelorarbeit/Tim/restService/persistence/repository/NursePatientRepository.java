package sssIT.Bachelorarbeit.Tim.restService.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.NursePatientKey;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.NursePatientRelationship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface NursePatientRepository extends JpaRepository<NursePatientRelationship, NursePatientKey> {

    Optional<NursePatientRelationship> findByPatientIdAndNurseId(UUID patientId, UUID nurseId);
    List<NursePatientRelationship> findByNurseId(UUID nurseId);
    List<NursePatientRelationship> findByPatientId(UUID patientId);
}
