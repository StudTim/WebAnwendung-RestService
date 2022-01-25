package sssIT.Bachelorarbeit.Tim.restService.presentation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import sssIT.Bachelorarbeit.Tim.restService.application.service.PatientNurseService;
import sssIT.Bachelorarbeit.Tim.restService.application.service.PatientService;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Nurse;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.CreatePatientRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.UUIDRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.NurseResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.PatientResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.service.ModelMapperService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@Api(value = "Patienten Controller", description = "Operationen zum verwalten von Patienten", tags = "Patients")
public class PatientController {

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientNurseService patientNurseService;

    /**
     * Beim Aufrufen von GET bei der Api liefert diese eine Liste mit allen zu dem Zeitpunkt gespeicherten patienten
     * Für die entsprechenden API Response Codes bitte die Swagger Doku aufrufen
     * @return liefert eine Liste mit allen Patienten
     */
    @ApiOperation(value = "Bekommen einer Liste aller Patienten ", response = Patient.class, tags = "Patients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!"),
            @ApiResponse(code = 404, message = "not found!|No Patients in DB")})
    @GetMapping("/api/v1/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> response = this.patientService
                .all()
                .stream()
                .map((Patient patient) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(patient, PatientResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    /**
     * Sucht in der Datenbank nach einem bestimmten Patienten der über eine Eindeutige Id identifizierbar ist
     * @param patientID Die eindeutige Patienten Id von dem jeweils gesuchten Patienten
     * @return Falls der Patient under der Id gefunden wurde liefert diesn zurück falls nicht kommt ein 404-Code mit Not Found
     */
    @ApiOperation(value = "Suche nach einem Patienten und wiedergabe dieses Patienten", response = Patient.class, tags = "Patients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alles in Ordnung"),
            @ApiResponse(code = 404, message = "Patient mit dieser ID wurde nicht gefunden")})
    @GetMapping("/api/v1/patients/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable(value = "id") UUID patientID) {
        return ResponseEntity.ok(
                this.modelMapperService
                        .getModelMapper()
                        .map(this.patientService.findById(patientID), PatientResponseDto.class)
        );
    }

    /**
     * Legt einen Neuen Patienten an der Vorher im Body definiert wurde
     * @param patient Anzulegende Patient
     * @return Patient Wird mithilfe des ModelMappers auf die ResponsePatientDTO transferiert und beim erfolgreichen anlegen
     * bekommt man die URL zu dem angelegten Patienten und dessen eindeutige Id
     */
    @ApiOperation(value = "Anlegen eines neuen Patienten in der DB ", response = PatientResponseDto.class, tags = "Patients")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Patient wurde angelegt"),
            @ApiResponse(code = 202, message = "Patient wird angelegt, längere Wartezeit wegen HIGH Serverload"),
            @ApiResponse(code = 406, message = "Gegebene Daten sind nicht Valid")})
    @PostMapping("/api/v1/patients")
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody @Valid CreatePatientRequestDto patient) {
        PatientResponseDto createdPatient = this.modelMapperService.getModelMapper().map(
                this.patientService.create(
                        this.modelMapperService
                                .getModelMapper()
                                .map(patient, Patient.class)),
                PatientResponseDto.class);
        UriComponents uriComponents = MvcUriComponentsBuilder.fromMethodCall(
                on(PatientController.class).getPatientById(createdPatient.getId())).buildAndExpand(1);
        return ResponseEntity.created(uriComponents.toUri()).body(createdPatient);
    }

    /**
     * Löschen eines Patienten aus der Datenbank
     * @param patientId die eindeutige Patienten Id von dem Patienten der gelöscht werden soll
     * @return HTTP-Status Code bitte in der Swagger Doku nachsehen
     */
    @ApiOperation(value = "Löschen eines Patienten", response = Patient.class, tags = "Patients")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content, Patient wurde erfolgreich gelöscht"),
            @ApiResponse(code = 404, message = "Patient wurde nicht gefunden")})
    @DeleteMapping("/api/v1/patients/{id}")
    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable("id") UUID patientId) {

        Patient patientToDelete = this.modelMapperService.getModelMapper().map(this.patientService.findById(patientId),Patient.class);
        patientService.delete(patientToDelete);
        return ResponseEntity.noContent().build();

    }

    /**
     * Liefert eine Ausführlichere Liste von den aufgetretnden Exceptions
     * @return Die aufgetretenden Exceptions
     */
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


    @PostMapping("/api/v1/patients/{idPatient}/nurses")
    public ResponseEntity<?> attachPatientToNurse(
            @PathVariable(value = "idPatient") UUID idPatient, @RequestBody() @Valid UUIDRequestDto uuidRequestDto) {
        patientNurseService.combinePatientNurse(idPatient, uuidRequestDto.getId());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/api/v1/patients/{idPatient}/nurses")
    public ResponseEntity<List<NurseResponseDto>> getAllNursesFromPatients(@PathVariable(value = "idPatient") UUID idPatient) {

        List<NurseResponseDto> response = this.patientService
                .getAllNurses(idPatient)
                .stream()
                .map((Nurse nurse) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(nurse, NurseResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);

    }
}
