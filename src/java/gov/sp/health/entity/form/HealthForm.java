/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity.form;

import gov.sp.health.data.AreaType;
import gov.sp.health.data.DurationType;
import gov.sp.health.data.InstitutionType;
import gov.sp.health.data.StaffRole;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.Item;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author buddhika
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HealthForm extends Item implements Serializable {

    private static final long serialVersionUID = 1L;
    //Main Properties
    
    @Enumerated(EnumType.STRING)
    AreaType areaType;
    
    @Enumerated(EnumType.STRING)
    DurationType durationType;
    
    @Enumerated(EnumType.STRING)
    InstitutionType institutionType;
    
    @Enumerated(EnumType.STRING)
    StaffRole staffRole;
    
    
    @ManyToOne
    private HealthFormCategory formCategory;
    @ManyToOne
    private HealthReportOtherCategory investigationTube;

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    
    
    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }

    public DurationType getDurationType() {
        return durationType;
    }

    public void setDurationType(DurationType durationType) {
        this.durationType = durationType;
    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }
        public HealthFormCategory getFormCategory() {
        return formCategory;
    }
        
        

    public void setFormCategory(HealthFormCategory formCategory) {
        this.formCategory = formCategory;
    }

    public HealthReportOtherCategory getInvestigationTube() {
        return investigationTube;
    }

    public void setInvestigationTube(HealthReportOtherCategory investigationTube) {
        this.investigationTube = investigationTube;
    }


}
