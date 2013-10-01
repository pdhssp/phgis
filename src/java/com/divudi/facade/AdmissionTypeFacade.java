/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.AdmissionType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author www.divudi.com
 */
@Stateless
public class AdmissionTypeFacade extends AbstractFacade<AdmissionType> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdmissionTypeFacade() {
        super(AdmissionType.class);
    }
}
