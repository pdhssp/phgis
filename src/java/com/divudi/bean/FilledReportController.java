/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.data.CalculationType;
import com.divudi.data.InvestigationItemType;
import com.divudi.ejb.PatientReportBean;
import com.divudi.entity.form.HealthForm;
import com.divudi.entity.form.HealthFormItem;
import com.divudi.entity.form.FormCal;
import com.divudi.entity.form.FilledHealthForm;
import com.divudi.entity.form.FilledHealthFormReport;
import com.divudi.entity.form.FilledHealthFormReportItemValue;
import com.divudi.facade.IxCalFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PatientInvestigationItemValueFacade;
import com.divudi.facade.PatientReportFacade;
import com.divudi.facade.PatientReportItemValueFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class FilledReportController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @Inject
    StaffController staffController;
    @EJB
    private PatientReportFacade ejbFacade;
    @EJB
    private PatientReportBean prBean;
    @EJB
    PatientInvestigationItemValueFacade piivFacade;
    @EJB
    PatientReportItemValueFacade pirivFacade;
    @EJB
    PatientInvestigationFacade piFacade;
    @EJB
    IxCalFacade ixCalFacade;
    String selectText = "";
    private FilledHealthForm currentPtIx;
    private FilledHealthFormReport currentPatientReport;
    HealthForm currentReportInvestigation;
    HealthForm alternativeInvestigation;

    public StaffController getStaffController() {
        return staffController;
    }

    public void setStaffController(StaffController staffController) {
        this.staffController = staffController;
    }

    private double findPtReportItemVal(HealthFormItem ii) {
        System.out.println("finding report item val");
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("No Report to calculate");
            return 0;
        }
        if (currentPatientReport.getFilledHealthFormReportItemValue() == null) {
            UtilityController.addErrorMessage("Report Items values is null");
            return 0;
        }
        if (currentPatientReport.getFilledHealthFormReportItemValue().isEmpty()) {
            UtilityController.addErrorMessage("Report Items values is empty");
            return 0;
        }
        for (FilledHealthFormReportItemValue priv : currentPatientReport.getFilledHealthFormReportItemValue()) {
            System.out.println("priv in finding val is " + priv.getHealthFormItem().getName());
            System.out.println("XXXXXXXXXXX compairing are " + priv.getHealthFormItem().getId() + "  vs " + ii.getId());
            if (priv.getHealthFormItem().getId() == ii.getId()) {
                System.out.println("double val is " + priv.getDoubleValue());
                return priv.getDoubleValue();
            }
        }
        return 0.0;
    }

    public void calculate() {
        System.out.println("Gong to calculate");
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("No Report to calculate");
            return;
        }
        if (currentPatientReport.getFilledHealthFormReportItemValue() == null) {
            UtilityController.addErrorMessage("Report Items values is null");
            return;
        }
        if (currentPatientReport.getFilledHealthFormReportItemValue().isEmpty()) {
            UtilityController.addErrorMessage("Report Items values is empty");
            return;
        }
        System.out.println("Gong to calculate");
        for (FilledHealthFormReportItemValue priv : currentPatientReport.getFilledHealthFormReportItemValue()) {
            System.out.println("priv " + priv.toString());
            if (priv.getHealthFormItem().getIxItemType() == InvestigationItemType.Calculation) {
                System.out.println("priv ix " + priv.getHealthFormItem());
                String sql = "select i from IxCal i where i.calIxItem.id = " + priv.getHealthFormItem().getId();
                System.out.println("sql is " + sql);
                List<FormCal> ixCals = getIxCalFacade().findBySQL(sql);
                double result = 0;
                Double lastVal = null;
                CalculationType ctype = null;
                System.out.println("ixcals size is " + ixCals.size());
                for (FormCal c : ixCals) {
                    System.out.println("c is " + c.getId());
                    if (c.getCalculationType() == CalculationType.Constant) {
                        System.out.println("constant result before is " + result);
                        if (ctype == null && lastVal == null) {
                            result = c.getConstantValue();
                        } else if (ctype == CalculationType.Addition) {
                            result = result + c.getConstantValue();
                        } else if (ctype == CalculationType.Substraction) {
                            result = result - c.getConstantValue();
                        } else if (ctype == CalculationType.Devision) {
                            if (c.getConstantValue() != 0) {
                                result = result / c.getConstantValue();
                            }
                        } else if (ctype == CalculationType.Multiplication) {
                            result = result * c.getConstantValue();
                        }
                        System.out.println("constant after before is " + result);
                    } else if (c.getCalculationType() == CalculationType.Value) {
                        System.out.println("val result before is " + result);
                        System.out.println("c val item is " + c.getValIxItem().getName());
                        double d = findPtReportItemVal(c.getValIxItem());
                        System.out.println("d is " + d);
                        if (ctype == null && lastVal == null) {
                            result = d;
                        } else if (ctype == CalculationType.Addition) {
                            result = result + d;
                        } else if (ctype == CalculationType.Substraction) {
                            result = result - d;
                        } else if (ctype == CalculationType.Devision) {
                            if (d != 0) {
                                result = result / d;
                            }
                        } else if (ctype == CalculationType.Multiplication) {
                            result = result * d;
                        }
                        System.out.println("val result after is " + result);
                    } else {
                        ctype = c.getCalculationType();
                    }
                    priv.setDoubleValue(result);
                }
            }
        }
    }

    public IxCalFacade getIxCalFacade() {
        return ixCalFacade;
    }

    public void setIxCalFacade(IxCalFacade ixCalFacade) {
        this.ixCalFacade = ixCalFacade;
    }

    public HealthForm getAlternativeInvestigation() {
        return alternativeInvestigation;
    }

    public void setAlternativeInvestigation(HealthForm alternativeInvestigation) {
        FilledHealthForm pi = new FilledHealthForm();
        this.alternativeInvestigation = alternativeInvestigation;
    }

    public void onCellEdit(CellEditEvent event) {
        try {
        } catch (Exception e) {
            UtilityController.addErrorMessage(e.getMessage());
        }

    }


    public HealthForm getCurrentReportInvestigation() {
        return currentReportInvestigation;
    }

    public void setCurrentReportInvestigation(HealthForm currentReportInvestigation) {
        System.out.println("setting currentReportInvestigation - " + currentReportInvestigation.getName());
        this.currentReportInvestigation = currentReportInvestigation;
    }

    public PatientReportItemValueFacade getPirivFacade() {
        return pirivFacade;
    }

    public void setPirivFacade(PatientReportItemValueFacade pirivFacade) {
        this.pirivFacade = pirivFacade;
    }

    public PatientInvestigationItemValueFacade getPiivFacade() {
        return piivFacade;
    }

    public void setPiivFacade(PatientInvestigationItemValueFacade piivFacade) {
        this.piivFacade = piivFacade;
    }

    public PatientInvestigationFacade getPiFacade() {
        return piFacade;
    }

    public void setPiFacade(PatientInvestigationFacade piFacade) {
        this.piFacade = piFacade;
    }

    public void savePatientReport() {
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }

        getCurrentPtIx().setDataEntered(true);
        currentPtIx.setDataEntryAt(Calendar.getInstance().getTime());
        currentPtIx.setDataEntryUser(getSessionController().getLoggedUser());
        currentPtIx.setDataEntryDepartment(getSessionController().getDepartment());

//        getPiFacade().edit(currentPtIx);
        currentPatientReport.setDataEntered(Boolean.TRUE);
        currentPatientReport.setDataEntryAt(Calendar.getInstance().getTime());
        currentPatientReport.setDataEntryDepartment(sessionController.getLoggedUser().getDepartment());
        currentPatientReport.setDataEntryInstitution(sessionController.getLoggedUser().getInstitution());
        currentPatientReport.setDataEntryUser(sessionController.getLoggedUser());
//        getPiFacade().edit(currentPtIx);
//        if (currentPatientReport.getId() == null || currentPatientReport.getId() == 0) {
//            currentPatientReport.setCreatedAt(Calendar.getInstance().getTime());
//            currentPatientReport.setCreater(getSessionController().getLoggedUser());
//            getFacade().create(currentPatientReport);
//        } else {
//            getFacade().edit(currentPatientReport);
//        }
//        for (FilledHealthFormReportItemValue v : currentPatientReport.getPatientReportItemValues()) {
//            v.setPatientReport(currentPatientReport);
//            if (v.getId() == null || v.getId() == 0) {
//                getPirivFacade().create(v);
//            } else {
//                getPirivFacade().edit(v);
//            }
//        }
        getFacade().edit(currentPatientReport);
        getPiFacade().edit(currentPtIx);
        UtilityController.addSuccessMessage("Saved");
//        return "lab_search_for_reporting";
    }

    public void approvePatientReport() {
        System.out.println("going to approve");
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("Nothing to approve");
            return;
        }
        if (currentPatientReport.getDataEntered() == false) {
            UtilityController.addErrorMessage("First Save report");
            return;
        }

        getCurrentPtIx().setApproved(true);
        currentPtIx.setApproveAt(Calendar.getInstance().getTime());
        currentPtIx.setApproveUser(getSessionController().getLoggedUser());
        currentPtIx.setApproveDepartment(getSessionController().getDepartment());
//        getPiFacade().edit(currentPtIx);
        currentPatientReport.setApproved(Boolean.FALSE);
        currentPatientReport.setApproved(Boolean.TRUE);
        currentPatientReport.setApproveAt(Calendar.getInstance().getTime());
        currentPatientReport.setApproveDepartment(sessionController.getLoggedUser().getDepartment());
        currentPatientReport.setApproveInstitution(sessionController.getLoggedUser().getInstitution());
        currentPatientReport.setApproveUser(sessionController.getLoggedUser());
//        if (currentPatientReport.getId() == null || currentPatientReport.getId() == 0) {
//            currentPatientReport.setCreatedAt(Calendar.getInstance().getTime());
//            currentPatientReport.setCreater(getSessionController().getLoggedUser());
//            getFacade().create(currentPatientReport);
//        } else {
//            getFacade().edit(currentPatientReport);
//        }
//        for (FilledHealthFormReportItemValue v : currentPatientReport.getPatientReportItemValues()) {
//            v.setPatientReport(currentPatientReport);
//            if (v.getId() == null || v.getId() == 0) {
//                getPirivFacade().create(v);
////            } else {
//                getPirivFacade().edit(v);
//            }
//        }
        getFacade().edit(currentPatientReport);
        getPiFacade().edit(currentPtIx);
        getStaffController().setCurrent(currentPatientReport.getApproveUser().getStaff());

        UtilityController.addSuccessMessage("Approved");
    }

    public void printPatientReport() {
        System.out.println("going to save as printed");
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("Nothing to approve");
            return;
        }
        currentPtIx.setPrinted(true);
        currentPtIx.setPrintingAt(Calendar.getInstance().getTime());
        currentPtIx.setPrintingUser(getSessionController().getLoggedUser());
        currentPtIx.setPrintingDepartment(getSessionController().getDepartment());

        currentPatientReport.setPrinted(Boolean.TRUE);
        currentPatientReport.setPrintingAt(Calendar.getInstance().getTime());
        currentPatientReport.setPrintingDepartment(sessionController.getLoggedUser().getDepartment());
        currentPatientReport.setPrintingInstitution(sessionController.getLoggedUser().getInstitution());
        currentPatientReport.setPrintingUser(sessionController.getLoggedUser());
        getFacade().edit(currentPatientReport);

        getPiFacade().edit(currentPtIx);

    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public PatientReportFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PatientReportFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public FilledReportController() {
    }

    private PatientReportFacade getFacade() {
        return ejbFacade;
    }

    public FilledHealthForm getCurrentPtIx() {
        if (currentPtIx == null) {
            if (currentPatientReport != null) {
                currentPtIx = currentPatientReport.getPatientInvestigation();
            }
        }
        return currentPtIx;
    }

    public void createNewPatientReport() {
        System.out.println("creating a new patient report");
        FilledHealthFormReport r;
        if (currentPtIx != null && currentPtIx.getId() != null && currentReportInvestigation != null) {
//          
//            getItemForItemController().setParentItem(currentPtIx.getInvestigation());
            r = new FilledHealthFormReport();
            r.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            r.setCreater(getSessionController().getLoggedUser());
            r.setItem(currentReportInvestigation);
            r.setPatientInvestigation(currentPtIx);
//            getEjbFacade().create(r);
            System.out.println("going to add report item values for the report");
            getPrBean().addPatientReportItemValuesForReport(r);
//            getEjbFacade().edit(r);
            setCurrentPatientReport(r);
            currentPtIx.getPatientReports().add(r);
            System.out.println("current pt report is " + r);
            System.out.println("its ix is " + r.getItem().getName());
//            for(ReportItem ri : currentPtIx.getInvestigation().getReportItems()){
//                System.out.println("ri is " + ri.getName());  
//            }
            getCommonReportItemController().setCategory(currentReportInvestigation.getReportFormat());
        } else {
            UtilityController.addErrorMessage("No ptIx or Ix selected to add");
        }
    }

    public void setCurrentPtIx(FilledHealthForm currentPtIx) {
        this.currentPtIx = currentPtIx;
    }

    public PatientReportBean getPrBean() {
        return prBean;
    }

    public void setPrBean(PatientReportBean prBean) {
        this.prBean = prBean;
    }
    @Inject
    ReportFormatController reportFormatController;
    @Inject
    CommonReportItemController commonReportItemController;

    public ReportFormatController getReportFormatController() {
        return reportFormatController;
    }

    public void setReportFormatController(ReportFormatController reportFormatController) {
        this.reportFormatController = reportFormatController;
    }

    public CommonReportItemController getCommonReportItemController() {
        return commonReportItemController;
    }

    public void setCommonReportItemController(CommonReportItemController commonReportItemController) {
        this.commonReportItemController = commonReportItemController;
    }

    public FilledHealthFormReport getCurrentPatientReport() {
//        for (FilledHealthFormReportItemValue ppiv : currentPatientReport.getPatientReportItemValues()) {
//            System.out.println("ppiv ix is  " + ppiv.getPatientReport().getItem().getName());
//            System.out.println("ppiv value is  " + ppiv.getStrValue());
//        }
//        if (currentPatientReport != null && currentPatientReport.getApproveUser() != null) {
//            getStaffController().setCurrent(currentPatientReport.getApproveUser().getStaff());
//            getCommonReportItemController().setCategory(currentPatientReport.getItem().getReportFormat());
//            System.out.println("while getting current patient report, staff controller current was set to " + getStaffController().getCurrent());
//        } else {
//            getStaffController().setCurrent(null);
//            System.out.println("while getting current patient report, staff controller current was set null");
//        }

        return currentPatientReport;
    }

    public String enterNewData() {
        System.out.println("Entering to enter new data");
        currentReportInvestigation = (HealthForm) currentPtIx.getInvestigation().getReportedAs();
        System.out.println("current report ix is " + currentReportInvestigation);

        createNewPatientReport();

        return "lab_patient_report";
    }

    public String enterNewReportFormat() {
//        System.out.println("Entering to enter new data");
//        currentReportInvestigation = (HealthForm) currentPtIx.getInvestigation().getReportedAs();
//        System.out.println("current report ix is " + currentReportInvestigation);
        createNewPatientReport();
        return "lab_patient_report";
    }

    public String editOldData() {
        if (currentPatientReport == null) {
            UtilityController.addErrorMessage("No report to enter date");
            return "";
        } else {
            return "lab_patient_report_dataentry";
        }
    }

    public void setCurrentPatientReport(FilledHealthFormReport currentPatientReport) {
        this.currentPatientReport = currentPatientReport;
        if (currentPtIx != null) {
            currentPtIx = currentPatientReport.getPatientInvestigation();
        } else {
            currentPtIx = null;
        }
        if (currentPatientReport != null && currentPatientReport.getApproveUser() != null) {
            getStaffController().setCurrent(currentPatientReport.getApproveUser().getStaff());
            getCommonReportItemController().setCategory(currentPatientReport.getItem().getReportFormat());
            System.out.println("while setting current patient report, staff controller current was set to " + getStaffController().getCurrent());
        } else {
            getStaffController().setCurrent(null);
            System.out.println("while setting patient ix, staff controller current set to null");
        }
    }

    @FacesConverter(forClass = FilledHealthFormReport.class)
    public static class PatientReportControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FilledReportController controller = (FilledReportController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "patientReportController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof FilledHealthFormReport) {
                FilledHealthFormReport o = (FilledHealthFormReport) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + FilledReportController.class.getName());
            }
        }
    }
}
