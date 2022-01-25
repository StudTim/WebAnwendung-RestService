package sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UUIDRequestDto {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
