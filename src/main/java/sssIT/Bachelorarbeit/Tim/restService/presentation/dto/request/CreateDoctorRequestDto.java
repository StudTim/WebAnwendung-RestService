package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateDoctorRequestDto {

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
    private String field;

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
}
