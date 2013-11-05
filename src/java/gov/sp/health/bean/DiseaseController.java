/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Disease;
import gov.sp.health.entity.Patient;
import gov.sp.health.facade.DiseaseFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;


@Named(value = "diseaseController")
@SessionScoped
public class DiseaseController implements Serializable {

    /**
     * Creates a new instance of DiseaseController
     */
    
    @EJB 
    private DiseaseFacade diseaseFacade;
    private Disease current;
    private  List<Disease> disease;
    
    
    public DiseaseController() {
    }

    public DiseaseFacade getDiseaseFacade() {
        return diseaseFacade;
    }

    public void setDiseaseFacade(DiseaseFacade diseaseFacade) {
        this.diseaseFacade = diseaseFacade;
    }

    public Disease getCurrent() {
        if(current==null)
            current=new Disease();
        return current;
    }

    public void setCurrent(Disease current) {
        this.current = current;
    }

    public List<Disease> getDisease() {
        disease=getDiseaseFacade().findAll();
        return disease;
    }

    public void setDisease(List<Disease> disease) {
        this.disease = disease;
    }
    
    public void saveDisease(){
        getDiseaseFacade().create(current);
    }
    
    public List<Disease> completeDisease(String query) {
        List<Disease> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Disease>();
        } else {
            sql = "select p from Disease p where p.retired=false and upper(p.Disease.name) like '%" + query.toUpperCase() + "%' order by p.Disease.name";
            System.out.println(sql);
            suggestions = getDiseaseFacade().findBySQL(sql);
        }
        return suggestions;
    }

     /*public void saveSelected() {
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getDiseaseFacade().edit(current);
            getDiseaseFacade().edit(getCurrent().getd));
            UtilityController.addSuccessMessage("savedOldSuccessfully");
        } else {
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            getFacade().create(current);
            UtilityController.addSuccessMessage("savedNewSuccessfully");
        }
        recreateModel();
        getItems();
    }*/
    
}
