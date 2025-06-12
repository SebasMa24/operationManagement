package com.quantumdev.integraservicios.operationManagement.entity;

import jakarta.persistence.*;
import java.io.Serializable;

//* hardware disponible para reservas, se mapea la tabla storedhardware de la base */

@Entity
@Table(name = "storedhardware")
@IdClass(StoredHardware.StoredHardwareId.class)
public class StoredHardware {
    @Id
    @Column(name = "building_storedhw")
    private Short buildingStoredhw;
    
    @Id
    @Column(name = "warehouse_storedhw")
    private Short warehouseStoredhw;
    
    @Id
    @Column(name = "code_storedhw")
    private Short codeStoredhw;
    
    @Column(name = "hardware_storedhw")
    private Long hardwareStoredhw;
    
    @Column(name = "state_storedhw")
    private String stateStoredhw;
    
    public StoredHardware() {}
    
    public Short getBuildingStoredhw() { return buildingStoredhw; }
    public void setBuildingStoredhw(Short buildingStoredhw) { this.buildingStoredhw = buildingStoredhw; }
    
    public Short getWarehouseStoredhw() { return warehouseStoredhw; }
    public void setWarehouseStoredhw(Short warehouseStoredhw) { this.warehouseStoredhw = warehouseStoredhw; }
    
    public Short getCodeStoredhw() { return codeStoredhw; }
    public void setCodeStoredhw(Short codeStoredhw) { this.codeStoredhw = codeStoredhw; }
    
    public Long getHardwareStoredhw() { return hardwareStoredhw; }
    public void setHardwareStoredhw(Long hardwareStoredhw) { this.hardwareStoredhw = hardwareStoredhw; }
    
    public String getStateStoredhw() { return stateStoredhw; }
    public void setStateStoredhw(String stateStoredhw) { this.stateStoredhw = stateStoredhw; }
    
    // Clase que permite el manejo de la clave compuesta 

    public static class StoredHardwareId implements Serializable {
        private Short buildingStoredhw;
        private Short warehouseStoredhw;
        private Short codeStoredhw;
        
        public StoredHardwareId() {}
        
        public StoredHardwareId(Short buildingStoredhw, Short warehouseStoredhw, Short codeStoredhw) {
            this.buildingStoredhw = buildingStoredhw;
            this.warehouseStoredhw = warehouseStoredhw;
            this.codeStoredhw = codeStoredhw;
        }
        
        public Short getBuildingStoredhw() { return buildingStoredhw; }
        public void setBuildingStoredhw(Short buildingStoredhw) { this.buildingStoredhw = buildingStoredhw; }
        
        public Short getWarehouseStoredhw() { return warehouseStoredhw; }
        public void setWarehouseStoredhw(Short warehouseStoredhw) { this.warehouseStoredhw = warehouseStoredhw; }
        
        public Short getCodeStoredhw() { return codeStoredhw; }
        public void setCodeStoredhw(Short codeStoredhw) { this.codeStoredhw = codeStoredhw; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StoredHardwareId)) return false;
            StoredHardwareId that = (StoredHardwareId) o;
            return buildingStoredhw.equals(that.buildingStoredhw) &&
                   warehouseStoredhw.equals(that.warehouseStoredhw) &&
                   codeStoredhw.equals(that.codeStoredhw);
        }
        
        @Override
        public int hashCode() {
            return buildingStoredhw.hashCode() + warehouseStoredhw.hashCode() + codeStoredhw.hashCode();
        }
    }
}