/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity.form;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Buddhika
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CommonReportItem extends ReportItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
}
