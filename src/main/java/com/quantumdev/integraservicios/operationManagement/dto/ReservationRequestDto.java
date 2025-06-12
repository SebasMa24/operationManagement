package com.quantumdev.integraservicios.operationManagement.dto;

import java.time.LocalDateTime;

public class ReservationRequestDto {
    private Short building;
    private Short resourceCode; 
    private Short storedResourceCode; 
    private String requester;
    private String manager;
    private LocalDateTime start;
    private LocalDateTime end;
    
    public ReservationRequestDto() {}
    
    public Short getBuilding() { return building; }
    public void setBuilding(Short building) { this.building = building; }
    
    public Short getResourceCode() { return resourceCode; }
    public void setResourceCode(Short resourceCode) { this.resourceCode = resourceCode; }
    
    public Short getStoredResourceCode() { return storedResourceCode; }
    public void setStoredResourceCode(Short storedResourceCode) { this.storedResourceCode = storedResourceCode; }
    
    public String getRequester() { return requester; }
    public void setRequester(String requester) { this.requester = requester; }
    
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}