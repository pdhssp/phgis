/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.data;

import gov.sp.health.entity.GisCoordinate;
import java.util.Comparator;

/**
 *
 * 
 */
public class AreaComparator implements Comparator<GisCoordinate> {

    @Override
    public int compare(GisCoordinate o1, GisCoordinate o2) {
        if (o1.getOrderNo() > o2.getOrderNo()) {
            return 1;
        } else {
            return -1;
        }
    }
}
