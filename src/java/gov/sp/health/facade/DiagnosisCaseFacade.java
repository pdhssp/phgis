/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.DiagnosisCase;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Etreame IT
 */
@Stateless
public class DiagnosisCaseFacade extends AbstractFacade<DiagnosisCase> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DiagnosisCaseFacade() {
        super(DiagnosisCase.class);
    }
    
}
