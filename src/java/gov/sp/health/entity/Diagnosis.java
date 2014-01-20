/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

    
@Entity
@Inheritance
public class Diagnosis extends Item implements Serializable {
    
    double langitude;
    double latitude;
    @ManyToOne
    private GisCoordinate coordinate;
    @ManyToOne
    private Area phiArea;

    public GisCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GisCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Area getPhiArea() {
        return phiArea;
    }

    public void setPhiArea(Area phiArea) {
        this.phiArea = phiArea;
    }
    
    
}
