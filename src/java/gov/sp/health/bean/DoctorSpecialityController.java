package gov.sp.health.bean;
import java.util.TimeZone;
import gov.sp.health.facade.DoctorSpecialityFacade;
import gov.sp.health.entity.DoctorSpeciality;
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
 *  
 *  )
 */
@Named
@SessionScoped
public  class DoctorSpecialityController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private DoctorSpecialityFacade ejbFacade;
    List<DoctorSpeciality> selectedItems;
    private DoctorSpeciality current;
    private List<DoctorSpeciality> items = null;
    String selectText = "";

    public List<DoctorSpeciality> getSelectedItems() {
        if (selectText.trim().equals("")) {
            selectedItems = getFacade().findBySQL("select c from DoctorSpeciality c where c.retired=false order by c.name");
        } else {
            selectedItems = getFacade().findBySQL("select c from DoctorSpeciality c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        }

        return selectedItems;
    }

    public void prepareAdd() {
        current = new DoctorSpeciality();
    }

    public void setSelectedItems(List<DoctorSpeciality> selectedItems) {
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

    public DoctorSpecialityFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DoctorSpecialityFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public DoctorSpecialityController() {
    }

    public DoctorSpeciality getCurrent() {
        if(current==null){
            current = new DoctorSpeciality();
        }
        return current;
    }

    public void setCurrent(DoctorSpeciality current) {
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

    private DoctorSpecialityFacade getFacade() {
        return ejbFacade;
    }

    public List<DoctorSpeciality> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = DoctorSpeciality.class)
    public static class DoctorSpecialityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DoctorSpecialityController controller = (DoctorSpecialityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "doctorSpecialityController");
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
            if (object instanceof DoctorSpeciality) {
                DoctorSpeciality o = (DoctorSpeciality) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + DoctorSpecialityController.class.getName());
            }
        }
    }
}
