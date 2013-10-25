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
import gov.sp.health.data.StaffRole;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormCategory;
import gov.sp.health.entity.form.ReportItem;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.SpecialityFacade;
import com.google.common.collect.HashBiMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.inject.Named;
import javax.ejb.EJB;
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
public class HealthFormController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InvestigationFacade ejbFacade;
    @EJB
    private SpecialityFacade specialityFacade;
    List<HealthForm> selectedItems;
    private HealthForm current;
    private List<HealthForm> items = null;
    String selectText = "";
    String bulkText = "";
    boolean billedAs;
    boolean reportedAs;
    HealthFormCategory category;
    List<HealthForm> catIxs;
   
    private List<HealthForm> midvifesForms;

    public List<HealthForm> getMidvifesForms() {
        midvifesForms = getStaffRoleForms(StaffRole.Phm);
        return midvifesForms;
    }

    public List<HealthForm> getStaffRoleForms(StaffRole sr) {
        Map m = new HashMap();
        String jpql = "select f from HealthForm f where f.staffRole =:sr order by f.code";
        m.put("sr", sr);
        return getFacade().findBySQL(jpql, m);
    }

    public void setMidvifesForms(List<HealthForm> midvifesForms) {
        this.midvifesForms = midvifesForms;
    }

    public HealthFormCategory getCategory() {
        return category;
    }

    public void setCategory(HealthFormCategory category) {
        catIxs = null;
        this.category = category;
    }

    public void catToIxCat() {
        for (HealthForm i : getItems()) {
            i.setCategory(i.getFormCategory());
            getFacade().edit(i);
        }
        UtilityController.addSuccessMessage("Saved");
    }
    @EJB
    ItemFacade itemFacade;

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public List<HealthForm> getCatIxs() {
        if (catIxs == null) {
            if (category == null) {
                catIxs = getItems();
            } else {
                Map m = new HashMap();
                String sql = "select i from HealthForm  i where i.retired=false and i.investigationCategory = :cat order by i.name";
                m.put("cat", getCategory());
                catIxs = getFacade().findBySQL(sql, m);
            }
        }
        return catIxs;
    }

    public void setCatIxs(List<HealthForm> catIxs) {
        this.catIxs = catIxs;
    }

    public List<HealthForm> completeInvest(String query) {
        List<HealthForm> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<HealthForm>();
        } else {
            sql = "select c from HealthForm  c where c.retired=false and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            System.out.println(sql);
            suggestions = getFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public List<HealthForm> completeInvestWithout(String query) {
        List<HealthForm> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<HealthForm>();
        } else {
            // sql = "select c from HealthForm c where c.retired=false and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            sql = "select c from HealthForm  c where c.retired=false and type(c)!=Packege and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            System.out.println(sql);
            suggestions = getFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public boolean isBilledAs() {
        return billedAs;
    }

    public void setBilledAs(boolean billedAs) {
        this.billedAs = billedAs;
    }

    public boolean isReportedAs() {
        return reportedAs;
    }

    public void setReportedAs(boolean reportedAs) {
        this.reportedAs = reportedAs;
    }

    public void correctIx1() {
        List<HealthForm> allItems = getEjbFacade().findAll();
        for (HealthForm i : allItems) {
            i.setBilledAs(i);
            i.setReportedAs(i);
            getEjbFacade().edit(i);
        }

    }

    public String getBulkText() {

        return bulkText;
    }

    public void setBulkText(String bulkText) {
        this.bulkText = bulkText;
    }

    public List<HealthForm> getSelectedItems() {
        if (selectText.trim().equals("")) {
            selectedItems = getFacade().findBySQL("select c from HealthForm  c where c.retired=false order by c.name");
        } else {
            selectedItems = getFacade().findBySQL("select c from HealthForm  c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        }
        return selectedItems;
    }

    public List<HealthForm> completeItem(String qry) {
        List<HealthForm> completeItems = getFacade().findBySQL("select c from Item c where ( type(c) = Investigation or type(c) = Packege ) and c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name");
        return completeItems;
    }

    public void prepareAdd() {
        current = new HealthForm();
    }

    public void bulkUpload() {
        List<String> lstLines = Arrays.asList(getBulkText().split("\\r?\\n"));
        for (String s : lstLines) {
            List<String> w = Arrays.asList(s.split(","));
            try {
                String code = w.get(0);
                String ix = w.get(1);
                String ic = w.get(2);
                String f = w.get(4);
                System.out.println(code + " " + ix + " " + ic + " " + f);


                HealthForm tix = new HealthForm();
                tix.setCode(code);
                tix.setName(ix);



            } catch (Exception e) {
            }

        }
    }

    public void setSelectedItems(List<HealthForm> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {

        getCurrent().setCategory(getCurrent().getFormCategory());
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            System.out.println("1");
            if (billedAs == false) {
                System.out.println("2");
                getCurrent().setBilledAs(getCurrent());

            }
            if (reportedAs == false) {
                System.out.println("3");
                getCurrent().setReportedAs(getCurrent());
            }
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            System.out.println("4");
            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getFacade().create(getCurrent());
            if (billedAs == false) {
                System.out.println("5");
                getCurrent().setBilledAs(getCurrent());
            }
            if (reportedAs == false) {
                System.out.println("6");
                getCurrent().setReportedAs(getCurrent());
            }
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public InvestigationFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvestigationFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public HealthFormController() {
    }

    public HealthForm getCurrent() {
        if (current == null) {
            current = new HealthForm();
        }
        return current;
    }

    public void setCurrent(HealthForm current) {
        this.current = current;
        if (current != null) {
            if (current.getBilledAs() == current) {
                billedAs = false;
            } else {
                billedAs = true;
            }
            if (current.getReportedAs() == current) {
                reportedAs = false;
            } else {
                reportedAs = true;
            }
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

    private InvestigationFacade getFacade() {
        return ejbFacade;
    }

    public List<HealthForm> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    public SpecialityFacade getSpecialityFacade() {
        return specialityFacade;
    }

    public void setSpecialityFacade(SpecialityFacade specialityFacade) {
        this.specialityFacade = specialityFacade;
    }

   

    /**
     *
     */
    @FacesConverter("hfcon")
    public static class HealthFormConverter implements Converter {

        public HealthFormConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthFormController controller = (HealthFormController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthFormController");
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
            if (object instanceof HealthForm) {
                HealthForm o = (HealthForm) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthFormController.class.getName());
            }
        }
    }
    
    
    @FacesConverter(forClass = HealthForm.class)
    public static class HealthFormControllerConverter implements Converter {

        public HealthFormControllerConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthFormController controller = (HealthFormController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthFormController");
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
            if (object instanceof HealthForm) {
                HealthForm o = (HealthForm) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthFormController.class.getName());
            }
        }
    }
    
}
