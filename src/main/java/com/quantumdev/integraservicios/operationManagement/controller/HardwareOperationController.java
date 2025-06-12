package com.quantumdev.integraservicios.operationManagement.controller;

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
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(
            @RequestParam(required = false) String filter) {
        List<ReservationResponseDto> reservations = hardwareOperationService.getAllReservations(filter);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> getUserReservations(
            @PathVariable String userId,
            @RequestParam(required = false) String filter) {
        List<ReservationResponseDto> reservations = hardwareOperationService.getUserReservations(userId, filter);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        Optional<ReservationResponseDto> reservation = hardwareOperationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilityDto>> getAvailableHardware() {
        List<AvailabilityDto> available = hardwareOperationService.getAvailableHardware();
        return ResponseEntity.ok(available);
    }
    
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto request) {
        try {
            ReservationResponseDto reservation = hardwareOperationService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        boolean cancelled = hardwareOperationService.cancelReservation(id);
        return cancelled ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/{id}/handOver")
    public ResponseEntity<ReservationResponseDto> handOverHardware(
            @PathVariable Long id,
            @RequestBody(required = false) HandoverReturnDto handoverData) {
        Optional<ReservationResponseDto> reservation = hardwareOperationService.handOverHardware(id, handoverData);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<ReservationResponseDto> returnHardware(
            @PathVariable Long id,
            @RequestBody HandoverReturnDto returnData) {
        Optional<ReservationResponseDto> reservation = hardwareOperationService.returnHardware(id, returnData);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}