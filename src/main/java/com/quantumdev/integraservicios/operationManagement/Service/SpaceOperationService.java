package com.quantumdev.integraservicios.operationManagement.Service;

import com.quantumdev.integraservicios.operationManagement.dto.*;
import com.quantumdev.integraservicios.operationManagement.entity.ReservedSpace;
import com.quantumdev.integraservicios.operationManagement.entity.Space;
import com.quantumdev.integraservicios.operationManagement.repository.ReservedSpaceRepository;
import com.quantumdev.integraservicios.operationManagement.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpaceOperationService {
    
    @Autowired
    private ReservedSpaceRepository reservedSpaceRepository;
    
    @Autowired
    private SpaceRepository spaceRepository;
    
    public List<ReservationResponseDto> getAllReservations(String filter) {
        List<ReservedSpace> reservations;
        
        switch (filter != null ? filter : "all") {
            case "past":
                reservations = reservedSpaceRepository.findPastReservations();
                break;
            case "active":
                reservations = reservedSpaceRepository.findActiveReservations();
                break;
            case "future":
                reservations = reservedSpaceRepository.findFutureReservations();
                break;
            default:
                reservations = reservedSpaceRepository.findAll();
        }
        
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public List<ReservationResponseDto> getUserReservations(String userId, String filter) {
        List<ReservedSpace> reservations;
        
        switch (filter != null ? filter : "all") {
            case "past":
                reservations = reservedSpaceRepository.findPastReservationsByUser(userId);
                break;
            case "active":
                reservations = reservedSpaceRepository.findActiveReservationsByUser(userId);
                break;
            case "future":
                reservations = reservedSpaceRepository.findFutureReservationsByUser(userId);
                break;
            default:
                reservations = reservedSpaceRepository.findByRequesterResspace(userId);
        }
        
        return reservations.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ReservationResponseDto> getReservationById(Long id) {
        return reservedSpaceRepository.findById(id)
                .map(this::convertToResponseDto);
    }
    
    public List<AvailabilityDto> getAvailableSpaces() {
        return spaceRepository.findAvailableSpaces().stream()
                .map(this::convertToAvailabilityDto)
                .collect(Collectors.toList());
    }
    
    public ReservationResponseDto createReservation(ReservationRequestDto request) {
        // Verificar disponibilidad
        Long conflicts = reservedSpaceRepository.countConflictingReservations(
                request.getBuilding(), 
                request.getResourceCode(),
                request.getStart(), 
                request.getEnd()
        );
        
        if (conflicts > 0) {
            throw new RuntimeException("Espacio no disponible en el período solicitado");
        }
        
        // Generar ID único 
        Long newId = System.currentTimeMillis();
        
        ReservedSpace reservation = new ReservedSpace();
        reservation.setCodeResspace(newId);
        reservation.setBuildingResspace(request.getBuilding());
        reservation.setSpaceResspace(request.getResourceCode());
        reservation.setRequesterResspace(request.getRequester());
        reservation.setManagerResspace(request.getManager());
        reservation.setStartResspace(request.getStart());
        reservation.setEndResspace(request.getEnd());
        
        ReservedSpace saved = reservedSpaceRepository.save(reservation);
        return convertToResponseDto(saved);
    }
    
    public boolean cancelReservation(Long id) {
        Optional<ReservedSpace> reservation = reservedSpaceRepository.findById(id);
        
        if (reservation.isPresent() && reservation.get().getHandoverResspace() == null) {
            reservedSpaceRepository.deleteById(id);
            return true;
        }
        
        return false; // No se puede cancelar si ya se entregó
    }
    
    public Optional<ReservationResponseDto> handOverSpace(Long id, HandoverReturnDto handoverData) {
        Optional<ReservedSpace> optReservation = reservedSpaceRepository.findById(id);
        
        if (optReservation.isPresent()) {
            ReservedSpace reservation = optReservation.get();
            
            if (reservation.getHandoverResspace() == null) {
                reservation.setHandoverResspace(LocalDateTime.now());
                ReservedSpace saved = reservedSpaceRepository.save(reservation);
                return Optional.of(convertToResponseDto(saved));
            }
        }
        
        return Optional.empty();
    }
    
    public Optional<ReservationResponseDto> returnSpace(Long id, HandoverReturnDto returnData) {
        Optional<ReservedSpace> optReservation = reservedSpaceRepository.findById(id);
        
        if (optReservation.isPresent()) {
            ReservedSpace reservation = optReservation.get();
            
            if (reservation.getHandoverResspace() != null && reservation.getReturnResspace() == null) {
                reservation.setReturnResspace(LocalDateTime.now());
                reservation.setCondrateResspace(returnData.getConditionRate());
                reservation.setSerrateResspace(returnData.getServiceRate());
                ReservedSpace saved = reservedSpaceRepository.save(reservation);
                return Optional.of(convertToResponseDto(saved));
            }
        }
        
        return Optional.empty();
    }
    
    private ReservationResponseDto convertToResponseDto(ReservedSpace reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setReservationCode(reservation.getCodeResspace());
        dto.setBuilding(reservation.getBuildingResspace());
        dto.setResourceCode(reservation.getSpaceResspace());
        dto.setRequester(reservation.getRequesterResspace());
        dto.setManager(reservation.getManagerResspace());
        dto.setStart(reservation.getStartResspace());
        dto.setEnd(reservation.getEndResspace());
        dto.setHandover(reservation.getHandoverResspace());
        dto.setReturnTime(reservation.getReturnResspace());
        dto.setConditionRate(reservation.getCondrateResspace());
        dto.setServiceRate(reservation.getSerrateResspace());
        
        // Determinar estado
        if (reservation.getReturnResspace() != null) {
            dto.setStatus("past");
        } else if (reservation.getHandoverResspace() != null) {
            dto.setStatus("active");
        } else {
            dto.setStatus("future");
        }
        
        return dto;
    }
    
    private AvailabilityDto convertToAvailabilityDto(Space space) {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setBuilding(space.getBuildingSpace());
        dto.setResourceCode(space.getCodeSpace());
        dto.setResourceName(space.getNameSpace());
        dto.setResourceType(space.getTypeSpace());
        dto.setState(space.getStateSpace());
        dto.setCapacity(space.getCapacitySpace());
        dto.setSchedule(space.getScheduleSpace());
        dto.setDescription(space.getDescSpace());
        return dto;
    }
}