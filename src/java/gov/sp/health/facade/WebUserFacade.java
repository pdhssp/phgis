
package gov.sp.health.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import gov.sp.health.entity.WebUser;

@Stateless
public class WebUserFacade extends AbstractFacade<WebUser> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WebUserFacade() {
        super(WebUser.class);
    }
    
}
