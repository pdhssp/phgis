/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.WebUserPaymentScheme;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Buddhika
 */
@Stateless
public class WebUserPaymentSchemeFacade extends AbstractFacade<WebUserPaymentScheme> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WebUserPaymentSchemeFacade() {
        super(WebUserPaymentScheme.class);
    }
    
}
