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
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * 
 */
@Named(value = "formDataentryController")
@SessionScoped
public class FormDataentryController implements Serializable{

    FilledHealthForm filledHealthForm;
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

    public FilledHealthForm getFilledHealthForm() {
        return filledHealthForm;
    }

    public void setFilledHealthForm(FilledHealthForm filledHealthForm) {
        this.filledHealthForm = filledHealthForm;
    }
    
    
    
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
        System.out.println("getting health form " + healthForm);
        return healthForm;
    }
    
    public void setHealthForm(HealthForm healthForm) {
        System.out.println("Setting health form " + healthForm);
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
    
    public void createFillefFormFromHealthForm(FilledHealthForm fhf) {
        HealthForm form;
        form = (HealthForm)fhf.getItem();
        System.out.println("creating filled health form");
        for (HealthFormItem item : form.getReportItems()) {
            System.out.println("Adding New Item " + item.toString());
            FilledHealthFormItemValue val = new FilledHealthFormItemValue();
            val.setFilledHealthFormReport(fhf);
            val.setHealthFormItem(item);
            getFilledHealthFormReportItemValueFacade().create(val);
            fhf.getFilledHealthFormReportItemValue().add(val);
        }
        getFilledHealthFormReportFacade().edit(fhf);
        System.out.println("all items after creation is " + fhf.getFilledHealthFormReportItemValue().toString());
    }
    
    public String startPhmDataEntry() {
        if (healthForm == null) {
            UtilityController.addErrorMessage("Please select a form");
            return "";
        }
        Map m = new HashMap();
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());
        
        
        String jpql;
        
        
        switch (healthForm.getDurationType()) {
            case Annually:
                System.out.println("anual report");
                jpql = "select f from FilledHealthForm f where f.area=:a and f.yearVal = " + getYear();
                filledHealthForm = getFilledHealthFormReportFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }
                
            case Daily:
            
            case Monthly:
            
            case Weekly:
            
            case Variable:
            
            case Quarterly:
            
        }
        return "health_form_fill" ;
    }

    /**
     * Creates a new instance of FormDataentryController
     */
    public FormDataentryController() {
    }
}
