/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.Consultant;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * 
 */
@Stateless
public class ConsultantFacade extends AbstractFacade<Consultant> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultantFacade() {
        super(Consultant.class);
    }
    
}
