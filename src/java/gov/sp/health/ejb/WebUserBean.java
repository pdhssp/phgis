/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.ejb;

import gov.sp.health.entity.WebUser;
import javax.ejb.Stateful;

/**
 *
 * @author Buddhika
 */
@Stateful
public class WebUserBean {

    WebUser loggedUser;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public WebUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(WebUser loggedUser) {
        this.loggedUser = loggedUser;
    }
}
