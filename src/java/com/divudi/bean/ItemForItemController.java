/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.entity.Item;
import com.divudi.entity.form.FormForForm;
import com.divudi.entity.form.PatientHealthForm;
import com.divudi.facade.ItemForItemFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
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
public  class ItemForItemController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private ItemForItemFacade ejbFacade;
    private FormForForm current;
    Item parentItem;
    Item childItem;
    private List<FormForForm> items = null;
    PatientHealthForm patientIx;

    public PatientHealthForm getPatientIx() {
        return patientIx;
    }

    public void setPatientIx(PatientHealthForm patientIx) {
        this.patientIx = patientIx;
        setParentItem(patientIx.getInvestigation());
    }
    
    

    public Item getChildItem() {
        return childItem;
    }

    public void setChildItem(Item childItem) {
        this.childItem = childItem;
    }

    
    
    public Item getParentItem() {
        return parentItem;
    }

    public void setParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }


    public void prepareAdd() {
        current = new FormForForm();
    }

    private void recreateModel() {
        items = null;
    }

    public void addItem(){
        if (parentItem==null || childItem ==null){
            UtilityController.addErrorMessage("Please select");
            return;
        }
        FormForForm temIi = new FormForForm();
        temIi.setParentItem(parentItem);
        temIi.setChildItem(childItem);
        temIi.setCreatedAt(Calendar.getInstance().getTime());
        temIi.setCreater(sessionController.getLoggedUser());
        getFacade().create(temIi);
        setChildItem(null);
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

    public ItemForItemFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ItemForItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ItemForItemController() {
    }

    public FormForForm getCurrent() {
        return current;
    }

    public void setCurrent(FormForForm current) {
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

    private ItemForItemFacade getFacade() {
        return ejbFacade;
    }

    public List<FormForForm> getItems() {
        if (getParentItem()!=null && getParentItem().getId()!=null ) {
            items = getFacade().findBySQL("select c from ItemForItem c where c.retired=false and c.parentItem.id = " + getParentItem().getId() + " ");
        }
        if (items == null) {
            items = new ArrayList<FormForForm>();
        }
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = FormForForm.class)
    public static class ItemForItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemForItemController controller = (ItemForItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemForItemController");
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
            if (object instanceof FormForForm) {
                FormForForm o = (FormForForm) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ItemForItemController.class.getName());
            }
        }
    }
}
