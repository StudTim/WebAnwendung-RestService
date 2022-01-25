package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "nurse_patients")
public class NursePatientRelationship {

    @EmbeddedId
    private NursePatientKey id;

    @ManyToOne
    @MapsId("nurseId")
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;

    @ManyToOne
    @MapsId("patientId")
    @JoinColumn(name = "pat_id")
    private Patient patient;

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
