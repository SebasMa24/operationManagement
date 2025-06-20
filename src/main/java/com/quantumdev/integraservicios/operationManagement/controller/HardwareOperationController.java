package com.quantumdev.integraservicios.operationManagement.controller;

import java.util.Map;
import java.util.HashMap;
import com.quantumdev.integraservicios.operationManagement.dto.*;
import com.quantumdev.integraservicios.operationManagement.Service.HardwareOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operations/hardware")
@CrossOrigin(origins = "*")
public class HardwareOperationController {
    
    @Autowired
    private HardwareOperationService hardwareOperationService;
    
    @GetMapping
    public ResponseEntity<?> getAllReservations(
            @RequestParam(required = false) String filter) {
        try {
            List<ReservationResponseDto> reservations = hardwareOperationService.getAllReservations(filter);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las reservaciones: " + e.getMessage());
        }
    }
    
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<?> getUserReservations(
            @PathVariable String userEmail,
            @RequestParam(required = false) String filter) {
        try {
            if (userEmail == null || userEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El ID de usuario es requerido");
            }
            
            List<ReservationResponseDto> reservations = hardwareOperationService.getUserReservations(userEmail, filter);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener reservaciones del usuario: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID de reservación inválido");
            }
            
            Optional<ReservationResponseDto> reservation = hardwareOperationService.getReservationById(id);
            if (reservation.isPresent()) {
                return ResponseEntity.ok(reservation.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Reservación no encontrada con ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la reservación: " + e.getMessage());
        }
    }
    
    @GetMapping("/availability")
    public ResponseEntity<?> getAvailableHardware() {
        try {
            List<AvailabilityDto> available = hardwareOperationService.getAvailableHardware();
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener hardware disponible: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDto request) {
        try {
            // Validaciones básicas
            if (request == null) {
                return ResponseEntity.badRequest().body("Los datos de la reservación son requeridos");
            }
            // Validaciones básicas
            if (request.getRequester() == null || request.getRequester().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El solicitante (requester) es requerido");
            }
            if (request.getResourceCode() == null) {
                return ResponseEntity.badRequest().body("El código de recurso es requerido");
            }
            if (request.getStart() == null || request.getEnd() == null) {
                return ResponseEntity.badRequest().body("Las fechas de inicio y fin son requeridas");
            }
            if (request.getStart().isAfter(request.getEnd())) {
                return ResponseEntity.badRequest().body("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
            
            ReservationResponseDto reservation = hardwareOperationService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Datos inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se pudo crear la reservación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear reservación: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ID de reservación inválido");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean cancelled = hardwareOperationService.cancelReservation(id);
            if (cancelled) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Reservación cancelada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se puede cancelar la reservación, ya que ha sido entregada o no existe");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al cancelar reservación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{id}/handOver")
    public ResponseEntity<?> handOverHardware(
            @PathVariable Long id,
            @RequestBody(required = false) HandoverReturnDto handoverData) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID de reservación inválido");
            }
            
            Optional<ReservationResponseDto> reservation = hardwareOperationService.handOverHardware(id, handoverData);
            if (reservation.isPresent()) {
                return ResponseEntity.ok(reservation.get());
            } else {
                return ResponseEntity.badRequest()
                        .body("No se pudo procesar la entrega del hardware");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al entregar hardware: " + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnHardware(
            @PathVariable Long id,
            @RequestBody HandoverReturnDto returnData) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID de reservación inválido");
            }
            if (returnData == null) {
                return ResponseEntity.badRequest().body("Los datos de devolución son requeridos");
            }
            
            Optional<ReservationResponseDto> reservation = hardwareOperationService.returnHardware(id, returnData);
            if (reservation.isPresent()) {
                return ResponseEntity.ok(reservation.get());
            } else {
                return ResponseEntity.badRequest()
                        .body("No se pudo procesar la devolución del hardware");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al devolver hardware: " + e.getMessage());
        }
    }
}