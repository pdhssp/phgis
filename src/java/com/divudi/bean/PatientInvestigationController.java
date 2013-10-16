/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.ejb.CommonFunctions;
import com.divudi.entity.Department;
import com.divudi.entity.form.HealthForm;
import com.divudi.entity.form.HealthFormItem;
import com.divudi.entity.form.FilledHealthForm;
import com.divudi.entity.form.FilledHealthFormReport;
import com.divudi.entity.form.ReportItem;
import com.divudi.facade.InvestigationFacade;
import com.divudi.facade.InvestigationItemFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PatientReportFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public  class PatientInvestigationController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private PatientInvestigationFacade ejbFacade;
    @EJB
    PatientReportFacade prFacade;
    List<FilledHealthForm> selectedItems;
    private FilledHealthForm current;
    HealthForm currentInvestigation;
    List<HealthFormItem> currentInvestigationItems;
    private List<FilledHealthForm> items = null;
    private List<FilledHealthForm> lstToSamle = null;
    private List<FilledHealthForm> lstToReceive = null;
    private List<FilledHealthForm> lstToEnterData = null;
    private List<FilledHealthFormReport> lstToApprove = null;
    private List<FilledHealthFormReport> lstToPrint = null;
    String selectText = "";
    private Department department;
    @Temporal(TemporalType.TIME)
    private Date fromDate;
    Date toDate;
    @EJB
    InvestigationItemFacade investigationItemFacade;
    @EJB
    CommonFunctions commonFunctions;
    @EJB
    private InvestigationFacade investFacade;
    List<HealthForm> investSummery;
    Date sampledOutsideDate;
    boolean sampledOutSide;
    boolean listIncludingReceived;
    boolean listIncludingEnteredData;
    private boolean listIncludingSampled;
    boolean listIncludingApproved;
    List<FilledHealthForm> selectedToReceive;

    public void resetLists() {
        items = null;
        lstToSamle = null;
        lstToReceive = null;
        lstToEnterData = null;
        lstToApprove = null;
        lstToPrint = null;
        selectedToReceive = null;
        investSummery = null;
        lstToReceiveSearch = null;
    }

    public boolean isListIncludingApproved() {
        return listIncludingApproved;
    }

    public void setListIncludingApproved(boolean listIncludingApproved) {
        this.listIncludingApproved = listIncludingApproved;
        lstToApprove = null;
    }

    public boolean isListIncludingEnteredData() {
        return listIncludingEnteredData;
    }

    public void setListIncludingEnteredData(boolean listIncludingEnteredData) {
        this.listIncludingEnteredData = listIncludingEnteredData;
        lstToEnterData = null;
    }

    public boolean isListIncludingReceived() {
        return listIncludingReceived;
    }

    public void setListIncludingReceived(boolean listIncludingReceived) {
        this.listIncludingReceived = listIncludingReceived;
        lstToReceive = null;
    }

    public List<FilledHealthForm> getSelectedToReceive() {
        System.out.println("selected to receive");
        if (selectedToReceive != null) {
            for (FilledHealthForm pi : selectedToReceive) {
                for (ReportItem ri : pi.getInvestigation().getReportItems()) {
                    System.out.println("ri is " + ri.getName());
                }
            }
        }
        return selectedToReceive;
    }

    public void setSelectedToReceive(List<FilledHealthForm> selectedToReceive) {
        this.selectedToReceive = selectedToReceive;
    }

    public Date getSampledOutsideDate() {
        if (sampledOutsideDate == null) {
            sampledOutsideDate = Calendar.getInstance().getTime();
        }
        return sampledOutsideDate;
    }

    public void setSampledOutsideDate(Date sampledOutsideDate) {
        this.sampledOutsideDate = sampledOutsideDate;
    }

    public boolean isSampledOutSide() {
        return sampledOutSide;
    }

    public void setSampledOutSide(boolean sampledOutSide) {
        this.sampledOutSide = sampledOutSide;
    }

    public List<HealthForm> getInvestSummery() {
        if (investSummery == null) {
            String temSql;
            Map temMap = new HashMap();
            temSql = "SELECT inv FROM Investigation inv WHERE inv.id in(SELECT i.investigation.id FROM PatientInvestigation i where i.retired=false and i.collected = false and i.billItem.bill.billDate between :fromDate and :toDate)";
            temMap.put("toDate", getToDate());
            temMap.put("fromDate", getFromDate());
            investSummery = getInvestFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
        }
        if (investSummery == null) {
            investSummery = new ArrayList<HealthForm>();
        }
        return investSummery;
    }

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = getCommonFunctions().getStartOfDay(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        resetLists();
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = getCommonFunctions().getEndOfDay(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
        resetLists();
    }

    public PatientReportFacade getPrFacade() {
        return prFacade;
    }

    public void setPrFacade(PatientReportFacade prFacade) {
        this.prFacade = prFacade;
    }

    public InvestigationItemFacade getInvestigationItemFacade() {
        return investigationItemFacade;
    }

    public void setInvestigationItemFacade(InvestigationItemFacade investigationItemFacade) {
        this.investigationItemFacade = investigationItemFacade;
    }

    public HealthForm getCurrentInvestigation() {
        return currentInvestigation;
    }

    public void setCurrentInvestigation(HealthForm currentInvestigation) {
        String sql;
        if (currentInvestigation != null) {

            sql = "select i from HealthFormItem i where i.retired = false and i.item.id = " + currentInvestigation.getId() + " and i.ixItemType = com.divudi.data.InvestigationItemType.Value order by i.cssTop, i.cssLeft";
            setCurrentInvestigationItems(getInvestigationItemFacade().findBySQL(sql));
        } else {
            setCurrentInvestigationItems(new ArrayList<HealthFormItem>());
        }
        this.currentInvestigation = currentInvestigation;
    }

    public List<HealthFormItem> getCurrentInvestigationItems() {
        return currentInvestigationItems;
    }

    public void setCurrentInvestigationItems(List<HealthFormItem> currentInvestigationItems) {
        this.currentInvestigationItems = currentInvestigationItems;
    }

    public List<FilledHealthForm> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from PatientInvestigation c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public void prepareAdd() {
        current = new FilledHealthForm();
    }

    public void setSelectedItems(List<FilledHealthForm> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
        lstToApprove = null;
        lstToSamle = null;
        lstToPrint = null;
        lstToReceiveSearch = null;
    }

    public void saveSelected() {

        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            getFacade().create(current);
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        recreateModel();
        this.selectText = selectText;
    }

    public PatientInvestigationFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PatientInvestigationFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public PatientInvestigationController() {
    }

    public FilledHealthForm getCurrent() {
        if (current == null) {
            current = new FilledHealthForm();
        }
        return current;
    }

    public void setCurrent(FilledHealthForm current) {
        if (current != null) {
            setCurrentInvestigation(current.getInvestigation());
        } else {
            setCurrentInvestigation(null);
        }
        this.current = current;
    }

    private PatientInvestigationFacade getFacade() {
        return ejbFacade;
    }

    public List<FilledHealthForm> getItems() {
        if (items == null) {
            String temSql;
            temSql = "SELECT i FROM PatientInvestigation i where i.retired=false ";
            items = getFacade().findBySQL(temSql);
            if (items == null) {
                items = new ArrayList<FilledHealthForm>();
            }

        }
        return items;
    }

    public List<FilledHealthForm> getLstToSamle() {
        if (lstToSamle == null) {
            String temSql;
            getCurrent().getSampledAt();
            Map temMap = new HashMap();

            if (listIncludingSampled) {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false  and i.billItem.bill.billDate between :fromDate and :toDate";
            } else {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.collected = false and i.billItem.bill.billDate between :fromDate and :toDate";
            }

            temMap.put("toDate", getToDate());
            temMap.put("fromDate", getFromDate());
            lstToSamle = getFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
        }
        if (lstToSamle == null) {
            lstToSamle = new ArrayList<FilledHealthForm>();
        }

        return lstToSamle;
    }

    public void markAsSampled() {
        if (current == null) {
            UtilityController.addErrorMessage("Nothing to sample");
            return;
        }
        getCurrent().setSampleCollecter(getSessionController().getLoggedUser());
        if (current.getSampleOutside()) {
            getCurrent().setSampledAt(sampledOutsideDate);
        } else {
            getCurrent().setSampledAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setSampleDepartment(getSessionController().getLoggedUser().getDepartment());
            current.setSampleInstitution(getSessionController().getLoggedUser().getInstitution());
        }
        if (getCurrent().getId() != null || getCurrent().getId() != 0) {
            getCurrent().setCollected(Boolean.TRUE);
            getCurrent().setSampleCollecter(sessionController.getLoggedUser());
            getEjbFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("Marked as Sampled");
        } else {
            UtilityController.addErrorMessage("Empty");
        }
        setSampledOutsideDate(Calendar.getInstance().getTime());
    }

    public void setLstToSamle(List<FilledHealthForm> lstToSamle) {
        this.lstToSamle = lstToSamle;
    }

    public List<FilledHealthForm> getLstToReceive() {
        if (lstToReceive == null) {
            String temSql;
            getCurrent().getSampledAt();
            Map temMap = new HashMap();
            if (listIncludingReceived) {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.collected = true and i.sampledAt between :fromDate and :toDate and i.receiveDepartment.id = " + getSessionController().getDepartment().getId();
            } else {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.collected = true and i.received=false and i.sampledAt between :fromDate and :toDate and i.receiveDepartment.id = " + getSessionController().getDepartment().getId();
            }
            System.out.println("Sql to get the receive list is " + temSql);
            System.out.println("FromDate to get the receive list is " + getFromDate());
            System.out.println("ToDate to get the receive list is " + getToDate());
            temMap.put("toDate", getToDate());
            temMap.put("fromDate", getFromDate());
            lstToReceive = getFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
        }
        if (lstToReceive == null) {
            lstToReceive = new ArrayList<FilledHealthForm>();
        }
        return lstToReceive;
    }
    List<FilledHealthForm> lstToReceiveSearch;

    public List<FilledHealthForm> getLstToReceiveSearch() {
        if (lstToReceiveSearch == null) {
            System.out.println("getting lst to receive search");
            String temSql;
            Map temMap = new HashMap();
            if (selectText == null || selectText.trim().equals("")) {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.collected = true and i.sampledAt between :fromDate and :toDate and i.receiveDepartment.id = " + getSessionController().getDepartment().getId();
                temMap.put("toDate", getToDate());
                temMap.put("fromDate", getFromDate());
                lstToReceiveSearch = getFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
            } else {
                temSql = "select pi from PatientInvestigation pi join pi.investigation i join pi.billItem.bill b join b.patient.person p   where (upper(p.name) like '%" + selectText.toUpperCase() + "%' or upper(b.insId) like '%" + selectText.toUpperCase() + "%' or p.phone like '%" + selectText + "%' or upper(i.name) like '%" + selectText.toUpperCase() + "%' )  and pi.retired=false and b.createdAt between :fromDate and :toDate and pi.receiveDepartment.id = " + getSessionController().getDepartment().getId();
                temMap.put("toDate", getToDate());
                temMap.put("fromDate", getFromDate());
                System.out.println("sql is " + temSql);
                lstToReceiveSearch = getFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);

            }

        }
        if (lstToReceiveSearch == null) {
//            System.out.println("lstToReceiveSearch is null");
            lstToReceiveSearch = new ArrayList<FilledHealthForm>();
        }
//        System.out.println("size is " + lstToReceiveSearch.size());
        return lstToReceiveSearch;
    }

    public void markYetToReceiveOnes() {
        selectedToReceive = new ArrayList<FilledHealthForm>();
        for (FilledHealthForm pi : lstToReceiveSearch) {
            if (pi.getReceived() != true) {
                selectedToReceive.add(pi);
            }
        }
    }

    public void markAsReceived() {
        if (getCurrent().getId() != null || getCurrent().getId() != 0) {
            getCurrent().setReceived(Boolean.TRUE);
            getCurrent().setReceivedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setReceivedCollecter(sessionController.getLoggedUser());
            getEjbFacade().edit(getCurrent());
        }
    }

    public void markSelectedAsReceived() {
        System.out.println("going to mark as received");
        for (FilledHealthForm pi : selectedToReceive) {
            pi.setReceived(Boolean.TRUE);
            pi.setReceivedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            pi.setReceivedCollecter(sessionController.getLoggedUser());
            getEjbFacade().edit(pi);
        }
        selectedToReceive = new ArrayList<FilledHealthForm>();
        listIncludingReceived = false;
        System.out.println("marked as received");
    }

    public void setLstToReceive(List<FilledHealthForm> lstToReceive) {
        this.lstToReceive = lstToReceive;
    }

    public List<FilledHealthForm> getLstToEnterData() {
        if (lstToEnterData == null) {
            String temSql;
            Map temMap = new HashMap();
            if (listIncludingEnteredData) {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.received=true and i.dataEntered between :fromDate and :toDate and i.performDepartment.id = " + getSessionController().getDepartment().getId();
            } else {
                temSql = "SELECT i FROM PatientInvestigation i where i.retired=false and i.received=true and i.dataEntered=false  and i.sampledAt between :fromDate and :toDate and i.performDepartment.id = " + getSessionController().getDepartment().getId();
            }
            System.out.println(temSql);
            temMap.put("toDate", getToDate());
            temMap.put("fromDate", getFromDate());
            lstToEnterData = getFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
        }
        if (lstToEnterData == null) {
            lstToEnterData = new ArrayList<FilledHealthForm>();
        }
        return lstToEnterData;
    }

    public void markAsDataEntered() {
        if (getCurrent().getId() != null || getCurrent().getId() != 0) {
            getCurrent().setDataEntered(Boolean.TRUE);
            getCurrent().setDataEntryAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setDataEntryUser(sessionController.getLoggedUser());
            getEjbFacade().edit(getCurrent());
        }
    }

    public void setLstToEnterData(List<FilledHealthForm> lstToEnterData) {
        this.lstToEnterData = lstToEnterData;
    }

    public List<FilledHealthFormReport> getLstToApprove() {
        if (lstToApprove == null) {
            String temSql;
            if (listIncludingApproved == true) {
                temSql = "SELECT i FROM PatientReport i where i.retired=false and i.dataEntered=true and i.dataEntryAt between :fromDate and :toDate";
            } else {
                temSql = "SELECT i FROM PatientReport i where i.retired=false and i.dataEntered=true and i.approved=false and i.dataEntryAt between :fromDate and :toDate";
            }
            Map temMap = new HashMap();
            temMap.put("toDate", getToDate());
            temMap.put("fromDate", getFromDate());
            lstToApprove = getPrFacade().findBySQL(temSql, temMap, TemporalType.TIMESTAMP);
        }
        if (lstToApprove == null) {
            lstToApprove = new ArrayList<FilledHealthFormReport>();
        }
        return lstToApprove;
    }

    public void markAsApproved() {
        if (getCurrent().getId() != null || getCurrent().getId() != 0) {
            getCurrent().setApproved(Boolean.TRUE);
            getCurrent().setApproveUser(sessionController.getLoggedUser());
            getCurrent().setApproveAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getEjbFacade().edit(getCurrent());
        }
    }

    public void setLstToApprove(List<FilledHealthFormReport> lstToApprove) {
        this.lstToApprove = lstToApprove;
    }

    public List<FilledHealthFormReport> getLstToPrint() {
        System.out.println("getting lst to print");

        String temSql;
        temSql = "SELECT i FROM PatientReport i";
        lstToPrint = getPrFacade().findBySQL(temSql);

        if (lstToPrint == null) {
            lstToPrint = new ArrayList<FilledHealthFormReport>();
        }
        return lstToPrint;
    }

    public void markAsPrinted() {
        if (getCurrent().getId() != null || getCurrent().getId() != 0) {
            getCurrent().setPrinted(Boolean.TRUE);
            getCurrent().setPrintingUser(sessionController.getLoggedUser());
            getCurrent().setPrintingAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getEjbFacade().edit(getCurrent());
        }
    }

    public void delete() {

        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setRetirer(sessionController.getLoggedUser());
            getFacade().edit(current);
            UtilityController.addSuccessMessage("DeleteSuccessfull");
        } else {
            UtilityController.addSuccessMessage("NothingToDelete");
        }
        recreateModel();
        getItems();
        current = null;
        getCurrent();
    }

    public void setLstToPrint(List<FilledHealthFormReport> lstToPrint) {
        this.lstToPrint = lstToPrint;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public InvestigationFacade getInvestFacade() {
        return investFacade;
    }

    public void setInvestFacade(InvestigationFacade investFacade) {
        this.investFacade = investFacade;
    }

    public boolean isListIncludingSampled() {
        return listIncludingSampled;
    }

    public void setListIncludingSampled(boolean listIncludingSampled) {
        this.listIncludingSampled = listIncludingSampled;


    }

    /**
     *
     */
    @FacesConverter(forClass = FilledHealthForm.class)
    public static class PatientInvestigationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PatientInvestigationController controller = (PatientInvestigationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "patientInvestigationController");
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
                        + object.getClass().getName() + "; expected type: " + PatientInvestigationController.class.getName());
            }
        }
    }
}
