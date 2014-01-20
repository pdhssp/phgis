/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.data;

import gov.sp.health.entity.GisCoordinate;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;


@Named(value = "defaultsBean")
@ApplicationScoped
public class DefaultsBean {
    GisCoordinate defaultCoordinate;

    public GisCoordinate getDefaultCoordinate() {
        return defaultCoordinate;
    }

    public void setDefaultCoordinate(GisCoordinate defaultCoordinate) {
        this.defaultCoordinate = defaultCoordinate;
    }
    
    
    
    /**
     * Creates a new instance of DefaultsBean
     */
    public DefaultsBean() {
        //6.0350° N, 80.2158
        defaultCoordinate.setLatitude(6.0350);
        defaultCoordinate.setLatitude(80.2158);
    }
}
