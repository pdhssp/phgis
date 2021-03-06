/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.data.DurationType;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.ejb.CommonFunctions;
import gov.sp.health.entity.Area;
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
    private Area area;
    private Date fDate;

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
    
    private List<FilledHealthForm> submittedForms;

    
    
    public List<HealthForm> getFormsToFill() {
        try {
            String sql;
            formsToFill = new ArrayList<HealthForm>();

//        healthForm.getStaffRole()
            FilledHealthForm ff = new FilledHealthForm();
            ff.getQuarterVal();

            sql = "select f from HealthForm f where f.retired=false and f.staffRole=:r order by f.id desc";
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
                    m.put("a", sessionController.getArea());
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
                    m.put("a", sessionController.getArea());
                    tff = getFilledHealthFormFacade().findBySQL(sql, m);
                    if (tff.isEmpty()) {
                        formsToFill.add(f);
                    }
                } else if (f.getDurationType() == DurationType.Monthly) {
                    sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.monthVal=:m ";
                    m.put("y", yearVal);
                    m.put("m", monthVal);
                    m.put("i", f);
                    m.put("a", sessionController.getArea());
                    tff = getFilledHealthFormFacade().findBySQL(sql, m);
                    if (tff.isEmpty()) {
                        formsToFill.add(f);
                    }
                } else if (f.getDurationType() == DurationType.Weekly) {
                    sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and ff.weekVal=:w ";
                    m.put("y", yearVal);
                //    m.put("m", monthVal);
//                m.put("d", dateVal);
                    m.put("w", weekVal);
                    m.put("i", f);
                    m.put("a", sessionController.getArea());
                    tff = getFilledHealthFormFacade().findBySQL(sql, m);
                    if (tff.isEmpty()) {
                        formsToFill.add(f);
                    }
                } else if (f.getDurationType() == DurationType.Quarterly) {
                    sql = "select ff from FilledHealthForm ff where ff.area=:a and ff.retired=false and ff.item=:i and ff.yearVal=:y and  ff.quarterVal=:q  ";
                    m.put("y", yearVal);
                    m.put("q", quarterVal);

                    m.put("i", f);
                    m.put("a", sessionController.getArea());
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
        return filledForms;
    }

    public void setFilledForms(List<FilledHealthForm> filledForms) {
        this.filledForms = filledForms;
    }

    public List<FilledHealthForm> getFilledFormsTopTen() {
        try {
            String sql;
            filledForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=false and ff.area=:a and ff.retired=false order by ff.fromDate ";
            m.put("a", sessionController.getArea());
            filledForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            filledForms = null;
        }

        return filledForms;
    }

    public List<FilledHealthForm> getFilledFormsAll() {
        try {
            String sql;
            filledForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=false and ff.area=:a and ff.retired=false ";
            m.put("a", sessionController.getArea());
            filledForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            filledForms = null;
        }

        return filledForms;
    }

    public List<FilledHealthForm> getSubmittedFormsTopTen() {
        try {
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=true and ff.area=:a and ff.retired=false order by ff.fromDate";
            m.put("a", sessionController.getArea());
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        return submittedForms;
    }

    public List<FilledHealthForm> getReceivedFormsTopTen() {
        try {
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a) and ff.retired=false ";
            m.put("a", sessionController.getArea());
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m, 10);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        return submittedForms;
    }

    public void listAllFormsForMyArea() {
        try {
            System.out.println("fromDate=" + fromDate);
            int yearValFrom = 1900 + fromDate.getYear();
            int yearValTo = 1900 + toDate.getYear();
            int monthValFrom = fromDate.getMonth();
            int monthValueTo = toDate.getMonth();
            System.out.println("toDate=" + yearValFrom);
            System.out.println("toDate=" + yearValTo);
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
             sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a or ff.area.superArea.superArea.superArea.superArea=:a) and  ff.fromDate >=:fd and ff.toDate<=:td  and ff.retired=false ";
             m.put("a", area);
            m.put("fd", fromDate);
            m.put("td", toDate);
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;
        }
    }

    public List<FilledHealthForm> getReceivedFormsTopTenBySearch() {
        try {
            System.out.println("fromDate=" + fromDate);
            int yearValFrom = 1900 + fromDate.getYear();
            int yearValTo = 1900 + toDate.getYear();
            int monthValFrom = fromDate.getMonth();
            int monthValueTo = toDate.getMonth();

            System.out.println("toDate=" + yearValFrom);
            System.out.println("toDate=" + yearValTo);
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
           // sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a) and  ff.yearVal between :yf and  :yt and ff.retired=false ";
            //  sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a or ff.area.superArea.superArea.superArea.superArea=:a) and  ff.fromDate >=:fd and ff.toDate<=:td and ff.retired=false ";
            sql = "select ff from FilledHealthForm ff where ff.approved=true  ";

            m.put("a", area);
            m.put("fd", fromDate);
            m.put("td", toDate);

          //  m.put("yf", yearValFrom);
            //  m.put("yt", yearValTo);
            System.out.println("area=" + area);
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        return submittedForms;
    }

    public void listReceivedFormsTopTenBySearchWithHealthForm() {
        try {
            System.out.println("HF=" + healthForm);

            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            // sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a) and  ff.yearVal between :yf and  :yt and ff.retired=false ";
            sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a or ff.area.superArea.superArea.superArea.superArea=:a) and  ff.fromDate >=:fd and ff.toDate<=:td and ff.item=:hf and ff.retired=false ";

            m.put("a", area);
            m.put("fd", fromDate);
            m.put("td", toDate);
            m.put("hf", healthForm);

          //  m.put("yf", yearValFrom);
            //  m.put("yt", yearValTo);
            System.out.println("area=" + area);
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        
    }

    public List<FilledHealthForm> getReceivedFormsTopTenBySearchWithHealthForm() {
        try {
            System.out.println("HF=" + healthForm);

            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            // sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a) and  ff.yearVal between :yf and  :yt and ff.retired=false ";
            sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a or ff.area.superArea.superArea.superArea.superArea=:a) and  ff.fromDate >=:fd and ff.toDate<=:td and ff.item=:hf and ff.retired=false ";

            m.put("a", area);
            m.put("fd", fromDate);
            m.put("td", toDate);
            m.put("hf", healthForm);

          //  m.put("yf", yearValFrom);
            //  m.put("yt", yearValTo);
            System.out.println("area=" + area);
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        return submittedForms;
    }

    public List<FilledHealthForm> getReceivedFormsAll() {
        try {
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=true and (ff.area.superArea=:a or ff.area.superArea.superArea=:a or ff.area=:a or ff.area.superArea.superArea.superArea=:a) and ff.retired=false ";
            m.put("a", sessionController.getArea());
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
            submittedForms = null;

        }
        return submittedForms;
    }

    public List<FilledHealthForm> getSubmittedFormsAll() {
        try {
            String sql;
            submittedForms = new ArrayList<FilledHealthForm>();
            Map m = new HashMap();
            sql = "select ff from FilledHealthForm ff where ff.approved=true and ff.area=:a and ff.retired=false ";
            m.put("a", sessionController.getArea());
            submittedForms = getFilledHealthFormFacade().findBySQL(sql, m);
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
        filledHealthForm.setCreatedAt(Calendar.getInstance().getTime());
        filledHealthForm.setCreater(getSessionController().getLoggedUser());
        System.out.println("filledHealthForm = " + filledHealthForm);
        if (filledHealthForm.getId() == null) {
            getFilledHealthFormFacade().create(filledHealthForm);
        } else {
            getFilledHealthFormFacade().edit(filledHealthForm);
        }
        getFilledHealthFormFacade().edit(filledHealthForm);
        System.out.println("filledHealthForm = " + filledHealthForm);
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

        if (filledHealthForm.getId() == null) {
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
        if (fhf.getId() == null) {
            getFilledHealthFormFacade().create(fhf);
        } else {
            getFilledHealthFormFacade().edit(fhf);
        }
        System.out.println("all items after creation is " + fhf.getFilledHealthFormReportItemValue().toString());
    }

    public String startRoleDataEntry() {
        if (healthForm == null || sessionController == null || sessionController.getLoggedUser() == null || sessionController.getLoggedUser().getStaff() == null) {
            UtilityController.addErrorMessage("Please select a form");
            return "";
        }
        Map m = new HashMap();
        m.put("a", sessionController.getArea());
        m.put("i", healthForm);
        String jpql;
        Calendar c = Calendar.getInstance();
        switch (healthForm.getDurationType()) {
            case Annually:

                if (yearVal <= 0) {
                    UtilityController.addErrorMessage("Please Select the Year");
                    return "";
                }

                System.out.println("anual report");
                jpql = "select f from FilledHealthForm f where f.item=:i and f.area=:a and f.yearVal = " + getYearVal();
                System.out.println("jpql = " + jpql);
                System.out.println("m = " + m);
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    //Date temFrdomDate = Calendar.getInstance().getTime();

                    c.set(Calendar.YEAR, yearVal);
                    c.set(Calendar.MONTH, 1);
                    c.set(Calendar.DATE, 1);
                    filledHealthForm.setFromDate(c.getTime());
                    System.out.println(fromDate);
                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, getYearVal());
                    c.set(Calendar.MONTH, 11);
                    c.set(Calendar.DATE, 1);
                    filledHealthForm.setToDate(c.getTime());
                    System.out.println(toDate);
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setArea(sessionController.getArea());
                    filledHealthForm.setDataEntryUser(sessionController.getLoggedUser());
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }
                break;

            case Daily:

                if (fromDate == null) {
                    UtilityController.addErrorMessage("Please Select the Date");
                    return "";
                }

                System.out.println("Daily report");
                System.out.println(fromDate);

                System.out.println("==========" + fDate);

                m.put("fd", fromDate);
                jpql = "select f from FilledHealthForm f where f.item=:i and f.area=:a and f.fromDate=:fd";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);
                    filledHealthForm.setFromDate(fromDate);
                    filledHealthForm.setToDate(fromDate);

                    filledHealthForm.setArea(sessionController.getArea());
                    filledHealthForm.setDataEntryUser(sessionController.getLoggedUser());
                    createFillefFormFromHealthForm(filledHealthForm);
                    System.out.println("filled health form values id " + filledHealthForm.getFilledHealthFormReportItemValue());
                }

                break;

            case Monthly:
                System.out.println("Monthly report");

                if (yearVal <= 0) {
                    UtilityController.addErrorMessage("Please Select the Year");
                    return "";
                }
                if (monthVal <= 0) {
                    UtilityController.addErrorMessage("Please Select the Month");
                    return "";
                }
                jpql = "select f from FilledHealthForm f where f.item=:i and f.area=:a and f.yearVal = " + getYearVal() + " and f.monthVal=" + getMonthVal() + "";
                filledHealthForm = getFilledHealthFormFacade().findFirstBySQL(jpql, m);
                System.out.println("filled health form is " + filledHealthForm);
                if (filledHealthForm == null) {
                    System.out.println("filled health form is null");
                    filledHealthForm = new FilledHealthForm();
                    filledHealthForm.setItem(healthForm);

                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.set(Calendar.MONTH, monthVal);
                    c.set(Calendar.DATE, 1);
                    c.add(Calendar.MONTH, -1);
                    filledHealthForm.setFromDate(c.getTime());  // set the from date of the form like this year this month
                    System.out.println("From Date" + c.getTime());
                    c = Calendar.getInstance();
                    c.set(Calendar.YEAR, yearVal);
                    c.set(Calendar.MONTH, monthVal);
                    c.set(Calendar.DATE, 1);
                    c.add(Calendar.DATE, -1);  // -1 for get the end of the month
                    filledHealthForm.setToDate(c.getTime()); // set the to date of the form
                    filledHealthForm.setYearVal(yearVal);
                    filledHealthForm.setMonthVal(monthVal);
                    System.out.println(filledHealthForm.getToDate());

                    filledHealthForm.setArea(sessionController.getArea());
                    filledHealthForm.setDataEntryUser(sessionController.getLoggedUser());
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
                    if (filledHealthForm.getId() == null) {
                        getFilledHealthFormFacade().create(filledHealthForm);
                    } else {
                        getFilledHealthFormFacade().edit(filledHealthForm);
                    }
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Date getfDate() {
        return fDate;
    }

    public void setfDate(Date fDate) {
        this.fDate = fDate;
    }

    public List<FilledHealthForm> getSubmittedForms() {
        return submittedForms;
    }
}
