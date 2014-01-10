
package gov.sp.health.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import gov.sp.health.entity.WebTheme;


@Stateless
public class WebThemeFacade extends AbstractFacade<WebTheme> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WebThemeFacade() {
        super(WebTheme.class);
    }
    
}
