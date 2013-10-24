/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.form.HealthFormItemValue;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * 
 */
@Stateless
public class InvestigationItemValueFacade extends AbstractFacade<HealthFormItemValue> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvestigationItemValueFacade() {
        super(HealthFormItemValue.class);
    }
    
}
