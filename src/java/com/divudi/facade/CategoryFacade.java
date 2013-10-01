/*
 * Author
 * Dr. M H B Ariyaratne, MO(Health Information), email : buddhika.ari@gmail.com
 */
package com.divudi.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.divudi.entity.Category;

/**
 *
 * @author Dr. M H B Ariyaratne <buddhika.ari at gmail.com>
 */
@Stateless
public class CategoryFacade extends AbstractFacade<Category> {
    @PersistenceContext(unitName = "phgisPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(Category.class);
    }
    
}
