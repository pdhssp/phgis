/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.CancelledBill;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Buddhika
 */
@Stateless
public class CancelledBillFacade extends AbstractFacade<CancelledBill> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CancelledBillFacade() {
        super(CancelledBill.class);
    }
    
}
