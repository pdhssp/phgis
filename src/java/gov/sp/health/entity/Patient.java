/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.entity;

import gov.sp.health.data.Title;
import gov.sp.health.ejb.CommonFunctions;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * 
 */
@Entity
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //Main Properties
    private Long id;
    @ManyToOne
    private Person person;
    //personaI dentification Number
    private Integer pinNo;
    //healthdentification Number
    private Integer hinNo;
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
    @Transient
    String age;
    
    

    public String getAge() {
        System.out.println("getting age");
        if (person == null) {
            System.out.println("patient is null");
            age="";
            return age;
        }
        if (person.getDob() == null) {
            System.out.println("dob is null");
            age = "";
            return age;
        }
        Date dob = person.getDob();
        Date toDate = Calendar.getInstance().getTime();
        if (dob == null || toDate == null) {
            age = "";
            return age;
        }
        long ageInDays;
        
        ageInDays = (toDate.getTime() - dob.getTime()) / (1000 * 60 * 60 * 24);
        System.out.println("age in days is " + ageInDays);
        if (ageInDays < 60) {
            age= ageInDays + " days";
        } else if (ageInDays < 366) {
            age= (ageInDays / 30) + " months";
        } else {
            age= (ageInDays / 365) + " years";
        }
        System.out.println("age is " + age);
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
        if (!(object instanceof Patient)) {
            return false;
        }
        Patient other = (Patient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.sp.health.entity.Patient[ id=" + id + " ]";
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getPinNo() {
        return pinNo;
    }

    public void setPinNo(Integer pinNo) {
        this.pinNo = pinNo;
    }

    public Integer getHinNo() {
        return hinNo;
    }

    public void setHinNo(Integer hinNo) {
        this.hinNo = hinNo;
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
}
