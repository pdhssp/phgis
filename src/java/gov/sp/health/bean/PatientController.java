
package gov.sp.health.bean;

import gov.sp.health.facade.PatientFacade;
import gov.sp.health.entity.Patient;
import gov.sp.health.entity.Person;
import gov.sp.health.facade.PersonFacade;
import java.util.TimeZone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
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
public  class PatientController implements Serializable {

    private static final long serialVersionUID = 1L;
    @EJB
    private PatientFacade ejbFacade;
    @Inject
    SessionController sessionController;
    List<Patient> selectedItems;
    private Patient current;
    private List<Patient> items = null;
    String selectText = "";
    @EJB
    private PersonFacade personFacade;

    public List<Patient> getSelectedItems() {
        String sql;
        if (getSelectText() == null) {
            selectedItems = new ArrayList<Patient>();
        } else {
            sql = "select c from Patient c where c.retired=false and (upper(c.person.name) like '%" + getSelectText().toUpperCase() + "%' or  upper(c.person.nic) like '%" + getSelectText().toUpperCase() + "%') order by c.person.name";
            selectedItems = getFacade().findBySQL(sql);
        }
//        Patient p = new Patient();
//        p.getPerson().
        return selectedItems;
    }

    public void prepareAdd() {
        current = new Patient();
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

    public void setSelectedItems(List<Patient> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void createRandomPatient(String ptName) {
        Person person = new Person();
        Patient pt = new Patient();
        person.setName(ptName);
        pt.setPerson(person);
        getPersonFacade().create(person);
        getFacade().create(pt);
    }

    public List<Patient> completePatient(String query) {
        List<Patient> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Patient>();
        } else {
            sql = "select p from Patient p where p.retired=false and upper(p.person.name) like '%" + query.toUpperCase() + "%' order by p.person.name";
            System.out.println(sql);
            suggestions = getFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public void saveSelected() {
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            getPersonFacade().edit(getCurrent().getPerson());
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

    public PatientFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PatientFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public PatientController() {
    }

    public Patient getCurrent() {
        return current;
    }

    public void setCurrent(Patient current) {
        this.current = current;
    }

    private PatientFacade getFacade() {
        return ejbFacade;
    }

    public List<Patient> getItems() {
        String sql;
        sql = "select p from Patient p where p.retired = false order by p.person.name";
        items = getFacade().findBySQL(sql);
        return items;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    /**
     *
     * Set all Patients to null
     *
     */
    /**
     *
     */
    /**
     *
     * Delete the current Patient
     *
     */
    /**
     *
     */
    @FacesConverter(forClass = Patient.class)
//    @FacesConverter("PatientConverter")
    public static class PatientControllerConverter implements Converter {

        /**
         *
         * @param facesContext
         * @param component
         * @param value
         * @return
         */
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PatientController controller = (PatientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "patientController");
            System.out.println("value at converter getAsObject is " + value);
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            System.out.println(value);
            if (value == null || value.equals("null") || value.trim().equals("")) {
                key = 0l;
            } else {
                key = Long.valueOf(value);
                System.out.println(key);
                System.out.println(value);
            }
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        /**
         *
         * @param facesContext
         * @param component
         * @param object
         * @return
         */
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Patient) {
                Patient o = (Patient) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + PatientController.class.getName());
            }
        }
    }
}
