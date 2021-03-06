package gov.sp.health.bean;

import gov.sp.health.entity.form.HealthFormCategory;
import gov.sp.health.facade.InvestigationCategoryFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Inject;
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
 *
 * )
 */
@Named
@SessionScoped
public class HealthFormCategoryController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InvestigationCategoryFacade ejbFacade;
    List<HealthFormCategory> selectedItems;
    private HealthFormCategory current;
    private List<HealthFormCategory> items = null;
    String selectText = "";

    public List<HealthFormCategory> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from HealthFormCategory c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public void prepareAdd() {
        current = new HealthFormCategory();
    }

    public void setSelectedItems(List<HealthFormCategory> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {

        if (getCurrent().getName().equals("")) {

            UtilityController.addSuccessMessage("Please Enter Category Name");
            return;
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("updated Successfully");
        } else {
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            getFacade().create(current);
            UtilityController.addSuccessMessage("saved Successfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public InvestigationCategoryFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InvestigationCategoryFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public HealthFormCategoryController() {
    }

    public HealthFormCategory getCurrent() {
        return current;
    }

    public void setCurrent(HealthFormCategory current) {
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

    private InvestigationCategoryFacade getFacade() {
        return ejbFacade;
    }

    public List<HealthFormCategory> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = HealthFormCategory.class)
    public static class HealthFormCategoryControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthFormCategoryController controller = (HealthFormCategoryController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthFormCategoryController");
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
            if (object instanceof HealthFormCategory) {
                HealthFormCategory o = (HealthFormCategory) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + HealthFormCategoryController.class.getName());
            }
        }
    }
}
