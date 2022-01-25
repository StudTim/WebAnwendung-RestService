package sssIT.Bachelorarbeit.Tim.restService.persistence.repository;

import org.springframework.stereotype.Repository;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.PatientHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientHistoryRepository extends JpaRepository<PatientHistory, UUID> {

    List<PatientHistory> findByPatientenId(UUID id);
    Optional<PatientHistory> findByPatientenIdAndId(UUID patientenId, UUID id);

}
