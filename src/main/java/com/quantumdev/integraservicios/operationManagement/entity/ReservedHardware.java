package com.quantumdev.integraservicios.operationManagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/* Se mapea la tabla reservedhardware en la Base */

@Entity
@Table(name = "reservedhardware")
public class ReservedHardware {
    @Id
    @Column(name = "code_reshw") //Código unico de la reserva
    private Long codeReshw;
    
    @Column(name = "building_reshw") //Código del edificio donde esta el hardware
    private Short buildingReshw;
    
    @Column(name = "warehouse_reshw") // Código del almacén donde se encuentra el equipo.
    private Short warehouseReshw;
    
    @Column(name = "storedhw_reshw") // Código interno del equipo reservado (referencia a StoredHardware).
    private Short storedhwReshw;
    
    @Column(name = "requester_reshw") // Usuario que solicita la reserva.
    private String requesterReshw;
    
    @Column(name = "manager_reshw") // Usuario encargado de aprobar/gestionar la entrega.
    private String managerReshw;
    
    @Column(name = "start_reshw") // (Inicio) Rango de tiempo solicitado.
    private LocalDateTime startReshw;
    
    @Column(name = "end_reshw") // (Fin) Rango de tiempo solicitado.
    private LocalDateTime endReshw;
    
    @Column(name = "handover_reshw") // (entrega) Rango de tiempo solicitado.
    private LocalDateTime handoverReshw;
    
    @Column(name = "return_reshw") // (devolución) Rango de tiempo solicitado.
    private LocalDateTime returnReshw;
    
    @Column(name = "condrate_reshw")  // Condición del Hardware
    private Short condrateReshw;
    
    @Column(name = "serrate_reshw") // Calificación servicio.
    private Short serrateReshw;
    
    public ReservedHardware() {}
    
    public Long getCodeReshw() { return codeReshw; }
    public void setCodeReshw(Long codeReshw) { this.codeReshw = codeReshw; }
    
    public Short getBuildingReshw() { return buildingReshw; }
    public void setBuildingReshw(Short buildingReshw) { this.buildingReshw = buildingReshw; }
    
    public Short getWarehouseReshw() { return warehouseReshw; }
    public void setWarehouseReshw(Short warehouseReshw) { this.warehouseReshw = warehouseReshw; }
    
    public Short getStoredhwReshw() { return storedhwReshw; }
    public void setStoredhwReshw(Short storedhwReshw) { this.storedhwReshw = storedhwReshw; }
    
    public String getRequesterReshw() { return requesterReshw; }
    public void setRequesterReshw(String requesterReshw) { this.requesterReshw = requesterReshw; }
    
    public String getManagerReshw() { return managerReshw; }
    public void setManagerReshw(String managerReshw) { this.managerReshw = managerReshw; }
    
    public LocalDateTime getStartReshw() { return startReshw; }
    public void setStartReshw(LocalDateTime startReshw) { this.startReshw = startReshw; }
    
    public LocalDateTime getEndReshw() { return endReshw; }
    public void setEndReshw(LocalDateTime endReshw) { this.endReshw = endReshw; }
    
    public LocalDateTime getHandoverReshw() { return handoverReshw; }
    public void setHandoverReshw(LocalDateTime handoverReshw) { this.handoverReshw = handoverReshw; }
    
    public LocalDateTime getReturnReshw() { return returnReshw; }
    public void setReturnReshw(LocalDateTime returnReshw) { this.returnReshw = returnReshw; }
    
    public Short getCondrateReshw() { return condrateReshw; }
    public void setCondrateReshw(Short condrateReshw) { this.condrateReshw = condrateReshw; }
    
    public Short getSerrateReshw() { return serrateReshw; }
    public void setSerrateReshw(Short serrateReshw) { this.serrateReshw = serrateReshw; }
}