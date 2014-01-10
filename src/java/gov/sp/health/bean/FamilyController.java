
package gov.sp.health.bean;

import gov.sp.health.data.DefaultsBean;
import java.util.TimeZone;
import gov.sp.health.facade.FamilyFacade;
import gov.sp.health.entity.Family;
import gov.sp.health.entity.GisCoordinate;
import gov.sp.health.entity.Person;
import gov.sp.health.facade.GisCoordinateFacade;
import gov.sp.health.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


@Named
@SessionScoped
public class FamilyController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private FamilyFacade ejbFacade;
    @EJB
    PersonFacade personFacade;
    List<Family> selectedItems;
    private Family current;
    private List<Family> items = null;
    String selectText = "";
    Person person;
    MapModel familyMapModel;

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }
    @EJB
    GisCoordinateFacade gisCoordinateFacade;

    public GisCoordinateFacade getGisCoordinateFacade() {
        return gisCoordinateFacade;
    }

    public void setGisCoordinateFacade(GisCoordinateFacade gisCoordinateFacade) {
        this.gisCoordinateFacade = gisCoordinateFacade;
    }

    public MapModel getFamilyMapModel() {
        return familyMapModel;
    }

    public void setFamilyMapModel(MapModel familyMapModel) {
        this.familyMapModel = familyMapModel;
    }

    public Person getPerson() {
        if (person == null) {
            person = new Person();
        }
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void addPersonToFamily() {
        if (person == null) {
            UtilityController.addErrorMessage("Select person");
            return;
        }
//        getPerson().setFamily(current);
//        getPersonFacade().edit(person);
//        
        getCurrent().getPersons().add(person);
        getFacade().edit(current);
        UtilityController.addSuccessMessage("Added");
        person = null;
    }

    public void prepareAdd() {
        current = null;
        getCurrent();
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
        familyMapModel = new DefaultMapModel();
    }

    public void addMarker(ActionEvent actionEvent) {
        Marker marker = new Marker(new LatLng(getCurrent().getCoordinate().getLatitude(), getCurrent().getCoordinate().getLongtide()), getCurrent().getAddress());
        familyMapModel = new DefaultMapModel();
        familyMapModel.addOverlay(marker);
        
        if (current == null) {
            UtilityController.addErrorMessage("Select family");
            return;
        }
        getGisCoordinateFacade().edit(current.getCoordinate());//save coordinate
        getEjbFacade().edit(current);//save family details
    }

    public Family getCurrent() {
        if (current == null) {
            current = new Family();
            GisCoordinate c = new GisCoordinate();
            c.setLatitude(6.0350);
            c.setLongtide(80.2158);
            current.setCoordinate(c);
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
        if (current.getCoordinate() != null) {
            System.out.println("current = " + current);
            System.out.println("current.getCoordinate() = " + current.getCoordinate());
            System.out.println("getGisCoordinateFacade() = " + getGisCoordinateFacade());
            getGisCoordinateFacade().create(current.getCoordinate());
        }
        getFacade().create(current);
        current = new Family();
    }

    public void listFamilies() {
        String sql = "SELECT i FROM Family i where i.retired=false order by i.address";
        items = getEjbFacade().findBySQL(sql);
        if (items == null) {
            items = new ArrayList<Family>();
        }
        familyMapModel = new DefaultMapModel();
        for (Family f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                familyMapModel.addOverlay(marker);
            }
        }
    }

    public List<Family> getItems() {
        return items;
    }
    /**
     *
     */
}
