package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response;
import java.time.LocalDate;
import java.util.UUID;

public class PatientHistoryResponseDto {

    private UUID id;
    private UUID patientenId;
    private String shortEntry;
    private String textEntry;
    private String created_by;
    private LocalDate created_at;
    private String updated_by;
    private LocalDate updated_at;

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

}
