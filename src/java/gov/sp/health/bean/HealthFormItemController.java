/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import java.util.TimeZone;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.facade.InvestigationItemFacade;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.HealthFormItemValue;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.InvestigationItemValueFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public  class HealthFormItemController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InvestigationItemFacade ejbFacade;
    @EJB
    InvestigationItemValueFacade iivFacade;
    List<HealthFormItem> selectedItems;
    private HealthFormItem current;
    private HealthForm currentInvestigation;
    private List<HealthFormItem> items = null;
    String selectText = "";
    HealthFormItemValue removingItem;
    HealthFormItemValue addingItem;
    String addingString;

    public InvestigationItemValueFacade getIivFacade() {
        return iivFacade;
    }

    public void setIivFacade(InvestigationItemValueFacade iivFacade) {
        this.iivFacade = iivFacade;
    }

    public String getAddingString() {
        return addingString;
    }

    public void setAddingString(String addingString) {
        this.addingString = addingString;
    }

    public List<HealthFormItem> completeIxItem(String qry) {
        List<HealthFormItem> iivs;
        if (qry.trim().equals("") || currentInvestigation == null || currentInvestigation.getId() == null) {
            return new ArrayList<HealthFormItem>();
        } else {
            String sql;
            sql = "select i from HealthFormItem i where i.retired=false and i.healthFormItemType = com.divudi.data.InvestigationItemType.Value and upper(i.name) like '%" + qry.toUpperCase() + "%' and i.item.id = " + currentInvestigation.getId();
            iivs = getEjbFacade().findBySQL(sql);
        }
        if (iivs == null) {
            iivs = new ArrayList<HealthFormItem>();
        }
        return iivs;
    }
    
     public List<HealthFormItem> getCurrentIxItems() {
        List<HealthFormItem> iivs;
        if (currentInvestigation == null || currentInvestigation.getId() == null) {
            return new ArrayList<HealthFormItem>();
        } else {
            String sql;
            sql = "select i from HealthFormItem i where i.retired=false and i.healthFormItemType = com.divudi.data.InvestigationItemType.Value and i.item.id = " + currentInvestigation.getId();
            iivs = getEjbFacade().findBySQL(sql);
        }
        if (iivs == null) {
            iivs = new ArrayList<HealthFormItem>();
        }
        return iivs;
    }

    public void addValueToIxItem() {
        if (current == null) {
            UtilityController.addErrorMessage("Please select an Ix");
            return;
        }
        if (addingString.trim().equals("")) {
            UtilityController.addErrorMessage("Enter a value");
            return;
        }
        HealthFormItemValue i = new HealthFormItemValue();
        i.setName(addingString);
        i.setHealthformItem(current);
        current.getHealthformItemValues().add(i);
        getEjbFacade().edit(current);
        UtilityController.addSuccessMessage("Added");
        addingString = "";
    }

    public HealthFormItemValue getRemovingItem() {
        return removingItem;
    }

    public void setRemovingItem(HealthFormItemValue removingItem) {
        this.removingItem = removingItem;
    }

    public HealthFormItemValue getAddingItem() {
        return addingItem;
    }

    public void setAddingItem(HealthFormItemValue addingItem) {
        this.addingItem = addingItem;
    }

    public List<HealthFormItem> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from HealthFormItem c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        if (selectedItems == null) {
            selectedItems = new ArrayList<HealthFormItem>();
        }

        return selectedItems;
    }

    public void listInvestigationItem() {
        if (getCurrentInvestigation() == null || getCurrentInvestigation().getId() == null) {
            items = new ArrayList<HealthFormItem>();
        } else {
            items = getEjbFacade().findBySQL("select ii from HealthFormItem ii where ii.retired=false and ii.item.id=" + getCurrentInvestigation().getId());
        }
    }

    public void addNewLabel() {
        if(currentInvestigation==null){
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }
        current = new HealthFormItem();
        current.setName("New Label");
        current.setItem(currentInvestigation);
        current.setHealthFormItemType(HealthFormItemType.Label);
//        getEjbFacade().create(current);
//        listInvestigationItem();
        currentInvestigation.getReportItems().add(current);
        getIxFacade().edit(currentInvestigation);
    }

    public void removeItem() {
        current.setRetired(true);
        current.setRetirer(sessionController.getLoggedUser());
        current.setRetiredAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        getEjbFacade().edit(getCurrent());
        getItems().remove(getCurrent());

    }

    public void removeInvestigationItemValue() {
        System.out.println("1");
        if (current == null) {
            UtilityController.addErrorMessage("Nothing to Remove");
            return;
        }
        System.out.println("1");
        if (removingItem == null) {
            UtilityController.addErrorMessage("Nothing to Remove");
            return;
        }
        System.out.println("3");
        getIivFacade().remove(removingItem);
        System.out.println("4");
        current.getHealthformItemValues().remove(removingItem);
        System.out.println("5");
        getEjbFacade().edit(current);
        System.out.println("6");

        UtilityController.addSuccessMessage("Removed");
    }

    public void addNewValue() {
        if(currentInvestigation==null){
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }
        current = new HealthFormItem();
        current.setName("New Value");
        current.setItem(currentInvestigation);
        current.setHealthFormItemType(HealthFormItemType.Value);
//        getEjbFacade().create(current);
        currentInvestigation.getReportItems().add(current);
        getIxFacade().edit(currentInvestigation);
        listInvestigationItem();
    }

    public void addNewCalculation() {
        if(currentInvestigation==null){
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }
        current = new HealthFormItem();
        current.setName("New Calculation");
        current.setItem(currentInvestigation);
        current.setHealthFormItemType(HealthFormItemType.Calculation);
//        getEjbFacade().create(current);
        currentInvestigation.getReportItems().add(current);
        getIxFacade().edit(currentInvestigation);
        listInvestigationItem();
        listInvestigationItem();
    }

    public void addNewFlag() {
        if(currentInvestigation==null){
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }
        current = new HealthFormItem();
        current.setName("New Flag");
        current.setItem(currentInvestigation);
//        getEjbFacade().create(current);
        currentInvestigation.getReportItems().add(current);
        getIxFacade().edit(currentInvestigation);
        listInvestigationItem();
        listInvestigationItem();
    }

    public void prepareAdd() {
        current = new HealthFormItem();
    }

    public void setSelectedItems(List<HealthFormItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
        current = null;
        currentInvestigation = null;


    }

    public void saveSelected() {

        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getFacade().create(getCurrent());
            UtilityController.addSuccessMessage("savedNewSuccessfully");
            getCurrentInvestigation().getReportItems().add(current);
            getIxFacade().edit(currentInvestigation);
        }



//        recreateModel();
//        getItems();
    }
    @EJB
    InvestigationFacade ixFacade;

    public InvestigationFacade getIxFacade() {
        return ixFacade;
    }

    public void setIxFacade(InvestigationFacade ixFacade) {
        this.ixFacade = ixFacade;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public InvestigationItemFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvestigationItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public HealthFormItemController() {
    }

    public HealthFormItem getCurrent() {
        if (current == null) {
            current = new HealthFormItem();
        }
        return current;
    }

    public void setCurrent(HealthFormItem current) {
        this.current = current;
    }

    private InvestigationItemFacade getFacade() {
        return ejbFacade;
    }

    public List<HealthFormItem> getItems() {
        if (getCurrentInvestigation().getId() != null) {
            String temSql;
            temSql = "SELECT i FROM HealthFormItem i where i.retired=false and i.item.id = " + getCurrentInvestigation().getId() + " order by i.healthFormItemType, i.cssTop , i.cssLeft";
            items = getFacade().findBySQL(temSql);
        } else {
            items = new ArrayList<HealthFormItem>();
        }
        return items;
    }

    public HealthForm getCurrentInvestigation() {
        if (currentInvestigation == null) {
            currentInvestigation = new HealthForm();
            //current = null;
        }
        current = null;
        return currentInvestigation;
    }

    public void setCurrentInvestigation(HealthForm currentInvestigation) {
        this.currentInvestigation = currentInvestigation;
        listInvestigationItem();
    }

    /**
     *
     */
    @FacesConverter("iiCon")
//    @FacesConverter("hfconverter")
    public static class InvestigationItemConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthFormItemController controller = (HealthFormItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthFormItemController");
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
            if (object instanceof HealthFormItem) {
                HealthFormItem o = (HealthFormItem) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthFormItemController.class.getName());
            }
        }
    }

    /**
     *
     */
    @FacesConverter(forClass = HealthFormItem.class)
//    @FacesConverter("hfconverter")
    public static class healthFormItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthFormItemController controller = (HealthFormItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthFormItemController");
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
            if (object instanceof HealthFormItem) {
                HealthFormItem o = (HealthFormItem) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthFormItemController.class.getName());
            }
        }
    }
}
