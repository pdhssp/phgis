/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.Institution;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * 
 */
@Stateless
public class InstitutionFacade extends AbstractFacade<Institution> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InstitutionFacade() {
        super(Institution.class);
    }
    
}
