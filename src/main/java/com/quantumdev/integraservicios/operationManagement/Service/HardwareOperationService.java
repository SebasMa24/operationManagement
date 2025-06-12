package com.quantumdev.integraservicios.operationManagement.Service;

import com.quantumdev.integraservicios.operationManagement.dto.*;
import com.quantumdev.integraservicios.operationManagement.entity.ReservedHardware;
import com.quantumdev.integraservicios.operationManagement.entity.StoredHardware;
import com.quantumdev.integraservicios.operationManagement.repository.ReservedHardwareRepository;
import com.quantumdev.integraservicios.operationManagement.repository.StoredHardwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HardwareOperationService {
    
    @Autowired
    private ReservedHardwareRepository reservedHardwareRepository;
    
    @Autowired
    private StoredHardwareRepository storedHardwareRepository;
    
    public List<ReservationResponseDto> getAllReservations(String filter) {
        List<ReservedHardware> reservations;
        
        switch (filter != null ? filter : "all") {
            case "past":
                reservations = reservedHardwareRepository.findPastReservations();
                break;
            case "active":
                reservations = reservedHardwareRepository.findActiveReservations();
                break;
            case "future":
                reservations = reservedHardwareRepository.findFutureReservations();
                break;
            default:
                reservations = reservedHardwareRepository.findAll();
        }
        
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public List<ReservationResponseDto> getUserReservations(String userId, String filter) {
        List<ReservedHardware> reservations;
        
        switch (filter != null ? filter : "all") {
            case "past":
                reservations = reservedHardwareRepository.findPastReservationsByUser(userId);
                break;
            case "active":
                reservations = reservedHardwareRepository.findActiveReservationsByUser(userId);
                break;
            case "future":
                reservations = reservedHardwareRepository.findFutureReservationsByUser(userId);
                break;
            default:
                reservations = reservedHardwareRepository.findByRequesterReshw(userId);
        }
        
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ReservationResponseDto> getReservationById(Long id) {
        return reservedHardwareRepository.findById(id)
                .map(this::convertToResponseDto);
    }
    
    public List<AvailabilityDto> getAvailableHardware() {
        return storedHardwareRepository.findAvailableHardware().stream()
                .map(this::convertToAvailabilityDto)
                .collect(Collectors.toList());
    }
    
    public ReservationResponseDto createReservation(ReservationRequestDto request) {
        // Verificar disponibilidad
        Long conflicts = reservedHardwareRepository.countConflictingReservations(
                request.getBuilding(), 
                request.getResourceCode(), 
                request.getStoredResourceCode(),
                request.getStart(), 
                request.getEnd()
        );
        
        if (conflicts > 0) {
            throw new RuntimeException("Hardware no disponible en el período solicitado");
        }
        
        // Generar ID único 
        Long newId = System.currentTimeMillis();
        
        ReservedHardware reservation = new ReservedHardware();
        reservation.setCodeReshw(newId);
        reservation.setBuildingReshw(request.getBuilding());
        reservation.setWarehouseReshw(request.getResourceCode());
        reservation.setStoredhwReshw(request.getStoredResourceCode());
        reservation.setRequesterReshw(request.getRequester());
        reservation.setManagerReshw(request.getManager());
        reservation.setStartReshw(request.getStart());
        reservation.setEndReshw(request.getEnd());
        
        ReservedHardware saved = reservedHardwareRepository.save(reservation);
        return convertToResponseDto(saved);
    }
    
    public boolean cancelReservation(Long id) {
        Optional<ReservedHardware> reservation = reservedHardwareRepository.findById(id);
        
        if (reservation.isPresent() && reservation.get().getHandoverReshw() == null) {
            reservedHardwareRepository.deleteById(id);
            return true;
        }
        
        return false; // No se puede cancelar si ya se entregó
    }
    
    public Optional<ReservationResponseDto> handOverHardware(Long id, HandoverReturnDto handoverData) {
        Optional<ReservedHardware> optReservation = reservedHardwareRepository.findById(id);
        
        if (optReservation.isPresent()) {
            ReservedHardware reservation = optReservation.get();
            
            if (reservation.getHandoverReshw() == null) {
                reservation.setHandoverReshw(LocalDateTime.now());
                ReservedHardware saved = reservedHardwareRepository.save(reservation);
                return Optional.of(convertToResponseDto(saved));
            }
        }
        
        return Optional.empty();
    }
    
    public Optional<ReservationResponseDto> returnHardware(Long id, HandoverReturnDto returnData) {
        Optional<ReservedHardware> optReservation = reservedHardwareRepository.findById(id);
        
        if (optReservation.isPresent()) {
            ReservedHardware reservation = optReservation.get();
            
            if (reservation.getHandoverReshw() != null && reservation.getReturnReshw() == null) {
                reservation.setReturnReshw(LocalDateTime.now());
                reservation.setCondrateReshw(returnData.getConditionRate());
                reservation.setSerrateReshw(returnData.getServiceRate());
                ReservedHardware saved = reservedHardwareRepository.save(reservation);
                return Optional.of(convertToResponseDto(saved));
            }
        }
        
        return Optional.empty();
    }
    
    private ReservationResponseDto convertToResponseDto(ReservedHardware reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setReservationCode(reservation.getCodeReshw());
        dto.setBuilding(reservation.getBuildingReshw());
        dto.setResourceCode(reservation.getWarehouseReshw());
        dto.setStoredResourceCode(reservation.getStoredhwReshw());
        dto.setRequester(reservation.getRequesterReshw());
        dto.setManager(reservation.getManagerReshw());
        dto.setStart(reservation.getStartReshw());
        dto.setEnd(reservation.getEndReshw());
        dto.setHandover(reservation.getHandoverReshw());
        dto.setReturnTime(reservation.getReturnReshw());
        dto.setConditionRate(reservation.getCondrateReshw());
        dto.setServiceRate(reservation.getSerrateReshw());
        
        if (reservation.getReturnReshw() != null) {
            dto.setStatus("past");
        } else if (reservation.getHandoverReshw() != null) {
            dto.setStatus("active");
        } else {
            dto.setStatus("future");
        }
        
        return dto;
    }
    
    private AvailabilityDto convertToAvailabilityDto(StoredHardware storedHardware) {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setBuilding(storedHardware.getBuildingStoredhw());
        dto.setResourceCode(storedHardware.getWarehouseStoredhw());
        dto.setStoredResourceCode(storedHardware.getCodeStoredhw());
        dto.setState(storedHardware.getStateStoredhw());
        return dto;
    }
}