/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.facade;

import gov.sp.health.entity.GisCoordinate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Etreame IT
 */
@Stateless
public class GisCoordinateFacade extends AbstractFacade<GisCoordinate> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GisCoordinateFacade() {
        super(GisCoordinate.class);
    }
    
}
