package gov.sp.health.entity.form;

import gov.sp.health.data.CalculationType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * 
 */
@Entity
public class FormCal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @ManyToOne
    HealthFormItem calIxItem;
    @Enumerated(EnumType.STRING)
    private CalculationType calculationType;
    private Double constantValue;
    @ManyToOne
    HealthFormItem valIxItem;
    Integer orderNo;
    
    public HealthFormItem getValIxItem() {
        return valIxItem;
    }

    public void setValIxItem(HealthFormItem valIxItem) {
        this.valIxItem = valIxItem;
    }

    public Double getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(Double constantValue) {
        this.constantValue = constantValue;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HealthFormItem getCalIxItem() {
        return calIxItem;
    }

    public void setCalIxItem(HealthFormItem calIxItem) {
        this.calIxItem = calIxItem;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FormCal)) {
            return false;
        }
        FormCal other = (FormCal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.divudi.entity.lab.IxCal[ id=" + id + " ]";
    }
}
