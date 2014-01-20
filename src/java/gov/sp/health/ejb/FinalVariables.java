/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.ejb;

import javax.ejb.Stateless;

/**
 *
 * @author safrin
 */
@Stateless
public class FinalVariables {

    public Integer getSessionSessionDayCounter() {
        return 5;
    }

    public double getCahnnelingDurationMinute() {
        return 10.0;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
