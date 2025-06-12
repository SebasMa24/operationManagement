package com.quantumdev.integraservicios.operationManagement.repository;

import com.quantumdev.integraservicios.operationManagement.entity.ReservedSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//Gestion de reservas de espacios fisicos 

@Repository
public interface ReservedSpaceRepository extends JpaRepository<ReservedSpace, Long> {
    
    // Buscar las reservaciones por usuario
    List<ReservedSpace> findByRequesterResspace(String requester);
    
    // Buscar reservaciones con status past (returned)
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.returnResspace IS NOT NULL")
    List<ReservedSpace> findPastReservations();
    
    // Buscar reservaciones con status active (handed over but not returned)
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.handoverResspace IS NOT NULL AND rs.returnResspace IS NULL")
    List<ReservedSpace> findActiveReservations();
    
    // Buscar reservaciones con status future (not handed over yet)
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.handoverResspace IS NULL")
    List<ReservedSpace> findFutureReservations();
    
    // Buscar reservaciones con status past por usuario
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.requesterResspace = :requester AND rs.returnResspace IS NOT NULL")
    List<ReservedSpace> findPastReservationsByUser(@Param("requester") String requester);
    
    // Buscar reservaciones con status active por usuario
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.requesterResspace = :requester AND rs.handoverResspace IS NOT NULL AND rs.returnResspace IS NULL")
    List<ReservedSpace> findActiveReservationsByUser(@Param("requester") String requester);
    
    // Buscar reservaciones con status future por usuario
    @Query("SELECT rs FROM ReservedSpace rs WHERE rs.requesterResspace = :requester AND rs.handoverResspace IS NULL")
    List<ReservedSpace> findFutureReservationsByUser(@Param("requester") String requester);
    
    // Validar si un espacio se encuentra disponible en un periodo de
    @Query("SELECT COUNT(rs) FROM ReservedSpace rs WHERE " +
           "rs.buildingResspace = :building AND rs.spaceResspace = :space AND " +
           "(rs.returnResspace IS NULL OR rs.returnResspace > :start) AND rs.startResspace < :end")
    Long countConflictingReservations(@Param("building") Short building, 
                                     @Param("space") Short space,
                                     @Param("start") LocalDateTime start, 
                                     @Param("end") LocalDateTime end);
}
