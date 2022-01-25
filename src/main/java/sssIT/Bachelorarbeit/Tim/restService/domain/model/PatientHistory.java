package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="patient_history")
public class PatientHistory {

    @Id
    @GeneratedValue
    @Column(name = "phi_id")
    private UUID id;

    @Column(name = "phi_pat_id",nullable = false)
    private UUID patientenId;

    @Column(name = "phi_short_entry")
    private String shortEntry;

    @Column(name = "phi_text_entry")
    private String textEntry;

    private String created_by;
    private LocalDate created_at;
    private String updated_by;
    private LocalDate updated_at;

    @ManyToOne
    @JoinColumn(name = "phi_pat_id",insertable = false, updatable = false)
    private Patient patient;

    public PatientHistory() {
    }

    public PatientHistory(UUID patientenId, String shortEntry, String textEntry, String created_by, String updated_by) {
        this.patientenId = patientenId;
        this.shortEntry = shortEntry;
        this.textEntry = textEntry;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientenId() {
        return patientenId;
    }

    public void setPatientenId(UUID patientenId) {
        this.patientenId = patientenId;
    }

    public String getShortEntry() {
        return shortEntry;
    }

    public void setShortEntry(String shortEntry) {
        this.shortEntry = shortEntry;
    }

    public String getTextEntry() {
        return textEntry;
    }

    public void setTextEntry(String textEntry) {
        this.textEntry = textEntry;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "PatientHistory: " + id + "PatientID: " + patientenId + getPatient().toString();
    }
}
