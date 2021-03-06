package gov.sp.health.bean;

import gov.sp.health.data.AreaType;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.entity.Area;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.omg.CORBA.Current;

@Named
@SessionScoped
public class AreaController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private AreaFacade ejbFacade;
    List<Area> selectedItems;
    private Area current;
    private List<Area> items = null;
    String selectText = "";

    public List<Area> getSelectedItems() {
        selectedItems = getFacade().findBySQL("select c from Area c where c.retired=false and upper(c.name) like '%" + getSelectText().toUpperCase() + "%' order by c.name");
        return selectedItems;
    }

    public List<Area> completeArea(String qry) {
        List<Area> a = null;
        if (qry != null) {
            a = getFacade().findBySQL("select c from Area c where c.retired=false and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name");
        }
        if (a == null) {
            a = new ArrayList<Area>();
        }
        return a;
    }

    public void prepareAdd() {
        current = new Area();
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
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("updated Successfully");
        } else {
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

    public AreaController() {
    }

    public Area getCurrent() {
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

    public Boolean checkCurrent() {
        if (current == null) {
            UtilityController.addSuccessMessage("Nothing To Delete");
            return false;
        } else {
            return true;
        }

    }

    
    
    public List<Area> getMyPhmAres() {
        String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phm", AreaType.PhmArea);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a.superArea.superArea.superArea.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a.superArea.superArea.superArea=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a.superArea.superArea=:ma";
                break;

            case PhiArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a.superArea=:ma";
                break;

            case PhmArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phm and a=:ma";
                break;
            default:
                 sql = "select a from Area a where a.retired=false and a.areaType=:phm";
        }
        return getFacade().findBySQL(sql, m);
    }

    
   
    
    
    
    public List<Area> getMyAres() {
        String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
       
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
                // sql = "select a from Area a where a.retired=false  and a.superArea=:ma";
               
                break;

            case District:
                sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.superArea.superArea=:ma";
                break;

            case PhiArea:
                sql = "select a from Area a where a.retired=false and a.superArea=:ma";
                break;

            case PhmArea:
                sql = "select a from Area a where a.retired=false  and a=:ma";
                break;
            default:
                 sql = "select a from Area a where a.retired=false ";
        }
        return getFacade().findBySQL(sql, m);
    }
    
    
     public List<Area> getMyProvinceAres() {
        String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();      
                 
                //sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
         sql = "select a from Area a where a.retired=false  and a.superArea.areaType<>=:ma";
               
               m.put("ma", getSessionController().getArea());
        return getFacade().findBySQL(sql, m);
    }
     
     public List<Area> getMyDistrictAres() {
        String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();      
                 
                //sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
         sql = "select a from Area a where a.retired=false  and a.superArea.superArea=:ma";
               
               m.put("ma", getSessionController().getArea());
        return getFacade().findBySQL(sql, m);
    }
     
     
      public List<Area> getMyDistrictAresNew() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.MohArea);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case PhiArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;

            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
     
     public List<Area> getMyMohAres() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.MohArea);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;

            
            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
     
     
//          public List<Area> getMyPhiAres() {
//        String sql;
//        Map m = new HashMap();
//        AreaType at = getSessionController().getArea().getAreaType();      
//                 
//                //sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
//         sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
//               
//               m.put("ma", getSessionController().getArea());
//        return getFacade().findBySQL(sql, m);
//    }
      public List<Area> getMyPhiAres() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.PhiArea);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case PhiArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;
                
          

            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
     
     
       public List<Area> getMyPhmmAres() {
        String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();      
                 
                //sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea=:ma";
         sql = "select a from Area a where a.retired=false  and a.superArea.superArea.superArea.superArea.superArea=:ma";
               
               m.put("ma", getSessionController().getArea());
        return getFacade().findBySQL(sql, m);
    }
       
       public List<Area> getMyRdhsAres() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.District);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;

                       
            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
       
              public List<Area> getMyPdhsAres() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.Province);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;

          

                       
            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
              
                 public List<Area> getMyPhmAresToAll() {
       String sql;
        Map m = new HashMap();
        AreaType at = getSessionController().getArea().getAreaType();
        m.put("phi", AreaType.PhmArea);
        m.put("ma", getSessionController().getArea());
        switch (at) {
            case Country:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea.superArea=:ma";
                break;

            case District:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea.superArea=:ma";
                break;

            case MohArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea.superArea=:ma";
                break;

            case PhiArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a.superArea=:ma";
                break;
            case PhmArea:
                sql = "select a from Area a where a.retired=false and a.areaType=:phi and a=:ma";
                break;
                
          

            
            default:
                 return null;
        }
        return getFacade().findBySQL(sql, m);
    }
     
     
    private AreaFacade getFacade() {
        return ejbFacade;
    }

    public List<Area> getItems() {
        items = getFacade().findAll("name", true);
        return items;
    }

    /**
     *
     */
    @FacesConverter("areaCon")
    public static class AreaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AreaController controller = (AreaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "areaController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            java.lang.Long key;

            try {
                key = Long.valueOf(value);
            } catch (Exception ee) {
                key = 0l;
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
                        + object.getClass().getName() + "; expected type: " + AreaController.class.getName());
            }
        }
    }
}
