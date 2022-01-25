package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue
    @Column(name = "doc_id")
    private UUID id;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Patient> patients;

    @Column(name = "doc_identifier",nullable = false, unique = true)
    private String identifier;


    @Column(name = "doc_firstname")
    private String firstname;


    @Column(name = "doc_lastname")
    private String lastname;

    @Column(name = "doc_field")
    private String field;

    public Doctor() {
    }

    public Doctor(String identifier, String firstname, String lastname, String field) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.field = field;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "Doctor: [ID: " + id + "; FirstName: " + firstname + "; LastName: " + lastname
                + "; Field: " + field;
    }
}
