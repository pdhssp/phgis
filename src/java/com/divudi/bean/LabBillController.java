
/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package com.divudi.bean;

import com.divudi.entity.BillEntry;
import com.divudi.data.BillType;
import com.divudi.data.PaymentMethod;
import com.divudi.ejb.BillBean;
import com.divudi.ejb.BillNumberBean;
import com.divudi.ejb.CommonFunctions;
import com.divudi.entity.BillComponent;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillItem;
import com.divudi.entity.form.HealthForm;
import com.divudi.facade.BillFacade;
import com.divudi.entity.Bill;
import com.divudi.entity.BilledBill;
import com.divudi.entity.Item;
import com.divudi.entity.Patient;
import com.divudi.entity.PatientEncounter;
import com.divudi.entity.form.PatientHealthForm;
import com.divudi.entity.PaymentScheme;
import com.divudi.entity.Person;
import com.divudi.facade.BillComponentFacade;
import com.divudi.facade.BillEntryFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.BillSessionFacade;
import com.divudi.facade.FeeFacade;
import com.divudi.facade.ItemFacade;
import com.divudi.facade.PatientEncounterFacade;
import com.divudi.facade.PatientFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public  class LabBillController implements Serializable {
//    UID

    private static final long serialVersionUID = 1L;
//    Managed Properties
    @Inject
    SessionController sessionController;
    //    EJBs
    @EJB
    private BillFacade ejbFacade;
    @EJB
    private BillItemFacade billItemFacade;
    @EJB
    PatientFacade patientFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    FeeFacade feeFacade;
    @EJB
    private PatientInvestigationFacade ptIxFacade;
    @EJB
    BillEntryFacade billEntryFacade;
    @EJB
    private PatientEncounterFacade patientEncounterFacade;
    @EJB
    private BillComponentFacade billComponentFacade;
    @EJB
    private BillFeeFacade billFeeFacade;
    @EJB
    private BillSessionFacade billSessionFacade;
    @EJB
    ItemFacade itemFacade;
    //
    @EJB
    private BillBean billBean;
    @EJB
    BillNumberBean billNumberBean;
    @EJB
    private CommonFunctions commonFunction;
    //
    String patientTabId = "tabNewPt";
    // Bill Values
    private Bill current;
    private List<Bill> items = null;
    String selectText = "";
    private Patient newPatient;
    private Patient searchedPatient;
    private PatientEncounter patientEncounter;
    private PaymentScheme paymentScheme;
    //
    private BillItem currentBillItem;
    private double billTotal;
    //
    List<BillComponent> lstBillComponents;
    List<BillFee> lstBillFees;
    List<BillItem> lstBillItems;
    List<BillEntry> lstBillEntries;
    private Integer index;
    //
//    private BillEntry entry;
    boolean printPreview;
    BillEntry removeBillEntry;
    double cashPaid;
    double cashBalance;
    @EJB
    CommonFunctions commonFunctions;
    String ageText;
    private Date newDob;
    boolean toClearBill = false;

    public boolean isToClearBill() {
        return toClearBill;
    }

    public void setToClearBill(boolean toClearBill) {
        this.toClearBill = toClearBill;
    }

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }

    public void updateFees(AjaxBehaviorEvent event) {
    }

    public String getAgeText() {
        ageText = getNewPatient().getAge();
        return ageText;
    }

    public void setAgeText(String ageText) {
        this.ageText = ageText;
        getNewPatient().getPerson().setDob(getCommonFunctions().guessDob(ageText));
    }

    public double getCashPaid() {
        return cashPaid;
    }

    public void setCashPaid(double cashPaid) {
        this.cashPaid = cashPaid;
        cashBalance = cashPaid - current.getNetTotal();
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BillNumberBean getBillNumberBean() {
        return billNumberBean;
    }

    public void setBillNumberBean(BillNumberBean billNumberBean) {
        this.billNumberBean = billNumberBean;
    }

    public BillEntry getRemoveBillEntry() {
        return removeBillEntry;
    }

    public void setRemoveBillEntry(BillEntry removeBillEntry) {
        this.removeBillEntry = removeBillEntry;
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }


    public List<Item> completeItem(String qry) {
        List<Item> completeItems = getItemFacade().findBySQL("select c from Item c where c.retired=false and (type(c) = Investigation or type(c) = Packege ) and upper(c.name) like '%" + qry.toUpperCase() + "%' order by c.name");
        return completeItems;
    }

    public boolean isPrintPreview() {
        return printPreview;
    }

    public void setPrintPreview(boolean printPreview) {
        this.printPreview = printPreview;
    }

    public BillEntryFacade getBillEntryFacade() {
        return billEntryFacade;
    }

    public void setBillEntryFacade(BillEntryFacade billEntryFacade) {
        this.billEntryFacade = billEntryFacade;
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

    public List<BillComponent> getLstBillComponents() {
        if (lstBillComponents == null) {
            lstBillComponents = getBillBean().billComponentsFromBillEntries(getLstBillEntries());
        }
        return lstBillComponents;
    }

    public String prepareNewBill() {
        clearBillItemValues();
        clearBillValues();
        setPrintPreview(true);
        printPreview = false;
        return "lab_bill";
    }

    public void clearForNewBill() {
        clearBillItemValues();
        clearBillValues();
        setPrintPreview(true);
        printPreview = false;
        toClearBill = true;
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
            lstBillItems = getBillBean().billItemsFromBillEntries(getLstBillEntries());
        }
        return lstBillItems;
    }

    public void setLstBillItems(List<BillItem> lstBillItems) {
        this.lstBillItems = lstBillItems;
    }

    public String getPatientTabId() {
        return patientTabId;
    }

    public void setPatientTabId(String patientTabId) {
        this.patientTabId = patientTabId;
    }

    public FeeFacade getFeeFacade() {
        return feeFacade;
    }

    public void setFeeFacade(FeeFacade feeFacade) {
        this.feeFacade = feeFacade;
    }

    private void recreateBillItems() {
        //Only remove Total and BillComponenbts,Fee and Sessions. NOT bill Entries
        lstBillComponents = null;
        lstBillFees = null;
        lstBillItems = null;

        billTotal = 0.0;
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

        getCurrent().setDepartment(sessionController.getLoggedUser().getDepartment());
        getCurrent().setInstitution(sessionController.getLoggedUser().getInstitution());
        getCurrentBillItem().setBill(getCurrent());
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

    public void feeChanged() {
        lstBillItems = null;
        getLstBillItems();
        calTotals();
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

                } else if (getCurrent().getCreditCompany() != null) {

                    if (bf.getBillItem().getItem().getDiscountAllowed() != null && bf.getBillItem().getItem().getDiscountAllowed() == true) {

                        bf.setFeeValue(bf.getFee().getFee() / 100 * (100 - getCurrent().getCreditCompany().getLabBillDiscount()));
                        dis += (bf.getFee().getFee() / 100 * (getCurrent().getCreditCompany().getLabBillDiscount()));
                        bf.getBillItem().setDiscount(bf.getBillItem().getDiscount() + bf.getFee().getFee() / 100 * (getCurrent().getCreditCompany().getLabBillDiscount()));
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
        getCurrent().setDiscount(dis);
        getCurrent().setTotal(tot);
        getCurrent().setNetTotal(tot - dis);
    }

    public void clearBillItemValues() {
        setCurrentBillItem(null);
        recreateBillItems();
    }

    public void onTabChange(TabChangeEvent event) {
        setPatientTabId(event.getTab().getId());

    }

    public void save(ActionEvent actionEvent) {
        settleBill();
    }

    public void settleBill() {
        if (getPatientTabId().toString().equals("tabNewPt")) {
            if (getNewPatient().getPerson().getName() == null || getNewPatient().getPerson().getName().trim().equals("") || getNewPatient().getPerson().getSex() == null || getAgeText() == null) {
                UtilityController.addErrorMessage("Can not bill without Patient Name, Age or Sex.");
                return;
            }
            if (!getCommonFunction().checkAgeSex(getNewPatient().getPerson().getDob(), getNewPatient().getPerson().getSex(), getNewPatient().getPerson().getTitle())) {
                UtilityController.addErrorMessage("Check Title,Age,Sex");
                return;
            }
            if (getNewPatient().getPerson().getPhone().length() < 1) {
                UtilityController.addErrorMessage("Phone Number is Required it should be fill");
                return;
            }
        }
        if (getLstBillEntries().isEmpty()) {
            UtilityController.addErrorMessage("No investigations are added to the bill to settle");
            return;
        }
        if (paymentScheme == null && !patientTabId.equalsIgnoreCase("tabBht") && current.getCollectingCentre() == null) {
            UtilityController.addErrorMessage("Please select a payment scheme");
            return;
        }

        if (patientTabId.equalsIgnoreCase("tabBht")) {
            if (patientEncounter == null) {
                UtilityController.addErrorMessage("Please select a BHT");
                return;
            } else {
                current.setPaymentScheme(null);
            }
        }
        if (current.getCollectingCentre() != null) {
            current.setPaymentScheme(null);
        }
        if (paymentScheme != null && paymentScheme.getPaymentMethod() != null && paymentScheme.getPaymentMethod().toString().equals("Cheque")) {
            if (getCurrent().getChequeBank() == null || getCurrent().getChequeRefNo() == null) {
                UtilityController.addErrorMessage("Please select Cheque Number and Bank");
                return;
            }
        }
        if (paymentScheme.getPaymentMethod() == PaymentMethod.Cash) {
            if (cashPaid == 0.0) {
                UtilityController.addErrorMessage("Please select tendered amount correctly");
                return;
            }
            if (cashPaid < current.getNetTotal()) {
                UtilityController.addErrorMessage("Please select tendered amount correctly");
                return;
            }
        }

        
        
        savePatient();
        getCurrent().setDeptId(getBillNumberBean().departmentBillNumberGenerator(getSessionController().getLoggedUser().getDepartment(), BillType.LabBill));
        getCurrent().setInsId(getBillNumberBean().institutionBillNumberGenerator(getSessionController().getLoggedUser().getInstitution(), BillType.LabBill));
        getCurrent().setCashBalance(cashBalance);
        getCurrent().setCashPaid(cashPaid);
        getCurrent().setBillDate(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        getCurrent().setBillTime(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        //Go through the Bill Fees
        // Add all fees with staff id to bill.staffFee
        //Add all Fee without staff id to 
        saveBill();
        saveBillItems();
        calculateBillItems();

        printPreview = true;
    }

    private void saveBill() {
        System.out.println("Saving the bill " + getCurrent());
        System.out.println("Bill Patient is " + getCurrent().getPatient());
        getCurrent().setPaymentScheme(getPaymentScheme());
        if (getCurrent().getId() == null || getCurrent().getId() == 0) {


            getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            getCurrent().setCreater(sessionController.getLoggedUser());
            getFacade().create(getCurrent());
        } else {
            getFacade().edit(getCurrent());
        }
        System.out.println("Saved the Bill " + getCurrent());
    }

    private void clearBillValues() {
        current = null;
        getCurrent();
        toClearBill = false;
        currentBillItem = new BillItem();
        Person p = new Person();
        newPatient = new Patient();
        newPatient.setPerson(p);
        setPatientEncounter(null);
        setSearchedPatient(null);

        setLstBillEntries(null);
    }

    public PatientFacade getPatientFacade() {
        return patientFacade;
    }

    public void setPatientFacade(PatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public BillFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(BillFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public LabBillController() {
    }

    public Bill getCurrent() {
        if (current == null) {
            current = new BilledBill();
            current.setBillType(BillType.LabBill);
            current.setDepartment(sessionController.getLoggedUser().getDepartment());
            current.setInstitution(sessionController.getLoggedUser().getInstitution());
        }
        return current;
    }

    public void setCurrent(Bill current) {
        this.current = current;
    }

    private BillFacade getFacade() {
        return ejbFacade;
    }

    public List<Bill> getItems() {
        //items = getFacade().findAll("name", true);
        String sql = "SELECT i FROM Bill i where i.retired=false and i.billType = com.divudi.data.BillType.LabBill ";
        items = getEjbFacade().findBySQL(sql);
        if (items == null) {
            items = new ArrayList<Bill>();
        }
        return items;

    }

    public BillItem getCurrentBillItem() {
        if (currentBillItem == null) {
            currentBillItem = new BillItem();
            currentBillItem.setNetValue(0.0);
        }
        return currentBillItem;
    }

    public void setCurrentBillItem(BillItem currentBillItem) {
        this.currentBillItem = currentBillItem;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    private void savePatient() {
        System.out.println("Saving the Patient");
        if (getPatientTabId().equals("tabNewPt")) {
            getCurrent().setPatient(getNewPatient());
            getPersonFacade().create(getNewPatient().getPerson());
            getPatientFacade().create(getNewPatient());
            System.out.println("Saved New Patient is " + getNewPatient());
        } else if (getPatientTabId().equals("tabSearchPt")) {
            System.out.println("1");
            System.out.println("Searched Patient is " + getSearchedPatient());
            getCurrent().setPatient(searchedPatient);
        } else if (getPatientTabId().equals("tabBht")) {
            getCurrent().setPatient(getPatientEncounter().getPatient());
            getCurrent().setPatientEncounter(getPatientEncounter());
        } else {
        }
    }

    private void saveBillItems() {
        for (BillEntry e : getLstBillEntries()) {

            BillItem temBi = e.getBillItem();
            System.out.println("BillItem is " + temBi);
            System.out.println("Bill of BillItem is " + temBi.getBill());
            if (temBi.getId() == null || temBi.getId() == 0) {
                temBi.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
                temBi.setCreater(getSessionController().getLoggedUser());
                getBillItemFacade().create(temBi);
            } else {
                getBillItemFacade().edit(temBi);
            }


            for (BillComponent bc : e.getLstBillComponents()) {
                if (bc.getId() == null || bc.getId() == 0) {
                    getBillComponentFacade().create(bc);
                    PatientHealthForm ptIx = new PatientHealthForm();
                    ptIx.setBillItem(e.getBillItem());
                    ptIx.setBillComponent(bc);
                    ptIx.setPackege(bc.getPackege());
                    ptIx.setApproved(Boolean.FALSE);
                    ptIx.setCancelled(Boolean.FALSE);
                    ptIx.setCollected(Boolean.FALSE);
                    ptIx.setDataEntered(Boolean.FALSE);
                    ptIx.setInvestigation((HealthForm) bc.getItem());
                    ptIx.setOutsourced(Boolean.FALSE);
                    ptIx.setPatient(getCurrent().getPatient());
                    ptIx.setEncounter(getCurrent().getPatientEncounter());
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
                    ptIxFacade.create(ptIx);
                } else {
                    getBillComponentFacade().edit(bc);
                }
            }
            for (BillFee bf : e.getLstBillFees()) {
                if (bf.getId() == null || bf.getId() == 0) {
                    getBillFeeFacade().create(bf);
                } else {
                    getBillFeeFacade().edit(bf);
                }
            }
//            for (BillSession bs : e.getLstBillSessions()) {
//                if (bs.getId() == null || bs.getId() == 0) {
//                    getBillSessionFacade().create(bs);
//                } else {
//                    getBillSessionFacade().edit(bs);
//                }
//            }
        }
    }

    private void calculateBillItems() {
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
        getCurrent().setStaffFee(s);
        getCurrent().setPerformInstitutionFee(i);
        getCurrent().setTotal(g);
        getFacade().edit(getCurrent());
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
            System.out.println(getCurrent().getNetTotal());
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

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public void setBillItemFacade(BillItemFacade billItemFacade) {
        this.billItemFacade = billItemFacade;
    }

    public PaymentScheme getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(PaymentScheme paymentScheme) {
        this.paymentScheme = paymentScheme;
        calTotals();
    }

    public PatientInvestigationFacade getPtIxFacade() {
        return ptIxFacade;
    }

    public void setPtIxFacade(PatientInvestigationFacade ptIxFacade) {
        this.ptIxFacade = ptIxFacade;
    }

    public Patient getNewPatient() {
        if (newPatient == null) {
            Person p = new Person();
            newPatient = new Patient();
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
        System.out.println("Set Patient");
        this.searchedPatient = searchedPatient;
        System.out.println("Patient is : " + searchedPatient);
    }

    public PatientEncounter getPatientEncounter() {
        return patientEncounter;
    }

    public void setPatientEncounter(PatientEncounter patientEncounter) {
        this.patientEncounter = patientEncounter;
    }

//    public BillEntry getEntry() {
//        if (entry == null) {
//            entry = new BillEntry();
//        }
//        return entry;
//    }
//
//    public void setEntry(BillEntry entry) {
//        this.entry = entry;
//    }
    public BillBean getBillBean() {
        return billBean;
    }

    public void setBillBean(BillBean billBean) {
        this.billBean = billBean;
    }

    public double getBillTotal() {
        if (billTotal == 0.0) {
            billTotal = getBillBean().billTotalFromBillEntries(getLstBillEntries());
        }
        return billTotal;
    }

    public void setBillTotal(double billTotal) {
        this.billTotal = billTotal;
    }

    public PatientEncounterFacade getPatientEncounterFacade() {
        return patientEncounterFacade;
    }

    public void setPatientEncounterFacade(PatientEncounterFacade patientEncounterFacade) {
        this.patientEncounterFacade = patientEncounterFacade;
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

    public BillSessionFacade getBillSessionFacade() {
        return billSessionFacade;
    }

    public void setBillSessionFacade(BillSessionFacade billSessionFacade) {
        this.billSessionFacade = billSessionFacade;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public CommonFunctions getCommonFunction() {
        return commonFunction;
    }

    public void setCommonFunction(CommonFunctions commonFunction) {
        this.commonFunction = commonFunction;
    }

    public Date getNewDob() {
        return newDob;
    }

    public void setNewDob(Date newDob) {
        this.newDob = newDob;
    }

    /**
     *
     */
//    @FacesValidator("labBillPsValidator")
//    public class LabBillPaymentSchemeValidator implements Validator {
//        public LabBillPaymentSchemeValidator() {
//        }
//        @Override
//        public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//            System.out.println("value of validator is going to be checked ");
//            System.out.println("tab is " + patientTabId);
//            System.out.println("to clear is " + toClearBill);
//            System.out.println("value is " + value);
//            if (value == null && !patientTabId.equalsIgnoreCase("tabBht") && toClearBill==false) {
//                System.out.println("value is null");
//                FacesMessage msg =
//                        new FacesMessage("Payment Scheme Error",
//                        "Please select a payment method");
//                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//                throw new ValidatorException(msg);
//            } else {
//                System.out.println("value of validator is " + value.toString());
//            }
//        }
//    }
    @FacesConverter(forClass = Bill.class)
    public static class LabBillControllerConverter implements Converter {

        public LabBillControllerConverter() {
        }

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BillController controller = (BillController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "labBillController");
            return controller.getEjbFacade().find(getKey(value));
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
                        + object.getClass().getName() + "; expected type: " + LabBillController.class.getName());
            }
        }
    }
}
