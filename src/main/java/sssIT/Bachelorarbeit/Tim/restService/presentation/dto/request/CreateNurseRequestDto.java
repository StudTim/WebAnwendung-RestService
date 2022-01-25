package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

public class CreateNurseRequestDto {

    @NotNull()
    @Length(max = 20)
    private String identifier;

    @NotNull()
    @Length(max = 20)
    private String firstname;

    @NotNull()
    @Length(max = 20)
    private String lastname;

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
}
