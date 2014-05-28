
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import gov.sp.health.data.HealthFormItemType;
import gov.sp.health.data.HealthFormItemValueType;
import gov.sp.health.data.SessionNumberType;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.entity.form.ReportItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 *
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
    @ManyToOne
    Institution institution;
    @ManyToOne
    Speciality speciality;
    @ManyToOne
    Staff staff;
    @ManyToOne
    Institution forInstitution;
    @ManyToOne
    Item billedAs;
    @ManyToOne
    Item reportedAs;
    String name;
    String sname;
    String tname;
    @Column(unique = true, nullable = false)   
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
    private Double dblValue = 0.0;
    private SessionNumberType sessionNumberType;
    @ManyToOne
    Category reportFormat;
    boolean billable;
    boolean formatable;
    @Lob
    String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isFormatable() {
        return formatable;
    }

    public void setFormatable(boolean formatable) {
        this.formatable = formatable;
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

    public Item() {
    }

    public SessionNumberType getSessionNumberType() {
        return sessionNumberType;
    }

    public void setSessionNumberType(SessionNumberType sessionNumberType) {
        this.sessionNumberType = sessionNumberType;
    }

    public List<HealthFormItem> getReportItems() {
        if (reportItems == null) {
            reportItems = new ArrayList<HealthFormItem>();
        }
        return reportItems;
    }

     public List<HealthFormItem> getReportItemsForGraphs() {
        getReportItems();
        List<HealthFormItem> gris=new ArrayList<HealthFormItem>();
        for(HealthFormItem i : reportItems){
            if(i.getHealthFormItemType()==HealthFormItemType.Value && i.getHealthFormItemValueType()==HealthFormItemValueType.Double ){
                gris.add(i);
            }
        }
        return gris;
    }
    
     public List<HealthFormItem> getReportItemsForSummary() {
        getReportItems();
        List<HealthFormItem> gris=new ArrayList<HealthFormItem>();
        for(HealthFormItem i : reportItems){
            if(i.getHealthFormItemType()==HealthFormItemType.Value ){
                gris.add(i);
            }
        }
        return gris;
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
        return "gov.sp.health.entity.Item[ id=" + id + " ]";
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

    public boolean isUserChangable() {
        return userChangable;
    }

    public void setUserChangable(boolean userChangable) {
        this.userChangable = userChangable;
    }

    public Double getDblValue() {
        return dblValue;
    }

    public void setDblValue(Double dblValue) {
        this.dblValue = dblValue;
    }
}
