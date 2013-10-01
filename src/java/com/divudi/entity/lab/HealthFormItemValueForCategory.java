/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity.lab;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Buddhika
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HealthFormItemValueForCategory extends HealthFormItemValue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    private HealthFormItemValueCategory investigationItemValueCategory;

    public HealthFormItemValueCategory getInvestigationItemValueCategory() {
        return investigationItemValueCategory;
    }

    public void setInvestigationItemValueCategory(HealthFormItemValueCategory investigationItemValueCategory) {
        this.investigationItemValueCategory = investigationItemValueCategory;
    }
    
    
    
}
