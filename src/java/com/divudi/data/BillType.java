
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.data;

/**
 *
 * @author Buddhika
 */
public enum BillType {

    LabBill,
    ChannelingBill,
    PaymentBill,
    OpdBill,
    InwardPaymentBill,
    InwardBill,
    AdmissionBill,
    CashRecieveBill,   
    PettyCash,    
    AgentPaymentReceiveBill,
    
    PharmacyBill, //Cash In
    PharmacyPre,
    PharmacyOrder,
    PharmacyOrderApprove,
    PharmacyGrnBill,//Cash out
    PharmacyPurchaseBill, //Cash out
    PharmacyTransferRequest,
    PharmacyTransferIssue,
    PharmacyTransferReceive,
    
    ChannelBill,
    ChannelCredit,
}
