package sssIT.Bachelorarbeit.Tim.restService.presentation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.PatientHistory;
import sssIT.Bachelorarbeit.Tim.restService.application.service.PatientHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.CreatePatientHistoryRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.PatientHistoryResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.service.ModelMapperService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@Api(value = "Patienten History Controller",description = "Operationen zum verwalten von Patienten Historien", tags = "PatientsHistory")
public class PatientHistoryController {

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private PatientHistoryService patientHistoryService;

    /**
     * Anzeigen aller gespeicherten Patienten historien die zu einem Angegebenen Patienten gefunden wurden
     * @return Liefert eine Liste aller Historien zu einem gewissen Patienten
     */
    @ApiOperation(value = "Es werden alle Historie Einträge zu einem Patienten zurückgegeben die vorher mit der Patienten ID gefiltert wurden", response = PatientHistory.class, tags = "PatientsHistory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alle Patienten wurden erfolgreich gesammelt und wiedergegeben"),
            @ApiResponse(code = 401, message = "Diese Operation ist nicht erlaubt. Die kann an fehlender Freigabe liegen oder falscher Anfrage"),
            @ApiResponse(code = 403, message = "Operation ist Verboten"),
            @ApiResponse(code = 404, message = "Es wurden keine Patienten in der Datenbank gefundenm, wohlmöglich wurden noch keine Angelegt") })
    @GetMapping("/api/v1/patients/{idPatient}/histories")
    public ResponseEntity<List<PatientHistoryResponseDto>> getAllPatientHistory(
            @PathVariable(value = "idPatient") UUID idPatient) {
        List<PatientHistoryResponseDto> response = this.patientHistoryService
                .all(idPatient)
                .stream()
                .map((PatientHistory patientHistory) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(patientHistory, PatientHistoryResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Suche von einem bestimmten Patienten mit seiner Id nach seiner PatientenHistorie mit ebenfalls der eindeutigen Id
     * des Historien eintrages
     * @param patientId  Die gesuchte Id als UUID vom Patienten selber
     * @param patientHistoryId Die gescuhte Id von dem Historien eintrag dieses Patienten
     * @return Falls gefunden den jeweiligen Patienten Historien Eintrag
     */
    @ApiOperation(value = "Suche nach einer PatientenHistorie und Wiedergabe dieser Historie", response = PatientHistory.class, tags = "PatientsHistory")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Die gesuchte Patient Historie wurde gefunden und wiedergegeben"),
            @ApiResponse(code = 404, message = "Patienten Historie mit dieser ID wurde nicht gefunden")})
    @GetMapping("/api/v1/patients/{idPatient}/histories/{patientHistoryId}")
    public ResponseEntity<PatientHistoryResponseDto> getPatientHistoryByID(
            @PathVariable(value = "idPatient") UUID patientId,
            @PathVariable(value = "patientHistoryId") UUID patientHistoryId) {
        return ResponseEntity.ok(this.modelMapperService.
                getModelMapper()
                .map(this.patientHistoryService.findById(patientId, patientHistoryId),PatientHistoryResponseDto.class));
    }

    /**
     * Hinzufügen eines neuen Historie zu einem Patienten
     * @param patientId Die Id eines Patienten zu der eine neue Patienten History angelgt werden soll
     * @return liefert URL und Body der angelegten Historie an um diese später über die Id wieder zu finden
     */
    @ApiOperation(value = "Anlegen einer neuen Patienten Historie in der DB ",response = PatientHistory.class, tags = "PatientsHistory")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Patienten History wurde erfolgreich in der Datanbank angelegt"),
            @ApiResponse(code = 202, message = "Patient History wird angelegt bitte noch etwas warten, längere Wartezeit wegen HIGH Serverload"),
            @ApiResponse(code = 406, message = "Die Angegebene Daten sind nicht valid, entweder enthalten sie nicht erlaubte Sonderzeichen oder Werte sind zu lang")})
    @PostMapping("/api/v1/patients/{idPatient}/histories")
    public ResponseEntity<PatientHistoryResponseDto> createPatientHistory(@Valid @RequestBody CreatePatientHistoryRequestDto patientHistory, @PathVariable(value = "idPatient") UUID patientId){
        PatientHistoryResponseDto createdPatientHistory = this.modelMapperService.getModelMapper().map(
                this.patientHistoryService.create(
                        patientId,
                        this.modelMapperService
                                .getModelMapper()
                                .map(patientHistory,PatientHistory.class))
                ,PatientHistoryResponseDto.class);

        UriComponents uriComponents = MvcUriComponentsBuilder.fromMethodCall(
                on(PatientController.class).getPatientById(createdPatientHistory.getId())).buildAndExpand(1);

        return ResponseEntity.created(uriComponents.toUri()).body(createdPatientHistory);
    }

    /**
     * Das löschen einer ausgewählten Historie von einem vorher eindeutig festgelegten Patienten
     * @param patientId Die Id des Patienten welcher einen Historien eintrag gelöscht werden sol
     * @param patientHistoryId Die Id des Historien erreignisses von der zu löschenden PatientenHistorie
     * @return Falls alles in Ordnung kommt der HTTP-Code für No-Content zurück, für weitere Informationen bitte Swagger Doku nachlesen
     */
    @ApiOperation(value = "Löschen einer Patienten Historie", response = PatientHistory.class, tags = "PatientsHistory")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content, Die angegebene Patienten History wurde erfolgreich gelöscht"),
            @ApiResponse(code = 404, message = "Die zu löschende Patienten History wurde nicht gefunden")})
    @DeleteMapping("/api/v1/patients/{idPatient}/histories/{id}")
    public ResponseEntity<PatientHistoryResponseDto> deletePatientHistory(@PathVariable(value = "idPatient") UUID patientId, @PathVariable("id") UUID patientHistoryId) {
        patientHistoryService.delete(patientId, patientHistoryId);
        return ResponseEntity.noContent().build();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
