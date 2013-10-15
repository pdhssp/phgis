/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity.form;

import com.divudi.data.Sex;
import com.divudi.entity.Item;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 *
 * @author Buddhika
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class HealthFormItemValueFlag extends HealthFormItemValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Enumerated(EnumType.STRING)
    Sex sex;
    @ManyToOne
    HealthFormItem investigationItemOfLabelType;
    @ManyToOne
    private HealthFormItem investigationItemOfValueType;
    @ManyToOne
    HealthFormItem investigationItemOfFlagType;
    @ManyToOne
    Item item;
    long fromAge;
    long toAge;
    @Lob
    private String flagMessage;
    @Lob
    String highMessage;
    @Lob
    String lowMessage;
    @Lob
    String normalMessage;
    boolean displayFlagMessage;
    boolean displayHighMessage;
    boolean displayLowMessage;
    boolean displayNormalMessage;
    double fromVal;
    double toVal;

    public double getFromVal() {
        return fromVal;
    }

    public void setFromVal(double fromVal) {
        this.fromVal = fromVal;
    }

    public double getToVal() {
        return toVal;
    }

    public void setToVal(double toVal) {
        this.toVal = toVal;
    }

    public HealthFormItem getInvestigationItemOfLabelType() {
        return investigationItemOfLabelType;
    }

    public void setInvestigationItemOfLabelType(HealthFormItem investigationItemOfLabelType) {
        this.investigationItemOfLabelType = investigationItemOfLabelType;
    }

    public String getHighMessage() {
        return highMessage;
    }

    public void setHighMessage(String highMessage) {
        this.highMessage = highMessage;
    }

    public String getLowMessage() {
        return lowMessage;
    }

    public void setLowMessage(String lowMessage) {
        this.lowMessage = lowMessage;
    }

    public String getNormalMessage() {
        return normalMessage;
    }

    public void setNormalMessage(String normalMessage) {
        this.normalMessage = normalMessage;
    }

    public boolean isDisplayFlagMessage() {
        return displayFlagMessage;
    }

    public void setDisplayFlagMessage(boolean displayFlagMessage) {
        this.displayFlagMessage = displayFlagMessage;
    }

    public boolean isDisplayHighMessage() {
        return displayHighMessage;
    }

    public void setDisplayHighMessage(boolean displayHighMessage) {
        this.displayHighMessage = displayHighMessage;
    }

    public boolean isDisplayLowMessage() {
        return displayLowMessage;
    }

    public void setDisplayLowMessage(boolean displayLowMessage) {
        this.displayLowMessage = displayLowMessage;
    }

    public boolean isDisplayNormalMessage() {
        return displayNormalMessage;
    }

    public void setDisplayNormalMessage(boolean displayNormalMessage) {
        this.displayNormalMessage = displayNormalMessage;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public HealthFormItemValueFlag() {
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public long getFromAge() {
        return fromAge;
    }

    public void setFromAge(long fromAge) {
        this.fromAge = fromAge;
    }

    public long getToAge() {
        return toAge;
    }

    public void setToAge(long toAge) {
        this.toAge = toAge;
    }

    public String getFlagMessage() {
        return flagMessage;
    }

    public void setFlagMessage(String flagMessage) {
        this.flagMessage = flagMessage;
    }

    public HealthFormItem getInvestigationItemOfValueType() {
        return investigationItemOfValueType;
    }

    public void setInvestigationItemOfValueType(HealthFormItem investigationItemOfValueType) {
        this.investigationItemOfValueType = investigationItemOfValueType;
    }

    public HealthFormItem getInvestigationItemOfFlagType() {
        return investigationItemOfFlagType;
    }

    public void setInvestigationItemOfFlagType(HealthFormItem investigationItemOfFlagType) {
        this.investigationItemOfFlagType = investigationItemOfFlagType;
    }
}
