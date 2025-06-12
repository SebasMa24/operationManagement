package com.quantumdev.integraservicios.operationManagement.dto;

public class AvailabilityDto {
    private Short building;
    private Short resourceCode;
    private Short storedResourceCode; 
    private String resourceName;
    private String resourceType;
    private String state;
    private Short capacity; 
    private String schedule;
    private String description;
    
 
    public AvailabilityDto() {}
    
    
    public Short getBuilding() { return building; }
    public void setBuilding(Short building) { this.building = building; }
    
    public Short getResourceCode() { return resourceCode; }
    public void setResourceCode(Short resourceCode) { this.resourceCode = resourceCode; }
    
    public Short getStoredResourceCode() { return storedResourceCode; }
    public void setStoredResourceCode(Short storedResourceCode) { this.storedResourceCode = storedResourceCode; }
    
    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public Short getCapacity() { return capacity; }
    public void setCapacity(Short capacity) { this.capacity = capacity; }
    
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}