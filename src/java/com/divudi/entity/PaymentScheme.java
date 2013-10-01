/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity;

import com.divudi.data.CliantType;
import com.divudi.data.PaymentMethod;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author buddhika
 */
@Entity
public class PaymentScheme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Main Properties
    private Long id;
    String name;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private CliantType cliantType;
    private Double discountPercentForLabBill=0.0;
    private Double discountPercentForOpdBill=0.0;
    private Double discountPercentForChannelingBill=0.0;
    @ManyToOne
    private Institution institution;
    @ManyToOne
    private Person person;
    //Created Properties
    @ManyToOne
    private WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;
    //Retairing properties
    private boolean retired;
    @ManyToOne
    private WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date retiredAt;
    private String retireComments;

    
    boolean validForPayments;
    boolean validForBilledBills;
    boolean validForCrBills;

    public boolean isValidForPayments() {
        return validForPayments;
    }

    public void setValidForPayments(boolean validForPayments) {
        this.validForPayments = validForPayments;
    }

    public boolean isValidForBilledBills() {
        return validForBilledBills;
    }

    public void setValidForBilledBills(boolean validForBilledBills) {
        this.validForBilledBills = validForBilledBills;
    }

    public boolean isValidForCrBills() {
        return validForCrBills;
    }

    public void setValidForCrBills(boolean validForCrBills) {
        this.validForCrBills = validForCrBills;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentScheme)) {
            return false;
        }
        PaymentScheme other = (PaymentScheme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.divudi.entity.PaymentSheme[ id=" + id + " ]";
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CliantType getCliantType() {
        return cliantType;
    }

    public void setCliantType(CliantType cliantType) {
        this.cliantType = cliantType;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public Double getDiscountPercentForLabBill() {
        return discountPercentForLabBill;
    }

    public void setDiscountPercentForLabBill(Double discountPercentForLabBill) {
        this.discountPercentForLabBill = discountPercentForLabBill;
    }

    public Double getDiscountPercentForOpdBill() {
        return discountPercentForOpdBill;
    }

    public void setDiscountPercentForOpdBill(Double discountPercentForOpdBill) {
        this.discountPercentForOpdBill = discountPercentForOpdBill;
    }

    public Double getDiscountPercentForChannelingBill() {
        return discountPercentForChannelingBill;
    }

    public void setDiscountPercentForChannelingBill(Double discountPercentForChannelingBill) {
        this.discountPercentForChannelingBill = discountPercentForChannelingBill;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
