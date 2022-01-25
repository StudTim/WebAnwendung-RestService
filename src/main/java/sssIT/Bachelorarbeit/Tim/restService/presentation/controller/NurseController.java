package sssIT.Bachelorarbeit.Tim.restService.presentation.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import sssIT.Bachelorarbeit.Tim.restService.application.service.NurseService;
import sssIT.Bachelorarbeit.Tim.restService.application.service.PatientNurseService;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Nurse;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.CreateNurseRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.UUIDRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.NurseResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.PatientResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.service.ModelMapperService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@Api(value = "Nurses Controller", description = "Operationen zum verwalten von Pflegern", tags = "Nurses")
public class NurseController {

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientNurseService patientNurseService;


    /**
     * Beim Aufrufen von GET bei der Api liefert diese eine Liste mit allen zu dem Zeitpunkt gespeicherten Pflegern
     * Für die entsprechenden API Response Codes bitte die Swagger Doku aufrufen
     * @return liefert eine Liste mit allen Pflegern
     */
    @ApiOperation(value = "Bekommen einer Liste aller Pfleger ", response = Nurse.class, tags = "Nurses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!"),
            @ApiResponse(code = 404, message = "not found!|No Patients in DB")})
    @GetMapping("/api/v1/nurses")
    public ResponseEntity<List<NurseResponseDto>> getAllNurses() {
        List<NurseResponseDto> response = this.nurseService
                .all()
                .stream()
                .map((Nurse nurse) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(nurse, NurseResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Sucht in der Datenbank nach einem bestimmten Pfleger der über eine Eindeutige Id identifizierbar ist
     * @param nurseID Die eindeutige Pfleger Id von dem jeweils gesuchten Pfleger
     * @return Falls der Pfleger under der Id gefunden wurde liefert diesn zurück falls nicht kommt ein 404-Code mit Not Found
     */
    @ApiOperation(value = "Suche nach einem Pfleger und wiedergabe dieses Pflegers", response = Nurse.class, tags = "Nurses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alles in Ordnung"),
            @ApiResponse(code = 404, message = "Pfleger mit dieser ID wurde nicht gefunden")})
    @GetMapping("/api/v1/nurses/{id}")
    public ResponseEntity<NurseResponseDto> getNursesById(@PathVariable(value = "id") UUID nurseID) {
        return ResponseEntity.ok(
                this.modelMapperService
                        .getModelMapper()
                        .map(this.nurseService.findById(nurseID), NurseResponseDto.class)
        );
    }

    /**
     * Legt einen Neuen Pfleger an der Vorher im Body definiert wurde
     * @param nurse Anzulegende Pfleger
     * @return Pfleger Wird mithilfe des ModelMappers auf die ResponseNurseDTO transferiert und beim erfolgreichen anlegen
     * bekommt man die URL zu dem angelegten Pfleger und dessen eindeutige Id
     */
    @ApiOperation(value = "Anlegen eines neuen Pfleger in der DB ", response = NurseResponseDto.class, tags = "Nurses")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pfleger wurde angelegt"),
            @ApiResponse(code = 202, message = "Pfleger wird angelegt, längere Wartezeit wegen HIGH Serverload"),
            @ApiResponse(code = 406, message = "Gegebene Daten sind nicht Valid")})
    @PostMapping("/api/v1/nurses")
    public ResponseEntity<NurseResponseDto> createNurses(@RequestBody @Valid CreateNurseRequestDto nurse) {
        NurseResponseDto createdNurse = this.modelMapperService.getModelMapper().map(
                this.nurseService.create(
                        this.modelMapperService
                                .getModelMapper()
                                .map(nurse, Nurse.class)),
                NurseResponseDto.class);
        UriComponents uriComponents = MvcUriComponentsBuilder.fromMethodCall(
                on(NurseController.class).getNursesById(createdNurse.getId())).buildAndExpand(1);
        return ResponseEntity.created(uriComponents.toUri()).body(createdNurse);
    }


    /**
     * Anzeigen aller Patienten die dem ausgewählten Pfleger zugewiesen worden sind
     * @return Liefert eine Liste aller Patienten zu einem Pfleger
     */
    @ApiOperation(value = "Es werden alle Patienten zurückgegeben die einer PflegerId zugewiesen wurden sind", response = Patient.class, tags = "Nurses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alle Patienten wurden erfolgreich gesammelt und wiedergegeben"),
            @ApiResponse(code = 401, message = "Diese Operation ist nicht erlaubt. Die kann an fehlender Freigabe liegen oder falscher Anfrage"),
            @ApiResponse(code = 403, message = "Operation ist Verboten"),
            @ApiResponse(code = 404, message = "Es wurden keine Patienten in der Datenbank gefunden, wohlmöglich wurden noch keine Angelegt") })
    @GetMapping("/api/v1/nurses/{idNurse}/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllNursesPatients(
            @PathVariable(value = "idNurse") UUID idNurse) {
        List<PatientResponseDto> response = this.nurseService
                .allPatientsWithNurseId(idNurse)
                .stream()
                .map((Patient nursePatient) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(nursePatient, PatientResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/v1/nurses/{idNurse}/patients")
    public ResponseEntity<?> attachPatientToNurse(
            @PathVariable(value = "idNurse") UUID idNurse, @RequestBody() @Valid UUIDRequestDto uuidRequestDto) {
        patientNurseService.combinePatientNurse(uuidRequestDto.getId(), idNurse);
        return ResponseEntity.noContent().build();
    }



}
