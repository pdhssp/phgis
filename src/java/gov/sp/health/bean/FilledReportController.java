
package gov.sp.health.bean;

import gov.sp.health.data.CalculationType;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.FormCal;
import gov.sp.health.entity.form.FilledHealthForm;
import gov.sp.health.entity.form.FilledHealthForm;
import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.facade.IxCalFacade;
import gov.sp.health.facade.PatientInvestigationFacade;
import gov.sp.health.facade.PatientInvestigationItemValueFacade;
import gov.sp.health.facade.PatientReportFacade;
import gov.sp.health.facade.PatientReportItemValueFacade;
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
    PatientInvestigationItemValueFacade piivFacade;
    @EJB
    PatientReportItemValueFacade pirivFacade;
    @EJB
    PatientInvestigationFacade piFacade;
    @EJB
    IxCalFacade ixCalFacade;
    String selectText = "";
    private FilledHealthForm currentFilledHealthForm;
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
        if (currentFilledHealthForm == null) {
            UtilityController.addErrorMessage("No Report to calculate");
            return 0;
        }
        if (currentFilledHealthForm.getFilledHealthFormReportItemValue() == null) {
            UtilityController.addErrorMessage("Report Items values is null");
            return 0;
        }
        if (currentFilledHealthForm.getFilledHealthFormReportItemValue().isEmpty()) {
            UtilityController.addErrorMessage("Report Items values is empty");
            return 0;
        }
        for (FilledHealthFormItemValue priv : currentFilledHealthForm.getFilledHealthFormReportItemValue()) {
            System.out.println("priv in finding val is " + priv.getHealthFormItem().getName());
            System.out.println("XXXXXXXXXXX compairing are " + priv.getHealthFormItem().getId() + "  vs " + ii.getId());
            if (priv.getHealthFormItem().getId() == ii.getId()) {
                System.out.println("double val is " + priv.getDoubleValue());
                return priv.getDoubleValue();
            }
        }
        return 0.0;
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
        if (currentFilledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }

        getCurrentFilledHealthForm().setDataEntered(true);
        currentFilledHealthForm.setDataEntryAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setDataEntryUser(getSessionController().getLoggedUser());

//        getPiFacade().edit(currentFilledHealthForm);
        currentFilledHealthForm.setDataEntered(Boolean.TRUE);
        currentFilledHealthForm.setDataEntryAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setDataEntryInstitution(sessionController.getLoggedUser().getInstitution());
        currentFilledHealthForm.setDataEntryUser(sessionController.getLoggedUser());
//        getPiFacade().edit(currentFilledHealthForm);
//        if (currentFilledHealthForm.getId() == null || currentFilledHealthForm.getId() == 0) {
//            currentFilledHealthForm.setCreatedAt(Calendar.getInstance().getTime());
//            currentFilledHealthForm.setCreater(getSessionController().getLoggedUser());
//            getFacade().create(currentFilledHealthForm);
//        } else {
//            getFacade().edit(currentFilledHealthForm);
//        }
//        for (FilledHealthFormItemValue v : currentFilledHealthForm.getPatientReportItemValues()) {
//            v.setPatientReport(currentFilledHealthForm);
//            if (v.getId() == null || v.getId() == 0) {
//                getPirivFacade().create(v);
//            } else {
//                getPirivFacade().edit(v);
//            }
//        }
        getFacade().edit(currentFilledHealthForm);
        getPiFacade().edit(currentFilledHealthForm);
        UtilityController.addSuccessMessage("Saved");
//        return "lab_search_for_reporting";
    }

    public void approvePatientReport() {
        System.out.println("going to approve");
        if (currentFilledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to approve");
            return;
        }
        if (currentFilledHealthForm.getDataEntered() == false) {
            UtilityController.addErrorMessage("First Save report");
            return;
        }

        getCurrentFilledHealthForm().setApproved(true);
        currentFilledHealthForm.setApproveAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setApproveUser(getSessionController().getLoggedUser());
//        getPiFacade().edit(currentFilledHealthForm);
        currentFilledHealthForm.setApproved(Boolean.FALSE);
        currentFilledHealthForm.setApproved(Boolean.TRUE);
        currentFilledHealthForm.setApproveAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setApproveInstitution(sessionController.getLoggedUser().getInstitution());
        currentFilledHealthForm.setApproveUser(sessionController.getLoggedUser());
//        if (currentFilledHealthForm.getId() == null || currentFilledHealthForm.getId() == 0) {
//            currentFilledHealthForm.setCreatedAt(Calendar.getInstance().getTime());
//            currentFilledHealthForm.setCreater(getSessionController().getLoggedUser());
//            getFacade().create(currentFilledHealthForm);
//        } else {
//            getFacade().edit(currentFilledHealthForm);
//        }
//        for (FilledHealthFormItemValue v : currentFilledHealthForm.getPatientReportItemValues()) {
//            v.setPatientReport(currentFilledHealthForm);
//            if (v.getId() == null || v.getId() == 0) {
//                getPirivFacade().create(v);
////            } else {
//                getPirivFacade().edit(v);
//            }
//        }
        getFacade().edit(currentFilledHealthForm);
        getPiFacade().edit(currentFilledHealthForm);
        getStaffController().setCurrent(currentFilledHealthForm.getApproveUser().getStaff());

        UtilityController.addSuccessMessage("Approved");
    }

    public void printPatientReport() {
        System.out.println("going to save as printed");
        if (currentFilledHealthForm == null) {
            UtilityController.addErrorMessage("Nothing to approve");
            return;
        }
        currentFilledHealthForm.setPrinted(true);
        currentFilledHealthForm.setPrintingAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setPrintingUser(getSessionController().getLoggedUser());

        currentFilledHealthForm.setPrinted(Boolean.TRUE);
        currentFilledHealthForm.setPrintingAt(Calendar.getInstance().getTime());
        currentFilledHealthForm.setPrintingInstitution(sessionController.getLoggedUser().getInstitution());
        currentFilledHealthForm.setPrintingUser(sessionController.getLoggedUser());
        getFacade().edit(currentFilledHealthForm);

        getPiFacade().edit(currentFilledHealthForm);

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

    public FilledHealthForm getCurrentFilledHealthForm() {
        if (currentFilledHealthForm == null) {
            if (currentFilledHealthForm != null) {
                currentFilledHealthForm = currentFilledHealthForm.getPatientInvestigation();
            }
        }
        return currentFilledHealthForm;
    }

    public void createNewPatientReport() {
        System.out.println("creating a new patient report");
        FilledHealthForm r;
        if (currentFilledHealthForm != null && currentFilledHealthForm.getId() != null && currentReportInvestigation != null) {
//          
//            getItemForItemController().setParentItem(currentFilledHealthForm.getInvestigation());
            r = new FilledHealthForm();
            r.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            r.setCreater(getSessionController().getLoggedUser());
            r.setItem(currentReportInvestigation);
            r.setPatientInvestigation(currentFilledHealthForm);
//            getEjbFacade().create(r);
            System.out.println("going to add report item values for the report");

//            getEjbFacade().edit(r);
            setCurrentFilledHealthForm(r);
            System.out.println("current pt report is " + r);
            System.out.println("its ix is " + r.getItem().getName());
//            for(ReportItem ri : currentFilledHealthForm.getInvestigation().getReportItems()){
//                System.out.println("ri is " + ri.getName());  
//            }
        } else {
            UtilityController.addErrorMessage("No ptIx or Ix selected to add");
        }
    }

    public void setCurrentFilledHealthForm(FilledHealthForm currentFilledHealthForm) {
        this.currentFilledHealthForm = currentFilledHealthForm;
    }


    @Inject
    ReportFormatController reportFormatController;

    public ReportFormatController getReportFormatController() {
        return reportFormatController;
    }

    public void setReportFormatController(ReportFormatController reportFormatController) {
        this.reportFormatController = reportFormatController;
    }

    public FilledHealthForm getCurrentPatientReport() {
//        for (FilledHealthFormItemValue ppiv : currentFilledHealthForm.getPatientReportItemValues()) {
//            System.out.println("ppiv ix is  " + ppiv.getPatientReport().getItem().getName());
//            System.out.println("ppiv value is  " + ppiv.getStrValue());
//        }
//        if (currentFilledHealthForm != null && currentFilledHealthForm.getApproveUser() != null) {
//            getStaffController().setCurrent(currentFilledHealthForm.getApproveUser().getStaff());
//            getCommonReportItemController().setCategory(currentFilledHealthForm.getItem().getReportFormat());
//            System.out.println("while getting current patient report, staff controller current was set to " + getStaffController().getCurrent());
//        } else {
//            getStaffController().setCurrent(null);
//            System.out.println("while getting current patient report, staff controller current was set null");
//        }

        return currentFilledHealthForm;
    }

    
    public String editOldData() {
        if (currentFilledHealthForm == null) {
            UtilityController.addErrorMessage("No report to enter date");
            return "";
        } else {
            return "lab_patient_report_dataentry";
        }
    }

   

    @FacesConverter(forClass = FilledHealthForm.class)
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
            if (object instanceof FilledHealthForm) {
                FilledHealthForm o = (FilledHealthForm) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + FilledReportController.class.getName());
            }
        }
    }
}
