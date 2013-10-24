/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.data;

/**
 *
 * @author www.divudi.com
 */
public enum Privileges {
    
    //Main Menu Privileges
    
    Opd,
    Inward,
    Lab,
    Pharmacy,
    Payment,
    Hr,
    Reports,
    User,
    Admin,
    
    //Submenu Privileges
    OpdBilling,
    OpdBillSearch,
    OpdBillItemSearch,
    OpdReprint,
    OpdCancel,
    OpdReturn,
    OpdReactivate,
    
    
    InwardBilling,
    InwardBillSearch,
    InwardBillItemSearch,
    InwardBillReprint,
    InwardCancel,
    InwardReturn,
    InwardReactivate,
    
    LabBilling,
    LabBillSearch,
    LabBillItemSearch,
    LabBillCancelling,
    LabBillReturning,
    LabBillReprint,
    LabBillRefunding,
    LabBillReactivating,
    LabSampleCollecting,
    LabSampleReceiving,
    LabReportFormatEditing,
    LabDataentry,
    LabAutherizing,
    LabDeAutherizing,
    LabPrinting,
    LabReprinting,
    LabSummeriesLevel1,
    LabSummeriesLevel2,
    LabSummeriesLevel3,
    LabReportSearchOwn,
    LabReportSearchAll,    
    LabReceive,        
    LabEditPatient,    
    
    
    PaymentBilling,
    PaymentBillSearch,
    PaymentBillReprint,
    PaymentBillCancel,
    PaymentBillRefund,
    PaymentBillReactivation,
    
    ReportsSearchCashCardOwn,
    ReportsSearchCreditOwn,    
    ReportsItemOwn,
    ReportsSearchCashCardOther,    
    ReportSearchCreditOther,
    ReportsItemOther,
    
    //TODO: Pharmacy Privileges
    
    AdminManagingUsers,
    AdminInstitutions,
    AdminStaff,
    AdminItems,
    AdminPrices,
    
}
