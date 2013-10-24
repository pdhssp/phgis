/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity.form;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author Buddhika
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HealthFormItem extends ReportItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
//    @OneToMany(mappedBy = "investigationItem", cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    
    @OneToMany(mappedBy = "healthformItem", cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    List<HealthFormItemValue> healthformItemValues;

    public List<HealthFormItemValue> getHealthformItemValues() {
        return healthformItemValues;
    }

    public void setHealthformItemValues(List<HealthFormItemValue> healthformItemValues) {
        this.healthformItemValues = healthformItemValues;
    }
    
    
    
}
