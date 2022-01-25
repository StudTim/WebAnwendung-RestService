package sssIT.Bachelorarbeit.Tim.restService.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.NotFoundException;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.PatientHistory;
import sssIT.Bachelorarbeit.Tim.restService.persistence.repository.PatientHistoryRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Dieser Service kümmert sich um die Bearbeitung aller PatientenHistorien im System
 */
@Service
public class PatientHistoryService {

    @Autowired
    private PatientHistoryRepository patientHistoryRepository;

    @Autowired
    private PatientService patientService;

    /**
     * @return Liefert alle PatientenHistorien zurück
     */
    public Collection<PatientHistory> all(UUID patientId) {
        this.patientService.findById(patientId);
        return this.patientHistoryRepository.findByPatientenId(patientId);
    }

    /**
     * Findet eine bestimmte PatientenHistory mit der gegebenen ID und liefert diese zurück
     * @param id Die ID der gesuchten Patienten Historie
     * @return Die PatientenHistorie mit der gesuchten ID
     * @throws NotFoundException falls keine PatientenHistorie zu dieser ID gefunden wurde
     */
    public PatientHistory findById(UUID patientId, UUID id) {
        patientService.findById(patientId);
        Optional<PatientHistory> patientHistory = this.patientHistoryRepository.findByPatientenIdAndId(patientId, id);
        if (patientHistory.isEmpty()) {
            throw new NotFoundException("Die Patienten Historie mit der ID: " + id.toString() + " konnte nicht gefunden werden");
        }
        return patientHistory.get();
    }

    /**
     * Anlegen einer neuen PatientenHistorie zu einem Patienten
     * @param patientHistory die anzulegenen Historie
     * @return liefert die angelegte PatientenHistory zurück
     */
    public PatientHistory create(UUID patientId, PatientHistory patientHistory) {
        this.patientService.findById(patientId);
        patientHistory.setCreated_at(LocalDate.now());
        patientHistory.setCreated_by("System");
        patientHistory.setUpdated_at(LocalDate.now());
        return this.patientHistoryRepository.save(patientHistory);
    }

    /**
     * Löscht eine Patienten History
     * @param id Die zu löschende Historie
     */
    public void delete(UUID patientId, UUID id) {
        PatientHistory history = this.findById(patientId, id);
        this.patientHistoryRepository.delete(history);
    }
}
