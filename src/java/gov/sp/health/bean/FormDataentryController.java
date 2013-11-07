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
    private Date formDate;
    int yearVal;
    int monthVal;
    int quarterVal;
    int dateVal;
    @EJB
    FilledHealthFormFacade filledHealthFormFacade;
    @EJB
    HealthFormItemValueFacade healthFormItemValueFacade;
    @EJB
    HealthFormFacade healthFormFacade;

    public int getDateVal() {
        return dateVal;
    }

    public void setDateVal(int dateVal) {
        this.dateVal = dateVal;
    }

    public HealthFormFacade getHealthFormFacade() {
        return healthFormFacade;
    }

    public void setHealthFormFacade(HealthFormFacade healthFormFacade) {
        this.healthFormFacade = healthFormFacade;
    }
    List<HealthForm> formsToFill;
    List<FilledHealthForm> filledForms;
    List<FilledHealthForm> submittedForms;

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

        int weekVal = c.get(Calendar.WEEK_OF_MONTH);


        if (monthVal < 3) {
            quarterVal = 1;
        } else if (monthVal < 6) {
            quarterVal = 2;
        } else if (monthVal < 9) {
            quarterVal = 3;
        } else {
            quarterVal = 4;
        }

        this.yearVal = yearVal;
        this.monthVal = monthVal;
        this.fromDate = c.getTime();
        this.toDate = c.getTime();
        this.quarterVal = quarterVal;
        this.dateVal = dateVal;

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
                sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.monthVal=:m and ff.dateVal=:d ";
                m.put("y", yearVal);
                m.put("m", monthVal);
                m.put("d", dateVal);
                m.put("i", f);//********************************************
                m.put("a", sessionController.getLoggedUser().getStaff().getArea());
                tff = getFilledHealthFormFacade().findBySQL(sql, m);
                if (tff.isEmpty()) {
                    formsToFill.add(f);
                }
            } else if (f.getDurationType() == DurationType.Monthly) {
                sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.monthVal=:m ";
                m.put("y", yearVal);
                m.put("m", monthVal);
                m.put("i", f);
                m.put("a", sessionController.getLoggedUser().getStaff().getArea());
                tff = getFilledHealthFormFacade().findBySQL(sql, m);
                if (tff.isEmpty()) {
                    formsToFill.add(f);
                }
            } else if (f.getDurationType() == DurationType.Weekly) {
                sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.monthVal=:m and ff.dateVal=:d ";
                m.put("y", yearVal);
                m.put("m", monthVal);
                m.put("d", dateVal);
                m.put("w", weekVal);
                m.put("i", f);
                m.put("a", sessionController.getLoggedUser().getStaff().getArea());
                tff = getFilledHealthFormFacade().findBySQL(sql, m);
                if (tff.isEmpty()) {
                    formsToFill.add(f);
                }
            } else if (f.getDurationType() == DurationType.Quarterly) {
                sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and  ff.quarterVal=:q  ";
                m.put("y", yearVal);
                m.put("q", quarterVal);

                m.put("i", f);
                m.put("a", sessionController.getLoggedUser().getStaff().getArea());
                tff = getFilledHealthFormFacade().findBySQL(sql, m);
                if (tff.isEmpty()) {
                    formsToFill.add(f);
                }
            }
        }

        return formsToFill;
    }

    public List<FilledHealthForm> getFilledForms() {
        String sql;
        filledForms = new ArrayList<FilledHealthForm>();
        Map m = new HashMap();
        sql = "select ff from FilledHealthForm ff where ff.approved=false and ff.area=:a and ff.retired=false ";
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());
        filledForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        return filledForms;
    }

    public List<FilledHealthForm> getSubmittedForms() {
        String sql;
        submittedForms = new ArrayList<FilledHealthForm>();
        Map m = new HashMap();
        sql = "select ff from FilledHealthForm ff where ff.approved=true and ff.area=:a and ff.retired=false ";
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());
        submittedForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        return submittedForms;
    }

    public void setSubmittedForms(List<FilledHealthForm> submittedForms) {
        this.submittedForms = submittedForms;
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
        getFilledHealthFormFacade().edit(filledHealthForm);
        UtilityController.addSuccessMessage("Saved");
        System.out.println("saved filled form " + filledHealthForm);
    }

    public void submitFilledForm() {
        System.out.println("going to save");
        if (filledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        filledHealthForm.setApproveAt(Calendar.getInstance().getTime());
        filledHealthForm.setApproved(Boolean.TRUE);
        filledHealthForm.setApproveUser(sessionController.getLoggedUser());
        getFilledHealthFormFacade().edit(filledHealthForm);
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

    public int getYearVal() {
        return yearVal;
    }

    public void setYearVal(int yearVal) {
        this.yearVal = yearVal;
    }

    public int getMonthVal() {
        return monthVal;
    }

    public void setMonthVal(int monthVal) {
        this.monthVal = monthVal;
    }

    public int getQuarterVal() {
        return quarterVal;
    }

    public void setQuarterVal(int quarterVal) {
        this.quarterVal = quarterVal;
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
                jpql = "select f from FilledHealthForm f where f.area=:a and f.yearVal = " + getYearVal();
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
        fCal.set(Calendar.YEAR, yearVal);
        formDate = fCal.getTime();
        UtilityController.addSuccessMessage("Health Form Selected is " + healthForm.getName());
        if (healthForm == null) {
            UtilityController.addErrorMessage("Please select a form");
            return "";
        }
        Map m = new HashMap();
        m.put("a", sessionController.getLoggedUser().getStaff().getArea());

        String jpql;
        switch (healthForm.getDurationType()) {
            case Annually:
                m.put("hf", healthForm);
                m.put("y", yearVal);
                System.out.println("anual report");
                jpql = "select f from FilledHealthForm f where f.item=:hf and f.area=:a and f.yearVal =:y ";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    filledHealthForm.setArea(getSessionController().getArea());
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setFromDate(CommonFunctions.firstDateOfYear(formDate));
                    filledHealthForm.setToDate(CommonFunctions.lastDateOfYear(formDate));
                    getFilledHealthFormFacade().create(filledHealthForm);
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }

            case Daily:
                m.put("hf", healthForm);
                m.put("y", yearVal);
                m.put("m", monthVal);
                m.put("d", dateVal);
                jpql = "select f from FilledHealthForm f where f.item=:hf and f.area=:a and f.yearVal =:y and f.monthVal=:m and f.dateVal=:d ";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    filledHealthForm.setArea(getSessionController().getArea());
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setMonthVal(monthVal);
                    filledHealthForm.setDateVal(dateVal);
                    filledHealthForm.setFromDate(CommonFunctions.getStartOfDay(formDate));
                    filledHealthForm.setToDate(CommonFunctions.getEndOfDay(formDate));
                    getFilledHealthFormFacade().create(filledHealthForm);
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }


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

    public Date getFormDate() {
        return formDate;
    }

    public void setFormDate(Date formDate) {
        this.formDate = formDate;
    }
}
