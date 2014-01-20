
package gov.sp.health.bean;

import gov.sp.health.data.AreaType;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.GisCoordinate;
import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Polygon;

/**
 *
 *  
 *  )
 */
@Named
@SessionScoped
public class ProvinceController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private AreaFacade ejbFacade;
    List<Area> selectedItems;
    private Area current;
    private List<Area> items = null;
    String selectText = "";
    GisCoordinate coordinate;
    MapModel mapModel;

    public void displayMap(){
        getMapModel();
    }
    
    public MapModel getMapModel() {
        mapModel = new DefaultMapModel();
        Polygon polygon = new Polygon();

        if (current != null && current.getCordinates() != null) {
            for (GisCoordinate c : current.getCordinates()) {
                LatLng l = new LatLng(c.getLatitude(), c.getLongtide());
                polygon.getPaths().add(l);
            }
        }
        mapModel.addOverlay(polygon);
        return mapModel;
    }

    public GisCoordinate getCoordinate() {
        if (coordinate == null) {
            coordinate = new GisCoordinate();
        }

        return coordinate;
    }

    public void setCoordinate(GisCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void addCoordinate() {
        if (coordinate == null) {
            return;
        }
        if (current == null) {
            return;
        }
        current.getCordinates().add(coordinate);
        getFacade().edit(current);
        coordinate = null;
    }

    public void removeCoordinate() {
        if (coordinate == null) {
            return;
        }
        if (current == null) {
            return;
        }
        current.getCordinates().remove(coordinate);
        coordinate = null;
    }

    public List<Area> getSelectedItems() {
        Map m = new HashMap();
        m.put("t", AreaType.Province);
        selectedItems = getFacade().findBySQL("select c from Area c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' and c.areaType =:t order by c.name", m);
        return selectedItems;
    }

    public List<Area> completeArea(String qry) {
        List<Area> a = null;
        Map m = new HashMap();
        m.put("t", AreaType.Province);

        if (qry != null) {
            a = getFacade().findBySQL("select c from Area c where c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' and c.areaType = :t order by c.name");
        }
        if (a == null) {
            a = new ArrayList<Area>();
        }
        return a;
    }

    public void prepareAdd() {
        System.out.println("a");
        current = new Area();
        current.setAreaType(AreaType.Province);
    }

    public void setSelectedItems(List<Area> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {
        System.out.println("a");
        if(getCurrent()==null){
             UtilityController.addErrorMessage("Please Click Add Button ");
            return;
        }
        if(getCurrent().getName().equals(""))
        {
            UtilityController.addErrorMessage("Please Select Province Name");
            return;
        
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {

            current.setAreaType(AreaType.Province);
            getFacade().edit(current);
            UtilityController.addSuccessMessage("updated Successfully");
        } else {
            current.setAreaType(AreaType.Province);
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

    public AreaFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(AreaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ProvinceController() {
    }

    public Area getCurrent() {
        if (current == null) {
            current = new Area();
            current.setAreaType(AreaType.Province);
        }
        return current;
    }

    public void setCurrent(Area current) {
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

    private AreaFacade getFacade() {
        return ejbFacade;
    }

    public List<Area> getItems() {
        Map m = new HashMap();
        m.put("t", AreaType.Province);
        items = getFacade().findBySQL("select c from Area c where c.retired=false  and c.areaType =:t order by c.name", m);
        return items;
    }

    /**
     *
     */
    @FacesConverter("provinceCon")
    public static class ProvinceConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProvinceController controller = (ProvinceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "provinceController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            try
            {
                
            key = Long.valueOf(value);
            }
            catch(Exception ee){
                key= 0l;
            }
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
            if (object instanceof Area) {
                Area o = (Area) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + ProvinceController.class.getName());
            }
        }
    }
}
