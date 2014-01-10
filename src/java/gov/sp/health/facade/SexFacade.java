/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.sp.health.facade;

import gov.sp.health.entity.Sex;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SexFacade extends AbstractFacade<Sex> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SexFacade() {
        super(Sex.class);
    }

}
