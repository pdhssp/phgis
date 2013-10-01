/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.bean;

import com.divudi.data.BillType;
import com.divudi.data.PaymentMethod;
import com.divudi.ejb.CommonFunctions;
import com.divudi.entity.Bill;
import com.divudi.entity.CancelledBill;
import com.divudi.entity.Department;
import com.divudi.entity.Institution;
import com.divudi.entity.RefundBill;
import com.divudi.entity.WebUser;
import com.divudi.facade.BillFacade;
import com.divudi.facade.CancelledBillFacade;
import com.divudi.facade.RefundBillFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author www.divudi.com
 */
@Named
@SessionScoped
public class CommonReport implements Serializable {

    @Inject
    SessionController sessionController;
    @EJB
    private BillFacade billFacade;
    @EJB
    private CancelledBillFacade cancelledBillFacade;
    @EJB
    private RefundBillFacade refundBillFacade;
    @EJB
    CommonFunctions commonFunctions;
    private Institution collectingIns;
    private List<Bill> billedBills;
    private List<CancelledBill> cancellededBills;
    private List<RefundBill> refundedBills;
    private List<Bill> paymentBills;
    private List<Bill> paymentCancelBills;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date toDate;
    private WebUser webUser;
    private Department department;
    private Double cashTot = 0.0;
    private Double creditTot = 0.0;
    private Double creditCard = 0.0;
    private Double chequeTot = 0.0;
    private Double cancelCashTot = 0.0;
    private Double cancelCreditTot = 0.0;
    private Double cancelCreditCard = 0.0;
    private Double refundCashTot = 0.0;
    private Double refundCreditTot = 0.0;
    private Double refundCreditCard = 0.0;
    private Double paymentCashTot = 0.0;
    private Double paymentCreditTot = 0.0;
    private Double paymentCreditCardTotal = 0.0;
    Institution institution;
    private double finalCashTotal;
    private double finalCreditTotal;
    private double finalCreditCardTotal;
    List<Bill> userBills;
    private List<CancelledBill> userCancellededBills;
    private List<RefundBill> userRefundedBills;
    private List<Bill> userPaymentBills;

    /**
     * Creates a new instance of CommonReport
     */
    public CommonReport() {
    }

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
        recreteModal();
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = getCommonFunctions().getStartOfDay(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
        recreteModal();
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = getCommonFunctions().getEndOfDay(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
        recreteModal();
    }

    public WebUser getWebUser() {

        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
        recreteModal();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        recreteModal();
    }

    public List<Bill> getBillsByReferingDoc() {

        Map temMap = new HashMap();


        String sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and b.createdAt between :fromDate and :toDate  order by b.referredBy.person.name";
        temMap.put("fromDate", getFromDate());
        temMap.put("toDate", getToDate());
        temMap.put("bTp", BillType.OpdBill);

        billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
        if (billedBills == null) {
            billedBills = new ArrayList<Bill>();
        }

        return billedBills;

    }

    public List<Bill> getBillsByCollecting() {

        Map temMap = new HashMap();
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        String sql;

        if (collectingIns == null) {
            //sql = "SELECT b FROM BilledBill b WHERE b.retired=false  and b.createdAt between :fromDate and :toDate  order by b.collectingCentre.name";
            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and  b.createdAt between :fromDate and :toDate  order by b.collectingCentre.name";
        } else {
            sql = "SELECT b FROM BilledBill b WHERE b.retired=false   and  b.collectingCentre.id=" + getCollectingIns().getId() + "  and b.createdAt between :fromDate and :toDate  order by b.collectingCentre.name";
        }
        billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
        if (billedBills == null) {
            billedBills = new ArrayList<Bill>();
        }

        return billedBills;

    }

//    @SuppressWarnings("unchecked")
    public List<Bill> getBilledBills() {
        if (billedBills == null) {
            System.out.println("1");
            if (getWebUser() != null && getDepartment() != null) {
                System.out.println("2");
                String sql = "SELECT b FROM Bill b WHERE b.retired=false AND b.creater.id=" + getWebUser().getId() + " and b.billType = com.divudi.data.BillType.OpdBill and b.creater.department.id = " + getDepartment().getId();
                billedBills = getBillFacade().findBySQL(sql);
                if (billedBills == null) {
                    billedBills = new ArrayList<Bill>();
                }
            }
            if (getWebUser() != null && getDepartment() == null) {
                System.out.println("3");
                String sql = "SELECT b FROM Bill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.creater.id=" + getWebUser().getId();
                billedBills = getBillFacade().findBySQL(sql);
                System.out.println("3_2");
                if (billedBills == null) {
                    billedBills = new ArrayList<Bill>();
                    System.out.println("3_2");
                }
            }
            if (getWebUser() != null && getDepartment() != null && getFromDate() != null && getToDate() != null) {
                System.out.println("4");
                String sql = "SELECT b FROM Bill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.creater.id=" + getWebUser().getId() + " and b.billType = com.divudi.data.BillType.OpdBill and b.creater.department.id=" + getDepartment().getId() + " and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", fromDate);
                temMap.put("toDate", toDate);
                billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (billedBills == null) {
                    billedBills = new ArrayList<Bill>();
                }
            }
            if (getWebUser() != null && getDepartment() == null && getFromDate() != null && getToDate() != null) {
                System.out.println("5");
                String sql = "SELECT b FROM Bill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.creater.id=" + getWebUser().getId() + " and b.billType = com.divudi.data.BillType.OpdBill and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", fromDate);
                temMap.put("toDate", toDate);
                System.out.println("sql is " + sql);
                System.out.println("fromDate is  " + fromDate);
                System.out.println("toDate is  " + toDate);

                billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

                System.out.println("bills is " + billedBills + " and the count is " + billedBills.size());

                if (billedBills == null) {
                    billedBills = new ArrayList<Bill>();
                }
            } else {
                System.out.println("6");
                // String sql = "SELECT b FROM Bill b WHERE b.retired=false";
                //    billedBills = getBillFacade().findBySQL(sql);
                if (billedBills == null) {
                    System.out.println("6_2");
                    billedBills = new ArrayList<Bill>();
                }
            }
            System.out.println("7");

            if (billedBills == null) {
                System.out.println("7_2");
                billedBills = new ArrayList<Bill>();
            }
            calTot();
        }
        return billedBills;
    }

    public List<Bill> getUserBills() {
        if (userBills == null) {
            if (getWebUser() != null) {
                System.out.println("5");
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and b.creater.id=" + getWebUser().getId() + " and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.OpdBill);
                System.out.println("sql is " + sql);

                userBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);


                if (userBills == null) {
                    userBills = new ArrayList<Bill>();
                }
            } else {
                //System.out.println("6");
                // String sql = "SELECT b FROM Bill b WHERE b.retired=false";
                //    billedBills = getBillFacade().findBySQL(sql);
                if (userBills == null) {
                    System.out.println("6_2");
                    userBills = new ArrayList<Bill>();
                }
            }
            System.out.println("7");
            billedBills = userBills;
            calTot();
            if (userBills == null) {
                System.out.println("7_2");
                userBills = new ArrayList<Bill>();
            }
            return userBills;
        }
        return userBills;
    }

    public List<Bill> getInstitutionBilledBills() {
        String sql;
        if (billedBills == null) {
            if (institution == null) {
                sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            } else {
                sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            }
            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (billedBills == null) {
                billedBills = new ArrayList<Bill>();
            }
            calTot();
        }
        return billedBills;
    }

    public List<CancelledBill> getUserCancelledBills() {
//        System.out.println("getting user cancelled bills");
        if (userCancellededBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.creater.id=" + getWebUser().getId() + " and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.OpdBill);
                userCancellededBills = getCancelledBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (userCancellededBills == null) {
                    userCancellededBills = new ArrayList<CancelledBill>();
                }
            } else {
                // String sql = "SELECT b FROM Bill b WHERE b.retired=false";
                //    billedBills = getBillFacade().findBySQL(sql);
                if (userCancellededBills == null) {
                    userCancellededBills = new ArrayList<CancelledBill>();
                }
            }
            cancellededBills = userCancellededBills;
            cancelCalTot();
            if (userCancellededBills == null) {
                userCancellededBills = new ArrayList<CancelledBill>();
            }
        }
        return userCancellededBills;
    }

    public List<Bill> getUserPaymentBillsOwn() {
        if (userPaymentBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType= :billTp and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.creater.id= " + getWebUser().getId() + " and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("billTp", BillType.PaymentBill);
                userPaymentBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                //If we use createdAt, use TemporalType.DATE , it we use createAt, use .TimeStamp
                if (userPaymentBills == null) {
                    userPaymentBills = new ArrayList<Bill>();
                }
            } else {
                // String sql = "SELECT b FROM Bill b WHERE b.retired=false";
                //    billedBills = getBillFacade().findBySQL(sql);
                if (userPaymentBills == null) {
                    userPaymentBills = new ArrayList<Bill>();
                }
            }
            paymentBills = userPaymentBills;
            payTot();
            if (userPaymentBills == null) {
                userPaymentBills = new ArrayList<Bill>();
            }

        }
        return userPaymentBills;
    }

    public List<Bill> getBillsByReferingDocOwn() {

        Map temMap = new HashMap();
        temMap.put("fromDate", getFromDate());
        temMap.put("toDate", getToDate());

        String sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.billType = com.divudi.data.BillType.OpdBill and b.createdAt between :fromDate and :toDate  order by b.referredBy.person.name ";
        billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
        if (billedBills == null) {
            billedBills = new ArrayList<Bill>();
        }

        return billedBills;

    }

    public List<Bill> getBillsByCollectingOwn() {

        Map temMap = new HashMap();
        temMap.put("fromDate", fromDate);
        temMap.put("toDate", toDate);
        String sql;

        if (collectingIns == null) {
            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and  b.billType = com.divudi.data.BillType.LabBill and  b.institution.id=" + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate  order by b.collectingCentre.name";
        } else {
            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and  b.billType = com.divudi.data.BillType.LabBill  and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.collectingCentre.id=" + getCollectingIns().getId() + "  and b.createdAt between :fromDate and :toDate  order by b.collectingCentre.name";
        }
        billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
        if (billedBills == null) {
            billedBills = new ArrayList<Bill>();
        }

        return billedBills;

    }

    public List<RefundBill> getUserRefundedBillsOwn() {
        if (userRefundedBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM RefundBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                userRefundedBills = getRefundBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (userRefundedBills == null) {
                    userRefundedBills = new ArrayList<RefundBill>();
                }
            } else {

                if (userRefundedBills == null) {
                    userRefundedBills = new ArrayList<RefundBill>();
                }
            }
            refundedBills = userRefundedBills;
            RefCalTot();
            if (userRefundedBills == null) {
                userRefundedBills = new ArrayList<RefundBill>();
            }
        }
        return userRefundedBills;
    }

    public List<CancelledBill> getUserCancelledBillsOwn() {
        if (userCancellededBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.creater.id=" + getWebUser().getId() + " and b.createdAt between :fromDate and :toDate order by b.department.name ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                userCancellededBills = getCancelledBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (userCancellededBills == null) {
                    userCancellededBills = new ArrayList<CancelledBill>();
                }
            } else {
                if (userCancellededBills == null) {
                    userCancellededBills = new ArrayList<CancelledBill>();
                }
            }
            cancellededBills = userCancellededBills;
            cancelCalTot();
            if (userCancellededBills == null) {
                userCancellededBills = new ArrayList<CancelledBill>();
            }
        }
        return userCancellededBills;
    }

    public List<Bill> getUserBillsOwn() {
        if (userBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.creater.id=" + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());

                userBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);


                if (userBills == null) {
                    userBills = new ArrayList<Bill>();
                }
            } else {
                if (userBills == null) {
                    userBills = new ArrayList<Bill>();
                }
            }
            billedBills = userBills;
            calTot();
            if (userBills == null) {
                userBills = new ArrayList<Bill>();
            }
            return userBills;
        }
        return userBills;
    }

    public List<Bill> getInstitutionPaymentBillsOwn() {
        if (paymentBills == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType= :billTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";
            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("billTp", BillType.PaymentBill);
            paymentBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (paymentBills == null) {
                paymentBills = new ArrayList<Bill>();
            }
            payTot();
        }
        return paymentBills;
    }

    public List<RefundBill> getInstitutionRefundedBillsOwn() {
        if (refundedBills == null) {
            String sql;
            sql = "SELECT b FROM RefundBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";


            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            refundedBills = getRefundBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (refundedBills == null) {
                refundedBills = new ArrayList<RefundBill>();
            }
            RefCalTot();
        }
        return refundedBills;
    }

    public List<CancelledBill> getInstitutionCancelledBillsOwn() {
        if (cancellededBills == null) {
            String sql;
            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            cancellededBills = getCancelledBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cancellededBills == null) {
                cancellededBills = new ArrayList<CancelledBill>();
            }
            cancelCalTot();
        }
        return cancellededBills;
    }

    public List<CancelledBill> getInstitutionCancelledBills() {
        if (cancellededBills == null) {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
//        cal.setTime(toDate);
//        cal.add(Calendar.HOUR_OF_DAY , 24 );
//        toDate=cal.getTime();
            String sql;

            if (institution == null) {
                sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            } else {
                sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            }

            Map temMap = new HashMap();
            temMap.put("fromDate", fromDate);
            temMap.put("toDate", toDate);
            cancellededBills = getCancelledBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cancellededBills == null) {
                cancellededBills = new ArrayList<CancelledBill>();
            }
            cancelCalTot();
        }
        return cancellededBills;
    }

    public List<RefundBill> getUserRefundedBills() {
//        System.out.println("getting user refunded bills");
        if (userRefundedBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM RefundBill b WHERE b.retired=false and b.billType = :bTp and b.creater.id= " + getWebUser().getId() + " and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.OpdBill);
                userRefundedBills = getRefundBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (userRefundedBills == null) {
                    userRefundedBills = new ArrayList<RefundBill>();
                }
            } else {
                // String sql = "SELECT b FROM Bill b WHERE b.retired=false";
                //    billedBills = getBillFacade().findBySQL(sql);
                if (userRefundedBills == null) {
                    userRefundedBills = new ArrayList<RefundBill>();
                }
            }
            refundedBills = userRefundedBills;
            RefCalTot();
            if (userRefundedBills == null) {
                userRefundedBills = new ArrayList<RefundBill>();
            }
        }
        return userRefundedBills;
    }

    public List<RefundBill> getInstitutionRefundedBills() {
        if (refundedBills == null) {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("IST"));
//        cal.setTime(toDate);
//        cal.add(Calendar.HOUR_OF_DAY , 24 );
//        toDate=cal.getTime();

            String sql;

            if (institution == null) {
                sql = "SELECT b FROM RefundBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            } else {
                sql = "SELECT b FROM RefundBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            }

            Map temMap = new HashMap();
            temMap.put("fromDate", fromDate);
            temMap.put("toDate", toDate);
            refundedBills = getRefundBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (refundedBills == null) {
                refundedBills = new ArrayList<RefundBill>();
            }
            RefCalTot();
        }
        return refundedBills;
    }

    public List<Bill> getUserPaymentBills() {
        if (userPaymentBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.PaymentBill);
                userPaymentBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (userPaymentBills == null) {
                    userPaymentBills = new ArrayList<Bill>();
                }
            } else {
                if (userPaymentBills == null) {
                    userPaymentBills = new ArrayList<Bill>();
                }
            }
            paymentBills = userPaymentBills;
            payTot();
            if (userPaymentBills == null) {
                userPaymentBills = new ArrayList<Bill>();
            }
        }
        return userPaymentBills;
    }
    private List<Bill> agentRecieves;

    public List<Bill> getAgentRecieveBills() {
        if (agentRecieves == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.AgentPaymentReceiveBill);
                agentRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (agentRecieves == null) {
                    agentRecieves = new ArrayList<Bill>();
                }
            } else {
                if (agentRecieves == null) {
                    agentRecieves = new ArrayList<Bill>();
                }
            }


            if (agentRecieves == null) {
                agentRecieves = new ArrayList<Bill>();
            }
        }
        calAgentTotal();

        return agentRecieves;
    }
    private double agentCash;
    private double agentCheque;
    private double agentSlip;

    public void calAgentTotal() {
        agentCash = 0.0;
        agentCheque = 0.0;
        agentSlip = 0.0;

        for (Bill b : agentRecieves) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                agentCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                agentCheque += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Slip) {
                agentSlip += b.getNetTotal();
            }
        }

    }
    private List<Bill> cashRecieves;

    public List<Bill> getCashRecieveBills() {
        if (cashRecieves == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.CashRecieveBill);
                cashRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (cashRecieves == null) {
                    cashRecieves = new ArrayList<Bill>();
                }
            } else {
                if (cashRecieves == null) {
                    cashRecieves = new ArrayList<Bill>();
                }
            }


            if (cashRecieves == null) {
                cashRecieves = new ArrayList<Bill>();
            }
        }

        calCashRecieveTot();
        return cashRecieves;
    }
    private double cashRecieveTotCash;
    private double cashRecieveTotCheque;
    private double cashRecieveTotSlip;

    public void calCashRecieveTot() {
        cashRecieveTotCash = 0.0;
        cashRecieveTotCheque = 0.0;
        cashRecieveTotSlip = 0.0;

        for (Bill b : cashRecieves) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                cashRecieveTotCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                cashRecieveTotCheque += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Slip) {
                cashRecieveTotSlip += b.getNetTotal();
            }
        }

    }
    private List<Bill> agentCancelBill;

    public List<Bill> getAgentRecieveBillCancel() {
        if (agentCancelBill == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.AgentPaymentReceiveBill);
                agentCancelBill = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (agentCancelBill == null) {
                    agentCancelBill = new ArrayList<Bill>();
                }
            } else {
                if (agentCancelBill == null) {
                    agentCancelBill = new ArrayList<Bill>();
                }
            }


            if (agentCancelBill == null) {
                agentCancelBill = new ArrayList<Bill>();
            }
        }
        calAgentCancelTot();
        return agentCancelBill;
    }
    private double agentCancelCash = 0.0;
    private double agentCancelCheque = 0.0;
    private double agentCancelSlip = 0.0;

    public void calAgentCancelTot() {
        agentCancelCash = 0.0;
        agentCancelCheque = 0.0;
        agentCancelSlip = 0.0;

        for (Bill b : agentCancelBill) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                agentCancelCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                agentCancelCheque += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Slip) {
                agentCancelSlip += b.getNetTotal();
            }
        }


    }
    private List<Bill> cashRecieveCancel;

    public List<Bill> getCashRecieveBillCancel() {
        if (cashRecieveCancel == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.CashRecieveBill);
                cashRecieveCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (cashRecieveCancel == null) {
                    cashRecieveCancel = new ArrayList<Bill>();
                }
            } else {
                if (cashRecieveCancel == null) {
                    cashRecieveCancel = new ArrayList<Bill>();
                }
            }


            if (cashRecieveCancel == null) {
                cashRecieveCancel = new ArrayList<Bill>();
            }
        }
        calCashRecieveCancelTot();
        return cashRecieveCancel;
    }
    private double cashRecieveCancelTotCash;
    private double cashRecieveCancelTotCheque;
    private double cashRecieveCancelTotSlip;

    public void calCashRecieveCancelTot() {
        cashRecieveCancelTotCash = 0.0;
        cashRecieveCancelTotCheque = 0.0;
        cashRecieveCancelTotSlip = 0.0;

        for (Bill b : cashRecieveCancel) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                cashRecieveCancelTotCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                cashRecieveCancelTotCheque += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Slip) {
                cashRecieveCancelTotSlip += b.getNetTotal();
            }
        }

    }
    private List<Bill> pettyPayments;

    public List<Bill> getPettyPaymentBills() {
        if (pettyPayments == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM BilledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.PettyCash);
                pettyPayments = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (pettyPayments == null) {
                    pettyPayments = new ArrayList<Bill>();
                }
            } else {
                if (pettyPayments == null) {
                    pettyPayments = new ArrayList<Bill>();
                }
            }


            if (pettyPayments == null) {
                pettyPayments = new ArrayList<Bill>();
            }
        }
        calPettyTotal();
        return pettyPayments;
    }
    private double pettyTotalCash;
    private double pettyTotalCheque;

    public void calPettyTotal() {
        pettyTotalCash = 0.0;
        pettyTotalCheque = 0.0;
        for (Bill b : pettyPayments) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                pettyTotalCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                pettyTotalCheque += b.getNetTotal();
            }
        }
    }
    private List<Bill> pettyPaymentsCancel;

    public List<Bill> getPettyPaymentCancelBills() {
        if (pettyPaymentsCancel == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.PettyCash);
                pettyPaymentsCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                if (pettyPaymentsCancel == null) {
                    pettyPaymentsCancel = new ArrayList<Bill>();
                }
            } else {
                if (pettyPaymentsCancel == null) {
                    pettyPaymentsCancel = new ArrayList<Bill>();
                }
            }


            if (pettyPaymentsCancel == null) {
                pettyPaymentsCancel = new ArrayList<Bill>();
            }
        }
        calPettyCancelTotal();
        return pettyPaymentsCancel;
    }
    private double pettyCancelTotalCash;
    private double pettyCancelTotalCheque;

    public void calPettyCancelTotal() {
        pettyCancelTotalCash = 0.0;
        pettyCancelTotalCheque = 0.0;

        for (Bill b : pettyPaymentsCancel) {
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                pettyCancelTotalCash += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                pettyCancelTotalCheque += b.getNetTotal();
            }
        }

    }
    List<Bill> userPaymentCancelBills;

    public List<Bill> getUserPaymentCancelBills() {
        if (userPaymentCancelBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.billType = :bTp and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.PaymentBill);

                userPaymentCancelBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                System.out.println("Sise of cancel " + userPaymentCancelBills.size());
                if (userPaymentCancelBills == null) {
                    userPaymentCancelBills = new ArrayList<Bill>();
                }
            } else {
                if (userPaymentCancelBills == null) {
                    userPaymentCancelBills = new ArrayList<Bill>();
                }
            }

            paymentCancelBills = userPaymentCancelBills;
            payTotC();

            if (userPaymentCancelBills == null) {
                userPaymentCancelBills = new ArrayList<Bill>();
            }
        }
        return userPaymentCancelBills;
    }

    public List<Bill> getUserPaymentCancelBillsOwn() {
        if (userPaymentCancelBills == null) {
            if (getWebUser() != null) {
                String sql = "SELECT b FROM CancelledBill b WHERE b.retired=false  and b.creater.id= " + getWebUser().getId() + " and b.billType = :bTp and b.institution.id=" + getSessionController().getInstitution().getId() + "  and b.createdAt between :fromDate and :toDate ";
                Map temMap = new HashMap();
                temMap.put("fromDate", getFromDate());
                temMap.put("toDate", getToDate());
                temMap.put("bTp", BillType.PaymentBill);

                userPaymentCancelBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);
                System.out.println("Sise of cancel " + userPaymentCancelBills.size());
                if (userPaymentCancelBills == null) {
                    userPaymentCancelBills = new ArrayList<Bill>();
                }
            } else {
                if (userPaymentCancelBills == null) {
                    userPaymentCancelBills = new ArrayList<Bill>();
                }
            }

            paymentCancelBills = userPaymentCancelBills;
            payTotC();

            if (userPaymentCancelBills == null) {
                userPaymentCancelBills = new ArrayList<Bill>();
            }
        }
        return userPaymentCancelBills;
    }

    public List<Bill> getInstitutionPaymentBills() {
        if (paymentBills == null) {
            String sql;

            if (institution == null) {
                sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.PaymentBill and b.createdAt between :fromDate and :toDate order by b.id";
            } else {
                sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.PaymentBill and b.institution.id= " + getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";
            }
            Map temMap = new HashMap();
            temMap.put("fromDate", fromDate);
            temMap.put("toDate", toDate);
            paymentBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (paymentBills == null) {
                paymentBills = new ArrayList<Bill>();
            }
            payTot();
        }
        return paymentBills;
    }

    public List<Bill> getInstitutionPaymentCancelBillsOwn() {
        if (paymentCancelBills == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PaymentBill);
            paymentCancelBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (paymentCancelBills == null) {
                paymentCancelBills = new ArrayList<Bill>();
            }
            payTotC();
        }
        return paymentCancelBills;
    }

    public List<Bill> getInstitutionPettyPaymentBillsOwn() {
        if (pettyPayments == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PettyCash);
            pettyPayments = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (pettyPayments == null) {
                pettyPayments = new ArrayList<Bill>();
            }

        }
        calPettyTotal();
        return pettyPayments;
    }

    public List<Bill> getInstitutionPettyPaymentBills() {
        if (pettyPayments == null) {
            String sql;
            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and  b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PettyCash);
            pettyPayments = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (pettyPayments == null) {
                pettyPayments = new ArrayList<Bill>();
            }

        }

        calPettyTotal();
        return pettyPayments;
    }

    public List<Bill> getInstitutionPettyCancellBillsOwn() {
        if (pettyPaymentsCancel == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PettyCash);
            pettyPaymentsCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (pettyPaymentsCancel == null) {
                pettyPaymentsCancel = new ArrayList<Bill>();
            }

        }

        calPettyCancelTotal();
        return pettyPaymentsCancel;
    }

    public List<Bill> getInstitutionPettyCancellBills() {
        if (pettyPaymentsCancel == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PettyCash);
            pettyPaymentsCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (pettyPaymentsCancel == null) {
                pettyPaymentsCancel = new ArrayList<Bill>();
            }

        }
        calPettyCancelTotal();
        return pettyPaymentsCancel;
    }

    public List<Bill> getInstitutionCashRecieveBillsOwn() {
        if (cashRecieves == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.CashRecieveBill);
            cashRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cashRecieves == null) {
                cashRecieves = new ArrayList<Bill>();
            }

        }
        calCashRecieveTot();
        return cashRecieves;
    }

    public List<Bill> getInstitutionCashRecieveBills() {
        if (cashRecieves == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and  b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.CashRecieveBill);
            cashRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cashRecieves == null) {
                cashRecieves = new ArrayList<Bill>();
            }

        }
        calCashRecieveTot();
        return cashRecieves;
    }

    public List<Bill> getInstitutionAgentBillsOwn() {
        if (agentRecieves == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.AgentPaymentReceiveBill);
            agentRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (agentRecieves == null) {
                agentRecieves = new ArrayList<Bill>();
            }

        }
        calAgentTotal();
        return agentRecieves;
    }

    public List<Bill> getInstitutionAgentBills() {
        if (agentRecieves == null) {
            String sql;

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = :bTp and  b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.AgentPaymentReceiveBill);
            agentRecieves = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (agentRecieves == null) {
                agentRecieves = new ArrayList<Bill>();
            }

        }
        calAgentTotal();
        return agentRecieves;
    }

    public List<Bill> getInstitutionCashRecieveCancellBillsOwn() {
        if (cashRecieveCancel == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.CashRecieveBill);
            cashRecieveCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cashRecieveCancel == null) {
                cashRecieveCancel = new ArrayList<Bill>();
            }

        }
        return cashRecieveCancel;
    }

    public List<Bill> getInstitutionCashRecieveCancellBills() {
        if (cashRecieveCancel == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.CashRecieveBill);
            cashRecieveCancel = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (cashRecieveCancel == null) {
                cashRecieveCancel = new ArrayList<Bill>();
            }

        }
        calCashRecieveCancelTot();
        return cashRecieveCancel;
    }

    public List<Bill> getInstitutionAgentCancellBillsOwn() {
        if (agentCancelBill == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.AgentPaymentReceiveBill);
            agentCancelBill = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (agentCancelBill == null) {
                agentCancelBill = new ArrayList<Bill>();
            }

        }
        calAgentCancelTot();
        return agentCancelBill;
    }

    public List<Bill> getInstitutionAgentCancellBills() {
        if (agentCancelBill == null) {
            String sql;

            sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp  and b.createdAt between :fromDate and :toDate order by b.id";

            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.AgentPaymentReceiveBill);
            agentCancelBill = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (agentCancelBill == null) {
                agentCancelBill = new ArrayList<Bill>();
            }

        }
        calAgentCancelTot();
        return agentCancelBill;
    }

    public List<Bill> getInstitutionPaymentCancelBills() {
        if (paymentCancelBills == null) {
            String sql;

            if (institution == null) {
                sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.createdAt between :fromDate and :toDate order by b.id";
            } else {
                sql = "SELECT b FROM CancelledBill b WHERE b.retired=false and b.billType = :bTp and b.institution.id= " + getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.id";
            }
            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            temMap.put("bTp", BillType.PaymentBill);
            paymentCancelBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (paymentCancelBills == null) {
                paymentCancelBills = new ArrayList<Bill>();
            }
            payTotC();
        }
        return paymentCancelBills;
    }

    public List<Bill> getInstitutionBilledBillsOwn() {
        String sql;
        if (billedBills == null) {

            sql = "SELECT b FROM BilledBill b WHERE b.retired=false and b.billType = com.divudi.data.BillType.OpdBill and b.institution.id= " + getSessionController().getInstitution().getId() + " and b.createdAt between :fromDate and :toDate order by b.toDepartment.name";
            Map temMap = new HashMap();
            temMap.put("fromDate", getFromDate());
            temMap.put("toDate", getToDate());
            billedBills = getBillFacade().findBySQL(sql, temMap, TemporalType.TIMESTAMP);

            if (billedBills == null) {
                billedBills = new ArrayList<Bill>();
            }

            calTot();
        }
        return billedBills;
    }
    private double slipTot = 0.0;

    private void calTot() {
        cashTot = 0.00;
        creditTot = 0.0;
        creditCard = 0.0;
        chequeTot = 0.0;
        slipTot = 0.0;
        for (Bill b : billedBills) {
            if (b.getPaymentScheme() == null || b.getPaymentScheme().getPaymentMethod() == null) {
                continue;
            }
            if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cash) {
                cashTot += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Credit) {
                creditTot += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Card) {
                creditCard += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Cheque) {
                chequeTot += b.getNetTotal();
            } else if (b.getPaymentScheme().getPaymentMethod() == PaymentMethod.Slip) {
                slipTot += b.getNetTotal();
            }
        }

    }
    private double paymentCancelCash;

    private void payTotC() {
        paymentCancelCash = 0.00;
        for (Bill b : paymentCancelBills) {
            if (b.getPaymentScheme() == null || b.getPaymentScheme().getPaymentMethod() == null) {
                continue;
            }
            if ("Cash".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                paymentCancelCash += b.getNetTotal();
            }
        }

    }

    private void payTot() {
        paymentCashTot = 0.00;
        for (Bill b : paymentBills) {
            if (b.getPaymentScheme() == null || b.getPaymentScheme().getPaymentMethod() == null) {
                continue;
            }
            if ("Cash".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                paymentCashTot += b.getNetTotal();
            }
        }

    }
    private Double refundCheque = 0.0;
    private double refundSlip = 0.0;

    private void RefCalTot() {
        refundCashTot = 0.00;
        refundCreditTot = 0.0;
        refundCreditCard = 0.0;
        refundCheque = 0.0;
        refundSlip = 0.0;
        for (RefundBill b : refundedBills) {
            if (b.getPaymentScheme() == null || b.getPaymentScheme().getPaymentMethod() == null) {
                continue;
            }
            if ("Cash".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                refundCashTot += b.getNetTotal();
            } else if ("Credit".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                refundCreditTot += b.getNetTotal();
            } else if ("Card".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                refundCreditCard += b.getNetTotal();
            } else if ("Cheque".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                refundCheque += b.getNetTotal();
            } else if ("Slip".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                refundSlip += b.getNetTotal();
            }
        }

    }
    private Double cancelCheque = 0.0;
    private double cancelSlip = 0.0;

    private void cancelCalTot() {
        cancelCashTot = 0.00;
        cancelCreditTot = 0.0;
        cancelCreditCard = 0.0;
        cancelCheque = 0.0;
        cancelSlip = 0.0;
        for (CancelledBill b : cancellededBills) {
            if (b.getPaymentScheme() == null || b.getPaymentScheme().getPaymentMethod() == null) {
                continue;
            }
            if ("Cash".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                cancelCashTot += b.getNetTotal();
            } else if ("Credit".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                cancelCreditTot += b.getNetTotal();
            } else if ("Card".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                cancelCreditCard += b.getNetTotal();
            } else if ("Cheque".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                cancelCheque += b.getNetTotal();
            } else if ("Slip".equals(b.getPaymentScheme().getPaymentMethod().toString())) {
                cancelSlip += b.getNetTotal();
            }
        }

    }

    public void setBilledBills(List<Bill> billedBills) {
        this.billedBills = billedBills;
    }

    public Double getCashTot() {
        System.out.println("getCashTot :" + cashTot);
        return cashTot;
    }

    public void setCashTot(Double cashTot) {
        System.out.println("SetCashTot :" + cashTot);
        this.cashTot = cashTot;
    }

    public Double getCreditTot() {
        return creditTot;
    }

    public void setCreditTot(Double creditTot) {
        this.creditTot = creditTot;
    }

    public Double getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Double creditCard) {
        this.creditCard = creditCard;
    }

    public List<CancelledBill> getCancellededBills() {
        return cancellededBills;
    }

    public void setCancellededBills(List<CancelledBill> cancellededBills) {
        this.cancellededBills = cancellededBills;
    }

    public List<RefundBill> getRefundedBills() {
        return refundedBills;
    }

    public void setRefundedBills(List<RefundBill> refundedBills) {
        this.refundedBills = refundedBills;
    }

    public CancelledBillFacade getCancelledBillFacade() {
        return cancelledBillFacade;
    }

    public void setCancelledBillFacade(CancelledBillFacade cancelledBillFacade) {
        this.cancelledBillFacade = cancelledBillFacade;
    }

    public Double getCancelCashTot() {
        return cancelCashTot;
    }

    public void setCancelCashTot(Double cancelCashTot) {
        this.cancelCashTot = cancelCashTot;
    }

    public Double getCancelCreditTot() {
        return cancelCreditTot;
    }

    public void setCancelCreditTot(Double cancelCreditTot) {
        this.cancelCreditTot = cancelCreditTot;
    }

    public Double getCancelCreditCard() {
        return cancelCreditCard;
    }

    public void setCancelCreditCard(Double cancelCreditCard) {
        this.cancelCreditCard = cancelCreditCard;
    }

    public RefundBillFacade getRefundBillFacade() {
        return refundBillFacade;
    }

    public void setRefundBillFacade(RefundBillFacade refundBillFacade) {
        this.refundBillFacade = refundBillFacade;
    }

    public Double getRefundCashTot() {
        return refundCashTot;
    }

    public void setRefundCashTot(Double refundCashTot) {
        this.refundCashTot = refundCashTot;
    }

    public Double getRefundCreditTot() {
        return refundCreditTot;
    }

    public void setRefundCreditTot(Double refundCreditTot) {
        this.refundCreditTot = refundCreditTot;
    }

    public Double getRefundCreditCard() {
        return refundCreditCard;
    }

    public void setRefundCreditCard(Double refundCreditCard) {
        this.refundCreditCard = refundCreditCard;
    }

    public Double getPaymentCashTot() {
        paymentCashTot = 0.0;
        for (Bill b : paymentBills) {
            paymentCashTot += b.getNetTotal();
        }

        return paymentCashTot;
    }

    public void setPaymentCashTot(Double paymentCashTot) {
        this.paymentCashTot = paymentCashTot;
    }

    public Double getPaymentCreditTot() {
        return paymentCreditTot;
    }

    public void setPaymentCreditTot(Double paymentCreditTot) {
        this.paymentCreditTot = paymentCreditTot;
    }

    public Double getPaymentCreditCardTotal() {
        return paymentCreditCardTotal;
    }

    public void setPaymentCreditCard(Double paymentCreditCard) {
        this.paymentCreditCardTotal = paymentCreditCard;
    }

    public double getFinalCashTotal() {
        finalCashTotal = getCancelCashTot() + getCashTot() + getRefundCashTot() + getPaymentCashTot() + getPaymentCancelCash() + pettyTotalCash + pettyCancelTotalCash + cashRecieveTotCash + cashRecieveCancelTotCash + agentCash + agentCancelCash;
        return finalCashTotal;
    }
    private double finalChequeTot;

    public void setFinalCashTotal(double finalCashTotal) {
        this.finalCashTotal = finalCashTotal;
    }

    public double getFinalCreditTotal() {
        finalCreditTotal = getCreditTot() + getRefundCreditTot() + getCancelCreditTot() + getPaymentCreditTot();
        return finalCreditTotal;
    }

    public void setFinalCreditTotal(double finalCreditTotal) {
        this.finalCreditTotal = finalCreditTotal;
    }

    public double getFinalCreditCardTotal() {
        finalCreditCardTotal = getCreditCard() + getRefundCreditCard() + getCancelCreditCard() + getPaymentCreditCardTotal();
        return finalCreditCardTotal;
    }

    public void setFinalCreditCardTotal(double finalCreditCardTotal) {
        this.finalCreditCardTotal = finalCreditCardTotal;
    }

    public void recreteModal() {
        collectingIns = null;
        userBills = null;
        userCancellededBills = null;
        userRefundedBills = null;
        userPaymentBills = null;
        userPaymentCancelBills = null;
        billedBills = null;
        cancellededBills = null;

        pettyPayments = null;
        pettyTotalCash = 0.0;
        pettyTotalCheque = 0.0;
        pettyPaymentsCancel = null;
        pettyCancelTotalCash = 0.0;
        pettyCancelTotalCheque = 0.0;

        agentCancelBill = null;
        agentRecieves = null;
        agentCancelCash = 0.0;
        agentCancelCheque = 0.0;
        agentCancelSlip = 0.0;
        agentCash = 0.0;
        agentCheque = 0.0;
        agentSlip = 0.0;

        cashRecieves = null;
        cashRecieveTotCash = 0.0;
        cashRecieveTotCheque = 0.0;
        cashRecieveTotSlip = 0.0;
        cashRecieveCancel = null;
        cashRecieveCancelTotCash = 0.0;
        cashRecieveCancelTotCheque = 0.0;
        cashRecieveCancelTotSlip = 0.0;

        refundedBills = null;
        paymentBills = null;
        paymentCancelBills = null;
        cashTot = 0.0;
        creditTot = 0.0;
        creditCard = 0.0;
        chequeTot = 0.0;
//
        cancelCashTot = 0.0;
        cancelCreditTot = 0.0;
        cancelCreditCard = 0.0;
        refundCashTot = 0.0;
        refundCreditTot = 0.0;
        refundCreditCard = 0.0;
        paymentCashTot = 0.0;
        paymentCancelCash = 0.0;
        paymentCreditTot = 0.0;
        paymentCreditCardTotal = 0.0;
    }

    public Institution getCollectingIns() {
        return collectingIns;
    }

    public void setCollectingIns(Institution collectingIns) {
        //recreteModal();
        this.collectingIns = collectingIns;
    }

    public List<CancelledBill> getUserCancellededBills() {
        return userCancellededBills;
    }

    public void setUserCancellededBills(List<CancelledBill> userCancellededBills) {
        this.userCancellededBills = userCancellededBills;
    }

    public List<Bill> getPaymentBills() {
        return paymentBills;
    }

    public void setPaymentBills(List<Bill> paymentBills) {
        this.paymentBills = paymentBills;
    }

    public Double getChequeTot() {
        return chequeTot;
    }

    public void setChequeTot(Double chequeTot) {
        this.chequeTot = chequeTot;
    }

    public Double getCancelCheque() {
        return cancelCheque;
    }

    public void setCancelCheque(Double cancelCheque) {
        this.cancelCheque = cancelCheque;
    }

    public Double getRefundCheque() {
        return refundCheque;
    }

    public void setRefundCheque(Double refundCheque) {
        this.refundCheque = refundCheque;
    }
    private double finalSlipTot = 0.0;

    public double getFinalChequeTot() {
        finalChequeTot = getCancelCheque() + getChequeTot() + getRefundCheque()
                + pettyTotalCheque + pettyCancelTotalCheque
                + cashRecieveTotCheque + cashRecieveCancelTotCheque
                + agentCheque + agentCancelCheque;
        return finalChequeTot;
    }

    public void setFinalChequeTot(double finalChequeTot) {
        this.finalChequeTot = finalChequeTot;
    }

    public double getSlipTot() {
        return slipTot;
    }

    public void setSlipTot(double slipTot) {
        this.slipTot = slipTot;
    }

    public double getCancelSlip() {
        return cancelSlip;
    }

    public void setCancelSlip(double cancelSlip) {
        this.cancelSlip = cancelSlip;
    }

    public double getRefundSlip() {
        return refundSlip;
    }

    public void setRefundSlip(double refundSlip) {
        this.refundSlip = refundSlip;
    }

    public double getFinalSlipTot() {
        finalSlipTot = getSlipTot() + getCancelSlip() + getRefundSlip()
                + cashRecieveTotSlip + cashRecieveCancelTotSlip
                + agentSlip + agentCancelSlip;

        return finalSlipTot;
    }

    public void setFinalSlipTot(double finalSlipTot) {
        this.finalSlipTot = finalSlipTot;
    }

    public double getPaymentCancelCash() {
        paymentCancelCash = 0.0;
        for (Bill b : paymentCancelBills) {
            paymentCancelCash += b.getNetTotal();
        }

        return paymentCancelCash;
    }

    public List<Bill> getAgentRecieves() {
        return agentRecieves;
    }

    public void setAgentRecieves(List<Bill> agentRecieves) {
        this.agentRecieves = agentRecieves;
    }

    public List<Bill> getCashRecieves() {
        return cashRecieves;
    }

    public void setCashRecieves(List<Bill> cashRecieves) {
        this.cashRecieves = cashRecieves;
    }

    public List<Bill> getAgentCancelBill() {
        return agentCancelBill;
    }

    public void setAgentCancelBill(List<Bill> agentCancelBill) {
        this.agentCancelBill = agentCancelBill;
    }

    public List<Bill> getCashRecieveCancel() {
        return cashRecieveCancel;
    }

    public void setCashRecieveCancel(List<Bill> cashRecieveCancel) {
        this.cashRecieveCancel = cashRecieveCancel;
    }

    public List<Bill> getPettyPayments() {
        return pettyPayments;
    }

    public void setPettyPayments(List<Bill> pettyPayments) {
        this.pettyPayments = pettyPayments;
    }

    public double getPettyTotalCash() {
        return pettyTotalCash;
    }

    public void setPettyTotalCash(double pettyTotalCash) {
        this.pettyTotalCash = pettyTotalCash;
    }

    public double getPettyTotalCheque() {
        return pettyTotalCheque;
    }

    public void setPettyTotalCheque(double pettyTotalCheque) {
        this.pettyTotalCheque = pettyTotalCheque;
    }

    public List<Bill> getPettyPaymentsCancel() {
        return pettyPaymentsCancel;
    }

    public void setPettyPaymentsCancel(List<Bill> pettyPaymentsCancel) {
        this.pettyPaymentsCancel = pettyPaymentsCancel;
    }

    public List<Bill> getPaymentCancelBills() {
        return paymentCancelBills;
    }

    public void setPaymentCancelBills(List<Bill> paymentCancelBills) {
        this.paymentCancelBills = paymentCancelBills;
    }

    public double getPettyCancelTotalCash() {
        return pettyCancelTotalCash;
    }

    public void setPettyCancelTotalCash(double pettyCancelTotalCash) {
        this.pettyCancelTotalCash = pettyCancelTotalCash;
    }

    public double getPettyCancelTotalCheque() {

        return pettyCancelTotalCheque;
    }

    public void setPettyCancelTotalCheque(double pettyCancelTotalCheque) {
        this.pettyCancelTotalCheque = pettyCancelTotalCheque;
    }

    public double getCashRecieveTotCash() {
        return cashRecieveTotCash;
    }

    public void setCashRecieveTotCash(double cashRecieveTotCash) {
        this.cashRecieveTotCash = cashRecieveTotCash;
    }

    public double getCashRecieveTotCheque() {
        return cashRecieveTotCheque;
    }

    public void setCashRecieveTotCheque(double cashRecieveTotCheque) {
        this.cashRecieveTotCheque = cashRecieveTotCheque;
    }

    public double getCashRecieveTotSlip() {
        return cashRecieveTotSlip;
    }

    public void setCashRecieveTotSlip(double cashRecieveTotSlip) {
        this.cashRecieveTotSlip = cashRecieveTotSlip;
    }

    public double getCashRecieveCancelTotCash() {
        return cashRecieveCancelTotCash;
    }

    public void setCashRecieveCancelTotCash(double cashRecieveCancelTotCash) {
        this.cashRecieveCancelTotCash = cashRecieveCancelTotCash;
    }

    public double getCashRecieveCancelTotCheque() {
        return cashRecieveCancelTotCheque;
    }

    public void setCashRecieveCancelTotCheque(double cashRecieveCancelTotCheque) {
        this.cashRecieveCancelTotCheque = cashRecieveCancelTotCheque;
    }

    public double getCashRecieveCancelTotSlip() {
        return cashRecieveCancelTotSlip;
    }

    public void setCashRecieveCancelTotSlip(double cashRecieveCancelTotSlip) {
        this.cashRecieveCancelTotSlip = cashRecieveCancelTotSlip;
    }

    public double getAgentCash() {
        return agentCash;
    }

    public void setAgentCash(double agentCash) {
        this.agentCash = agentCash;
    }

    public double getAgentCheque() {
        return agentCheque;
    }

    public void setAgentCheque(double agentCheque) {
        this.agentCheque = agentCheque;
    }

    public double getAgentSlip() {
        return agentSlip;
    }

    public void setAgentSlip(double agentSlip) {
        this.agentSlip = agentSlip;
    }

    public double getAgentCancelCash() {
        return agentCancelCash;
    }

    public void setAgentCancelCash(double agentCancelCash) {
        this.agentCancelCash = agentCancelCash;
    }

    public double getAgentCancelCheque() {
        return agentCancelCheque;
    }

    public void setAgentCancelCheque(double agentCancelCheque) {
        this.agentCancelCheque = agentCancelCheque;
    }

    public double getAgentCancelSlip() {
        return agentCancelSlip;
    }

    public void setAgentCancelSlip(double agentCancelSlip) {
        this.agentCancelSlip = agentCancelSlip;
    }
}
