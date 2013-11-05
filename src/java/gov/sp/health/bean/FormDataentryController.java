/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.data.DurationType;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.ejb.CommonFunctions;
import gov.sp.health.entity.form.FilledHealthForm;
import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.facade.FilledHealthFormFacade;
import gov.sp.health.facade.FilledHealthFormReportItemValueFacade;
import gov.sp.health.facade.HealthFormFacade;
import gov.sp.health.facade.HealthFormItemValueFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 *
 */
@Named(value = "formDataentryController")
@SessionScoped
public class FormDataentryController implements Serializable {

    FilledHealthForm filledHealthForm;
    HealthForm healthForm;
    Date fromDate;
    Date toDate;
    int year;
    int month;
    int quarter;
    @EJB
    FilledHealthFormFacade filledHealthFormFacade;
    @EJB
    HealthFormItemValueFacade healthFormItemValueFacade;
    @EJB
    HealthFormFacade healthFormFacade;

    public HealthFormFacade getHealthFormFacade() {
        return healthFormFacade;
    }

    public void setHealthFormFacade(HealthFormFacade healthFormFacade) {
        this.healthFormFacade = healthFormFacade;
    }
    List<HealthForm> formsToFill;

    public List<HealthForm> getFormsToFill() {
        String sql;
        formsToFill = new ArrayList<HealthForm>();
//        healthForm.getStaffRole()
        FilledHealthForm ff = new FilledHealthForm();
        ff.getQuarterVal();


        sql = "select f from HealthForm f where f.retired=false and f.staffRole=:r";
        Map m = new HashMap();
        m.put("r", sessionController.getLoggedUser().getStaff().getStaffRole());
        List<HealthForm> fs = getHealthFormFacade().findBySQL(sql, m);
        Calendar c = Calendar.getInstance();

        int yearVal = c.get(Calendar.YEAR);
        int monthVal = c.get(Calendar.MONTH);
        int dateVal = c.get(Calendar.DATE);
        int quarterVal;
        if (monthVal < 3) {
            quarterVal = 1;
        } else if (monthVal < 6) {
            quarterVal = 2;
        } else if (monthVal < 9) {
            quarterVal = 3;
        } else {
            quarterVal = 4;
        }
        List<FilledHealthForm> tff;
        for (HealthForm f : fs) {
            m = new HashMap();
            if (f.getDurationType() == DurationType.Annually) {
                sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y ";
                m.put("y", yearVal);
                m.put("i", f);
                m.put("a", sessionController.getLoggedUser().getStaff().getArea());
                tff = getFilledHealthFormFacade().findBySQL(sql, m);
                if (tff.isEmpty()) {
                    formsToFill.add(f);
                }
            } else if (f.getDurationType() == DurationType.Daily) {
            } else if (f.getDurationType() == DurationType.Monthly) {
            } else if (f.getDurationType() == DurationType.Weekly) {
            } else if (f.getDurationType() == DurationType.Quarterly) {
            }
        }

        return formsToFill;
    }

    public void setFormsToFill(List<HealthForm> formsToFill) {
        this.formsToFill = formsToFill;
    }

    public void saveFilledForm() {
        System.out.println("going to save");
        if (filledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        if (filledHealthForm.getId() == null || filledHealthForm.getId() == 0) {
            getFilledHealthFormFacade().create(filledHealthForm);
        } else {
            getFilledHealthFormFacade().edit(filledHealthForm);
        }
        UtilityController.addSuccessMessage("Saved");
        System.out.println("saved filled form " + filledHealthForm);
    }

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

    public FilledHealthFormFacade getFilledHealthFormFacade() {
        return filledHealthFormFacade;
    }

    public void setFilledHealthFormFacade(FilledHealthFormFacade filledHealthFormFacade) {
        this.filledHealthFormFacade = filledHealthFormFacade;
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
        form = (HealthForm) fhf.getItem();
        System.out.println("creating filled health form");
        for (HealthFormItem item : form.getReportItems()) {
            if (item.getHealthFormItemType() == HealthFormItemType.Value) {
                System.out.println("Adding New Item " + item.toString());
                FilledHealthFormItemValue val = new FilledHealthFormItemValue();
                val.setFilledHealthFormReport(fhf);
                val.setHealthFormItem(item);
                fhf.getFilledHealthFormReportItemValue().add(val);
            }
        }
        getFilledHealthFormFacade().edit(fhf);
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
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
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
        return "health_form_fill";
    }

    public String startDataEntry() {
        Date formDate;
        Calendar fCal = Calendar.getInstance();
        fCal.set(Calendar.YEAR, year);
        formDate = fCal.getTime();
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
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    filledHealthForm.setArea(getSessionController().getArea());
                    filledHealthForm.setYearVal(year);
                    filledHealthForm.setFromDate(CommonFunctions.firstDateOfYear(formDate));
                    filledHealthForm.setToDate(CommonFunctions.lastDateOfYear(formDate));
                    getFilledHealthFormFacade().create(filledHealthForm);
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }

            case Daily:

            case Monthly:

            case Weekly:

            case Variable:

            case Quarterly:

        }
        return "health_form_fill";
    }

    /**
     * Creates a new instance of FormDataentryController
     */
    public FormDataentryController() {
    }
}
