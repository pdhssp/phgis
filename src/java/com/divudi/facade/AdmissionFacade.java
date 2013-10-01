/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.Admission;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author buddhika
 */
@Stateless
public class AdmissionFacade extends AbstractFacade<Admission> {

    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdmissionFacade() {
        super(Admission.class);
    }

    public Long bhtBySql(String sql) {
        Query q = getEntityManager().createQuery(sql);
        return (Long) q.getSingleResult();
    }
}
