package com.quantumdev.integraservicios.operationManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantumdev.integraservicios.operationManagement.Service.HardwareOperationService;
import com.quantumdev.integraservicios.operationManagement.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HardwareOperationControllerTest {

    @Mock
    private HardwareOperationService hardwareOperationService;

    @InjectMocks
    private HardwareOperationController hardwareOperationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(hardwareOperationController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void getAllReservations_Success() throws Exception {
        // Given
        List<ReservationResponseDto> reservations = Arrays.asList(
            createMockReservationResponse(1L, "usuario1@example.com"),
            createMockReservationResponse(2L, "usuario2@example.com")
        );
        when(hardwareOperationService.getAllReservations(null)).thenReturn(reservations);

        // When & Then
        mockMvc.perform(get("/api/operations/hardware"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(hardwareOperationService).getAllReservations(null);
    }

    @Test
    void getAllReservations_WithFilter_Success() throws Exception {
        // Given
        String filter = "ACTIVE";
        List<ReservationResponseDto> reservations = Arrays.asList(
            createMockReservationResponse(1L, "usuario1@example.com")
        );
        when(hardwareOperationService.getAllReservations(filter)).thenReturn(reservations);

        // When & Then
        mockMvc.perform(get("/api/operations/hardware")
                .param("filter", filter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(hardwareOperationService).getAllReservations(filter);
    }

    @Test
    void getAllReservations_ServiceException_InternalServerError() throws Exception {
        // Given
        when(hardwareOperationService.getAllReservations(any())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/operations/hardware"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error al obtener las reservaciones")));
    }

    @Test
    void getUserReservations_Success() throws Exception {
        // Given
        String userEmail = "usuario1@example.com";
        List<ReservationResponseDto> reservations = Arrays.asList(
            createMockReservationResponse(1L, userEmail)
        );
        when(hardwareOperationService.getUserReservations(userEmail, null)).thenReturn(reservations);

        // When & Then
        mockMvc.perform(get("/api/operations/hardware/user/{userEmail}", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].requester").value(userEmail));

        verify(hardwareOperationService).getUserReservations(userEmail, null);
    }

    @Test
    void getUserReservations_EmptyEmail_BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/operations/hardware/user/{userEmail}", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El ID de usuario es requerido"));
    }

    @Test
    void getReservationById_Success() throws Exception {
        // Given
        Long id = 1L;
        ReservationResponseDto reservation = createMockReservationResponse(id, "usuario1@example.com");
        when(hardwareOperationService.getReservationById(id)).thenReturn(Optional.of(reservation));

        // When & Then
        mockMvc.perform(get("/api/operations/hardware/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.requester").value("usuario1@example.com"));

        verify(hardwareOperationService).getReservationById(id);
    }

    @Test
    void getReservationById_NotFound() throws Exception {
        // Given
        Long id = 999L;
        when(hardwareOperationService.getReservationById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/operations/hardware/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Reservación no encontrada con ID: " + id)));
    }

    @Test
    void getReservationById_InvalidId_BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/operations/hardware/{id}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("ID de reservación inválido"));
    }

    @Test
    void getAvailableHardware_Success() throws Exception {
        // Given
        List<AvailabilityDto> available = Arrays.asList(
            createMockAvailabilityDto(1L, "Hardware 1"),
            createMockAvailabilityDto(2L, "Hardware 2")
        );
        when(hardwareOperationService.getAvailableHardware()).thenReturn(available);

        // When & Then
        mockMvc.perform(get("/api/operations/hardware/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(hardwareOperationService).getAvailableHardware();
    }

    @Test
    void createReservation_Success() throws Exception {
        // Given
        ReservationRequestDto request = createMockReservationRequest();
        ReservationResponseDto response = createMockReservationResponse(1L, request.getRequester());
        when(hardwareOperationService.createReservation(any(ReservationRequestDto.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/operations/hardware")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.requester").value(request.getRequester()));

        verify(hardwareOperationService).createReservation(any(ReservationRequestDto.class));
    }

    @Test
    void createReservation_NullRequest_BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/operations/hardware")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los datos de la reservación son requeridos"));
    }

    @Test
    void createReservation_EmptyRequester_BadRequest() throws Exception {
        // Given
        ReservationRequestDto request = createMockReservationRequest();
        request.setRequester("");

        // When & Then
        mockMvc.perform(post("/api/operations/hardware")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El solicitante (requester) es requerido"));
    }

    @Test
    void createReservation_InvalidDates_BadRequest() throws Exception {
        // Given
        ReservationRequestDto request = createMockReservationRequest();
        request.setStart(LocalDateTime.now().plusHours(2));
        request.setEnd(LocalDateTime.now().plusHours(1)); // End before start

        // When & Then
        mockMvc.perform(post("/api/operations/hardware")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La fecha de inicio no puede ser posterior a la fecha de fin"));
    }

    @Test
    void createReservation_ServiceConflict_Conflict() throws Exception {
        // Given
        ReservationRequestDto request = createMockReservationRequest();
        when(hardwareOperationService.createReservation(any(ReservationRequestDto.class)))
                .thenThrow(new RuntimeException("Hardware no disponible"));

        // When & Then
        mockMvc.perform(post("/api/operations/hardware")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("No se pudo crear la reservación")));
    }

    @Test
    void cancelReservation_Success() throws Exception {
        // Given
        Long id = 1L;
        when(hardwareOperationService.cancelReservation(id)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/operations/hardware/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reservación cancelada exitosamente"));

        verify(hardwareOperationService).cancelReservation(id);
    }

    @Test
    void cancelReservation_NotFound() throws Exception {
        // Given
        Long id = 999L;
        when(hardwareOperationService.cancelReservation(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/operations/hardware/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se puede cancelar la reservación, ya que ha sido entregada o no existe"));
    }

    @Test
    void handOverHardware_Success() throws Exception {
        // Given
        Long id = 1L;
        HandoverReturnDto handoverData = new HandoverReturnDto();
        ReservationResponseDto response = createMockReservationResponse(id, "usuario1@example.com");
        when(hardwareOperationService.handOverHardware(eq(id), any())).thenReturn(Optional.of(response));

        // When & Then
        mockMvc.perform(post("/api/operations/hardware/{id}/handOver", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(handoverData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(hardwareOperationService).handOverHardware(eq(id), any());
    }

    @Test
    void handOverHardware_NoBody_Success() throws Exception {
        // Given
        Long id = 1L;
        ReservationResponseDto response = createMockReservationResponse(id, "usuario1@example.com");
        when(hardwareOperationService.handOverHardware(eq(id), any())).thenReturn(Optional.of(response));

        // When & Then
        mockMvc.perform(post("/api/operations/hardware/{id}/handOver", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void handOverHardware_ServiceReturnsEmpty_BadRequest() throws Exception {
        // Given
        Long id = 1L;
        when(hardwareOperationService.handOverHardware(eq(id), any())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/operations/hardware/{id}/handOver", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No se pudo procesar la entrega del hardware"));
    }

    @Test
    void returnHardware_Success() throws Exception {
        // Given
        Long id = 1L;
        HandoverReturnDto returnData = new HandoverReturnDto();
        returnData.setConditionRate(5);
        returnData.setServiceRate(4);
        ReservationResponseDto response = createMockReservationResponse(id, "usuario1@example.com");
        when(hardwareOperationService.returnHardware(eq(id), any())).thenReturn(Optional.of(response));

        // When & Then
        mockMvc.perform(post("/api/operations/hardware/{id}/return", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(returnData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(hardwareOperationService).returnHardware(eq(id), any());
    }

    @Test
    void returnHardware_NullData_BadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/operations/hardware/{id}/return", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los datos de devolución son requeridos"));
    }

    // Helper methods
    private ReservationResponseDto createMockReservationResponse(Long id, String requester) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(id);
        dto.setRequester(requester);
        dto.setResourceCode(2L);
        dto.setStoredResourceCode(2L);
        dto.setManager("manager@example.com");
        dto.setBuilding(1L);
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(3));
        return dto;
    }

    private ReservationRequestDto createMockReservationRequest() {
        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setRequester("usuario2@example.com");
        dto.setResourceCode(2L);
        dto.setStoredResourceCode(2L);
        dto.setManager("usuario40@example.com");
        dto.setBuilding(1L);
        dto.setStart(LocalDateTime.now().plusHours(1));
        dto.setEnd(LocalDateTime.now().plusHours(3));
        return dto;
    }

    private AvailabilityDto createMockAvailabilityDto(Long id, String name) {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }
}