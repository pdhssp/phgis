/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.data.CalculationType;
import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.FormCal;
import gov.sp.health.entity.form.ReportItem;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.InvestigationItemFacade;
import gov.sp.health.facade.IxCalFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public  class IxCalController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private IxCalFacade ixCalFacade;
    @EJB
    InvestigationItemFacade iiFacade;
    @EJB
    InvestigationFacade ixFacade;
    @EJB
    IxCalFacade iivcFacade;
    private List<FormCal> items = null;
    List<HealthFormItem> vals;
    List<HealthFormItem> cals;
    HealthForm ix;
    HealthFormItem val;
    HealthFormItem cal;
    FormCal reIxCal;
    FormCal addingIxCal;

    public IxCalFacade getIivcFacade() {
        return iivcFacade;
    }

    public void setIivcFacade(IxCalFacade iivcFacade) {
        this.iivcFacade = iivcFacade;
    }

    public FormCal getAddingIxCal() {
        if (addingIxCal == null) {
            addingIxCal = new FormCal();
        }
        return addingIxCal;
    }

    public void setAddingIxCal(FormCal addingIxCal) {
        this.addingIxCal = addingIxCal;
    }

    public FormCal getReIxCal() {
        return reIxCal;
    }

    public void setReIxCal(FormCal reIxCal) {
        this.reIxCal = reIxCal;
    }

    public void addCal() {
        if (ix == null) {
            UtilityController.addErrorMessage("Investigation ?");
            return;
        }
        if (cal == null) {
            UtilityController.addErrorMessage("Calculation ?");
            return;
        }
        if (addingIxCal == null) {
            UtilityController.addErrorMessage("Cal?");
            return;
        }
        addingIxCal.setCalIxItem(cal);
        System.out.println("id is " + addingIxCal.getId());
        if (addingIxCal.getId() == null || addingIxCal.getId() == 0) {
            System.out.println("iivc creating");
            getIivcFacade().create(addingIxCal);
        } else {
            System.out.println("iivc editing");
            getIivcFacade().edit(addingIxCal);
        }
        items.add(addingIxCal);
        addingIxCal = new FormCal();
        UtilityController.addSuccessMessage("Added");

    }

    public FormCal lastCal() {
        FormCal tcal = null;
        if (items != null || items.isEmpty()!=true) {
            System.out.println("items are null or empty");
            tcal = items.get(items.size() - 1);
        }
        return tcal;
    }

    public void removeLastCal() {
        System.out.println("removing last cal");
        if (items != null && items.isEmpty() != true) {
            System.out.println("not empty");
            FormCal tcal = lastCal();
            System.out.println("last cal is - " + lastCal());
            items.remove(tcal);
            System.out.println("remoed from list");
            getIivcFacade().remove(tcal);
            System.out.println("removed from db");
        }
    }

    public HealthFormItem getCal() {
        return cal;
    }

    public void setCal(HealthFormItem cal) {
        this.cal = cal;
    }

    public InvestigationItemFacade getIiFacade() {
        return iiFacade;
    }

    public void setIiFacade(InvestigationItemFacade iiFacade) {
        this.iiFacade = iiFacade;
    }

    public InvestigationFacade getIxFacade() {
        return ixFacade;
    }

    public void setIxFacade(InvestigationFacade ixFacade) {
        this.ixFacade = ixFacade;
    }

    public HealthForm getIx() {
        return ix;
    }

    public void setIx(HealthForm ix) {
        this.ix = ix;
    }

    public List<HealthFormItem> getVals() {
        if (ix != null) {
            Map m = new HashMap();
            m.put("iit", HealthFormItemType.Value);
            vals = getIiFacade().findBySQL("select i from HealthFormItem i where i.retired=false and i.item.id = " + ix.getId() + " and i.healthFormItemType =:iit", m, TemporalType.TIMESTAMP);
        }
        if (vals == null) {
            vals = new ArrayList<HealthFormItem>();
        }
//        vals = new ArrayList<InvestigationItem>();
//        if (ix != null) {
//            for (ReportItem ii : ix.getReportItems()) {
//                if (ii instanceof HealthFormItem && ii.gethealthFormItemType() == HealthFormItemType.Value) {
//                    vals.add((HealthFormItem) ii);
//                }
//            }
//        }
        return vals;
    }

    public List<HealthFormItem> getCals() {
        if (ix != null) {
            String jpql;
            jpql = "select i from HealthFormItem i where i.retired=false and i.item.id = " + ix.getId() + " and i.healthFormItemType = :iit order by i.cssTop";
            Map m = new HashMap();
            m.put("iit", HealthFormItemType.Calculation);
            cals = getIiFacade().findBySQL(jpql, m, TemporalType.TIMESTAMP);
            if (cals == null) {
                cals = new ArrayList<HealthFormItem>();
            }
        }
//        cals = new ArrayList<InvestigationItem>();
        if (ix != null) {
            System.out.println("ii count is " + ix.getReportItems().size());
            for (ReportItem ii : ix.getReportItems()) {
                
                if (ii instanceof HealthFormItem && ii.getHealthFormItemType() == HealthFormItemType.Calculation) {
                    System.out.println("ii is " + ii);
                }
            }
        }
        return cals;
    }

    public HealthFormItem getVal() {
        return val;
    }

    public void setVal(HealthFormItem val) {
        this.val = val;
    }

    public IxCalFacade getEjbFacade() {
        return ixCalFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public IxCalController() {
    }

    private IxCalFacade getFacade() {
        return ixCalFacade;
    }

    public List<FormCal> getItems() {
        String sql;
        if (ix != null && cal != null) {
            sql = "select i from IxCal i where i.calIxItem.id = " + cal.getId();
            items = getFacade().findBySQL(sql);
        }
        if (items == null) {
            items = new ArrayList<FormCal>();
        }
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = FormCal.class)
    public static class IxCalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            IxCalController controller = (IxCalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ixCalController");
            return controller.ixCalFacade.find(getKey(value));
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
            if (object instanceof FormCal) {
                FormCal o = (FormCal) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + IxCalController.class.getName());
            }
        }
    }
}
