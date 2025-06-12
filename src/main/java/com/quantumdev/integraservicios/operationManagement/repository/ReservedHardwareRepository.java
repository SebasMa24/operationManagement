package com.quantumdev.integraservicios.operationManagement.repository;

import com.quantumdev.integraservicios.operationManagement.entity.ReservedHardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//Encargado de gestionar las reservas de hardware

@Repository
public interface ReservedHardwareRepository extends JpaRepository<ReservedHardware, Long> {
    
    // Buscar todas las reservaciones por usuario 
    List<ReservedHardware> findByRequesterReshw(String requester);
    
    // Buscar reservaciones con status past (returned)
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.returnReshw IS NOT NULL")
    List<ReservedHardware> findPastReservations();
    
    // Buscar reservaciones con status active (handed over but not returned)
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.handoverReshw IS NOT NULL AND rh.returnReshw IS NULL")
    List<ReservedHardware> findActiveReservations();
    
    // Buscar reservaciones con status future (not handed over yet)
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.handoverReshw IS NULL")
    List<ReservedHardware> findFutureReservations();
    
    // Buscar reservaciones past por usuario 
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.requesterReshw = :requester AND rh.returnReshw IS NOT NULL")
    List<ReservedHardware> findPastReservationsByUser(@Param("requester") String requester);
    
    // Buscar reservaciones active por usuario 
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.requesterReshw = :requester AND rh.handoverReshw IS NOT NULL AND rh.returnReshw IS NULL")
    List<ReservedHardware> findActiveReservationsByUser(@Param("requester") String requester);
    
    // Buscar reservaciones future por usuario 
    @Query("SELECT rh FROM ReservedHardware rh WHERE rh.requesterReshw = :requester AND rh.handoverReshw IS NULL")
    List<ReservedHardware> findFutureReservationsByUser(@Param("requester") String requester);
    
    // Validar si un hardware esta disponible en un rango de tiempo 
    @Query("SELECT COUNT(rh) FROM ReservedHardware rh WHERE " +
           "rh.buildingReshw = :building AND rh.warehouseReshw = :warehouse AND rh.storedhwReshw = :storedHw AND " +
           "(rh.returnReshw IS NULL OR rh.returnReshw > :start) AND rh.startReshw < :end")
    Long countConflictingReservations(@Param("building") Short building, 
                                     @Param("warehouse") Short warehouse, 
                                     @Param("storedHw") Short storedHw,
                                     @Param("start") LocalDateTime start, 
                                     @Param("end") LocalDateTime end);
}