package gov.sp.health.bean;

import gov.sp.health.data.DefaultsBean;
import gov.sp.health.ejb.GisEjb;
import gov.sp.health.entity.Area;
import java.util.TimeZone;
import gov.sp.health.facade.FamilyFacade;
import gov.sp.health.entity.Family;
import gov.sp.health.entity.GisCoordinate;
import gov.sp.health.entity.Person;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.facade.GisCoordinateFacade;
import gov.sp.health.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    MapModel allFamiliesModel;
    GisCoordinate defaultCoordinate;
    
     @EJB
    AreaFacade areaFacade;

    public AreaFacade getAreaFacade() {
        return areaFacade;
    }

    public void setAreaFacade(AreaFacade areaFacade) {
        this.areaFacade = areaFacade;
    }

    
   

    public GisCoordinate getDefaultCoordinate() {
        if (defaultCoordinate == null) {
            defaultCoordinate = new GisCoordinate();
            defaultCoordinate.setLatitude(6);
            defaultCoordinate.setLongtide(81);
        }
        return defaultCoordinate;
    }

    public void setDefaultCoordinate(GisCoordinate defaultCoordinate) {
        this.defaultCoordinate = defaultCoordinate;
    }

    public MapModel getAllFamiliesModel() {
        if (allFamiliesModel == null) {
            allFamiliesModel = new DefaultMapModel();
        }
        return allFamiliesModel;
    }

    public void setAllFamiliesModel(MapModel allFamiliesModel) {
        this.allFamiliesModel = allFamiliesModel;
    }

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
        if(person.getName().equals("")){
            UtilityController.addErrorMessage("Enter person name");
            return;
        }
        
        
        getCurrent().getPersons().add(person);
        getFacade().edit(current);
        UtilityController.addSuccessMessage("Added");
        person = null;
    }

    public String prepareAdd() {
        current = null;
        getCurrent();
        return "family_members";
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
          // current.setPhmArea(sessionController.getLoggedUser().getArea());
            getFacade().edit(current);
            UtilityController.addSuccessMessage("Updated Successfully");
        } else {
           
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            System.out.println("this = " + sessionController.getLoggedUser().getArea());
            System.out.println("this = " + sessionController.getArea());
            current.setPhmArea(sessionController.getArea());
            if(current.getCoordinate()!=null){
                if(current.getCoordinate().getId()==null || current.getCoordinate().getId()==0){
                    getGisCoordinateFacade().create(current.getCoordinate());
                }else{
                    getGisCoordinateFacade().edit(current.getCoordinate());
                }
            }
            
            getFacade().create(current);
            UtilityController.addSuccessMessage("saved New Successfully");
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
        if (current != null) {
            familyMapModel = new DefaultMapModel();
            Marker marker = new Marker(new LatLng(current.getCoordinate().getLatitude(), current.getCoordinate().getLongtide()), current.getAddress());
            familyMapModel.addOverlay(marker);
            defaultCoordinate = current.getCoordinate();
        }
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

    public String listFamilies() {
        String sql = "SELECT i FROM Family i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) order by i.address";
        Map m = new HashMap();
        m.put("a", getSessionController().getArea());
        items = getEjbFacade().findBySQL(sql,m);
        
        if (items == null) {
            items = new ArrayList<Family>();
        }
        allFamiliesModel = new DefaultMapModel();
        for (Family f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                allFamiliesModel.addOverlay(marker);
                
            }
        }
        defaultCoordinate= getGisEjb().getCentre(allFamiliesModel);
        return "family";
    }

    
    @EJB
    GisEjb gisEjb;

    public GisEjb getGisEjb() {
        return gisEjb;
    }

    public void setGisEjb(GisEjb gisEjb) {
        this.gisEjb = gisEjb;
    }
    public List<Family> getItems() {
        return items;
    }

  

        
}
