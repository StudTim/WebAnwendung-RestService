package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="patient")
public class Patient {


    @Id
    @GeneratedValue
    @Column(name = "pat_id")
    private UUID id;

    @Column(name = "pat_doc_id")
    private UUID patientDoctorId;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientHistory> patientHistories;

    @ManyToOne
    @JoinColumn(name = "pat_doc_id",insertable = false, updatable = false)
    private Doctor doctor;

    @OneToMany(mappedBy = "nurse")
    private Set<NursePatientRelationship> nursePatientRelationships;

    @Column(name = "pat_identifier",nullable = false, unique = true)
    private String identifier;


    @Column(name = "pat_firstname", nullable = false)
    private String firstname;


    @Column(name = "pat_lastname", nullable = false)
    private String lastname;


    @Column(name = "pat_care_concept_name")
    private String careConcept;


    @Column(name = "pat_comment")
    private String patComment;

    public Patient() {
    }

    public Patient(String identifier, UUID patientDoctorId, String firstname, String lastname, String careConcept, String patComment) {
        this.identifier = identifier;
        this.patientDoctorId = patientDoctorId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.careConcept = careConcept;
        this.patComment = patComment;
    }

    public UUID getId(){
        return id;
    }

    public void setId(UUID uudi){
        this.id=uudi;
    }

    public String getIdentifier(){
        return identifier;
    }

    public void setIdentifier(String identifier){
        this.identifier=identifier;
    }

    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname=firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastaname){
        this.lastname=lastaname;
    }

    public String getCareConcept(){
        return careConcept;
    }

    public void setCareConcept(String careConcept){
        this.careConcept=careConcept;
    }

    public String getPatComment(){
        return patComment;
    }

    public void setPatComment(String patComment){
        this.patComment=patComment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public UUID getPatientDoctorId() {
        return patientDoctorId;
    }

    public void setPatientDoctorId(UUID patientDoctorId) {
        this.patientDoctorId = patientDoctorId;
    }

    public Set<NursePatientRelationship> getNurses() {
        return nursePatientRelationships;
    }

    public void setNurses(Set<NursePatientRelationship> nursePatientRelationships) {
        this.nursePatientRelationships = nursePatientRelationships;
    }

    @Override
    public String toString() {
        return "Patient: [ID: " + id + "; FirstName: " + firstname + "; LastName: " + lastname
                + "; PatientCareConcept: " + careConcept;
    }

}
