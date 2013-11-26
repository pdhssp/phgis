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
import gov.sp.health.entity.form.ReportItem;
import gov.sp.health.facade.InvestigationFacade;
import gov.sp.health.facade.ItemFacade;
import gov.sp.health.facade.SpecialityFacade;
import com.google.common.collect.HashBiMap;
import gov.sp.health.entity.Diagnosis;
import gov.sp.health.entity.Message;
import gov.sp.health.facade.DiagnosisFacade;
import gov.sp.health.facade.MessageFacade;
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
public class HealthMessageController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private MessageFacade ejbFacade;
    @EJB
    private SpecialityFacade specialityFacade;
    List<Message> selectedItems;
    private Message current;
    
    private List<Message> items = null;
    
    private List<Message> myItems = null;
    String selectText = "";
  
    @EJB
    MessageFacade itemFacade;

    
    public MessageFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(MessageFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public void prepareAdd() {
        current = new Message();
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
            getEjbFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            System.out.println("4");
                       getEjbFacade().create(getCurrent());

            getEjbFacade().edit(getCurrent());
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public MessageFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(MessageFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public HealthMessageController() {
    }

    public Message getCurrent() {
        if (current == null) {
            current = new Message();
        }
        return current;
    }

    public void setCurrent(Message current) {
        this.current = current;
        if (current != null) {
        }
    }

    public void delete() {
        getEjbFacade().remove(current);
        recreateModel();
        getItems();
        current = null;
        getCurrent();
    }

    public List<Message> getItems() {
        items = getEjbFacade().findAll();
        return items;
    }

    public List<Message> getMyItems() {
        String sql;
        sql="select m from Message m where m.toPerson=:p";
        Map m = new HashMap();
        m.put("p", getSessionController().getLoggedUser().getWebUserPerson());
        myItems = getEjbFacade().findBySQL(sql, m);
        return myItems;
    }

    public void setMyItems(List<Message> myItems) {
        this.myItems = myItems;
    }

    

    /**
     *
     */
    @FacesConverter("messagecon")
    public static class MessageConverter implements Converter {

        public MessageConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthMessageController controller = (HealthMessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthMessageController");
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
                        + object.getClass().getName() + "; expected type: " + HealthMessageController.class.getName());
            }
        }
    }

    @FacesConverter(forClass = Diagnosis.class)
    public static class HealthMessageControllerConverter implements Converter {

        public HealthMessageControllerConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HealthMessageController controller = (HealthMessageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "healthMessageController");
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
                        + object.getClass().getName() + "; expected type: " + HealthMessageController.class.getName());
            }
        }
    }
}
