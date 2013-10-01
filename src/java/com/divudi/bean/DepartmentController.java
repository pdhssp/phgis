/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.data.DepartmentType;
import com.divudi.entity.Department;
import com.divudi.entity.Institution;
import com.divudi.facade.DepartmentFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
public class DepartmentController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private DepartmentFacade ejbFacade;
    List<Department> selectedItems;
    private Department current;
    private List<Department> items = null;
    String selectText = "";
    private Boolean codeDisabled = false;
    private Institution institution;

    public List<Department> getInstitutionDepatrments() {
        if (getInstitution() == null) {
            return new ArrayList<Department>();
        } else {
            String sql = "Select d From Department d where d.retired=false and d.institution.id=" + getInstitution().getId();
            items = getFacade().findBySQL(sql);
        }

        //items = getFacade().findAll("name", true);


        return items;
    }

    public DepartmentType[] getDepartmentType() {
        return DepartmentType.values();
    }

    public List<Department> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from Department c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public List<Department> completeDept(String qry) {
        String sql;
        sql = "select c from Department c where c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name";
        return getFacade().findBySQL(sql);
    }

    public void prepareAdd() {
        codeDisabled = false;
        current = new Department();
    }

    public void setSelectedItems(List<Department> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    private Boolean checkCodeExist() {
        String sql = "SELECT i FROM Department i where i.retired=false ";
        List<Department> ins = getEjbFacade().findBySQL(sql);
        if (ins != null) {
            for (Department i : ins) {
                if (i.getDepartmentCode().trim().equals("")) {
                    continue;
                }
                if (i.getDepartmentCode() != null && i.getDepartmentCode().equals(getCurrent().getDepartmentCode())) {
                    UtilityController.addErrorMessage("Insituion Code Already Exist Try another Code");
                    return true;
                }
            }
        }
        return false;
    }

    public void saveSelected() {
        if (getCurrent() == null || getCurrent().getName().trim().equals("")) {
            UtilityController.addErrorMessage("Please enter a name");
            return;
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            if (getCurrent().getDepartmentCode() != null) {
                getCurrent().setDepartmentCode(getCurrent().getDepartmentCode());
            }
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            if (getCurrent().getDepartmentCode() != null) {
                if (!checkCodeExist()) {
                    getCurrent().setDepartmentCode(getCurrent().getDepartmentCode());

                } else {
                    return;
                }
            }
            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getFacade().create(getCurrent());
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();

    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public DepartmentFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DepartmentFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public DepartmentController() {
    }

    public Department getCurrent() {
        return current;
    }

    public void setCurrent(Department current) {
        codeDisabled = true;
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

    private DepartmentFacade getFacade() {
        return ejbFacade;
    }

    public List<Department> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    public Boolean getCodeDisabled() {
        return codeDisabled;
    }

    public void setCodeDisabled(Boolean codeDisabled) {
        this.codeDisabled = codeDisabled;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    /**
     *
     */
    @FacesConverter("dep")
    public static class DepartmentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DepartmentController controller = (DepartmentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "departmentController");
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
            if (object instanceof Department) {
                Department o = (Department) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + DepartmentController.class.getName());
            }
        }
    }
}
