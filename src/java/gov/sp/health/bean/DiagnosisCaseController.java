package gov.sp.health.bean;

import gov.sp.health.ejb.GisEjb;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.Diagnosis;
import gov.sp.health.entity.DiagnosisCase;
import gov.sp.health.entity.DiagnosisList;
import gov.sp.health.entity.Disease;
import gov.sp.health.entity.GisCoordinate;
import gov.sp.health.entity.Person;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.facade.DiagnosisCaseFacade;
import gov.sp.health.facade.DiseaseFacade;
import gov.sp.health.facade.GisCoordinateFacade;
import gov.sp.health.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datalist.DataList;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@Named
@SessionScoped
public class DiagnosisCaseController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private DiagnosisCaseFacade ejbFacade;
    @EJB
    PersonFacade personFacade;
    List<DiagnosisCase> selectedItems;
    private DiagnosisCase current;
    private List<DiagnosisCase> items = null;
    String selectText = "";
    Diagnosis diagnosis;
    MapModel familyMapModel;
    MapModel allFamiliesModel;
    GisCoordinate defaultCoordinate;
    @EJB
    AreaFacade areaFacade;
    private Date fromDate;
    private Date toDate;
    private int count;
    List<DiagnosisList> groupList;

    private List<DiagnosisCaseController> dcist;

    private DataList dl;

    public List<DiagnosisList> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<DiagnosisList> groupList) {
        this.groupList = groupList;
    }

    public List<DiagnosisCase> getSelectedItems() {
        return selectedItems;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public void addDiagnosisCase() {
        if (current.getDiagnosis() == null) {
            UtilityController.addErrorMessage("Select disease");
            return;
        }
        if (current.getPerson().getName() == null || current.getPerson().getName().trim().equals("")) {
            UtilityController.addErrorMessage("Select Person");
            return;
        }
        if (getCurrent().getPerson().getId() == null) {
            getPersonFacade().create(current.getPerson());
        } else {
            getPersonFacade().edit(current.getPerson());
        }
        if (getCurrent().getId() == null) {
            getFacade().create(current);
        } else {
            getFacade().edit(current);
        }
        UtilityController.addSuccessMessage("Added");
        diagnosis = null;
    }

    public String prepareAdd() {
        current = null;
        getCurrent();
        return "diagnosis_case";
    }

    public void setSelectedItems(List<DiagnosisCase> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {
        if ( diagnosis == null) {
            UtilityController.addErrorMessage("Please Select Diagnosis  ");
            return;
        }
        current.setDiagnosis(diagnosis);
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("Updated Successfully");
        } else {
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            System.out.println("3");
          //  current.setPhmArea(sessionController.getArea());
            getFacade().create(current);
            UtilityController.addSuccessMessage("saved New Successfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public DiagnosisCaseFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(DiagnosisCaseFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public DiagnosisCaseController() {
        familyMapModel = new DefaultMapModel();
    }

    public void addMarker(ActionEvent actionEvent) {
        Marker marker = new Marker(new LatLng(getCurrent().getCoordinate().getLatitude(), getCurrent().getCoordinate().getLongtide()), getCurrent().getAddress());
        familyMapModel = new DefaultMapModel();
        familyMapModel.addOverlay(marker);

        if (current == null) {
            UtilityController.addErrorMessage("Select ");
            return;
        }
        getGisCoordinateFacade().edit(current.getCoordinate());//save coordinate
        getEjbFacade().edit(current);//save family details
    }

    public DiagnosisCase getCurrent() {
        if (current == null) {
            current = new DiagnosisCase();
            GisCoordinate c = new GisCoordinate();
            c.setLatitude(6.0350);
            c.setLongtide(80.2158);
            current.setCoordinate(c);
        }
        return current;
    }

    public void setCurrent(DiagnosisCase current) {
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

    private DiagnosisCaseFacade getFacade() {
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
        current = new DiagnosisCase();
    }

    public String listMyAreaCases() {
        // String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a)  or i.phiArea.superArea=:a or i.phiArea.superArea.superArea=:a or i.phiArea.superArea.superArea.superArea=:a or i.phiArea.superArea.superArea.superArea.superArea=:a)";
        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a)  ";
        Map m = new HashMap();
        System.out.println("sql = " + sql);

        m.put("a", getSessionController().getArea());
        System.out.println("m = " + m);
        items = getEjbFacade().findBySQL(sql, m);

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }
        allFamiliesModel = new DefaultMapModel();
        for (DiagnosisCase f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                allFamiliesModel.addOverlay(marker);

            }
        }
        defaultCoordinate = getGisEjb().getCentre(allFamiliesModel);
        return "area_diagnosis_cases";
    }

    public String listMyAreaCasesSummary() {
        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) and i.diagnosis=:d";
        Map m = new HashMap();
        m.put("a", getSessionController().getArea());
        m.put("d", diagnosis);
        items = getEjbFacade().findBySQL(sql, m);

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }

        count = items.size();

        return "area_diagnosis_cases_by_duration_count_summary";
    }

    private Area area;
    
    
    
    public String listMyAreaCasesByCase() {
        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) and i.diagnosis=:d";
        Map m = new HashMap();
        m.put("a", getSessionController().getArea());
        m.put("d", diagnosis);
        items = getEjbFacade().findBySQL(sql, m);

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }
        allFamiliesModel = new DefaultMapModel();
        for (DiagnosisCase f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                allFamiliesModel.addOverlay(marker);

            }
        }
        defaultCoordinate = getGisEjb().getCentre(allFamiliesModel);
        return "area_diagnosis_cases_by_case";
    }

     public String listMyAreaCasesByCaseArea() {
        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) and i.diagnosis=:d";
        Map m = new HashMap();
        m.put("a", area );
        m.put("d", diagnosis);
        items = getEjbFacade().findBySQL(sql, m);

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }
        allFamiliesModel = new DefaultMapModel();
        for (DiagnosisCase f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                allFamiliesModel.addOverlay(marker);

            }
        }
        defaultCoordinate = getGisEjb().getCentre(allFamiliesModel);
        return "area_diagnosis_cases_by_case_phm";
    }

    public String listMyAreaCasesByCaseByDate() {
        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) and i.diagnosis=:d and i.createdAt between :df and :dt";
        Map m = new HashMap();
        m.put("a", getSessionController().getArea());
        m.put("d", diagnosis);
        m.put("df", fromDate);
        m.put("dt", toDate);
        items = getEjbFacade().findBySQL(sql, m);
        count = items.size();

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }
        allFamiliesModel = new DefaultMapModel();
        for (DiagnosisCase f : items) {
            if (f.getCoordinate() != null) {
                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
                allFamiliesModel.addOverlay(marker);

            }
        }
        defaultCoordinate = getGisEjb().getCentre(allFamiliesModel);
        return "area_diagnosis_cases_by_duration";
    }

    public String listMyAreaCasesByCaseByDateCount() {

        String sql = "SELECT i FROM DiagnosisCase i where i.retired=false and (i.phmArea=:a or i.phmArea.superArea=:a or i.phmArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea=:a or i.phmArea.superArea.superArea.superArea.superArea=:a) and i.diagnosis=:d and i.createdAt between :df and :dt ";
        Map m = new HashMap();
        m.put("a", getSessionController().getArea());
        m.put("d", diagnosis);
        m.put("df", fromDate);
        m.put("dt", toDate);
        items = getEjbFacade().findBySQL(sql, m);
        count = items.size();

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }

//        allFamiliesModel = new DefaultMapModel();
//        for (DiagnosisCase f : items) {
//            if (f.getCoordinate() != null) {
//                Marker marker = new Marker(new LatLng(f.getCoordinate().getLatitude(), f.getCoordinate().getLongtide()), f.getAddress());
//                allFamiliesModel.addOverlay(marker);
//
//            }
//        }
        //defaultCoordinate = getGisEjb().getCentre(allFamiliesModel);
        return "area_diagnosis_cases_by_duration";
    }

    public String listMyAreaCasesByCaseByDateCountSummary() {

        String sql = "select i.name,count(d.diagnosis_id) from item i,DiagnosisCase d where i.dtype='Diagnosis' group by i.name ";
        Map m = new HashMap();
//        m.put("a", getSessionController().getArea());
//        m.put("d",diagnosis);
//        m.put("df", fromDate);
//        m.put("dt", toDate);

        count = items.size();

        if (items == null) {
            items = new ArrayList<DiagnosisCase>();
        }

//       
        return "area_diagnosis_cases_by_duration";
    }

    @EJB
    GisEjb gisEjb;

    public GisEjb getGisEjb() {
        return gisEjb;
    }

    public void setGisEjb(GisEjb gisEjb) {
        this.gisEjb = gisEjb;
    }

    public List<DiagnosisCase> getItems() {
        return items;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataList getDl() {
        return dl;
    }

    public void setDl(DataList dl) {
        this.dl = dl;
    }

    public List<DiagnosisCaseController> getDcist() {
        return dcist;
    }

    public void setDcist(List<DiagnosisCaseController> dcist) {
        this.dcist = dcist;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
