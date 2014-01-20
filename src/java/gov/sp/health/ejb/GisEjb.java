/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.ejb;

import gov.sp.health.entity.GisCoordinate;
import javax.ejb.Stateless;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Etreame IT
 */
@Stateless
public class GisEjb {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public GisCoordinate getCentre(MapModel mapModel){
        GisCoordinate upperLeft= new GisCoordinate();
        GisCoordinate lowerRight= new GisCoordinate();
        GisCoordinate centre= new GisCoordinate();
        upperLeft.setLongtide(80.58);
        upperLeft.setLatitude(6.054481);
        lowerRight.setLongtide(80.58);
        lowerRight.setLatitude(6.054481);
        
        for(Marker m :mapModel.getMarkers()){
            LatLng ll = m.getLatlng();
            if(upperLeft.getLongtide() > ll.getLng()){
                upperLeft.setLongtide(ll.getLng());
            }
            if(upperLeft.getLatitude()<ll.getLat()){
                upperLeft.setLatitude(ll.getLat());
            }
            if(lowerRight.getLongtide() < ll.getLng()){
                lowerRight.setLongtide(ll.getLng());
            }
            if(lowerRight.getLatitude()>ll.getLat()){
                lowerRight.setLatitude(ll.getLat());
            }
        }
        
        centre.setLatitude((lowerRight.getLatitude() + upperLeft.getLatitude())/2);
        centre.setLongtide((lowerRight.getLatitude() + upperLeft.getLatitude())/2);
        return centre;
    }
    
}
