
package gov.sp.health.entity;

import gov.sp.health.data.Sex;
import gov.sp.health.data.Title;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
 *  
 *  )
 */
@Entity
public class Person implements Serializable {
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String name;
    String description;
    private String nic;
    private String address;
    private String fax;
    private String email;
    private String website;
    private String mobile;
    private String tName;
    private String sName;
    private String phone;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dob;
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
    private Area area;
    @ManyToOne
    private Institution institution;

    @Enumerated(EnumType.STRING)
    private Title title;
    @Enumerated(EnumType.STRING)
    Sex sex;
    @Transient
    String nameWithTitle;
    boolean foreigner = false;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Family family;

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }
    
    
    
    
    public boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(boolean foreigner) {
        this.foreigner = foreigner;
    }

    public String getNameWithTitle() {
        String temT = "";
        Title t;
        if (getName() != null) {
            t = getTitle();
            if (t == Title.Baby) {
                temT = "Baby ";
            } else if (t == Title.Dr) {
                temT = "Dr. ";
            } else if (t == Title.DrMiss) {
                temT = "Dr(Miss). ";
            } else if (t == Title.DrMrs) {
                temT = "Dr(Mrs). ";
            } else if (t == Title.DrMs) {
                temT = "Dr(Ms). ";
            } else if (t == Title.Hon) {
                temT = "Hon. ";
            } else if (t == Title.Master) {
                temT = "Master. ";
            } else if (t == Title.Miss) {
                temT = "Miss. ";
            } else if (t == Title.Mr) {
                temT = "Mr. ";
            } else if (t == Title.Mrs) {
                temT = "Mrs. ";
            } else if (t == Title.Ms) {
                temT = "Ms. ";
            } else if (t == Title.Prof) {
                temT = "Prof. ";
            } else if (t == Title.Rev) {
                temT = "Rev. ";
            } else if (t == Title.RtHon) {
                temT = "Rt. Hon. ";
            } else if (t == Title.RtRev) {
                temT = "Rt. Rev. ";
            } else {
                temT = "";
            }
            
            nameWithTitle = temT + getName();
        }

       
        return nameWithTitle;
    }

    public void setNameWithTitle(String nameWithTitle) {
        this.nameWithTitle = nameWithTitle.toUpperCase();
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getRetireComments() {
        return retireComments;
    }

    public void setRetireComments(String retireComments) {
        this.retireComments = retireComments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public WebUser getCreater() {
        return creater;
    }

    public void setCreater(WebUser creater) {
        this.creater = creater;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public boolean isRetired() {
        return retired;
    }

    public void setRetired(boolean retired) {
        this.retired = retired;
    }

    public Date getRetiredAt() {
        return retiredAt;
    }

    public void setRetiredAt(Date retiredAt) {
        this.retiredAt = retiredAt;
    }

    public WebUser getRetirer() {
        return retirer;
    }

    public void setRetirer(WebUser retirer) {
        this.retirer = retirer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address.toUpperCase();
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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



    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
}
