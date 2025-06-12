package com.quantumdev.integraservicios.operationManagement.entity;

import jakarta.persistence.*;
import java.io.Serializable;


//* espacio físico disponible para reservas, se mapea la tabla space de la base */

@Entity
@Table(name = "space")
@IdClass(Space.SpaceId.class)
public class Space {
    @Id
    @Column(name = "building_space")
    private Short buildingSpace;
    
    @Id
    @Column(name = "code_space")
    private Short codeSpace;
    
    @Column(name = "type_space")
    private String typeSpace;
    
    @Column(name = "state_space")
    private String stateSpace;
    
    @Column(name = "name_space")
    private String nameSpace;
    
    @Column(name = "capacity_space")
    private Short capacitySpace;
    
    @Column(name = "schedule_space")
    private String scheduleSpace;
    
    @Column(name = "desc_space")
    private String descSpace;
    
    public Space() {}
    
    public Short getBuildingSpace() { return buildingSpace; }
    public void setBuildingSpace(Short buildingSpace) { this.buildingSpace = buildingSpace; }
    
    public Short getCodeSpace() { return codeSpace; }
    public void setCodeSpace(Short codeSpace) { this.codeSpace = codeSpace; }
    
    public String getTypeSpace() { return typeSpace; }
    public void setTypeSpace(String typeSpace) { this.typeSpace = typeSpace; }
    
    public String getStateSpace() { return stateSpace; }
    public void setStateSpace(String stateSpace) { this.stateSpace = stateSpace; }
    
    public String getNameSpace() { return nameSpace; }
    public void setNameSpace(String nameSpace) { this.nameSpace = nameSpace; }
    
    public Short getCapacitySpace() { return capacitySpace; }
    public void setCapacitySpace(Short capacitySpace) { this.capacitySpace = capacitySpace; }
    
    public String getScheduleSpace() { return scheduleSpace; }
    public void setScheduleSpace(String scheduleSpace) { this.scheduleSpace = scheduleSpace; }
    
    public String getDescSpace() { return descSpace; }
    public void setDescSpace(String descSpace) { this.descSpace = descSpace; }

    // Clase que permite el manejo de la clave compuesta
    
    public static class SpaceId implements Serializable {
        private Short buildingSpace;
        private Short codeSpace;
        
        public SpaceId() {}
        
        public SpaceId(Short buildingSpace, Short codeSpace) {
            this.buildingSpace = buildingSpace;
            this.codeSpace = codeSpace;
        }
        
        public Short getBuildingSpace() { return buildingSpace; }
        public void setBuildingSpace(Short buildingSpace) { this.buildingSpace = buildingSpace; }
        
        public Short getCodeSpace() { return codeSpace; }
        public void setCodeSpace(Short codeSpace) { this.codeSpace = codeSpace; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SpaceId)) return false;
            SpaceId spaceId = (SpaceId) o;
            return buildingSpace.equals(spaceId.buildingSpace) && codeSpace.equals(spaceId.codeSpace);
        }
        
        @Override
        public int hashCode() {
            return buildingSpace.hashCode() + codeSpace.hashCode();
        }
    }
}