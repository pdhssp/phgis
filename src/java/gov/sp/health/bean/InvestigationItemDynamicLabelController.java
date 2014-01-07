/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.data.Sex;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.HealthFormItemValueFlag;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.InvestigationItemFacade;
import gov.sp.health.facade.InvestigationItemValueFlagFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.inject.Inject;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.TemporalType;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public  class InvestigationItemDynamicLabelController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InvestigationItemValueFlagFacade ejbFacade;
    @EJB
    InvestigationItemFacade investigationItemFacade;
    @EJB
    InvestigationFacade investigationFacade;
    List<HealthFormItemValueFlag> selectedItems;
    private HealthFormItemValueFlag current;
    private List<HealthFormItemValueFlag> items = null;
    private List<HealthFormItemValueFlag> dynamicLabels = null;
    String selectText = "";
    HealthForm investigation;
    List<HealthFormItem> investigationItemsOfValueType;
    List<HealthFormItem> investigationItemsOfDynamicLabelType;
    HealthFormItem investigationItemOfValueType;
    HealthFormItem investigationItemofDynamicLabelType;
    long fromAge;
    long toAge;
    String fromAgeUnit;
    String toAgeUnit;
    String flagMessage;
    String lowMessage;
    String highMessage;
    double fromValue;
    double toValue;
    Sex sex;
    HealthFormItemValueFlag removingInvestigationItemofDynamicLabelType;

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }

    public String getLowMessage() {
        return lowMessage;
    }

    public void setLowMessage(String lowMessage) {
        this.lowMessage = lowMessage;
    }

    public String getHighMessage() {
        return highMessage;
    }

    public void setHighMessage(String highMessage) {
        this.highMessage = highMessage;
    }

    public HealthFormItemValueFlag getRemovingInvestigationItemofDynamicLabelType() {
        return removingInvestigationItemofDynamicLabelType;
    }

    public void setRemovingInvestigationItemofDynamicLabelType(HealthFormItemValueFlag removingInvestigationItemofDynamicLabelType) {
        this.removingInvestigationItemofDynamicLabelType = removingInvestigationItemofDynamicLabelType;
    }

    public HealthFormItem getInvestigationItemofDynamicLabelType() {
        return investigationItemofDynamicLabelType;
    }

    public void setInvestigationItemofDynamicLabelType(HealthFormItem investigationItemofDynamicLabelType) {
        this.investigationItemofDynamicLabelType = investigationItemofDynamicLabelType;
        recreateModel();
    }

    public List<HealthFormItem> getInvestigationItemsOfDynamicLabelType() {
        if (investigation != null) {
            investigationItemsOfDynamicLabelType = getInvestigationItemFacade().findBySQL("select i from HealthFormItem i where i.retired=false and i.item.id = " + investigation.getId() + " and i.healthFormItemType = gov.sp.health.data.InvestigationItemType.DynamicLabel");
        }
        if (investigationItemsOfDynamicLabelType == null) {
            investigationItemsOfDynamicLabelType = new ArrayList<HealthFormItem>();
        }
        return investigationItemsOfDynamicLabelType;
    }

    public void setInvestigationItemsOfDynamicLabelType(List<HealthFormItem> investigationItemsOfDynamicLabelType) {
        this.investigationItemsOfDynamicLabelType = investigationItemsOfDynamicLabelType;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getFlagMessage() {
        return flagMessage;
    }

    public void setFlagMessage(String flagMessage) {
        this.flagMessage = flagMessage;
    }


    private void clearForNew() {
        investigationItemOfValueType = null;
        fromAge = 0;
        toAge = 0;
        fromAgeUnit = "Years";
        toAgeUnit = "Years";
        sex = null;
        flagMessage = "";
        lowMessage = "";
        highMessage = "";
        fromValue=0.0;
        toValue=0.0;
    }

    public String getToAgeUnit() {
        return toAgeUnit;
    }

    public void setToAgeUnit(String toAgeUnit) {
        this.toAgeUnit = toAgeUnit;
    }

    public long getFromAge() {
        return fromAge;
    }

    public void setFromAge(long fromAge) {
        this.fromAge = fromAge;
    }

    public long getToAge() {
        return toAge;
    }

    public void setToAge(long toAge) {
        this.toAge = toAge;
    }

    public String getFromAgeUnit() {
        return fromAgeUnit;
    }

    public void setFromAgeUnit(String fromAgeUnit) {
        this.fromAgeUnit = fromAgeUnit;
    }


    public InvestigationItemFacade getInvestigationItemFacade() {
        return investigationItemFacade;
    }

    public void setInvestigationItemFacade(InvestigationItemFacade investigationItemFacade) {
        this.investigationItemFacade = investigationItemFacade;
    }

    public InvestigationFacade getInvestigationFacade() {
        return investigationFacade;
    }

    public void setInvestigationFacade(InvestigationFacade investigationFacade) {
        this.investigationFacade = investigationFacade;
    }

    public HealthForm getInvestigation() {
        return investigation;
    }

    public void setInvestigation(HealthForm investigation) {
        this.investigation = investigation;
        recreateModel();
    }

    public List<HealthFormItem> getInvestigationItemsOfValueType() {
        if (investigation != null) {
            Map m = new HashMap();
            m.put("iit", HealthFormItemType.Value);
            investigationItemsOfValueType = getInvestigationItemFacade().findBySQL("select i from HealthFormItem i where i.retired=false and i.item.id = " + investigation.getId() + " and i.healthFormItemType =:iit", m, TemporalType.TIMESTAMP);
        }
        if (investigationItemsOfValueType == null) {
            investigationItemsOfValueType = new ArrayList<HealthFormItem>();
        }
        return investigationItemsOfValueType;
    }


    public void setInvestigationItemsOfValueType(List<HealthFormItem> investigationItemsOfValueType) {
        this.investigationItemsOfValueType = investigationItemsOfValueType;
    }

    public HealthFormItem getInvestigationItemOfValueType() {
        return investigationItemOfValueType;
    }

    public void setInvestigationItemOfValueType(HealthFormItem investigationItemOfValueType) {
        this.investigationItemOfValueType = investigationItemOfValueType;
    }

    public List<HealthFormItemValueFlag> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from InvestigationItemValueFlag c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public void prepareAdd() {
        current = new HealthFormItemValueFlag();
    }

    public void setSelectedItems(List<HealthFormItemValueFlag> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
        dynamicLabels = null;
    }

    public void saveSelected() {
        System.out.println("going to save");
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
        this.selectText = selectText;
    }

    public InvestigationItemValueFlagFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvestigationItemValueFlagFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public InvestigationItemDynamicLabelController() {
    }

    public HealthFormItemValueFlag getCurrent() {
        return current;
    }

    public void setCurrent(HealthFormItemValueFlag current) {
        this.current = current;
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

    private InvestigationItemValueFlagFacade getFacade() {
        return ejbFacade;
    }

    public List<HealthFormItemValueFlag> getItems() {
        String sql;
        if (investigation != null) {
            sql = "select i from InvestigationItemValueFlag i where i.retired=false and i.item.id = " + investigation.getId();
            items = getFacade().findBySQL(sql);
        }
        if (items == null) {
            items = new ArrayList<HealthFormItemValueFlag>();
        }
        return items;
    }

    public void saveForDynamicLabel() {
        if (investigation == null) {
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }
        if (investigationItemofDynamicLabelType == null) {
            UtilityController.addErrorMessage("Please select a dynamic label");
            return;
        }
        if (sex == null) {
            UtilityController.addErrorMessage("Please select a sex");
            return;
        }
        if (toAge == 0) {
            UtilityController.addErrorMessage("Please select a dynamic label");
            return;
        }
        HealthFormItemValueFlag i = new HealthFormItemValueFlag();
//        i.setItem(investigation);
        i.setCreatedAt(Calendar.getInstance().getTime());
        i.setCreater(getSessionController().getLoggedUser());
        i.setFlagMessage(flagMessage);
        if ("Days".equals(fromAgeUnit)) {
            i.setFromAge(fromAge);
        } else if ("Months".equals(fromAgeUnit)) {
            i.setFromAge(fromAge * 30);
        } else {
            i.setFromAge(fromAge * 365);
        }
        if ("Days".equals(toAgeUnit)) {
            i.setToAge(toAge);
        } else if ("Months".equals(toAgeUnit)) {
            i.setToAge(toAge * 30);
        } else {
            i.setToAge(toAge * 365);
        }
        i.setInvestigationItemOfLabelType(investigationItemofDynamicLabelType);
        i.setSex(sex);
        if (i.getId() == null) {
            getEjbFacade().create(i);
        } else {
            getEjbFacade().edit(i);
        }
        UtilityController.addSuccessMessage("Saved");
    }

    public void saveFlags() {
        for (HealthFormItemValueFlag f : dynamicLabels) {
            getFacade().edit(f);
        }
    }

    public List<HealthFormItemValueFlag> getDynamicLabels() {
        String sql;
        System.out.println("getting dynamic labels");
        if (dynamicLabels == null) {
            if (investigation != null && investigationItemofDynamicLabelType != null) {

                sql = "select i from InvestigationItemValueFlag i where i.retired=false and i.investigationItemOfLabelType.id = " + investigationItemofDynamicLabelType.getId();
                System.out.println("sql is " + sql);
                dynamicLabels = getFacade().findBySQL(sql);
                System.out.println("size is " + dynamicLabels.size());
            } else {
                System.out.println("no sql");
                dynamicLabels = null;
            }
        }
        if (dynamicLabels == null) {
            System.out.println("null");
            dynamicLabels = new ArrayList<HealthFormItemValueFlag>();
        }
        return dynamicLabels;
    }

    public void setDynamicLabels(List<HealthFormItemValueFlag> dynamicLabels) {
        this.dynamicLabels = dynamicLabels;
    }

    /**
     *
     */
    @FacesConverter(forClass = HealthFormItemValueFlag.class)
    public static class InvestigationItemValueFlagControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InvestigationItemDynamicLabelController controller = (InvestigationItemDynamicLabelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "investigationItemValueFlagController");
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
            if (object instanceof HealthFormItemValueFlag) {
                HealthFormItemValueFlag o = (HealthFormItemValueFlag) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InvestigationItemDynamicLabelController.class.getName());
            }
        }
    }
}
