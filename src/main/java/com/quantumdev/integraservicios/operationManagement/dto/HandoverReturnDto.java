package com.quantumdev.integraservicios.operationManagement.dto;

public class HandoverReturnDto {
    private Short conditionRate;
    private Short serviceRate;
   
    public HandoverReturnDto() {}
    
    public Short getConditionRate() { return conditionRate; }
    public void setConditionRate(Short conditionRate) { this.conditionRate = conditionRate; }
    
    public Short getServiceRate() { return serviceRate; }
    public void setServiceRate(Short serviceRate) { this.serviceRate = serviceRate; }
}