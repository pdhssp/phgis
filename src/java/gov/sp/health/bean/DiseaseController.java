/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Disease;
import gov.sp.health.facade.DiseaseFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Etreame IT
 */
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
    
    
    
}
