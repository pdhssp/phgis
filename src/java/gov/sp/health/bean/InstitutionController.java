/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import gov.sp.health.data.InstitutionType;
import java.util.TimeZone;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.entity.Institution;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@Named
@SessionScoped
public class InstitutionController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private InstitutionFacade ejbFacade;
    List<Institution> selectedItems;
    private Institution current;
    private List<Institution> items = null;
    //   private String insCode;
    String selectText = "";
    
    private Boolean codeDisabled = false;
    private InstitutionType[] institutionTypes;

    public List<Institution> getSelectedItems() {
        if (selectText.trim().equals("")) {
            selectedItems = getFacade().findBySQL("select c from Institution c where c.retired=false order by c.name");
        } else {
            selectedItems = getFacade().findBySQL("select c from Institution c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        }

        return selectedItems;
    }

    public List<Institution> completeIns(String qry) {
        String sql;
        sql = "select c from Institution c where c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name";
        return getFacade().findBySQL(sql);
    }
    
    public List<Institution> completeCompany(String qry) {
        String sql;
        sql = "select c from Institution c where c.retired=false and c.institutionType=gov.sp.health.data.InstitutionType.Company and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name";
        return getFacade().findBySQL(sql);
    }


    public void prepareAdd() {
        codeDisabled = false;
        current = new Institution();
    }

    public void setSelectedItems(List<Institution> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    private Boolean checkCodeExist() {
        String sql = "SELECT i FROM Institution i where i.retired=false ";
        List<Institution> ins = getEjbFacade().findBySQL(sql);
        if (ins != null) {
            for (Institution i : ins) {
                if (i.getInstitutionCode() == null || i.getInstitutionCode().trim().equals("")) {
                    continue;
                }
                if (i.getInstitutionCode() != null && i.getInstitutionCode().equals(getCurrent().getInstitutionCode())) {
                    UtilityController.addErrorMessage("Insituion Code Already Exist Try another Code");
                    return true;
                }
            }
        }
        return false;
    }

    public void saveSelected() {
        if (getCurrent().getInstitutionType() == null) {
            UtilityController.addErrorMessage("Select Instituion Type");
            return;
        }
        
        if (getCurrent().getName().equals("")) {
            UtilityController.addErrorMessage("Please Enter a Institution Name");
            return;
        }
        
        if (getCurrent().getInstitutionCode().equals("")) {
            UtilityController.addErrorMessage("Please Enter Instituion Code");
            return;
        }
          if (getCurrent().getAddress().equals("")) {
            UtilityController.addErrorMessage("Please Enter address");
            return;
        }
        
        
         if (getCurrent().getDistric() == null) {
            UtilityController.addErrorMessage("Please Enter a District");
            return;
        }
          if (getCurrent().getProvince()== null) {
            UtilityController.addErrorMessage("Select a Province");
            return;
        }

          if(!getCurrent().getEmail().equals(""))
              {
        try {
          String lineIwant = getCurrent().getEmail();
                String emailreg = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Boolean b = lineIwant.matches(emailreg);

                if (b == false) {
                    UtilityController.addErrorMessage("Invalid E-Mail");
                    return;
                }
                        



        } catch (Exception e) {
            System.out.println(e.getMessage());
            
            return;
        }
    }
              
              
              
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {

            if (getCurrent().getInstitutionCode() != null) {
                getCurrent().setInstitutionCode(getCurrent().getInstitutionCode());
            }
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("Updated Successfully");
        } else {
            if (getCurrent().getInstitutionCode() != null) {
                if (!checkCodeExist()) {
                    getCurrent().setInstitutionCode(getCurrent().getInstitutionCode());

                } else {
                    return;
                }
            }
            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getFacade().create(getCurrent());
            UtilityController.addSuccessMessage("Saved New Institute Successfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public InstitutionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(InstitutionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public InstitutionController() {
    }

    public Institution getCurrent() {
        if (current == null) {
            current = new Institution();
        }
        return current;
    }

    public void setCurrent(Institution current) {
        codeDisabled = true;
        this.current = current;
    }

    public void delete() {

        if (getCurrent() != null) {
            getCurrent().setRetired(true);
            getCurrent().setRetiredAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setRetirer(sessionController.getLoggedUser());
            getFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("DeleteSuccessfull");
        } else {
            UtilityController.addSuccessMessage("NothingToDelete");
        }
        recreateModel();
        getItems();
        current = null;
        getCurrent();
    }

    private InstitutionFacade getFacade() {
        return ejbFacade;
    }

    public List<Institution> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

     public List<Institution> getCompany() {
        items = getFacade().findBySQL("select c from Institution c where c.retired=false and c.institutionType=gov.sp.health.data.InstitutionType.Company  order by c.name");
        if (items == null) {
            items = new ArrayList<Institution>();
        }

        return items;
    }
    
    public List<Institution> getBanks() {
        items = getFacade().findBySQL("select c from Institution c where c.retired=false and c.institutionType=gov.sp.health.data.InstitutionType.Bank  order by c.name");
        if (items == null) {
            items = new ArrayList<Institution>();
        }

        return items;
    }

    public Boolean getCodeDisabled() {
        return codeDisabled;
    }

    public void setCodeDisabled(Boolean codeDisabled) {
        this.codeDisabled = codeDisabled;
    }

    public InstitutionType[] getInstitutionTypes() {
        return InstitutionType.values();
    }

    public void setInstitutionTypes(InstitutionType[] institutionTypes) {
        this.institutionTypes = institutionTypes;
    }

    /**
     *
     */
    @FacesConverter(forClass = Institution.class )
    public static class InstitutionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstitutionController controller = (InstitutionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "institutionController");
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
            if (object instanceof Institution) {
                Institution o = (Institution) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + InstitutionController.class.getName());
            }
        }
    }
}
