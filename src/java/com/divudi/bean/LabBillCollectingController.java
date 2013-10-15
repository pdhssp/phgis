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
import com.divudi.data.DepartmentType;
import com.divudi.ejb.BillBean;
import com.divudi.ejb.BillNumberBean;
import com.divudi.ejb.CommonFunctions;
import com.divudi.facade.BillFacade;
import com.divudi.entity.Bill;
import com.divudi.entity.BillComponent;
import com.divudi.entity.BillEntry;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillItem;
import com.divudi.entity.BilledBill;
import com.divudi.entity.Department;
import com.divudi.entity.Doctor;
import com.divudi.entity.Institution;
import com.divudi.entity.Patient;
import com.divudi.entity.PatientEncounter;
import com.divudi.entity.PaymentScheme;
import com.divudi.entity.Person;
import com.divudi.entity.Staff;
import com.divudi.entity.form.HealthForm;
import com.divudi.entity.form.FilledHealthForm;
import com.divudi.facade.BillComponentFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.PatientFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import javax.inject.Inject;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public  class LabBillCollectingController implements Serializable {

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
    private Institution collectingCentre;
    private Staff staff;
    private double total;
    private double discount;
    private double netTotal;
    private double cashPaid;
    private double cashBalance;
    private String creditCardRefNo;
    private String chequeRefNo;
    private Institution chequeBank;
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
        Set<Department> billDepts = new HashSet<Department>();
        //List<Bill> bills = new ArrayList<Bill>();
        for (BillEntry e : lstBillEntries) {
            billDepts.add(e.getBillItem().getItem().getDepartment());
        }
        for (Department d : billDepts) {
            BilledBill myBill = new BilledBill();
            saveBill(d, myBill);

            for (BillEntry e : lstBillEntries) {
                if (e.getBillItem().getItem().getDepartment().getId() == d.getId()) {
                    //e.setBill(myBill);
                    saveBillItem(myBill, e);
                    calculateBillItem(myBill, e);
                }
            }
            //set Bill Item Properties like bill TOtal, Discount
            //Save Bill
        }

//        for (Bill myBill : bills) {
//            for (BillEntry e : lstBillEntries) {
//                if (e.getBill() == myBill) {
//                    ///Save Bill Items
//                }
//            }
//            // Save Bill Changes
//        }
    }

    private int checkDepartment() {

        int c = 0;
        Department tdep = new Department();
        tdep.setId(0L);
        // System.out.println("Check Dept 1 " + c);
        //Createa a list of bills
        for (BillEntry be : getLstBillEntries()) {
            if (be.getBillItem().getItem().getDepartment().getId() != tdep.getId()) {
                tdep = be.getBillItem().getItem().getDepartment();
                c++;
                //  System.out.println("Check Dept  " + be.getBillItem().getItem().getDepartment().getName());
            }
        }
        System.out.println("Check Dept 4 " + c);

        return c;
    }

    public void settleBill() {
        if (errorCheck()) {
            return;
        }

        savePatient();
        if (checkDepartment() == 1) {
            BilledBill temp = new BilledBill();
            Bill b = saveBill(lstBillEntries.get(0).getBillItem().getItem().getDepartment(), temp);
            saveBillItems(b);
            calculateBillItems(b);
        } else {
            putToBills();
        }

        UtilityController.addSuccessMessage("Bill Saved");
        printPreview = true;
    }

    private void calculateBillItem(Bill bill, BillEntry e) {
        double s = 0.0;
        double i = 0.0;
        double g = 0.0;
        for (BillFee bf : e.getLstBillFees()) {
            g = g + bf.getFee().getFee();
            if (bf.getFee().getStaff() == null) {
                i = i + bf.getFeeValue();
            } else {
                s = s + bf.getFeeValue();
            }
            if (bf.getId() == null || bf.getId() == 0) {
                getBillFeeFacade().create(bf);
            } else {
                getBillFeeFacade().edit(bf);
            }
        }

        bill.setStaffFee(s);
        bill.setPerformInstitutionFee(i);
        bill.setTotal(g);
        bill.setNetTotal(g);
        getFacade().edit(bill);
    }

    private void calculateBillItems(Bill bill) {
        double s = 0.0;
        double i = 0.0;
        double g = 0.0;
        for (BillEntry e : getLstBillEntries()) {
            for (BillFee bf : e.getLstBillFees()) {
                g = g + bf.getFee().getFee();
                if (bf.getFee().getStaff() == null) {
                    i = i + bf.getFeeValue();
                } else {
                    s = s + bf.getFeeValue();
                }
                if (bf.getId() == null || bf.getId() == 0) {
                    getBillFeeFacade().create(bf);
                } else {
                    getBillFeeFacade().edit(bf);
                }
            }
        }
        bill.setStaffFee(s);
        bill.setPerformInstitutionFee(i);
        bill.setTotal(g);
        bill.setNetTotal(g);
        getFacade().edit(bill);
    }

    private void savePatientInvestigation(BillEntry e, BillComponent bc) {
        FilledHealthForm ptIx = new FilledHealthForm();
        ptIx.setBillItem(e.getBillItem());
        ptIx.setBillComponent(bc);
        ptIx.setPackege(bc.getPackege());
        ptIx.setApproved(Boolean.FALSE);
        ptIx.setCancelled(Boolean.FALSE);
        ptIx.setCollected(Boolean.FALSE);
        ptIx.setDataEntered(Boolean.FALSE);
        ptIx.setInvestigation((HealthForm) bc.getItem());
        ptIx.setOutsourced(Boolean.FALSE);
        ptIx.setPatient(tmpPatient);
//        ptIx.setEncounter(tmpPatientEncounter);
        ptIx.setPerformed(Boolean.FALSE);
        ptIx.setPrinted(Boolean.FALSE);
        ptIx.setPrinted(Boolean.FALSE);
        ptIx.setReceived(Boolean.FALSE);

        ptIx.setReceiveDepartment(e.getBillItem().getItem().getDepartment());
        ptIx.setApproveDepartment(e.getBillItem().getItem().getDepartment());
        ptIx.setDataEntryDepartment(e.getBillItem().getItem().getDepartment());
        ptIx.setPrintingDepartment(e.getBillItem().getItem().getDepartment());
        ptIx.setPerformDepartment(e.getBillItem().getItem().getDepartment());


        if (e.getBillItem().getItem() == null) {
            UtilityController.addErrorMessage("No Bill Item Selected");
        } else if (e.getBillItem().getItem().getDepartment() == null) {
            UtilityController.addErrorMessage("Under administration, add a Department for this investigation " + e.getBillItem().getItem().getName());
        } else if (e.getBillItem().getItem().getDepartment().getInstitution() == null) {
            UtilityController.addErrorMessage("Under administration, add an Institution for the department " + e.getBillItem().getItem().getDepartment());
        } else if (e.getBillItem().getItem().getDepartment().getInstitution() != sessionController.getLoggedUser().getInstitution()) {
            ptIx.setOutsourcedInstitution(e.getBillItem().getItem().getInstitution());
        }

        ptIx.setRetired(false);
        getPatientInvestigationFacade().create(ptIx);

    }

    private void saveBillComponent(BillEntry e, Bill b) {
        for (BillComponent bc : e.getLstBillComponents()) {
            bc.setBill(b);
            getBillComponentFacade().create(bc);
            if (bc.getItem() instanceof HealthForm) {
                savePatientInvestigation(e, bc);
            }

        }
    }

    private void saveBillFee(BillEntry e, Bill b) {
        for (BillFee bf : e.getLstBillFees()) {
            bf.setBill(b);
            getBillFeeFacade().create(bf);

        }
    }

    private void saveBillItem(Bill b, BillEntry e) {
        //   BillItem temBi = e.getBillItem();
        e.getBillItem().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        e.getBillItem().setCreater(getSessionController().getLoggedUser());
        //  e.getBillItem().setDeptId(e.getBillItem().getItem().getDepartment().getId());
        e.getBillItem().setBill(b);
        getBillItemFacade().create(e.getBillItem());
        //System.out.println("Saving Bill Item : " + temBi.getItem().getName());

        saveBillComponent(e, b);
        saveBillFee(e, b);
    }

    private void saveBillItems(Bill b) {
        for (BillEntry e : getLstBillEntries()) {
            // BillItem temBi = e.getBillItem();
            e.getBillItem().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            e.getBillItem().setCreater(getSessionController().getLoggedUser());
            e.getBillItem().setBill(b);
            getBillItemFacade().create(e.getBillItem());
            //System.out.println("Saving Bill Item : " + e.getBillItem().getItem().getName());

            saveBillComponent(e, b);
            saveBillFee(e, b);
        }
    }

    private Bill saveBill(Department bt, BilledBill temp) {
        temp.setDeptId(getBillNumberBean().departmentBillNumberGenerator(getSessionController().getDepartment() ,  bt));
        temp.setInsId(getBillNumberBean().institutionBillNumberGenerator(getSessionController().getInstitution(), bt  ) );
        //getCurrent().setCashBalance(cashBalance); 
        //getCurrent().setCashPaid(cashPaid);
        //  temp.setBillType(bt);
        temp.setBillType(BillType.LabBill);

        temp.setDepartment(getSessionController().getLoggedUser().getDepartment());
        temp.setInstitution(getSessionController().getLoggedUser().getDepartment().getInstitution());
        
        temp.setFromDepartment(getSessionController().getLoggedUser().getDepartment());
        temp.setFromInstitution(getSessionController().getLoggedUser().getDepartment().getInstitution());

        temp.setToDepartment(bt);
        temp.setToInstitution(bt.getInstitution());        

        temp.setCollectingCentre(collectingCentre);

        temp.setStaff(staff);
        temp.setReferredBy(referredBy);
        temp.setCreditCardRefNo(creditCardRefNo);
        temp.setChequeBank(chequeBank);
        temp.setChequeRefNo(chequeRefNo);

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

        if (collectingCentre == null) {
            UtilityController.addErrorMessage("Please Select Collecting Centre");
            return true;

        }

        return false;
    }

    public void addToBill() {

        if (getCurrentBillItem() == null) {
            UtilityController.addErrorMessage("Nothing to add");
            return;
        }
        if (getCurrentBillItem().getItem() == null) {
            UtilityController.addErrorMessage("Please select an investigation");
            return;
        }

//        getCurrent().setDepartment(sessionController.getLoggedUser().getDepartment());
//        getCurrent().setInstitution(sessionController.getLoggedUser().getInstitution());
//        getCurrentBillItem().setBill(getCurrent());
        BillEntry addingEntry = new BillEntry();
        addingEntry.setBillItem(getCurrentBillItem());
        addingEntry.setLstBillComponents(getBillBean().billComponentsFromBillItem(getCurrentBillItem()));
        addingEntry.setLstBillFees(getBillBean().billFeefromBillItem(getCurrentBillItem()));
        addingEntry.setLstBillSessions(getBillBean().billSessionsfromBillItem(getCurrentBillItem()));
        lstBillEntries.add(addingEntry);
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
        setCurrentBillItem(null);
        recreateBillItems();
    }

    private void recreateBillItems() {
        //Only remove Total and BillComponenbts,Fee and Sessions. NOT bill Entries
        lstBillComponents = null;
        lstBillFees = null;
        lstBillItems = null;

        //billTotal = 0.0;
    }

    public void calTotals() {
        double tot = 0.0;
        double dis = 0.0;

        for (BillEntry be : getLstBillEntries()) {
            BillItem bi = be.getBillItem();
            bi.setDiscount(0.0);
            bi.setGrossValue(0.0);
            bi.setNetValue(0.0);

            for (BillFee bf : be.getLstBillFees()) {
                if (bf.getBillItem().getItem().isUserChangable() && bf.getBillItem().getItem().getDiscountAllowed() != true) {
                    System.out.println("Total is " + tot);
                    System.out.println("Bill Fee value is " + bf.getFeeValue());
                    tot += bf.getFeeValue();
                    System.out.println("After addition is " + tot);
                    bf.getBillItem().setNetValue(bf.getBillItem().getNetValue() + bf.getFeeValue());
                    bf.getBillItem().setGrossValue(bf.getBillItem().getGrossValue() + bf.getFeeValue());

                } else if (getCreditCompany() != null) {

                    if (bf.getBillItem().getItem().getDiscountAllowed() != null && bf.getBillItem().getItem().getDiscountAllowed() == true) {

                        bf.setFeeValue(bf.getFee().getFee() / 100 * (100 - getCreditCompany().getLabBillDiscount()));
                        dis += (bf.getFee().getFee() / 100 * (getCreditCompany().getLabBillDiscount()));
                        bf.getBillItem().setDiscount(bf.getBillItem().getDiscount() + bf.getFee().getFee() / 100 * (getCreditCompany().getLabBillDiscount()));
                        tot += bf.getFee().getFee();
                        bf.getBillItem().setGrossValue(bf.getBillItem().getGrossValue() + bf.getFee().getFee());
                        bf.getBillItem().setNetValue(bf.getBillItem().getNetValue() + bf.getBillItem().getGrossValue() - bf.getBillItem().getDiscount());
                    } else {

                        tot = tot + bf.getFeeValue();
                        bf.setFeeValue(bf.getFee().getFee());
                        bf.getBillItem().setGrossValue(bf.getBillItem().getGrossValue() + bf.getFee().getFee());
                        bf.getBillItem().setNetValue(bf.getBillItem().getNetValue() + bf.getFee().getFee());
                    }
                } else {
                    System.out.println("12");
                    if (bf.getBillItem().getItem().getDiscountAllowed() != null && bf.getBillItem().getItem().getDiscountAllowed() == true) {
                        if (getPaymentScheme() == null) {
                            bf.setFeeValue(bf.getFee().getFee());
                            dis = 0.0;
                            bf.getBillItem().setDiscount(0.0);
                        } else {
                            bf.setFeeValue(bf.getFee().getFee() / 100 * (100 - getPaymentScheme().getDiscountPercentForLabBill()));
                            dis += (bf.getFee().getFee() / 100 * (getPaymentScheme().getDiscountPercentForLabBill()));
                            bf.getBillItem().setDiscount(bf.getBillItem().getDiscount() + bf.getFee().getFee() / 100 * (getPaymentScheme().getDiscountPercentForLabBill()));
                        }
                        tot += bf.getFee().getFee();
                        bf.getBillItem().setGrossValue(bf.getBillItem().getGrossValue() + bf.getFee().getFee());

                        bf.getBillItem().setNetValue(bf.getBillItem().getNetValue() + bf.getBillItem().getGrossValue() - bf.getBillItem().getDiscount());
                    } else {
                        System.out.println("13");
                        tot = tot + bf.getFeeValue();
                        bf.setFeeValue(bf.getFee().getFee());
                        bf.getBillItem().setGrossValue(bf.getBillItem().getGrossValue() + bf.getFee().getFee());
                        bf.getBillItem().setNetValue(bf.getBillItem().getNetValue() + bf.getFee().getFee());
                    }
                }
            }
        }
        setDiscount(dis);
        setTotal(tot);        
        setNetTotal(tot - dis);
        
        

    }

    public void feeChanged() {
        lstBillItems = null;
        getLstBillItems();
        calTotals();
    }

    public void prepareNewBill() {
    }

    public void removeBillItem() {

        //TODO: Need to add Logic
        System.out.println(getIndex());
        if (getIndex() != null) {
            boolean remove;
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

    public LabBillCollectingController() {
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
        return paymentScheme;
    }

    public void setPaymentScheme(PaymentScheme paymentScheme) {
        this.paymentScheme = paymentScheme;
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
    }

    public double getCashBalance() {
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
        if (chequeBank == null) {
            chequeBank = new Institution();
        }

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

    public void setCurrentBillItem(BillItem currentBillItem) {
        this.currentBillItem = currentBillItem;
    }

    public String getAgeText() {
        ageText = getNewPatient().getAge();
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

    public Institution getCollectingCentre() {
        return collectingCentre;
    }

    public void setCollectingCentre(Institution collectingCentre) {
        this.collectingCentre = collectingCentre;
    }

    /**
     *
     */
    @FacesConverter(forClass = Bill.class)
    public static class BillControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LabBillCollectingController controller = (LabBillCollectingController) facesContext.getApplication().getELResolver().
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
                        + object.getClass().getName() + "; expected type: " + LabBillCollectingController.class.getName());
            }
        }
    }
}
