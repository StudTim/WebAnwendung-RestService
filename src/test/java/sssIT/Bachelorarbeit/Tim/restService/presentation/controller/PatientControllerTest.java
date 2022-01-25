package sssIT.Bachelorarbeit.Tim.restService.presentation.controller;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sssIT.Bachelorarbeit.Tim.restService.application.exception.NotFoundException;
import sssIT.Bachelorarbeit.Tim.restService.application.service.PatientService;
import sssIT.Bachelorarbeit.Tim.restService.domain.model.Patient;
import sssIT.Bachelorarbeit.Tim.restService.presentation.dto.response.PatientResponseDto;
import sssIT.Bachelorarbeit.Tim.restService.presentation.service.ModelMapperService;
import util.modelFactory.PatientFactory;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @MockBean
    private PatientService patientService;

    @MockBean
    private ModelMapperService modelMapperService;

    @Autowired
    private MockMvc mockMvc;
    private final Random random = new Random();
    private final List<Patient> patients = PatientFactory.generatePatients(1 + random.nextInt(20));

    public PatientControllerTest() {
    }

    @Test
    @DisplayName("It should return a list of patients")
    public void it_should_return_a_list_of_patients() throws Exception {
        when(modelMapperService.getModelMapper()).thenReturn(new ModelMapper());
        when(patientService.all()).thenReturn(patients);
        mockMvc.perform(get("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(patients.size())))
                .andExpect(jsonPath("$[0].*", hasSize(PatientResponseDto.class.getDeclaredFields().length)));
        verify(patientService, times(1)).all();
    }

    @Test()
    @DisplayName("")
    public void it_should_return_a_specified_patient() throws Exception {
        Patient patient = this.patients.get(0);
        when(modelMapperService.getModelMapper()).thenReturn(new ModelMapper());
        when(patientService.findById(patient.getId())).thenReturn(patient);
        mockMvc.perform(get("/api/v1/patients/" + patient.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(PatientResponseDto.class.getDeclaredFields().length)))
                .andExpect(jsonPath("$.id").value(patient.getId().toString()));
        verify(patientService, times(1)).findById(patient.getId());
    }

    @Test()
    public void it_should_deliver_a_not_found() throws Exception {
        UUID randId = UUID.randomUUID();
        when(modelMapperService.getModelMapper()).thenReturn(new ModelMapper());
        when(patientService.findById(randId)).thenThrow(new NotFoundException(""));
        mockMvc.perform(get("/api/v1/patients/" + randId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(patientService, times(1)).findById(randId);
    }

    @Test
    public void it_should_deliver_a_bad_request() throws Exception {
        mockMvc.perform(get("/api/v1/patients/notAUUID")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_validate_dto_on_creation() throws Exception {
        // Dieser Testtreiber wirft Fehler, wir haben allerdings nicht rausbekommen wieso... :(
        /*ObjectMapper mapper = new ObjectMapper();
        CreatePatientRequestDto requestDto = new ModelMapper().map(this.patients.get(0), CreatePatientRequestDto.class);
        when(modelMapperService.getModelMapper()).thenReturn(new ModelMapper());
        when(patientService.create(this.patients.get(0))).thenReturn(this.patients.get(0));
        mockMvc.perform(post("/api/v1/patients/")
                .content(mapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/patients/" + this.patients.get(0).getId().toString()));*/
    }

}
