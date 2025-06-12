package com.quantumdev.integraservicios.operationManagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/* Se mapea la tabla reservedspace en la Base */
@Entity
@Table(name = "reservedspace")
public class ReservedSpace {
    @Id
    @Column(name = "code_resspace") //Código unico de la reserva
    private Long codeResspace;
    
    @Column(name = "building_resspace") //Código del edificio donde esta el espacio
    private Short buildingResspace;
    
    @Column(name = "space_resspace") //código del espacio reservado
    private Short spaceResspace;
    
    @Column(name = "requester_resspace") // Usuario que solicita la reserva.
    private String requesterResspace;
    
    @Column(name = "manager_resspace") // Usuario encargado de aprobar/gestionar la entrega.
    private String managerResspace;
    
    @Column(name = "start_resspace") // (Inicio) Rango de tiempo solicitado.
    private LocalDateTime startResspace;
    
    @Column(name = "end_resspace") // (Fin) Rango de tiempo solicitado.
    private LocalDateTime endResspace;
    
    @Column(name = "handover_resspace") // (entrega) Rango de tiempo solicitado.
    private LocalDateTime handoverResspace;
    
    @Column(name = "return_resspace") // (devolución) Rango de tiempo solicitado.
    private LocalDateTime returnResspace;
    
    @Column(name = "condrate_resspace") // Condición del espacio
    private Short condrateResspace;
    
    @Column(name = "serrate_resspace") // Calificación del servicio
    private Short serrateResspace;
    
    public ReservedSpace() {}
    
    public Long getCodeResspace() { return codeResspace; }
    public void setCodeResspace(Long codeResspace) { this.codeResspace = codeResspace; }
    
    public Short getBuildingResspace() { return buildingResspace; }
    public void setBuildingResspace(Short buildingResspace) { this.buildingResspace = buildingResspace; }
    
    public Short getSpaceResspace() { return spaceResspace; }
    public void setSpaceResspace(Short spaceResspace) { this.spaceResspace = spaceResspace; }
    
    public String getRequesterResspace() { return requesterResspace; }
    public void setRequesterResspace(String requesterResspace) { this.requesterResspace = requesterResspace; }
    
    public String getManagerResspace() { return managerResspace; }
    public void setManagerResspace(String managerResspace) { this.managerResspace = managerResspace; }
    
    public LocalDateTime getStartResspace() { return startResspace; }
    public void setStartResspace(LocalDateTime startResspace) { this.startResspace = startResspace; }
    
    public LocalDateTime getEndResspace() { return endResspace; }
    public void setEndResspace(LocalDateTime endResspace) { this.endResspace = endResspace; }
    
    public LocalDateTime getHandoverResspace() { return handoverResspace; }
    public void setHandoverResspace(LocalDateTime handoverResspace) { this.handoverResspace = handoverResspace; }
    
    public LocalDateTime getReturnResspace() { return returnResspace; }
    public void setReturnResspace(LocalDateTime returnResspace) { this.returnResspace = returnResspace; }
    
    public Short getCondrateResspace() { return condrateResspace; }
    public void setCondrateResspace(Short condrateResspace) { this.condrateResspace = condrateResspace; }
    
    public Short getSerrateResspace() { return serrateResspace; }
    public void setSerrateResspace(Short serrateResspace) { this.serrateResspace = serrateResspace; }
}