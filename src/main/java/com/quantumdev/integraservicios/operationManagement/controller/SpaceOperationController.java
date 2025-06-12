package com.quantumdev.integraservicios.operationManagement.controller;

import com.quantumdev.integraservicios.operationManagement.dto.*;
import com.quantumdev.integraservicios.operationManagement.Service.SpaceOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operations/space")
@CrossOrigin(origins = "*")
public class SpaceOperationController {
    
    @Autowired
    private SpaceOperationService spaceOperationService;
    
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(
            @RequestParam(required = false) String filter) {
        List<ReservationResponseDto> reservations = spaceOperationService.getAllReservations(filter);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> getUserReservations(
            @PathVariable String userId,
            @RequestParam(required = false) String filter) {
        List<ReservationResponseDto> reservations = spaceOperationService.getUserReservations(userId, filter);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        Optional<ReservationResponseDto> reservation = spaceOperationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/availability")
    public ResponseEntity<List<AvailabilityDto>> getAvailableSpaces() {
        List<AvailabilityDto> available = spaceOperationService.getAvailableSpaces();
        return ResponseEntity.ok(available);
    }
    
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto request) {
        try {
            ReservationResponseDto reservation = spaceOperationService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        boolean cancelled = spaceOperationService.cancelReservation(id);
        return cancelled ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/{id}/handOver")
    public ResponseEntity<ReservationResponseDto> handOverSpace(
            @PathVariable Long id,
            @RequestBody(required = false) HandoverReturnDto handoverData) {
        Optional<ReservationResponseDto> reservation = spaceOperationService.handOverSpace(id, handoverData);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<ReservationResponseDto> returnSpace(
            @PathVariable Long id,
            @RequestBody HandoverReturnDto returnData) {
        Optional<ReservationResponseDto> reservation = spaceOperationService.returnSpace(id, returnData);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}