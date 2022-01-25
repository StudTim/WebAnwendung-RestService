package sssIT.Bachelorarbeit.Tim.restService.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Doctor;

import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
}
