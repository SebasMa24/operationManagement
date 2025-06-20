package com.quantumdev.integraservicios.operationManagement.repository;

import com.quantumdev.integraservicios.operationManagement.entity.StoredHardware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//acceder y validar el inventario de hardware

@Repository
public interface StoredHardwareRepository extends JpaRepository<StoredHardware, StoredHardware.StoredHardwareId> {
    
    // Buscar hardware disponible (not currently reserved)
    @Query("SELECT sh FROM StoredHardware sh WHERE sh.stateStoredhw = 'DISPONIBLE' AND " +
           "NOT EXISTS (SELECT rh FROM ReservedHardware rh WHERE " +
           "rh.buildingReshw = sh.buildingStoredhw AND rh.warehouseReshw = sh.warehouseStoredhw AND " +
           "rh.storedhwReshw = sh.codeStoredhw AND rh.handoverReshw IS NOT NULL AND rh.returnReshw IS NULL)")
    List<StoredHardware> findAvailableHardware();
}