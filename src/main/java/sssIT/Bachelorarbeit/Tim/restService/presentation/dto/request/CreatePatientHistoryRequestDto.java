package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class CreatePatientHistoryRequestDto {

    @NotNull
    private UUID patientHistoryPatientenId;

    @NotNull
    @Length(max = 100)
    private String patientHistoryShortEntry;

    @NotNull
    @Length(max = 100)
    private String patientHistoryTextEntry;

    public UUID getPatientHistoryPatientenId() {
        return patientHistoryPatientenId;
    }

    public void setPatientHistoryPatientenId(UUID patientHistoryPatientenId) {
        this.patientHistoryPatientenId = patientHistoryPatientenId;
    }

    public String getPatientHistoryShortEntry() {
        return patientHistoryShortEntry;
    }

    public void setPatientHistoryShortEntry(String patientHistoryShortEntry) {
        this.patientHistoryShortEntry = patientHistoryShortEntry;
    }

    public String getPatientHistoryTextEntry() {
        return patientHistoryTextEntry;
    }

    public void setPatientHistoryTextEntry(String patientHistoryTextEntry) {
        this.patientHistoryTextEntry = patientHistoryTextEntry;
    }
}
