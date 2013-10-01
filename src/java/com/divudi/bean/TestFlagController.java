/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.data.InvestigationItemType;
import com.divudi.data.Sex;
import com.divudi.entity.lab.HealthForm;
import com.divudi.entity.lab.HealthFormItem;
import com.divudi.entity.lab.HealthFormItemValueFlag;
import com.divudi.entity.lab.TestFlag;
import com.divudi.facade.InvestigationFacade;
import com.divudi.facade.InvestigationItemFacade;
import com.divudi.facade.InvestigationItemValueFlagFacade;
import com.divudi.facade.TestFlagFacade;
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
public  class TestFlagController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private TestFlagFacade ejbFacade;
    @EJB
    InvestigationItemFacade investigationItemFacade;
    @EJB
    InvestigationFacade investigationFacade;

    private TestFlag current;
    private List<TestFlag> items = null;
    HealthForm investigation;
    List<HealthFormItem> investigationItemsOfValueType;
    List<HealthFormItem> investigationItemsOfFlagType;
    HealthFormItem investigationItemOfValueType;
    HealthFormItem investigationItemofFlagType;
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
    TestFlag removingFlag;

    public double getFromValue() {
        return fromValue;
    }
    public void removeFlag(){
        if(removingFlag==null){
            UtilityController.addErrorMessage("Nothing to remove");
            return;
        }
        getFacade().remove(removingFlag);
        UtilityController.addSuccessMessage("Removed");
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

    public TestFlag getRemovingFlag() {
        return removingFlag;
    }

    public void setRemovingFlag(TestFlag removingFlag) {
        this.removingFlag = removingFlag;
    }

    TestFlag removingCal;


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

    public void addFlag() {
        if (investigation == null) {
            return;
        }
        if (investigationItemOfValueType == null) {
            return;
        }
        if (investigationItemofFlagType == null) {
            return;
        }


        TestFlag f = new TestFlag();
        f.setCreatedAt(Calendar.getInstance().getTime());
        f.setCreater(getSessionController().getLoggedUser());
        f.setInvestigationItemOfValueType(investigationItemOfValueType);
        f.setFlagMessage(flagMessage);
//        f.setItem(investigation);
        if (getFromAgeUnit().equalsIgnoreCase("Days")) {
            f.setFromAge(fromAge);
        } else if (getFromAgeUnit().equalsIgnoreCase("Months")) {
            f.setFromAge(fromAge * 30);
        } else {
            f.setFromAge(fromAge * 365);
        }
        if (getToAgeUnit().equalsIgnoreCase("Days")) {
            f.setToAge(toAge);
        } else if (getToAgeUnit().equalsIgnoreCase("Months")) {
            f.setToAge(toAge * 30);
        } else {
            f.setToAge(toAge * 365);
        }
        f.setInvestigationItemOfFlagType(investigationItemofFlagType);
        f.setInvestigationItemOfValueType(investigationItemOfValueType);
        f.setSex(sex);
        f.setHighMessage(highMessage);
        f.setLowMessage(lowMessage);
        f.setNormalMessage(flagMessage);
        f.setFromVal(fromValue);
        f.setToVal(toValue);
        getEjbFacade().create(f);
        clearForNew();
        UtilityController.addErrorMessage("Added");
    }

    private void clearForNew() {
        investigationItemOfValueType = null;
        investigationItemofFlagType = null;
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

    public HealthFormItem getInvestigationItemofFlagType() {
        return investigationItemofFlagType;
    }

    public void setInvestigationItemofFlagType(HealthFormItem investigationItemofFlagType) {
        this.investigationItemofFlagType = investigationItemofFlagType;
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
            m.put("iit", InvestigationItemType.Value);
            investigationItemsOfValueType = getInvestigationItemFacade().findBySQL("select i from HealthFormItem i where i.retired=false and i.item.id = " + investigation.getId() + " and i.ixItemType =:iit", m, TemporalType.TIMESTAMP);
        }
        if (investigationItemsOfValueType == null) {
            investigationItemsOfValueType = new ArrayList<HealthFormItem>();
        }
        return investigationItemsOfValueType;
    }

    public List<HealthFormItem> getInvestigationItemsOfFlagType() {

        if (investigation != null) {
            Map m = new HashMap();
            m.put("iit", InvestigationItemType.Flag);
            investigationItemsOfFlagType = getInvestigationItemFacade().findBySQL("select i from HealthFormItem i where i.retired=false and i.item.id = " + investigation.getId() + " and i.ixItemType=:iit", m, TemporalType.DATE);
        }
        if (investigationItemsOfFlagType == null) {
            investigationItemsOfFlagType = new ArrayList<HealthFormItem>();
        }
        return investigationItemsOfFlagType;
    }

    public void setInvestigationItemsOfFlagType(List<HealthFormItem> investigationItemsOfFlagType) {
        this.investigationItemsOfFlagType = investigationItemsOfFlagType;
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


   
    private void recreateModel() {
        items = null;
    }

   
    public TestFlagFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(TestFlagFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public TestFlagController() {
    }

    public TestFlag getCurrent() {
        return current;
    }

    public void setCurrent(TestFlag current) {
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

    private TestFlagFacade getFacade() {
        return ejbFacade;
    }

    public List<TestFlag> getItems() {
        String sql;
        if (investigation != null) {
            sql = "select i from TestFlag i where i.retired=false and i.item.id = " + investigation.getId();
            items = getFacade().findBySQL(sql);
        }
        if (items == null) {
            items = new ArrayList<TestFlag>();
        }
        return items;
    }


    /**
     *
     */
    @FacesConverter(forClass = TestFlag.class)
    public static class TestFlagControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TestFlagController controller = (TestFlagController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "testFlagController");
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
            if (object instanceof TestFlag) {
                TestFlag o = (TestFlag) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + TestFlagController.class.getName());
            }
        }
    }
}
