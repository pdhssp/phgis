/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;
import java.util.TimeZone;
import com.divudi.bean.SessionController;
import com.divudi.bean.UtilityController;
import com.divudi.facade.InvestigationTubeFacade;
import com.divudi.entity.form.HealthReportOtherCategory;
import java.io.Serializable;
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
public  class InvestigationTubeController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InvestigationTubeFacade ejbFacade;
    List<HealthReportOtherCategory> selectedItems;
    private HealthReportOtherCategory current;
    private List<HealthReportOtherCategory> items = null;
    String selectText = "";

    public List<HealthReportOtherCategory> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from InvestigationTube c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public void prepareAdd() {
        current = new HealthReportOtherCategory();
    }

    public void setSelectedItems(List<HealthReportOtherCategory> selectedItems) {
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

    public InvestigationTubeFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvestigationTubeFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public InvestigationTubeController() {
    }

    public HealthReportOtherCategory getCurrent() {
        return current;
    }

    public void setCurrent(HealthReportOtherCategory current) {
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

    private InvestigationTubeFacade getFacade() {
        return ejbFacade;
    }

    public List<HealthReportOtherCategory> getItems() {

        items = getFacade().findAll();

        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = HealthReportOtherCategory.class)
    public static class InvestigationTubeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InvestigationTubeController controller = (InvestigationTubeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "investigationTubeController");
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
            if (object instanceof HealthReportOtherCategory) {
                HealthReportOtherCategory o = (HealthReportOtherCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InvestigationTubeController.class.getName());
            }
        }
    }
}
