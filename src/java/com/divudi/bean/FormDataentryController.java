/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.bean;

import com.divudi.entity.form.FilledHealthFormReport;
import com.divudi.entity.form.HealthForm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author Buddhika
 */
@Named(value = "formDataentryController")
@Dependent
public class FormDataentryController {

    HealthForm healthForm;
    Date fromDate;
    Date toDate;
    int year;
    int month;
    int quarter;

    public HealthForm getHealthForm() {
        return healthForm;
    }

    public void setHealthForm(HealthForm healthForm) {
        this.healthForm = healthForm;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    @Inject
    SessionController sessionController;

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    
    
    
    public void startPhmDataEntry() {
        if (healthForm == null) {
            UtilityController.addErrorMessage("Please select a form");
        }
        Map m = new HashMap();
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());
        
        FilledHealthFormReport f = new FilledHealthFormReport();
        
        f.getYearVal();
        String jpql;
        switch (healthForm.getDurationType()) {
            case Annually:
                jpql = "select f from FilledHealthFormReport f where f.area=:a and a.yearVal = " + getYear() ;
                

            case Daily:

            case Monthly:

            case Weekly:

            case Variable:

            case Quarterly:

        }

    }

    /**
     * Creates a new instance of FormDataentryController
     */
    public FormDataentryController() {
    }
}
