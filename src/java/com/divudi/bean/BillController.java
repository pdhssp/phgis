/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.data.BillType;
import com.divudi.data.PaymentMethod;
import com.divudi.data.Sex;
import com.divudi.data.Title;
import com.divudi.ejb.BillBean;
import com.divudi.ejb.BillNumberBean;
import com.divudi.ejb.CommonFunctions;
import com.divudi.ejb.ServiceSessionBean;
import com.divudi.entity.Area;
import com.divudi.facade.BillFacade;
import com.divudi.entity.Bill;
import com.divudi.entity.BillComponent;
import com.divudi.entity.BillEntry;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillItem;
import com.divudi.entity.BillSession;
import com.divudi.entity.BilledBill;
import com.divudi.entity.Department;
import com.divudi.entity.Doctor;
import com.divudi.entity.Institution;
import com.divudi.entity.Item;
import com.divudi.entity.Patient;
import com.divudi.entity.PaymentScheme;
import com.divudi.entity.Person;
import com.divudi.entity.Staff;
import com.divudi.facade.BillComponentFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.BillSessionFacade;
import com.divudi.facade.PatientFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.TemporalType;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class BillController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private BillFacade billFacade;
    @EJB
    private BillItemFacade billItemFacade;
    private boolean printPreview;
    private String patientTabId = "tabNewPt";
    private String ageText = "";
    //Interface Data
    private PaymentScheme paymentScheme;
    private Patient newPatient;
    private Patient searchedPatient;
    private Doctor referredBy;
    private Institution creditCompany;
    private Staff staff;
    private double total;
    private double discount;
    private double netTotal;
    private double cashPaid;
    private double cashBalance;
    private String creditCardRefNo;
    private String chequeRefNo;
    private Institution chequeBank;
    private Institution slipBank;
    private BillItem currentBillItem;
    //Bill Items
    private List<BillComponent> lstBillComponents;
    private List<BillFee> lstBillFees;
    private List<BillItem> lstBillItems;
    private List<BillEntry> lstBillEntries;
    private Integer index;
    @EJB
    private PatientInvestigationFacade patientInvestigationFacade;
    @EJB
    private BillBean billBean;
    @EJB
    CommonFunctions commonFunctions;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private PatientFacade patientFacade;
    @EJB
    private BillNumberBean billNumberBean;
    @EJB
    private BillComponentFacade billComponentFacade;
    @EJB
    private BillFeeFacade billFeeFacade;
    //Temprory Variable
    private Patient tmpPatient;
    List<Bill> bills;
    boolean foreigner = false;
    private String comment = "";
    Date sessionDate;
    private Institution creditBank;
    private Date chequeDate;
    private Date slipDate;

    public Title[] getTitle() {
        return Title.values();
    }

    public Sex[] getSex() {
        return Sex.values();
    }

    public List<Bill> completeOpdCreditBill(String qry) {
        List<Bill> a = null;
        String sql;
        HashMap hash = new HashMap();
        if (qry != null) {
            sql = "select c from BilledBill c where c.paidAmount is null and c.billType= :btp and c.paymentScheme.paymentMethod= :pm and c.cancelledBill is null and c.refundedBill is null and c.retired=false and upper(c.insId) like '%" + qry.toUpperCase() + "%' order by c.creditCompany.name";
            hash.put("btp", BillType.OpdBill);
            hash.put("pm", PaymentMethod.Credit);
            a = getFacade().findBySQL(sql, hash, TemporalType.TIME);
        }
        if (a == null) {
            a = new ArrayList<Bill>();
        }
        return a;
    }

    public Date getSessionDate() {
        if (sessionDate == null) {
            sessionDate = Calendar.getInstance().getTime();
        }
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(boolean foreigner) {
        this.foreigner = foreigner;
    }

    public List<Bill> getBills() {
        if (bills == null) {
            bills = new ArrayList<Bill>();
        }
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    private void savePatient() {
        if (getPatientTabId().equals("tabNewPt")) {
            getNewPatient().setCreater(getSessionController().getLoggedUser());
            getNewPatient().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());

            getNewPatient().getPerson().setCreater(getSessionController().getLoggedUser());
            getNewPatient().getPerson().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());

            getPersonFacade().create(getNewPatient().getPerson());
            getPatientFacade().create(getNewPatient());
            tmpPatient = getNewPatient();

        } else if (getPatientTabId().equals("tabSearchPt")) {
            tmpPatient = getSearchedPatient();
        }
    }

    public void putToBills() {
        bills = new ArrayList<Bill>();
        Set<Department> billDepts = new HashSet<Department>();
        for (BillEntry e : lstBillEntries) {
            billDepts.add(e.getBillItem().getItem().getDepartment());
        }

        for (Department d : billDepts) {
            BilledBill myBill = new BilledBill();
            saveBill(d, myBill);

            List<BillEntry> tmp = new ArrayList<BillEntry>();

            for (BillEntry e : lstBillEntries) {
                if (e.getBillItem().getItem().getDepartment().getId() == d.getId()) {
                    getBillBean().saveBillItem(myBill, e, getSessionController().getLoggedUser());
                    //getBillBean().calculateBillItem(myBill, e);
                    tmp.add(e);
                }
            }

            getBillBean().calculateBillItems(myBill, tmp);
            bills.add(myBill);
        }
    }

    public void settleBill() {
        if (errorCheck()) {
            return;
        }

        savePatient();
        if (getBillBean().checkDepartment(getLstBillEntries()) == 1) {
            BilledBill temp = new BilledBill();
            Bill b = saveBill(lstBillEntries.get(0).getBillItem().getItem().getDepartment(), temp);
            getBillBean().saveBillItems(b, getLstBillEntries(), getSessionController().getLoggedUser());
            getBillBean().calculateBillItems(b, getLstBillEntries());
            getBills().add(b);
        } else {
            putToBills();
        }

        UtilityController.addSuccessMessage("Bill Saved");
        printPreview = true;
    }

    private Bill saveBill(Department bt, BilledBill temp) {

        temp.setDeptId(getBillNumberBean().departmentBillNumberGenerator(getSessionController().getDepartment(), bt));
        temp.setInsId(getBillNumberBean().institutionBillNumberGenerator(getSessionController().getInstitution(), bt));
        temp.setBillType(BillType.OpdBill);

        temp.setDepartment(getSessionController().getLoggedUser().getDepartment());
        temp.setInstitution(getSessionController().getLoggedUser().getDepartment().getInstitution());

        temp.setToDepartment(bt);
        temp.setToInstitution(bt.getInstitution());

        temp.setFromDepartment(getSessionController().getLoggedUser().getDepartment());
        temp.setFromInstitution(getSessionController().getLoggedUser().getDepartment().getInstitution());

        temp.setStaff(staff);
        temp.setReferredBy(referredBy);
        temp.setCreditCompany(creditCompany);

        if (paymentScheme.getPaymentMethod().equals(PaymentMethod.Cheque)) {
            temp.setChequeBank(chequeBank);
            temp.setChequeRefNo(chequeRefNo);
            temp.setChequeDate(chequeDate);
        }

        if (paymentScheme.getPaymentMethod().equals(PaymentMethod.Slip)) {
            temp.setChequeBank(slipBank);
            temp.setChequeDate(slipDate);
            temp.setComments(comment);
        }

        if (paymentScheme.getPaymentMethod().equals(PaymentMethod.Card)) {
            temp.setCreditCardRefNo(creditCardRefNo);
            temp.setChequeBank(creditBank);
        }

        temp.setBillDate(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        temp.setBillTime(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        temp.setPatient(tmpPatient);
//        temp.setPatientEncounter(patientEncounter);
        temp.setPaymentScheme(getPaymentScheme());

        temp.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        temp.setCreater(sessionController.getLoggedUser());
        getFacade().create(temp);
        return temp;

    }

    private boolean errorCheck() {
        if (getPatientTabId().toString().equals("tabNewPt")) {

            if (getNewPatient().getPerson().getName() == null || getNewPatient().getPerson().getName().trim().equals("") || getNewPatient().getPerson().getSex() == null || getAgeText() == null) {
                UtilityController.addErrorMessage("Can not bill without Patient Name, Age or Sex.");
                return true;
            }

            if (!getCommonFunctions().checkAgeSex(getNewPatient().getPerson().getDob(), getNewPatient().getPerson().getSex(), getNewPatient().getPerson().getTitle())) {
                UtilityController.addErrorMessage("Check Title,Age,Sex");
                return true;
            }

            if (getNewPatient().getPerson().getPhone().length() < 1) {
                UtilityController.addErrorMessage("Phone Number is Required it should be fill");
                return true;
            }

        }
        if (getLstBillEntries().isEmpty()) {

            UtilityController.addErrorMessage("No investigations are added to the bill to settle");
            return true;
        }

        if (paymentScheme != null && paymentScheme.getPaymentMethod() != null && paymentScheme.getPaymentMethod() == PaymentMethod.Cheque) {
            if (getChequeBank() == null || getChequeRefNo() == null || getChequeDate() == null) {
                UtilityController.addErrorMessage("Please select Cheque Number,Bank and Cheque Date");
                return true;
            }

        }


        if (paymentScheme != null && paymentScheme.getPaymentMethod() != null && paymentScheme.getPaymentMethod() == PaymentMethod.Slip) {
            if (getSlipBank() == null || getComment() == null || getSlipDate() == null) {
                UtilityController.addErrorMessage("Please Fill Memo,Bank and Slip Date ");
                return true;
            }

        }

        if (paymentScheme != null && paymentScheme.getPaymentMethod() != null && paymentScheme.getPaymentMethod() == PaymentMethod.Card) {
            if (getCreditBank() == null || getCreditCardRefNo() == null) {
                UtilityController.addErrorMessage("Please Fill Credit Card Number and Bank");
                return true;
            }

            if (getCreditCardRefNo().trim().length() < 16) {
                UtilityController.addErrorMessage("Enter 16 Digit");
                return true;
            }

        }

        if (paymentScheme != null && paymentScheme.getPaymentMethod() != null && paymentScheme.getPaymentMethod() == PaymentMethod.Credit) {
            if (getCreditCompany() == null) {
                UtilityController.addErrorMessage("Please Select Credit Company");
                return true;
            }

        }

        if (getCreditCompany() != null && getPaymentScheme().getPaymentMethod() != PaymentMethod.Credit) {
            UtilityController.addErrorMessage("Please Select Payment Scheme with Credit");
            return true;
        }

        if (paymentScheme.getPaymentMethod() == PaymentMethod.Cash) {
            if (cashPaid == 0.0) {
                UtilityController.addErrorMessage("Please select tendered amount correctly");
                return true;
            }
            if (cashPaid < getNetTotal()) {
                UtilityController.addErrorMessage("Please select tendered amount correctly");
                return true;
            }
        }

        return false;
    }
    List<BillSession> billSessions;
    @EJB
    BillSessionFacade billSessionFacade;
    @EJB
    ServiceSessionBean serviceSessionBean;

    public List<BillSession> getBillSessions() {
        System.out.println("getting bill sessions 1");
        if (lastBillItem != null && lastBillItem.getItem() != null) {
            billSessions = getServiceSessionBean().getBillSessions(lastBillItem.getItem(), getSessionDate());
            System.out.println("bill sessions - 2" + billSessions);
        } else {
            System.out.println("items null");
        }
        return billSessions;
    }

    public ServiceSessionBean getServiceSessionBean() {
        return serviceSessionBean;
    }

    public void setServiceSessionBean(ServiceSessionBean serviceSessionBean) {
        this.serviceSessionBean = serviceSessionBean;
    }

    public BillSessionFacade getBillSessionFacade() {
        return billSessionFacade;
    }

    public void setBillSessionFacade(BillSessionFacade billSessionFacade) {
        this.billSessionFacade = billSessionFacade;
    }

    public void setBillSessions(List<BillSession> billSessions) {
        this.billSessions = billSessions;
    }

    public void addToBill() {

        if (getCurrentBillItem() == null) {
            UtilityController.addErrorMessage("Nothing to add");
            return;
        }
        if (getCurrentBillItem().getItem() == null) {
            UtilityController.addErrorMessage("Please select an Item");
            return;
        }
        if (getCurrentBillItem().getItem().getTotal() == 0.0) {
            UtilityController.addErrorMessage("Please corect item fee");
            return;
        }

        if (getCurrentBillItem().getItem().getDepartment() == null) {
            UtilityController.addErrorMessage("Please set Department to Item");
            return;
        }

        if (getCurrentBillItem().getItem().getCategory() == null) {
            UtilityController.addErrorMessage("Please set Category to Item");
            return;
        }

        getCurrentBillItem().setSessionDate(sessionDate);
        lastBillItem = getCurrentBillItem();
        BillEntry addingEntry = new BillEntry();
        addingEntry.setBillItem(getCurrentBillItem());
        addingEntry.setLstBillComponents(getBillBean().billComponentsFromBillItem(getCurrentBillItem()));
        addingEntry.setLstBillFees(getBillBean().billFeefromBillItem(getCurrentBillItem()));
        addingEntry.setLstBillSessions(getBillBean().billSessionsfromBillItem(getCurrentBillItem()));
        getLstBillEntries().add(addingEntry);
        getCurrentBillItem().setRate(getBillBean().billItemRate(addingEntry));
        getCurrentBillItem().setQty(1.0);
        getCurrentBillItem().setNetValue(getCurrentBillItem().getRate() * getCurrentBillItem().getQty()); // Price == Rate as Qty is 1 here

        calTotals();
        if (getCurrentBillItem().getNetValue() == 0.0) {
            UtilityController.addErrorMessage("Please enter the rate");
            return;
        }
        clearBillItemValues();
        //UtilityController.addSuccessMessage("Item Added");
    }

    public void clearBillItemValues() {
        currentBillItem = null;

        recreateBillItems();
    }

    private void clearBillValues() {
        setNewPatient(null);
        setSearchedPatient(null);
        setReferredBy(null);
        setSessionDate(null);
        setCreditCompany(null);
        setAgeText("");
        setBills(null);
        setComment("");
        setChequeBank(null);
        setPaymentScheme(null);
        setSlipBank(null);
        setSlipDate(null);
        setChequeRefNo("");
        setChequeDate(null);
        setCreditCardRefNo("");

        currentBillItem = null;
        setLstBillComponents(null);
        setLstBillEntries(null);
        setLstBillFees(null);
        setStaff(null);
        lstBillEntries = new ArrayList<BillEntry>();
        setForeigner(false);
        setSessionDate(Calendar.getInstance().getTime());
        calTotals();

        setCashPaid(0.0);
        setDiscount(0.0);
        setCashBalance(0.0);
    }

    private void recreateBillItems() {
        //Only remove Total and BillComponenbts,Fee and Sessions. NOT bill Entries
        lstBillComponents = null;
        lstBillFees = null;
        lstBillItems = null;

        //billTotal = 0.0;
    }

    public void calTotals() {
        System.out.println("calculating totals");
        double disPercent = 0.0;
        double billGross = 0.0;
        double billDis = 0.0;
        double billNet = 0.0;
        for (BillEntry be : getLstBillEntries()) {
            System.out.println("bill item entry");
            double entryGross = 0.0;
            double entryDis = 0.0;
            double entryNet = 0.0;
            BillItem bi = be.getBillItem();
            for (BillFee bf : be.getLstBillFees()) {
                System.out.println("bill item fee");
                if (bf.getBillItem().getItem().getDiscountAllowed() == false && bf.getBillItem().getItem().isUserChangable() == false) {
                    System.out.println("billing for not discount allowed");
                    if (isForeigner()) {
                        bf.setFeeValue(bf.getFee().getFfee());
                    } else {
                        bf.setFeeValue(bf.getFee().getFee());
                    }
                } else if (bf.getBillItem().getItem().isUserChangable()) {
                    System.out.println("billing for user Changable");

                } else if (getCreditCompany() != null) {
                    System.out.println("billing for company " + getCreditCompany().getName());
                    if (isForeigner()) {
                        bf.setFeeValue(bf.getFee().getFfee() / 100 * (100 - getCreditCompany().getLabBillDiscount()));
                    } else {
                        bf.setFeeValue(bf.getFee().getFee() / 100 * (100 - getCreditCompany().getLabBillDiscount()));
                    }
                } else {
                    System.out.println("billing for payment method");
                    if (isForeigner()) {
                        if (getPaymentScheme() == null) {
                            bf.setFeeValue(bf.getFee().getFfee());
                        } else {
                            System.out.println("payment method for foreigner is " + getPaymentScheme());
                            if (bf.getFee().getFfee() != null && getPaymentScheme().getDiscountPercentForLabBill() != null) {
                                disPercent = getPaymentScheme().getDiscountPercentForLabBill();
                                if (disPercent > bf.getBillItem().getItem().getDepartment().getMaxDiscount()) {
                                    disPercent = bf.getBillItem().getItem().getDepartment().getMaxDiscount();
                                }
                                bf.setFeeValue(bf.getFee().getFfee() / 100 * (100 - disPercent));
                            }
                        }
                    } else {
                        if (getPaymentScheme() == null) {
                            bf.setFeeValue(bf.getFee().getFee());
                        } else {
                            System.out.println("payment method for local is " + getPaymentScheme());

                            if (bf.getFee().getFee() != null && getPaymentScheme().getDiscountPercentForLabBill() != null) {
                                disPercent = getPaymentScheme().getDiscountPercentForLabBill();
                                if (disPercent > bf.getBillItem().getItem().getDepartment().getMaxDiscount()) {
                                    disPercent = bf.getBillItem().getItem().getDepartment().getMaxDiscount();
                                }
                                bf.setFeeValue(bf.getFee().getFee() / 100 * (100 - disPercent));
                            }
                        }
                    }
                }

                if (isForeigner()) {
                    entryGross = entryGross + bf.getFee().getFfee();
                    //entryGross = entryGross + bf.getFeeValue();
                    System.out.println("fee foreign gross is " + bf.getFee().getFfee());
                } else {
                    entryGross = entryGross + bf.getFee().getFee();
                    //entryGross = entryGross + bf.getFeeValue();
                    System.out.println("fee gross is " + bf.getFee().getFee());
                }

                entryNet = entryNet + bf.getFeeValue();
                entryDis = entryDis + (entryGross - entryNet);
                System.out.println("fee net is " + bf.getFeeValue());

            }

            bi.setDiscount(entryDis);
            bi.setGrossValue(entryGross);
            bi.setNetValue(entryNet);

            System.out.println("item is " + bi.getItem().getName());
            System.out.println("item gross is " + bi.getGrossValue());
            System.out.println("item net is " + bi.getNetValue());
            System.out.println("item dis is " + bi.getDiscount());

            billGross = billGross + entryGross;
            billNet = billNet + entryNet;
            //     billDis = billDis + entryDis;
        }
        setDiscount(billGross - billNet);
        setTotal(billGross);
        setNetTotal(billNet);
        System.out.println("bill tot is " + billGross);
    }

    public void feeChanged() {
        lstBillItems = null;
        getLstBillItems();
        calTotals();
    }

    public void markAsForeigner() {
        setForeigner(true);
        calTotals();
    }

    public void markAsLocal() {
        setForeigner(false);
        calTotals();
    }

    public String prepareNewBill() {
        clearBillItemValues();
        clearBillValues();
        setPrintPreview(true);
        printPreview = false;
        return "opd_bill";
    }

    public void removeBillItem() {

        //TODO: Need to add Logic
        System.out.println(getIndex());
        if (getIndex() != null) {
            //  boolean remove;
            BillEntry temp = getLstBillEntries().get(getIndex());
            System.out.println("Removed Item:" + temp.getBillItem().getNetValue());
            recreateList(temp);
            // remove = getLstBillEntries().remove(getIndex());

            //  getLstBillEntries().remove(index);
            //System.out.println("Is Removed:" + remove);

            calTotals();

        }

    }

    public void recreateList(BillEntry r) {
        List<BillEntry> temp = new ArrayList<BillEntry>();
        for (BillEntry b : getLstBillEntries()) {
            if (b.getBillItem().getItem() != r.getBillItem().getItem()) {
                temp.add(b);
                System.out.println(b.getBillItem().getNetValue());
            }
        }
        lstBillEntries = temp;
        lstBillComponents = getBillBean().billComponentsFromBillEntries(lstBillEntries);
        lstBillFees = getBillBean().billFeesFromBillEntries(lstBillEntries);
    }

    public void onTabChange(TabChangeEvent event) {
        setPatientTabId(event.getTab().getId());

    }

    public BillFacade getEjbFacade() {
        return billFacade;
    }

    public void setEjbFacade(BillFacade ejbFacade) {
        this.billFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public BillController() {
    }

    private BillFacade getFacade() {
        return billFacade;
    }

    public boolean isPrintPreview() {
        return printPreview;
    }

    public void setPrintPreview(boolean printPreview) {
        this.printPreview = printPreview;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public void setBillFacade(BillFacade billFacade) {
        this.billFacade = billFacade;
    }

    public PaymentScheme getPaymentScheme() {
        if (paymentScheme == null) {
            paymentScheme = new PaymentScheme();
        }
        return paymentScheme;
    }

    public void setPaymentScheme(PaymentScheme paymentScheme) {
        this.paymentScheme = paymentScheme;
        calTotals();
    }

    public String getPatientTabId() {
        return patientTabId;
    }

    public void setPatientTabId(String patientTabId) {
        this.patientTabId = patientTabId;
    }

    public Patient getNewPatient() {
        if (newPatient == null) {
            newPatient = new Patient();
            Person p = new Person();

            newPatient.setPerson(p);
        }
        return newPatient;
    }

    public void setNewPatient(Patient newPatient) {
        this.newPatient = newPatient;
    }

    public Patient getSearchedPatient() {
        return searchedPatient;
    }

    public void setSearchedPatient(Patient searchedPatient) {
        this.searchedPatient = searchedPatient;
    }

    public Doctor getReferredBy() {

        return referredBy;
    }

    public void setReferredBy(Doctor referredBy) {
        this.referredBy = referredBy;
    }

    public Institution getCreditCompany() {

        return creditCompany;
    }

    public void setCreditCompany(Institution creditCompany) {
        this.creditCompany = creditCompany;

    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public List<BillComponent> getLstBillComponents() {
        if (lstBillComponents == null) {
            lstBillComponents = getBillBean().billComponentsFromBillEntries(getLstBillEntries());
        }

        return lstBillComponents;
    }

    public void setLstBillComponents(List<BillComponent> lstBillComponents) {
        this.lstBillComponents = lstBillComponents;
    }

    public List<BillFee> getLstBillFees() {
        if (lstBillFees == null) {
            lstBillFees = getBillBean().billFeesFromBillEntries(getLstBillEntries());
        }

        return lstBillFees;
    }

    public void setLstBillFees(List<BillFee> lstBillFees) {
        this.lstBillFees = lstBillFees;
    }

    public List<BillItem> getLstBillItems() {
        if (lstBillItems == null) {
            lstBillItems = new ArrayList<BillItem>();
        }
        return lstBillItems;
    }

    public void setLstBillItems(List<BillItem> lstBillItems) {
        this.lstBillItems = lstBillItems;
    }

    public List<BillEntry> getLstBillEntries() {
        if (lstBillEntries == null) {
            lstBillEntries = new ArrayList<BillEntry>();
        }
        return lstBillEntries;
    }

    public void setLstBillEntries(List<BillEntry> lstBillEntries) {
        this.lstBillEntries = lstBillEntries;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }

    public double getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(double cashPaid) {
        this.cashPaid = cashPaid;
//        cashBalance = cashPaid - getNetTotal();
    }

    public double getCashBalance() {
        cashBalance = cashPaid - netTotal;
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getCreditCardRefNo() {
        return creditCardRefNo;
    }

    public void setCreditCardRefNo(String creditCardRefNo) {
        this.creditCardRefNo = creditCardRefNo;
    }

    public String getChequeRefNo() {
        return chequeRefNo;
    }

    public void setChequeRefNo(String chequeRefNo) {
        this.chequeRefNo = chequeRefNo;
    }

    public Institution getChequeBank() {
      
        return chequeBank;
    }

    public void setChequeBank(Institution chequeBank) {
        this.chequeBank = chequeBank;
    }

    public BillItem getCurrentBillItem() {
        if (currentBillItem == null) {
            currentBillItem = new BillItem();
        }

        return currentBillItem;
    }
    BillItem lastBillItem;

    public BillItem getLastBillItem() {
        return lastBillItem;
    }

    public void setLastBillItem(BillItem lastBillItem) {
        this.lastBillItem = lastBillItem;
    }

    public void setCurrentBillItem(BillItem currentBillItem) {
        this.currentBillItem = currentBillItem;
        lastBillItem = currentBillItem;
    }

    public String getAgeText() {
        ageText = getNewPatient().getAge();
        if (ageText.startsWith("0 days")) {
            return "";
        }
        return ageText;
    }

    public void setAgeText(String ageText) {
        this.ageText = ageText;
        getNewPatient().getPerson().setDob(getCommonFunctions().guessDob(ageText));
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public BillBean getBillBean() {
        return billBean;
    }

    public void setBillBean(BillBean billBean) {
        this.billBean = billBean;


    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    public PatientFacade getPatientFacade() {
        return patientFacade;
    }

    public void setPatientFacade(PatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    public BillNumberBean getBillNumberBean() {
        return billNumberBean;
    }

    public void setBillNumberBean(BillNumberBean billNumberBean) {
        this.billNumberBean = billNumberBean;


    }

    public BillComponentFacade getBillComponentFacade() {
        return billComponentFacade;
    }

    public void setBillComponentFacade(BillComponentFacade billComponentFacade) {
        this.billComponentFacade = billComponentFacade;
    }

    public BillFeeFacade getBillFeeFacade() {
        return billFeeFacade;
    }

    public void setBillFeeFacade(BillFeeFacade billFeeFacade) {
        this.billFeeFacade = billFeeFacade;
    }

    private Patient getTmpPatient() {
        return tmpPatient;
    }

    public void setTmpPatient(Patient tmpPatient) {
        this.tmpPatient = tmpPatient;
    }

    public PatientInvestigationFacade getPatientInvestigationFacade() {
        return patientInvestigationFacade;
    }

    public void setPatientInvestigationFacade(PatientInvestigationFacade patientInvestigationFacade) {
        this.patientInvestigationFacade = patientInvestigationFacade;
    }

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;


    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Institution getSlipBank() {       
        return slipBank;
    }

    public void setSlipBank(Institution slipBank) {
        this.slipBank = slipBank;
    }

    public Institution getCreditBank() {
        return creditBank;
    }

    public void setCreditBank(Institution creditBank) {
        this.creditBank = creditBank;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Date getSlipDate() {
        return slipDate;
    }

    public void setSlipDate(Date slipDate) {
        this.slipDate = slipDate;
    }

    /**
     *
     */
    @FacesConverter("bill")
    public static class BillControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BillController controller = (BillController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "billController");
            return controller.getBillFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Bill) {
                Bill o = (Bill) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + BillController.class.getName());
            }
        }
    }
}
