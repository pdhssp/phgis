/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.form.FilledHealthForm;
import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.facade.FilledHealthFormReportFacade;
import gov.sp.health.facade.FilledHealthFormReportItemValueFacade;
import gov.sp.health.facade.HealthFormItemValueFacade;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * 
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
    @EJB
    FilledHealthFormReportFacade filledHealthFormReportFacade;
    @EJB
    HealthFormItemValueFacade healthFormItemValueFacade;
    
    public HealthFormItemValueFacade getHealthFormItemValueFacade() {
        return healthFormItemValueFacade;
    }
    
    public void setHealthFormItemValueFacade(HealthFormItemValueFacade healthFormItemValueFacade) {
        this.healthFormItemValueFacade = healthFormItemValueFacade;
    }
    
    public FilledHealthFormReportFacade getFilledHealthFormReportFacade() {
        return filledHealthFormReportFacade;
    }
    
    public void setFilledHealthFormReportFacade(FilledHealthFormReportFacade filledHealthFormReportFacade) {
        this.filledHealthFormReportFacade = filledHealthFormReportFacade;
    }
    
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
    @EJB
    FilledHealthFormReportItemValueFacade filledHealthFormReportItemValueFacade;
    
    public FilledHealthFormReportItemValueFacade getFilledHealthFormReportItemValueFacade() {
        return filledHealthFormReportItemValueFacade;
    }
    
    public void setFilledHealthFormReportItemValueFacade(FilledHealthFormReportItemValueFacade filledHealthFormReportItemValueFacade) {
        this.filledHealthFormReportItemValueFacade = filledHealthFormReportItemValueFacade;
    }
    
    public void createFillefFormFromHealthForm(HealthForm form, FilledHealthForm report) {
        for (HealthFormItem item : healthForm.getReportItems()) {
            System.out.println("Adding New Item " + item.toString());
            FilledHealthFormItemValue val = new FilledHealthFormItemValue();
            val.setFilledHealthFormReport(report);
            val.setHealthFormItem(item);
            getFilledHealthFormReportItemValueFacade().create(val);
            report.getFilledHealthFormReportItemValue().add(val);
        }
        getFilledHealthFormReportFacade().edit(report);
    }
    
    public void startPhmDataEntry() {
        if (healthForm == null) {
            UtilityController.addErrorMessage("Please select a form");
        }
        Map m = new HashMap();
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());
        
        FilledHealthForm f = new FilledHealthForm();
        
        f.getYearVal();
        String jpql;
        System.out.println("health form " + healthForm);
        System.out.println("health form duration type " + healthForm.getDurationType());
        
        
        switch (healthForm.getDurationType()) {
            case Annually:
                jpql = "select f from FilledHealthFormReport f where f.area=:a and a.yearVal = " + getYear();
                FilledHealthForm ff = getFilledHealthFormReportFacade().findFirstBySQL(jpql, m);
                if (ff == null) {
                    ff = new FilledHealthForm();
                    createFillefFormFromHealthForm(healthForm, ff);
                }
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
