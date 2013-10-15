
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.entity;

import com.divudi.data.BillType;
import com.divudi.data.InwardChargeType;
import com.divudi.data.SessionNumberType;
import com.divudi.entity.form.HealthFormItem;
import com.divudi.entity.form.ReportItem;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author buddhika
 */
@Entity
public class Item implements Serializable {

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    List<HealthFormItem> reportItems;
    
    
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Main Properties
    Long id;

    
    @ManyToOne
    Category category;
    Double total = 0.0;
    Boolean discountAllowed = false;
    @ManyToOne
    Institution institution;
    @ManyToOne
    Department department;
    @ManyToOne
    Speciality speciality;
    @ManyToOne
    Staff staff;
    @ManyToOne
    Institution forInstitution;
    @ManyToOne
    Department forDepartment;
    @Enumerated(EnumType.STRING)
    BillType forBillType;
    @ManyToOne
    Item billedAs;
    @ManyToOne
    Item reportedAs;
    String name;
    String sname;
    String tname;
    String code;
    String printName;
    String shortName;
    String fullName;
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
    @ManyToOne
    Item parentItem;
    boolean userChangable;
    @Enumerated(EnumType.STRING)
    InwardChargeType inwardChargeType;
    private Double dblValue = 0.0;
    private SessionNumberType sessionNumberType;
    boolean priceByBatch;
    
    @ManyToOne
    Category worksheet;
    @ManyToOne
    Category reportFormat;
    boolean billable;
    boolean formatable;

    public boolean isFormatable() {
        return formatable;
    }

    public void setFormatable(boolean formatable) {
        this.formatable = formatable;
    }

    
    
    public Category getWorksheet() {
        return worksheet;
    }

    public void setWorksheet(Category worksheet) {
        this.worksheet = worksheet;
    }

    public Category getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(Category reportFormat) {
        this.reportFormat = reportFormat;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }


    public boolean isPriceByBatch() {
        return priceByBatch;
    }

    public void setPriceByBatch(boolean priceByBatch) {
        this.priceByBatch = priceByBatch;
    }

    public Item() {
        
    }
    
    

    public SessionNumberType getSessionNumberType() {
        return sessionNumberType;
    }

    public void setSessionNumberType(SessionNumberType sessionNumberType) {
        this.sessionNumberType = sessionNumberType;
    }

    public InwardChargeType getInwardChargeType() {
        return inwardChargeType;
    }

    public void setInwardChargeType(InwardChargeType inwardChargeType) {
        this.inwardChargeType = inwardChargeType;
    }

    public List<HealthFormItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<HealthFormItem> reportItems) {
        this.reportItems = reportItems;
    }

    public Item getBilledAs() {
        return billedAs;
    }

    public void setBilledAs(Item billedAs) {
        this.billedAs = billedAs;
    }

    public Item getReportedAs() {
        return reportedAs;
    }

    public void setReportedAs(Item reportedAs) {
        this.reportedAs = reportedAs;
    }

    public Item getParentItem() {
        return parentItem;
    }

    public void setParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.divudi.entity.Item[ id=" + id + " ]";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getDiscountAllowed() {
        if (discountAllowed == null) {
            discountAllowed = false;
        }
        return discountAllowed;
    }

    public void setDiscountAllowed(Boolean discountAllowed) {
        this.discountAllowed = discountAllowed;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Institution getForInstitution() {
        return forInstitution;
    }

    public void setForInstitution(Institution forInstitution) {
        this.forInstitution = forInstitution;
    }

    public Department getForDepartment() {
        return forDepartment;
    }

    public void setForDepartment(Department forDepartment) {
        this.forDepartment = forDepartment;
    }

    public BillType getForBillType() {
        return forBillType;
    }

    public void setForBillType(BillType forBillType) {
        this.forBillType = forBillType;
    }

    public boolean isUserChangable() {
        return userChangable;
    }

    public void setUserChangable(boolean userChangable) {
        this.userChangable = userChangable;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDblValue() {
        return dblValue;
    }

    public void setDblValue(Double dblValue) {
        this.dblValue = dblValue;
    }
    
    @Transient
    private double hospitalFee;
    @Transient
    private double professionalFee;
    @Transient
    private double hospitalFfee;
    @Transient
    private double professionalFfee;

    public double getHospitalFee() {
        return hospitalFee;
    }

    public void setHospitalFee(double hospitalFee) {
        this.hospitalFee = hospitalFee;
    }

    public double getProfessionalFee() {
        return professionalFee;
    }

    public void setProfessionalFee(double professionalFee) {
        this.professionalFee = professionalFee;
    }

    public double getHospitalFfee() {
        return hospitalFfee;
    }

    public void setHospitalFfee(double hospitalFfee) {
        this.hospitalFfee = hospitalFfee;
    }

    public double getProfessionalFfee() {
        return professionalFfee;
    }

    public void setProfessionalFfee(double professionalFfee) {
        this.professionalFfee = professionalFfee;
    }
}
