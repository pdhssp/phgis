/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.form.HealthFormItemValueFlag;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Buddhika
 */
@Stateless
public class InvestigationItemValueFlagFacade extends AbstractFacade<HealthFormItemValueFlag> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvestigationItemValueFlagFacade() {
        super(HealthFormItemValueFlag.class);
    }
    
}
