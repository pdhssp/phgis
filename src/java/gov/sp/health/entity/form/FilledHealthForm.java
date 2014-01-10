/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity.form;

import gov.sp.health.entity.Area;
import gov.sp.health.entity.Family;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.Item;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.WebUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * 
 */
@Entity
public class FilledHealthForm implements Serializable {

    @OneToMany(mappedBy = "filledHealthFormReport", cascade = CascadeType.ALL,fetch= FetchType.EAGER)
    private List<FilledHealthFormItemValue> filledHealthFormReportItemValue;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Item item;
    @ManyToOne
    private FilledHealthForm patientInvestigation;
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
    //DataEntry
    private Boolean dataEntered = false;
    @ManyToOne
    private WebUser dataEntryUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataEntryAt;
    private String dataEntryComments;

    @ManyToOne
    private Institution dataEntryInstitution;
    //Approve
    private Boolean approved = false;
    @ManyToOne
    private WebUser approveUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date approveAt;
    private String approveComments;

    @ManyToOne
    private Institution approveInstitution;
    //Printing
    private Boolean printed = false;
    @ManyToOne
    private WebUser printingUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date printingAt;
    private String printingComments;
    @ManyToOne
    private Institution printingInstitution;
    //Cancellation
    private Boolean cancelled = false;
    @ManyToOne
    private WebUser cancelledUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date cancelledAt;
    private String cancellComments;
    @ManyToOne
    private Institution cancellInstitution;
    //Return
    private Boolean returned = false;
    @ManyToOne
    private WebUser returnedUser;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date returnedAt;
    private String returnComments;
    @ManyToOne
    private Institution returnInstitution;

    @ManyToOne
    Area area;
    @ManyToOne
    Institution institution;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date fromDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date toDate;
    int yearVal;
    int monthVal;
    int dateVal;
    int quarterVal;
    int weekVal;
    @ManyToOne
    Family family;
    @ManyToOne
    Person person;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getYearVal() {
        return yearVal;
    }

    public void setYearVal(int yearVal) {
        this.yearVal = yearVal;
    }

    public int getMonthVal() {
        return monthVal;
    }

    public void setMonthVal(int monthVal) {
        this.monthVal = monthVal;
    }

    public int getDateVal() {
        return dateVal;
    }

    public void setDateVal(int dateVal) {
        this.dateVal = dateVal;
    }

    public int getQuarterVal() {
        return quarterVal;
    }

    public void setQuarterVal(int quarterVal) {
        this.quarterVal = quarterVal;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    
    
    public List<FilledHealthFormItemValue> getFilledHealthFormReportItemValue() {
        
        if (filledHealthFormReportItemValue != null) {
            Collections.sort(filledHealthFormReportItemValue, new PatientReportItemValueComparator());
        }else{
            filledHealthFormReportItemValue = new ArrayList<FilledHealthFormItemValue>();
        }
        return filledHealthFormReportItemValue;
    }

    public void setFilledHealthFormReportItemValue(List<FilledHealthFormItemValue> filledHealthFormReportItemValue) {
        this.filledHealthFormReportItemValue = filledHealthFormReportItemValue;
    }

    public Boolean getDataEntered() {
        return dataEntered;
    }

    public void setDataEntered(Boolean dataEntered) {
        this.dataEntered = dataEntered;
    }

    public WebUser getDataEntryUser() {
        return dataEntryUser;
    }

    public void setDataEntryUser(WebUser dataEntryUser) {
        this.dataEntryUser = dataEntryUser;
    }

    public Date getDataEntryAt() {
        return dataEntryAt;
    }

    public void setDataEntryAt(Date dataEntryAt) {
        this.dataEntryAt = dataEntryAt;
    }

    public String getDataEntryComments() {
        return dataEntryComments;
    }

    public void setDataEntryComments(String dataEntryComments) {
        this.dataEntryComments = dataEntryComments;
    }



    public Institution getDataEntryInstitution() {
        return dataEntryInstitution;
    }

    public void setDataEntryInstitution(Institution dataEntryInstitution) {
        this.dataEntryInstitution = dataEntryInstitution;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public WebUser getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(WebUser approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveAt() {
        return approveAt;
    }

    public void setApproveAt(Date approveAt) {
        this.approveAt = approveAt;
    }

    public String getApproveComments() {
        return approveComments;
    }

    public void setApproveComments(String approveComments) {
        this.approveComments = approveComments;
    }



    public Institution getApproveInstitution() {
        return approveInstitution;
    }

    public void setApproveInstitution(Institution approveInstitution) {
        this.approveInstitution = approveInstitution;
    }

    public Boolean getPrinted() {
        return printed;
    }

    public void setPrinted(Boolean printed) {
        this.printed = printed;
    }

    public WebUser getPrintingUser() {
        return printingUser;
    }

    public void setPrintingUser(WebUser printingUser) {
        this.printingUser = printingUser;
    }

    public Date getPrintingAt() {
        return printingAt;
    }

    public void setPrintingAt(Date printingAt) {
        this.printingAt = printingAt;
    }

    public String getPrintingComments() {
        return printingComments;
    }

    public void setPrintingComments(String printingComments) {
        this.printingComments = printingComments;
    }



    public Institution getPrintingInstitution() {
        return printingInstitution;
    }

    public void setPrintingInstitution(Institution printingInstitution) {
        this.printingInstitution = printingInstitution;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public WebUser getCancelledUser() {
        return cancelledUser;
    }

    public void setCancelledUser(WebUser cancelledUser) {
        this.cancelledUser = cancelledUser;
    }

    public Date getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Date cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellComments() {
        return cancellComments;
    }

    public void setCancellComments(String cancellComments) {
        this.cancellComments = cancellComments;
    }


    public Institution getCancellInstitution() {
        return cancellInstitution;
    }

    public void setCancellInstitution(Institution cancellInstitution) {
        this.cancellInstitution = cancellInstitution;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public WebUser getReturnedUser() {
        return returnedUser;
    }

    public void setReturnedUser(WebUser returnedUser) {
        this.returnedUser = returnedUser;
    }

    public Date getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(Date returnedAt) {
        this.returnedAt = returnedAt;
    }

    public String getReturnComments() {
        return returnComments;
    }

    public void setReturnComments(String returnComments) {
        this.returnComments = returnComments;
    }

    public Institution getReturnInstitution() {
        return returnInstitution;
    }

    public void setReturnInstitution(Institution returnInstitution) {
        this.returnInstitution = returnInstitution;
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
        if (!(object instanceof FilledHealthForm)) {
            return false;
        }
        FilledHealthForm other = (FilledHealthForm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "gov.sp.health.entity.lab.PatientReport[ id=" + id + " ]";
//    }

    public FilledHealthForm getPatientInvestigation() {
        return patientInvestigation;
    }

    public void setPatientInvestigation(FilledHealthForm patientInvestigation) {
        this.patientInvestigation = patientInvestigation;
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

    public int getWeekVal() {
        return weekVal;
    }

    public void setWeekVal(int weekVal) {
        this.weekVal = weekVal;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    static class PatientReportItemValueComparator implements Comparator<FilledHealthFormItemValue> {

        @Override
        public int compare(FilledHealthFormItemValue o1, FilledHealthFormItemValue o2) {
            return o1.getHealthFormItem().getCssTop().compareTo(o2.getHealthFormItem().getCssTop());  //To change body of generated methods, choose Tools | Templates.
        }
    }
}
