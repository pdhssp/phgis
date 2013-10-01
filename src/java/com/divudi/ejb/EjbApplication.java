/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.ejb;

import com.divudi.entity.CancelledBill;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author Buddhika
 */
@Singleton
public class EjbApplication {

    List<CancelledBill> billsToCancel;
    
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public List<CancelledBill> getBillsToCancel() {
        if(billsToCancel==null){
            billsToCancel=new ArrayList<CancelledBill>();
        }
        
        return billsToCancel;
    }

    public void setBillsToCancel(List<CancelledBill> billsToCancel) {
        this.billsToCancel = billsToCancel;
    }

}
