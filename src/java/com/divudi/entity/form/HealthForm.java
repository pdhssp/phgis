/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity.form;

import com.divudi.entity.Item;
import java.io.Serializable;
import javax.persistence.Entity;
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
    @ManyToOne
    private HealthFormCategory investigationCategory;
    @ManyToOne
    private HealthReportOtherCategory investigationTube;
    @ManyToOne
    private Sample sample;
    private Double SampleVolume;

    public HealthFormCategory getInvestigationCategory() {
        return investigationCategory;
    }

    public void setInvestigationCategory(HealthFormCategory investigationCategory) {
        this.investigationCategory = investigationCategory;
    }

    public HealthReportOtherCategory getInvestigationTube() {
        return investigationTube;
    }

    public void setInvestigationTube(HealthReportOtherCategory investigationTube) {
        this.investigationTube = investigationTube;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Double getSampleVolume() {
        return SampleVolume;
    }

    public void setSampleVolume(Double SampleVolume) {
        this.SampleVolume = SampleVolume;
    }
}
