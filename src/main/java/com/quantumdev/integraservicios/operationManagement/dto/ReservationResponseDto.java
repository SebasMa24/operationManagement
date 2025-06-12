package com.quantumdev.integraservicios.operationManagement.dto;

import java.time.LocalDateTime;

public class ReservationResponseDto {
    private Long reservationCode;
    private Short building;
    private Short resourceCode;
    private Short storedResourceCode;
    private String requester;
    private String manager;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime handover;
    private LocalDateTime returnTime;
    private Short conditionRate;
    private Short serviceRate;
    private String status; 
    
    public ReservationResponseDto() {}
    
    public Long getReservationCode() { return reservationCode; }
    public void setReservationCode(Long reservationCode) { this.reservationCode = reservationCode; }
    
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
    
    public LocalDateTime getHandover() { return handover; }
    public void setHandover(LocalDateTime handover) { this.handover = handover; }
    
    public LocalDateTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
    
    public Short getConditionRate() { return conditionRate; }
    public void setConditionRate(Short conditionRate) { this.conditionRate = conditionRate; }
    
    public Short getServiceRate() { return serviceRate; }
    public void setServiceRate(Short serviceRate) { this.serviceRate = serviceRate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}