/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.data.CalculationType;
import gov.sp.health.data.DurationType;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.ejb.CommonFunctions;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.form.FilledHealthForm;
import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.entity.form.FormCal;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.HealthFormItemValue;
import gov.sp.health.facade.FilledHealthFormFacade;
import gov.sp.health.facade.FilledHealthFormReportItemValueFacade;
import gov.sp.health.facade.HealthFormFacade;
import gov.sp.health.facade.HealthFormItemValueFacade;
import gov.sp.health.facade.IxCalFacade;
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
import javax.persistence.TemporalType;

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
    @EJB
    private IxCalFacade ixCalFacade;

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
        try {
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


            this.fromDate = c.getTime();
            this.toDate = c.getTime();

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

                    yearVal = fromDate.getYear();
                    monthVal = fromDate.getMonth();
                    dateVal = fromDate.getDate();

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
                    sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.weekVal=:w ";
                    m.put("y", yearVal);
//                m.put("m", monthVal);
//                m.put("d", dateVal);
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
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            formsToFill = null;
        }



        return formsToFill;
    }

    public List<FilledHealthForm> getFilledForms() {
        try {
            String sql;
            filledForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=false and ff.area=:a and ff.retired=false ";
            m.put("a", sessionController.getLoggedUser().getStaff().getArea());
            filledForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);

        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            filledForms = null;
        }

        return filledForms;
    }

    public List<FilledHealthForm> getSubmittedForms() {
        try {
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=true and ff.area=:a and ff.retired=false ";
            m.put("a", sessionController.getLoggedUser().getStaff().getArea());
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
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

    public void performCals() {
        System.out.println("going to save");
        if (filledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        System.out.println("filledHealthForm = " + filledHealthForm);
        
        for (FilledHealthFormItemValue fhfiv : filledHealthForm.getFilledHealthFormReportItemValue()) {
            
            System.out.println("fhfiv = " + fhfiv);
            
            if (fhfiv.getHealthFormItem().getHealthFormItemType() == HealthFormItemType.Calculation) {
                String sql;
                System.out.println("is a cal = ");
                if (fhfiv.getHealthFormItem() != null) {
                    System.out.println("fhfiv = " + fhfiv);
                    sql = "select i from FormCal i where i.calIxItem.id = " + fhfiv.getHealthFormItem().getId();

                    List<FormCal> items = getIxCalFacade().findBySQL(sql);
                    CalculationType ct = null;
                    
                    for (FormCal f : items) {
                        System.out.println("f = " + f);
                        if (f.getCalculationType() == CalculationType.Sum) {
                            System.out.println("sum");
                            ct = CalculationType.Sum;
                        }
                        if (f.getCalculationType() == CalculationType.Value) {
                            System.out.println("cal");
                            if (ct == CalculationType.Sum) {
                                System.out.println("calculating sum");
                                fhfiv.setDoubleValue(findSumOfFfiv(f.getValIxItem(), fhfiv.getFilledHealthFormReport().getFromDate() , fhfiv.getFilledHealthFormReport().getToDate(), fhfiv.getFilledHealthFormReport().getArea() ));
                            } else if (ct == CalculationType.Average) {
                                System.out.println("calculating ave");
                                
                            }
                        }
                    }
                }
            }
        }
    }

    public double findSumOfFfiv(HealthFormItem hfi, Date fromDate, Date toDate, Area a) {
        System.out.println("findSumOfFfiv");
        System.out.println("hfi = " + hfi);
        System.out.println("fromDate = " + fromDate);
        System.out.println("toDate = " + toDate);
        System.out.println("a = " + a);
        if (hfi == null) {
            System.out.println("hfi not null");
            FilledHealthFormItemValue hfiv = new FilledHealthFormItemValue();
            System.out.println("hfiv = " + hfiv);
            hfiv.getDoubleValue();
            System.out.println("value = " );
            hfiv.getHealthFormItem();
            System.out.println("item = " );
            hfiv.getFilledHealthFormReport().getFromDate();
            System.out.println("FromDate");
            hfiv.getFilledHealthFormReport().getToDate();
            System.out.println("To date");
            hfiv.getFilledHealthFormReport().getArea().getSuperArea();
            System.out.println("area");
            Map m = new HashMap();
            m.put("fd", fromDate);
            m.put("td", toDate);
            m.put("hfi", hfi);
            m.put("ar", a);
            String sql = "select sum(hfiv.doubleValue) from FilledHealthFormItemValue hfiv where hfiv.healthFormItem=:hfi and "
                    + " hfiv.filledHealthFormReport.fromDate >=:fd and "
                    + " hfiv.filledHealthFormReport.toDate<=:td and "
                    + " (hfiv.filledHealthFormReport.area=:a or hfiv.filledHealthFormReport.area.superArea=:a or hfiv.filledHealthFormReport.area.superArea.superArea=:a or hfiv.filledHealthFormReport.area.superArea.superArea.superArea=:a) ";
            System.out.println("sql = " + sql);
            return getFilledHealthFormFacade().findDoubleByJpql(sql, m, TemporalType.DATE);
        } else {
            return 0;
        }


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
            }else if (item.getHealthFormItemType() == HealthFormItemType.Calculation) {
                System.out.println("Adding New Calculation " + item.toString());
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
        if (healthForm == null || sessionController == null || sessionController.getLoggedUser() == null || sessionController.getLoggedUser().getStaff() == null) {
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
                System.out.println("jpql = " + jpql);
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    //Date temFrdomDate = Calendar.getInstance().getTime();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.set(Calendar.MONTH, 1);
                    c.set(Calendar.DATE, 1);
                    filledHealthForm.setFromDate(c.getTime());
                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, getYearVal());
                    c.set(Calendar.MONTH, 12);
                    c.set(Calendar.DATE, 31);
                    filledHealthForm.setToDate(c.getTime());
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setArea(sessionController.getLoggedUser().getStaff().getArea());
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }
                break;

            case Daily:
                System.out.println("Daily report");
                yearVal = fromDate.getYear();
                System.out.println(yearVal);
                monthVal = fromDate.getMonth();
                System.out.println(monthVal);
                dateVal = fromDate.getDate();
                System.out.println(dateVal);
                jpql = "select f from FilledHealthForm f where f.area=:a and f.yearVal = " + getYearVal() + " and f.monthVal=" + getMonthVal() + " and f.dateVal=" + getDateVal() + "";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    Date temFrdomDate = Calendar.getInstance().getTime();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.add(Calendar.MONTH, monthVal);
                    c.add(Calendar.DATE, dateVal);
                    filledHealthForm.setFromDate(c.getTime());
                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.add(Calendar.MONTH, 1);
                    c.set(Calendar.DATE, 1);
                    filledHealthForm.setToDate(c.getTime());

                    filledHealthForm.setArea(sessionController.getLoggedUser().getStaff().getArea());
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }

                break;

            case Monthly:
                System.out.println("Monthly report");
                jpql = "select f from FilledHealthForm f where f.area=:a and f.yearVal = " + getYearVal() + " and f.monthVal=" + getMonthVal() + "";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    Date temFrdomDate = Calendar.getInstance().getTime();
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);//this year
                    c.add(Calendar.MONTH, monthVal);//this month
                    c.set(Calendar.DATE, 1);
                    filledHealthForm.setFromDate(c.getTime());  // set the from date of the form like this year this month
                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.set(Calendar.MONTH, monthVal);
                    c.add(Calendar.MONTH, 1);
                    c.set(Calendar.DATE, 1);
                    c.add(Calendar.DATE, -1);  // -1 for get the end of the month
                    filledHealthForm.setToDate(c.getTime()); // set the to date of the form
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setMonthVal(monthVal);

                    filledHealthForm.setArea(sessionController.getLoggedUser().getStaff().getArea());
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }
                break;
            case Weekly:
                break;

            case Variable:
                break;

            case Quarterly:
                break;
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

    public IxCalFacade getIxCalFacade() {
        return ixCalFacade;
    }

    public void setIxCalFacade(IxCalFacade ixCalFacade) {
        this.ixCalFacade = ixCalFacade;
    }
}
