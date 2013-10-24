/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import java.util.TimeZone;
import gov.sp.health.facade.FamilyFacade;
import gov.sp.health.entity.Family;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class FamilyController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private FamilyFacade ejbFacade;
    List<Family> selectedItems;
    private Family current;
    private List<Family> items = null;
    String selectText = "";

    public void prepareAdd() {
        current = new Family();
    }

    public void setSelectedItems(List<Family> selectedItems) {
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

    public FamilyFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FamilyFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public FamilyController() {
    }

    public Family getCurrent() {
        if (current == null) {
            current = new Family();
        }
        return current;
    }

    public void setCurrent(Family current) {
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

    private FamilyFacade getFacade() {
        return ejbFacade;
    }

    public void addDirectly() {
        if (current == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        getFacade().create(current);
        current = new Family();
    }

    public List<Family> getItems() {
        // items = getFacade().findAll("name", true);
        String sql = "SELECT i FROM Family i where i.retired=false order by i.address";
        items = getEjbFacade().findBySQL(sql);
        if (items == null) {
            items = new ArrayList<Family>();
        }
        return items;
    }
    /**
     *
     */
}
