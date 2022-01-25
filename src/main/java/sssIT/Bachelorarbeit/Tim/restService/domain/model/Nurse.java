package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "nurse")
public class Nurse {

    @Id
    @GeneratedValue
    @Column(name = "nurse_id")
    private UUID id;

    @Column(name = "nurse_identifier")
    private String identifier;

    @Column(name = "nurse_firstname")
    private String firstname;

    @Column(name = "nurse_lastname")
    private String lastname;

    @OneToMany(mappedBy = "patient")
    public Set<NursePatientRelationship> nursePatientRelationships = new HashSet<>();

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<NursePatientRelationship> nursePatientRelationships() {
        return nursePatientRelationships;
    }

    public void setPatients(Set<NursePatientRelationship> nursePatientRelationships) {
        this.nursePatientRelationships = nursePatientRelationships;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
