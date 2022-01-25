package sssIT.Bachelorarbeit.Tim.restService.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class NursePatientKey implements Serializable {

    @Column(name = "nurse_id")
    private UUID nurseId;

    @Column(name = "pat_id")
    private UUID patientId;



}
