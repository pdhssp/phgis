/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.ejb;

import com.divudi.data.BillType;
import com.divudi.entity.Bill;
import com.divudi.entity.Department;
import com.divudi.entity.Institution;
import com.divudi.facade.BillFacade;
import com.divudi.facade.DepartmentFacade;
import com.divudi.facade.InstitutionFacade;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.TemporalType;

/**
 *
 * @author Dr. M H B Ariyaratne <buddhika.ari at gmail.com>
 */
@Singleton
public class BillNumberBean {

    @EJB
    private DepartmentFacade depFacade;
    @EJB
    private InstitutionFacade insFacade;
    @EJB
    private BillFacade billFacade;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String institutionBillNumberGenerator(Institution ins, BillType billType) {
        if (ins == null || billType == null) {
            return "";
        }

        String sql = "SELECT b FROM BilledBill b where b.retired=false AND b.institution.id=" + ins.getId() + " AND b.billType= com.divudi.data.BillType." + billType;
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = ins.getInstitutionCode() + billType.toString().substring(0, 2) + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + billType.toString().substring(0, 2) + 1;
            return result;
        }

    }

    public String institutionBillNumberGenerator(Institution ins, BillType billType, String suf) {
        if (ins == null || billType == null) {
            return "";
        }

        String sql = "SELECT b FROM BilledBill b where b.createdAt is not null and b.billType= :type and b.retired=false AND b.institution.id=" + ins.getId();
        String result;

        HashMap tmp = new HashMap();
        tmp.put("type", billType);
        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);
        if (b != null) {
            result = ins.getInstitutionCode() + suf + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + suf + 1;
            return result;
        }

    }

    public String institutionBillNumberGenerator(Institution ins, Department toDept) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM BilledBill b where b.retired=false AND b.institution.id=" + ins.getId() + " and b.toDepartment.id = " + toDept.getId();
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + 1;
            return result;
        }

    }

    public String institutionPaymentBillNumberGenerator(Institution ins) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM BilledBill b where b.billType= :type and b.retired=false AND b.institution.id=" + ins.getId();
        String result;

        HashMap tmp = new HashMap();
        tmp.put("type", BillType.PaymentBill);
        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);
        if (b != null) {
            result = ins.getInstitutionCode() + "W" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + "W" + 1;
            return result;
        }

    }

    public String departmentBillNumberGenerator(Department dep, BillType billType) {

        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM BilledBill b where b.billType= :type and b.retired=false AND b.department.id=" + dep.getId();
        HashMap tmp = new HashMap();
        tmp.put("type", billType);

        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);
        String result;
        if (b != null) {
            result = dep.getDepartmentCode() + billType.toString().substring(0, 2) + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + billType.toString().substring(0, 2) + 1;
            return result;
        }

    }

    public String departmentBillNumberGenerator(Department dep, BillType billType, String suf) {

        if (dep == null || dep.getId() == null) {
            return "";
        }

        String sql = "SELECT b FROM BilledBill b where b.createdAt is not null and b.billType= :type and b.retired=false AND b.department.id=" + dep.getId();
        HashMap tmp = new HashMap();
        tmp.put("type", billType);

        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);
        String result;
        if (b != null) {
            result = dep.getDepartmentCode() + suf + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + suf + 1;
            return result;
        }

    }

    public String departmentBillNumberGenerator(Department dep, Department toDept) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM BilledBill b where b.retired=false AND b.department.id=" + dep.getId() + " AND b.toDepartment.id=" + toDept.getId();
        System.out.println("sql");
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + 1;
            return result;
        }

    }

    public String departmentPaymentBillNumberGenerator(Department dep) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM BilledBill b where b.billType= :type and b.retired=false AND b.department.id=" + dep.getId();
        HashMap tmp = new HashMap();
        tmp.put("type", BillType.PaymentBill);

        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);
        String result;
        if (b != null) {
            result = dep.getDepartmentCode() + "P" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + "W" + 1;
            return result;
        }

    }

    public String institutionCancelledBill(Institution ins, Department toDept) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM CancelledBill b where b.retired=false AND b.institution.id=" + ins.getId() + " and b.toDepartment.id = " + toDept.getId();
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + "C" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + "C" + 1;
            return result;
        }
    }

    public String departmentCancelledBill(Department dep, BillType type, String suf) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM CancelledBill b where b.retired=false AND b.department.id=" + dep.getId() + " AND b.billType= :btp";
        System.out.println("sql");
        String result;
        HashMap h = new HashMap();
        h.put("btp", type);
        List<Bill> b = getBillFacade().findBySQL(sql, h, TemporalType.TIMESTAMP);

        if (b != null) {
            result = dep.getDepartmentCode() + suf + "C" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + suf + "C" + 1;
            return result;
        }

    }

    public String institutionCancelledBill(Institution ins, BillType type, String suf) {
        if (ins == null || ins.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM CancelledBill b where b.retired=false AND b.institution.id=" + ins.getId() + " AND b.billType= :btp";
        System.out.println("sql");
        String result;
        HashMap h = new HashMap();
        h.put("btp", type);
        List<Bill> b = getBillFacade().findBySQL(sql, h, TemporalType.TIMESTAMP);

        if (b != null) {
            result = ins.getInstitutionCode() + suf + "C" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + suf + "C" + 1;
            return result;
        }

    }

    public String departmentCancelledBill(Department dep, Department toDept) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM CancelledBill b where b.retired=false AND b.department.id=" + dep.getId() + " AND b.toDepartment.id=" + toDept.getId();
        System.out.println("sql");
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + "C" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + "C" + 1;
            return result;
        }

    }

    public String departmentPaymentCancelledBill(Department dep) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM CancelledBill b where b.retired=false and b.billType= :type AND b.department.id=" + dep.getId();
        System.out.println("sql");
        String result;
        HashMap tmp = new HashMap();
        tmp.put("type", BillType.PaymentBill);
        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);

        if (b != null) {
            result = dep.getDepartmentCode() + "WC" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + "PC" + 1;
            return result;
        }

    }

    public String institutionPaymentCancelledBill(Institution ins) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM CancelledBill b where b.retired=false AND b.billType= :type and b.institution.id=" + ins.getId();
        String result;
        HashMap tmp = new HashMap();
        tmp.put("type", BillType.PaymentBill);
        List<Bill> b = getBillFacade().findBySQL(sql, tmp, TemporalType.TIMESTAMP);


        if (b != null) {
            result = ins.getInstitutionCode() + "WC" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + "WC" + 1;
            return result;
        }

    }

    public String institutionRefundBill(Institution ins, Department toDept) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM RefundBill b where b.retired=false AND b.institution.id=" + ins.getId() + " and b.toDepartment.id = " + toDept.getId();
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + "R" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + toDept.getDepartmentCode() + "R" + 1;
            return result;
        }
    }

    public String institutionReturnBill(Institution ins, BillType billType) {
        if (ins == null) {
            return "";
        }

        String sql = "SELECT b FROM RefundBill b where b.retired=false AND b.institution.id=" + ins.getId() + " and b.billType = :bTp ";
        String result;
        HashMap hash = new HashMap();
        hash.put("bTp", billType);
        List<Bill> b = getBillFacade().findBySQL(sql, hash, TemporalType.TIMESTAMP);

        if (b != null) {
            result = ins.getInstitutionCode() + "Rn" + (b.size() + 1);
            return result;
        } else {
            result = ins.getInstitutionCode() + "Rn" + 1;
            return result;
        }
    }

    public String departmentReturnBill(Department dep, BillType billType) {
        if (dep == null) {
            return "";
        }

        String sql = "SELECT b FROM RefundBill b where b.retired=false AND b.department.id=" + dep.getId() + " and b.billType = :bTp ";
        String result;
        HashMap hash = new HashMap();
        hash.put("bTp", billType);
        List<Bill> b = getBillFacade().findBySQL(sql, hash, TemporalType.TIMESTAMP);

        if (b != null) {
            result = dep.getDepartmentCode() + "Rn" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + "Rn" + 1;
            return result;
        }
    }

    public String departmentRefundBill(Department dep, Department toDept) {
        if (dep == null || dep.getId() == null) {
            return "";
        }
        String sql = "SELECT b FROM RefundBill b where b.retired=false AND b.department.id=" + dep.getId() + " AND b.toDepartment.id=" + toDept.getId();
        System.out.println("sql");
        String result;
        List<Bill> b = getBillFacade().findBySQL(sql);

        if (b != null) {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + "R" + (b.size() + 1);
            return result;
        } else {
            result = dep.getDepartmentCode() + toDept.getDepartmentCode() + "R" + 1;
            return result;
        }


    }

    public DepartmentFacade getDepFacade() {
        return depFacade;
    }

    public void setDepFacade(DepartmentFacade depFacade) {
        this.depFacade = depFacade;
    }

    public InstitutionFacade getInsFacade() {
        return insFacade;
    }

    public void setInsFacade(InstitutionFacade insFacade) {
        this.insFacade = insFacade;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }
}
