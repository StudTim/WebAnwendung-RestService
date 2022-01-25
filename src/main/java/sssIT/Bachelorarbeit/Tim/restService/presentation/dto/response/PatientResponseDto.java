package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response;

import java.util.UUID;

public class PatientResponseDto {

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private UUID id;
    private UUID doctorId;
    private String identifier;
    private String firstname;
    private String lastname;
    private String careConcept;
    private String patComment;


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

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }
}
