/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import gov.sp.health.data.InstitutionType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * 
 */
@Entity
public class Institution implements Serializable {

    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Main Properties   
    Long id;
    String institutionCode;
    String name;
    String address;
    String fax;
    String email;
    String phone;
    String mobile;
    String web;
    @Lob
    String labBillHeading;
    @Lob
    String pharmacyBillHeading;
    @Lob
    String radiologyBillHeading;
    @Lob
    String opdBillHeading;
    @Lob
    String cashierBillHeading;
    @Enumerated(EnumType.STRING)
    InstitutionType institutionType;
    //Created Properties
    @ManyToOne
    WebUser creater;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date createdAt;
    //Retairing properties
    boolean retired;
    @ManyToOne
    WebUser retirer;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date retiredAt;
    String retireComments;
    double labBillDiscount;
    double opdBillDiscount;
    double inwardDiscount;
    private double ballance;
    private double allowedCredit;

    public double getLabBillDiscount() {
        return labBillDiscount;
    }

    public void setLabBillDiscount(double labBillDiscount) {
        this.labBillDiscount = labBillDiscount;
    }

    public double getOpdBillDiscount() {
        return opdBillDiscount;
    }

    public void setOpdBillDiscount(double opdBillDiscount) {
        this.opdBillDiscount = opdBillDiscount;
    }

    public double getInwardDiscount() {
        return inwardDiscount;
    }

    public void setInwardDiscount(double inwardDiscount) {
        this.inwardDiscount = inwardDiscount;
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
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.entity.Institution[ id=" + id + " ]";
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

    public String getLabBillHeading() {
        return labBillHeading;
    }

    public void setLabBillHeading(String labBillHeading) {
        this.labBillHeading = labBillHeading;
    }

    public String getPharmacyBillHeading() {
        return pharmacyBillHeading;
    }

    public void setPharmacyBillHeading(String pharmacyBillHeading) {
        this.pharmacyBillHeading = pharmacyBillHeading;
    }

    public String getRadiologyBillHeading() {
        return radiologyBillHeading;
    }

    public void setRadiologyBillHeading(String radiologyBillHeading) {
        this.radiologyBillHeading = radiologyBillHeading;
    }

    public String getOpdBillHeading() {
        return opdBillHeading;
    }

    public void setOpdBillHeading(String opdBillHeading) {
        this.opdBillHeading = opdBillHeading;
    }

    public String getCashierBillHeading() {
        return cashierBillHeading;
    }

    public void setCashierBillHeading(String cashierBillHeading) {
        this.cashierBillHeading = cashierBillHeading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public InstitutionType getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public double getBallance() {
        return ballance;
    }

    public void setBallance(double ballance) {
        this.ballance = ballance;
    }

    public double getAllowedCredit() {
        return allowedCredit;
    }

    public void setAllowedCredit(double allowedCredit) {
        this.allowedCredit = allowedCredit;
    }
}
