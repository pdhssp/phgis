/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity.form;

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
    
    
    @OneToMany(mappedBy = "investigationItem", cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    List<HealthFormItemValue> investigationItemValues;

    public List<HealthFormItemValue> getInvestigationItemValues() {
        return investigationItemValues;
    }

    public void setInvestigationItemValues(List<HealthFormItemValue> investigationItemValues) {
        this.investigationItemValues = investigationItemValues;
    }
    
    
    
}
