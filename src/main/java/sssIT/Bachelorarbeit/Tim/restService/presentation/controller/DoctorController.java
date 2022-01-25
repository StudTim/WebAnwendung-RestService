package sssIT.Bachelorarbeit.Tim.restService.presentation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import sssIT.Bachelorarbeit.Tim.restService.application.service.DoctorService;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Doctor;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.request.CreateDoctorRequestDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.DoctorResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.PatientResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.service.ModelMapperService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@Api(value = "Doctors Controller", description = "Operationen zum verwalten von Doktoren", tags = "Doctors")
public class DoctorController {

    @Autowired
    private ModelMapperService modelMapperService;

    @Autowired
    private DoctorService doctorService;

    /**
     * Beim Aufrufen von GET bei der Api liefert diese eine Liste mit allen zu dem Zeitpunkt gespeicherten doctoren
     * Für die entsprechenden API Response Codes bitte die Swagger Doku aufrufen
     * @return liefert eine Liste mit allen Doctoren
     */
    @ApiOperation(value = "Bekommen einer Liste aller Doctoren ", response = Doctor.class, tags = "Doctors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!"),
            @ApiResponse(code = 404, message = "not found!|No Doctors in DB")})
    @GetMapping("/api/v1/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        List<DoctorResponseDto> response = this.doctorService
                .all()
                .stream()
                .map((Doctor doctor) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(doctor, DoctorResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    /**
     * Sucht in der Datenbank nach einem bestimmten Doctor der über eine Eindeutige Id identifizierbar ist
     * @param doctorID Die eindeutige Doctor Id von dem jeweils gesuchten Doctor
     * @return Falls der Doctor under der Id gefunden wurde liefert diesn zurück falls nicht kommt ein 404-Code mit Not Found
     */
    @ApiOperation(value = "Suche nach einem Doctor und wiedergabe dieses Doctoren", response = Doctor.class, tags = "Doctors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alles in Ordnung"),
            @ApiResponse(code = 404, message = "Doctor mit dieser ID wurde nicht gefunden")})
    @GetMapping("/api/v1/doctors/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable(value = "id") UUID doctorID) {
        return ResponseEntity.ok(
                this.modelMapperService
                        .getModelMapper()
                        .map(this.doctorService.findById(doctorID), DoctorResponseDto.class)
        );
    }

    /**
     * Legt einen Neuen Doctor an der Vorher im Body definiert wurde
     * @param doctor Anzulegende Doctor
     * @return Doctor Wird mithilfe des ModelMappers auf die ResponseDoctorDTO transferiert und beim erfolgreichen anlegen
     * bekommt man die URL zu dem angelegten Doktoren und dessen eindeutige Id
     */
    @ApiOperation(value = "Anlegen eines neuen Doctors in der DB ", response = DoctorResponseDto.class, tags = "Doctors")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Doctor wurde angelegt"),
            @ApiResponse(code = 202, message = "Doctor wird angelegt, längere Wartezeit wegen HIGH Serverload"),
            @ApiResponse(code = 406, message = "Gegebene Daten sind nicht Valid")})
    @PostMapping("/api/v1/doctors")
    public ResponseEntity<DoctorResponseDto> createDoctor(@RequestBody @Valid CreateDoctorRequestDto doctor) {
        DoctorResponseDto createdDoctor = this.modelMapperService.getModelMapper().map(
                this.doctorService.create(
                        this.modelMapperService
                                .getModelMapper()
                                .map(doctor, Doctor.class)),
                DoctorResponseDto.class);
        UriComponents uriComponents = MvcUriComponentsBuilder.fromMethodCall(
                on(DoctorController.class).getDoctorById(createdDoctor.getId())).buildAndExpand(1);
        return ResponseEntity.created(uriComponents.toUri()).body(createdDoctor);
    }

    /**
     * Löschen eines Doctor aus der Datenbank
     * @param doctorId die eindeutige Doctor Id von dem Doctor der gelöscht werden soll
     * @return HTTP-Status Code bitte in der Swagger Doku nachsehen
     */
    @ApiOperation(value = "Löschen eines Doctors", response = Doctor.class, tags = "Doctors")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content, Doctor wurde erfolgreich gelöscht"),
            @ApiResponse(code = 404, message = "Doctor wurde nicht gefunden")})
    @DeleteMapping("/api/v1/doctors/{id}")
    public ResponseEntity<DoctorResponseDto> deleteDoctor(@PathVariable("id") UUID doctorId) {

        Doctor doctorToDelete = this.modelMapperService.getModelMapper().map(this.doctorService.findById(doctorId),Doctor.class);
        doctorService.delete(doctorToDelete);
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

    /**
     * Anzeigen aller gespeicherten Patienten die dem ausgewählten Doktor zugewiesen wurden sind.
     * @return Liefert eine Liste aller zugehörigen Patienten
     */
    @ApiOperation(value = "Es werden alle Patienten zurück geliefert die vorher mit einer DoctorId gefiltert wurden sind", response = Patient.class, tags = "Patients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alle Patienten wurden erfolgreich gesammelt und wiedergegeben"),
            @ApiResponse(code = 401, message = "Diese Operation ist nicht erlaubt. Die kann an fehlender Freigabe liegen oder falscher Anfrage"),
            @ApiResponse(code = 403, message = "Operation ist Verboten"),
            @ApiResponse(code = 404, message = "Es wurden keine Patienten in der Datenbank gefundenm, wohlmöglich wurden noch keine Angelegt") })
    @GetMapping("/api/v1/doctors/{idDoctor}/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllDoctorsPatients(
            @PathVariable(value = "idDoctor") UUID idDoctor) {
        List<PatientResponseDto> response = this.doctorService
                .allPatientsWithDoctorId(idDoctor)
                .stream()
                .map((Patient patient) -> {
                    return this.modelMapperService
                            .getModelMapper()
                            .map(patient, PatientResponseDto.class);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

}
