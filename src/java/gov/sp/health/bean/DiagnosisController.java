
package gov.sp.health.bean;

import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.data.StaffRole;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.ReportItem;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.SpecialityFacade;
import com.google.common.collect.HashBiMap;
import gov.sp.health.entity.Diagnosis;
import gov.sp.health.facade.DiagnosisFacade;
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
public class DiagnosisController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private DiagnosisFacade ejbFacade;
    @EJB
    private SpecialityFacade specialityFacade;
    List<Diagnosis> selectedItems;
    private Diagnosis current;
    private List<Diagnosis> items = null;
    String selectText = "";
    String bulkText = "";
    boolean billedAs;
    boolean reportedAs;
    List<Diagnosis> catIxs;
   
   
  

    public List<Diagnosis> getStaffRoleForms(StaffRole sr) {
        Map m = new HashMap();
        String jpql = "select f from Diagnosis f where f.staffRole =:sr order by f.code";
        m.put("sr", sr);
        return getEjbFacade().findBySQL(jpql, m);
    }

    
    @EJB
    ItemFacade itemFacade;

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    
    public void setCatIxs(List<Diagnosis> catIxs) {
        this.catIxs = catIxs;
    }

    public List<Diagnosis> completeInvest(String query) {
        List<Diagnosis> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Diagnosis>();
        } else {
            sql = "select c from Diagnosis  c where c.retired=false and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            System.out.println(sql);
            suggestions = getEjbFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public List<Diagnosis> completeInvestWithout(String query) {
        List<Diagnosis> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Diagnosis>();
        } else {
            // sql = "select c from Diagnosis c where c.retired=false and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            sql = "select c from Diagnosis  c where c.retired=false and type(c)!=Packege and upper(c.name) like '%" + query.toUpperCase() + "%' order by c.name";
            System.out.println(sql);
            suggestions = getEjbFacade().findBySQL(sql);
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
        List<Diagnosis> allItems = getEjbFacade().findAll();
        for (Diagnosis i : allItems) {
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

    public List<Diagnosis> getSelectedItems() {
        if (selectText.trim().equals("")) {
            selectedItems = getEjbFacade().findBySQL("select c from Diagnosis  c where c.retired=false order by c.name");
        } else {
            selectedItems = getEjbFacade().findBySQL("select c from Diagnosis  c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        }
        return selectedItems;
    }

    public List<Diagnosis> completeItem(String qry) {
        List<Diagnosis> completeItems = getEjbFacade().findBySQL("select c from Item c where ( type(c) = Investigation or type(c) = Packege ) and c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name");
        return completeItems;
    }

    public void prepareAdd() {
        current = new Diagnosis();
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


                Diagnosis tix = new Diagnosis();
                tix.setCode(code);
                tix.setName(ix);



            } catch (Exception e) {
            }

        }
    }

    public void setSelectedItems(List<Diagnosis> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {


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
            getEjbFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            System.out.println("4");
            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getEjbFacade().create(getCurrent());
            if (billedAs == false) {
                System.out.println("5");
                getCurrent().setBilledAs(getCurrent());
            }
            if (reportedAs == false) {
                System.out.println("6");
                getCurrent().setReportedAs(getCurrent());
            }
            getEjbFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public DiagnosisFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DiagnosisFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public DiagnosisController() {
    }

    public Diagnosis getCurrent() {
        if (current == null) {
            current = new Diagnosis();
        }
        return current;
    }

    public void setCurrent(Diagnosis current) {
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
            getEjbFacade().edit(current);
            UtilityController.addSuccessMessage("DeleteSuccessfull");
        } else {
            UtilityController.addSuccessMessage("NothingToDelete");
        }
        recreateModel();
        getItems();
        current = null;
        getCurrent();
    }

   
    

    public List<Diagnosis> getItems() {
        items = getEjbFacade().findAll("name", true);
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
    public static class DiagnosisConverter implements Converter {

        public DiagnosisConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DiagnosisController controller = (DiagnosisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "diagnosisController");
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
            if (object instanceof Diagnosis) {
                Diagnosis o = (Diagnosis) object;
                return getStringKey(o.getId());
            } else {
                System.out.println("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + DiagnosisController.class.getName());
            return null;
            }
        }
    }
    
    
    @FacesConverter(forClass = Diagnosis.class)
    public static class DiagnosisControllerConverter implements Converter {

        public DiagnosisControllerConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DiagnosisController controller = (DiagnosisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "diagnosisController");
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
            if (object instanceof Diagnosis) {
                Diagnosis o = (Diagnosis) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + DiagnosisController.class.getName());
            }
        }
    }
    
}
