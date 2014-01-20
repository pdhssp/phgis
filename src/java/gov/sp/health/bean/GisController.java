/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Area;
import gov.sp.health.entity.GisCoordinate;
import gov.sp.health.facade.AreaFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;


@Named(value = "gisController")
@SessionScoped
public class GisController implements Serializable {

    /**
     * Creates a new instance of GisController
     */
    public GisController() {
        emptyModel = new DefaultMapModel();  
    }
    
    
    Area province;
    Area district;
    Area moh;
    Area phi;
    Area phm;
    List<Area> districts;
    List<Area> mohs;
    List<Area> phis;
    List<Area> phms;
    @EJB
    AreaFacade areaFacade;
    MapModel mapModel;
    
        private MapModel emptyModel;  
      
    private String title;  
      
    private double lat;  
      
    private double lng;  
  
     
      
    public MapModel getEmptyModel() {  
        return emptyModel;  
    }  
      
    public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
      
    public String getTitle() {  
        return title;  
    }  
  
    public void setTitle(String title) {  
        this.title = title;  
    }  
  
    public double getLat() {  
        return lat;  
    }  
  
    public void setLat(double lat) {  
        this.lat = lat;  
    }  
  
    public double getLng() {  
        return lng;  
    }  
  
    public void setLng(double lng) {  
        this.lng = lng;  
    }  
      
    public void addMarker(ActionEvent actionEvent) {  
        Marker marker = new Marker(new LatLng(lat, lng), title);  
        emptyModel.addOverlay(marker);  
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));  
    } 
    

    public MapModel getProvinceMapModel() {
        mapModel = new DefaultMapModel();
        Polygon polygon = new Polygon();
        for (Area d : districts) {
            if (d.getCordinates() != null) {
                for (GisCoordinate c : d.getCordinates()) {
                    LatLng l = new LatLng(c.getLatitude(), c.getLongtide());
                    System.out.println("map");
                    polygon.getPaths().add(l);

                }
            }
            polygon.setStrokeColor("#FF9900");
            polygon.setFillColor("#FF9900");
            polygon.setStrokeOpacity(0.7);
            polygon.setFillOpacity(0.7);
            mapModel.addOverlay(polygon);
        }
        
        return mapModel;
    }

    public AreaFacade getAreaFacade() {
        return areaFacade;
    }

    public void setAreaFacade(AreaFacade areaFacade) {
        this.areaFacade = areaFacade;
    }

    public Area getProvince() {
        return province;
    }

    public void setProvince(Area province) {
        this.province = province;
    }

    public Area getDistrict() {
        return district;
    }

    public void setDistrict(Area district) {
        this.district = district;
    }

    public Area getMoh() {
        return moh;
    }

    public void setMoh(Area moh) {
        this.moh = moh;
    }

    public Area getPhi() {
        return phi;
    }

    public void setPhi(Area phi) {
        this.phi = phi;
    }

    public List<Area> getDistricts() {
        if (province == null) {
            return new ArrayList<Area>();
        }
        String jql = "select a from Area a where a.superArea = :s and a.retired = false order by a.name";
        Map m = new HashMap();
        m.put("s", province);       
        districts = getAreaFacade().findBySQL(jql, m);
        return districts;
    }

    public void setDistricts(List<Area> districts) {
        this.districts = districts;
    }

    public List<Area> getMohs() {
        if (district == null) {
            return new ArrayList<Area>();
        }
        String jql = "select a from Area a where a.superArea = :s and a.retired = false order by a.name";
        Map m = new HashMap();
        m.put("s", district);
        mohs = getAreaFacade().findBySQL(jql, m);
        return mohs;
    }

    public void setMohs(List<Area> mohs) {
        this.mohs = mohs;
    }

    public List<Area> getPhis() {
        if (moh == null) {
            return new ArrayList<Area>();
        }
        String jql = "select a from Area a where a.superArea = :s and a.retired = false order by a.name";
        Map m = new HashMap();
        m.put("s", moh);
        phis = getAreaFacade().findBySQL(jql, m);
        return phis;
    }

    public void setPhis(List<Area> phis) {
        this.phis = phis;
    }
    
    
    
}
