/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.PatientEncounter;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * 
 */
@Stateless
public class PatientEncounterFacade extends AbstractFacade<PatientEncounter> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PatientEncounterFacade() {
        super(PatientEncounter.class);
    }
    
}
