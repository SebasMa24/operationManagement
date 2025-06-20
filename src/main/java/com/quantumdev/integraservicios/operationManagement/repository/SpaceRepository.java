package com.quantumdev.integraservicios.operationManagement.repository;

import com.quantumdev.integraservicios.operationManagement.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//acceder y validar el inventario de espacios

@Repository
public interface SpaceRepository extends JpaRepository<Space, Space.SpaceId> {
    
    // Buscar espacios disponibles (not currently reserved)
    @Query("SELECT s FROM Space s WHERE s.stateSpace = 'DISPONIBLE'")
    List<Space> findAvailableSpaces();
}