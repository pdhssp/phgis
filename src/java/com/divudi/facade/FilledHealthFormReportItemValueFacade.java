/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.facade;

import com.divudi.entity.form.FilledHealthFormReportItemValue;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Buddhika
 */
@Stateless
public class FilledHealthFormReportItemValueFacade extends AbstractFacade<FilledHealthFormReportItemValue> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FilledHealthFormReportItemValueFacade() {
        super(FilledHealthFormReportItemValue.class);
    }
    
}
