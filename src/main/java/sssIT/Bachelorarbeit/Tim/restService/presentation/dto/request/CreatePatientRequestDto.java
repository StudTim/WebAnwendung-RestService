package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreatePatientRequestDto {

    @NotNull
    private UUID patientDoctorId;

    @NotNull()
    @Length(max = 20)
    private String identifier;

    @NotNull()
    @Length(max = 20)
    private String firstname;

    @NotNull()
    @Length(max = 20)
    private String lastname;

    @Length(max = 50)
    private String careConcept;

    @Length(max = 500)
    private String patComment;


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

    public String getCareConcept() {
        return careConcept;
    }

    public void setCareConcept(String careConcept) {
        this.careConcept = careConcept;
    }

    public String getPatComment() {
        return patComment;
    }

    public void setPatComment(String patComment) {
        this.patComment = patComment;
    }

    public UUID getPatientDoctorId() {
        return patientDoctorId;
    }

    public void setPatientDoctorId(UUID patientDoctorId) {
        this.patientDoctorId = patientDoctorId;
    }
}
