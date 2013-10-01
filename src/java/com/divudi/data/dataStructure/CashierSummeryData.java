/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.data.dataStructure;

import com.divudi.entity.WebUser;

/**
 *
 * @author Buddhika
 */
public class CashierSummeryData {

    private WebUser casheir;
    ////////////////////////
    private double billedCash;
    private double billedCredit;
    private double billedCreditCard;
    private double billedCheque;
    private double billedSlip;
    //////////////////////////
    private double cancelledCash;
    private double cancelledCredit;
    private double cancelledCreditCard;
    private double cancelledCheque;
    private double cancelledSlip;
    //////////////////////////
    private double refundedCash;
    private double refundedCredit;
    private double refundedCreditCard;
    private double refundCheque;
    private double refundSlip;
    /////////////////////////
    private double paymentCash;
    private double paymentCashCancel;
    ////////////////////////
    private double pettyCash;
    private double pettyCheque;
    private double pettyCancelCash;
    private double pettyCancelCheque;
    ////////////////////////
    private double companyCash;
    private double companyCheque;
    private double companySlip;
    private double companyCancelCash;
    private double companyCancelCheque;
    private double companyCancelSlip;
    ////////////////////////
    private double agentCash;
    private double agentCheque;
    private double agentSlip;
    private double agentCancelCash;
    private double agentCancelCheque;
    private double agentCancelSlip;
    ///////////////////////////////
    private double netCash;
    private double netCredit;
    private double netCreditCard;
    private double netCheque;
    private double netSlip;
    /////////////////////

    public WebUser getCasheir() {
        return casheir;
    }

    public void setCasheir(WebUser casheir) {
        this.casheir = casheir;
    }

    public double getBilledCash() {
        return billedCash;
    }

    public void setBilledCash(double billedCash) {
        this.billedCash = billedCash;
    }

    public double getBilledCredit() {
        return billedCredit;
    }

    public void setBilledCredit(double billedCredit) {
        this.billedCredit = billedCredit;
    }

    public double getBilledCreditCard() {
        return billedCreditCard;
    }

    public void setBilledCreditCard(double billedCreditCard) {
        this.billedCreditCard = billedCreditCard;
    }

    public double getCancelledCash() {
        return cancelledCash;
    }

    public void setCancelledCash(double cancelledCash) {
        this.cancelledCash = cancelledCash;
    }

    public double getCancelledCredit() {
        return cancelledCredit;
    }

    public void setCancelledCredit(double cancelledCredit) {
        this.cancelledCredit = cancelledCredit;
    }

    public double getCancelledCreditCard() {
        return cancelledCreditCard;
    }

    public void setCancelledCreditCard(double cancelledCreditCard) {
        this.cancelledCreditCard = cancelledCreditCard;
    }

    public double getRefundedCash() {
        return refundedCash;
    }

    public void setRefundedCash(double refundedCash) {
        this.refundedCash = refundedCash;
    }

    public double getRefundedCredit() {
        return refundedCredit;
    }

    public void setRefundedCredit(double refundedCredit) {
        this.refundedCredit = refundedCredit;
    }

    public double getRefundedCreditCard() {
        return refundedCreditCard;
    }

    public void setRefundedCreditCard(double refundedCreditCard) {
        this.refundedCreditCard = refundedCreditCard;
    }

    public double getNetCash() {
        netCash = billedCash + cancelledCash + refundedCash
                + paymentCashCancel + paymentCash
                + pettyCash + pettyCancelCash
                + companyCash + companyCancelCash
                + agentCash + agentCancelCash;

        return netCash;
    }

    public void setNetCash(double netCash) {
        this.netCash = netCash;
    }

    public double getNetCredit() {
        netCredit = billedCredit + cancelledCredit + refundedCredit;
        return netCredit;
    }

    public void setNetCredit(double netCredit) {
        this.netCredit = netCredit;
    }

    public double getNetCreditCard() {
        netCreditCard = billedCreditCard + cancelledCreditCard + refundedCreditCard;
        return netCreditCard;
    }

    public void setNetCreditCard(double netCreditCard) {
        this.netCreditCard = netCreditCard;
    }

    public double getPaymentCash() {
        return paymentCash;
    }

    public void setPaymentCash(double paymentCash) {
        this.paymentCash = paymentCash;
    }

    public double getBilledCheque() {
        return billedCheque;
    }

    public void setBilledCheque(double billedCheque) {
        this.billedCheque = billedCheque;
    }

    public double getCancelledCheque() {
        return cancelledCheque;
    }

    public void setCancelledCheque(double cancelledCheque) {
        this.cancelledCheque = cancelledCheque;
    }

    public double getRefundCheque() {
        return refundCheque;
    }

    public void setRefundCheque(double refundCheque) {
        this.refundCheque = refundCheque;
    }

    public double getNetCheque() {
        netCheque = billedCheque + cancelledCheque + refundCheque
                + pettyCheque + pettyCancelCheque
                + agentCheque + agentCancelCheque
                + companyCheque + companyCancelCheque;
        return netCheque;
    }

    public void setNetCheque(double netCheque) {
        this.netCheque = netCheque;
    }

    public double getBilledSlip() {
        return billedSlip;
    }

    public void setBilledSlip(double billedSlip) {
        this.billedSlip = billedSlip;
    }

    public double getCancelledSlip() {
        return cancelledSlip;
    }

    public void setCancelledSlip(double cancelledSlip) {
        this.cancelledSlip = cancelledSlip;
    }

    public double getRefundSlip() {
        return refundSlip;
    }

    public void setRefundSlip(double refundSlip) {
        this.refundSlip = refundSlip;
    }

    public double getNetSlip() {
        netSlip = billedSlip + cancelledSlip + refundSlip
                + agentSlip + agentCancelSlip
                + companySlip + companyCancelSlip;

        return netSlip;
    }

    public void setNetSlip(double netSlip) {
        this.netSlip = netSlip;
    }

    public double getPaymentCashCancel() {
        return paymentCashCancel;
    }

    public void setPaymentCashCancel(double paymentCashCancel) {
        this.paymentCashCancel = paymentCashCancel;
    }

    public double getPettyCash() {
        return pettyCash;
    }

    public void setPettyCash(double pettyCash) {
        this.pettyCash = pettyCash;
    }

    public double getPettyCancelCash() {
        return pettyCancelCash;
    }

    public void setPettyCancelCash(double pettyCancelCash) {
        this.pettyCancelCash = pettyCancelCash;
    }

    public double getPettyCheque() {
        return pettyCheque;
    }

    public void setPettyCheque(double pettyCheque) {
        this.pettyCheque = pettyCheque;
    }

    public double getPettyCancelCheque() {
        return pettyCancelCheque;
    }

    public void setPettyCancelCheque(double pettyCancelCheque) {
        this.pettyCancelCheque = pettyCancelCheque;
    }

    public double getCompanyCash() {
        return companyCash;
    }

    public void setCompanyCash(double companyCash) {
        this.companyCash = companyCash;
    }

    public double getCompanyCheque() {
        return companyCheque;
    }

    public void setCompanyCheque(double companyCheque) {
        this.companyCheque = companyCheque;
    }

    public double getCompanySlip() {
        return companySlip;
    }

    public void setCompanySlip(double companySlip) {
        this.companySlip = companySlip;
    }

    public double getCompanyCancelCash() {
        return companyCancelCash;
    }

    public void setCompanyCancelCash(double companyCancelCash) {
        this.companyCancelCash = companyCancelCash;
    }

    public double getCompanyCancelCheque() {
        return companyCancelCheque;
    }

    public void setCompanyCancelCheque(double companyCancelCheque) {
        this.companyCancelCheque = companyCancelCheque;
    }

    public double getCompanyCancelSlip() {
        return companyCancelSlip;
    }

    public void setCompanyCancelSlip(double companyCancelSlip) {
        this.companyCancelSlip = companyCancelSlip;
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
