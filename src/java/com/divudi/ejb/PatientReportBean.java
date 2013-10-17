/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.ejb;

import java.util.TimeZone;
import com.divudi.bean.SessionController;
import com.divudi.data.InvestigationItemType;
import com.divudi.data.InvestigationItemValueType;
import com.divudi.data.Sex;
import com.divudi.entity.Patient;
import com.divudi.entity.form.HealthForm;
import com.divudi.entity.form.HealthFormItem;
import com.divudi.entity.form.HealthFormItemValueFlag;
import com.divudi.entity.form.FilledHealthForm;
import com.divudi.entity.form.FilledHealthFormReport;
import com.divudi.entity.form.FilledHealthFormReportItemValue;
import com.divudi.entity.form.ReportItem;
import com.divudi.facade.InvestigationItemFacade;
import com.divudi.facade.InvestigationItemValueFlagFacade;
import com.divudi.facade.PatientReportFacade;
import com.divudi.facade.PatientReportItemValueFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ejb.Stateless;

/**
 *
 * @author Buddhika
 */
@Stateless
public class PatientReportBean {

    @EJB
    private InvestigationItemFacade ixItemFacade;
    @EJB
    private PatientReportItemValueFacade ptRivFacade;
    @EJB
    private PatientReportFacade prFacade;

    public FilledHealthFormReport patientReportFromPatientIx(FilledHealthForm pi) {
        String sql;
        FilledHealthFormReport r;
        if (pi == null) {
            return null;
        }
        if (pi.getId() == null || pi.getId() == 0) {
            return null;
        }
        sql = "Select r from PatientReport r where r.retired=false and r.patientInvestigation.id = " + pi.getId();
        r = getPrFacade().findFirstBySQL(sql);
        if (r == null) {
            r = new FilledHealthFormReport();
            r.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            r.setItem(pi.getInvestigation());
            r.setPatientInvestigation(pi);
            getPrFacade().create(r);
        }
        return r;
    }

    public FilledHealthFormReport newPatientReportFromPatientIx(FilledHealthForm pi, HealthForm ix) {
        FilledHealthFormReport r;
        if (pi == null) {
            return null;
        }
        if (pi.getId() == null || pi.getId() == 0) {
            return null;
        }
        r = new FilledHealthFormReport();
        r.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        r.setItem(ix);
        r.setPatientInvestigation(pi);
        getPrFacade().create(r);
        return r;
    }

    public List<HealthFormItem> getInvestigationItemsForInvestigation(HealthForm ix) {
        List<HealthFormItem> ii;
        if (ix == null || ix.getId() == null) {
            ii = null;
        } else {
            String sql;
            sql = "select ii from InvestigationItem ii where ii.retired = false and ii.item.id = " + ix.getId() + " order by ii.cssTop, ii.cssLeft ";
            ii = getIxItemFacade().findBySQL(sql);
        }
        if (ii == null) {
            ii = new ArrayList<HealthFormItem>();
        }
        return ii;
    }

//    Label,
//    Value,
//    Calculation,
//    Flag,
//    List,
    public List<HealthFormItem> getInvestigationItemsOfValueTypeForInvestigation(HealthForm ix) {
        List<HealthFormItem> ii;
        if (ix == null || ix.getId() == null) {
            ii = null;
        } else {
            String sql;
            sql = "select ii from InvestigationItem ii where ii.retired = false and ii.ixItemType = com.divudi.data.InvestigationItemType.Value and ii.item.id = " + ix.getId() + " order by ii.cssTop, ii.cssLeft ";
            System.out.println(sql);
            ii = getIxItemFacade().findBySQL(sql);
            System.out.println("ii is " + ii + " and the cou");
        }
        if (ii == null) {
            ii = new ArrayList<HealthFormItem>();
        }
        return ii;
    }

    public Double getDefaultDoubleValue(HealthFormItem item, Patient patient) {
        //TODO: Create Logic
        return 0.0;
    }

    public String getDefaultVarcharValue(HealthFormItem item, Patient patient) {
        //TODO: Create Logic
        return "";
    }

    public String getDefaultMemoValue(HealthFormItem item, Patient patient) {
        //TODO: Create Logic
        return "";
    }

    public byte[] getDefaultImageValue(HealthFormItem item, Patient patient) {
        //TODO: Create Logic
        return null;
    }

    public void addPatientReportItemValuesForReport(FilledHealthFormReport ptReport) {
        String sql = "";
        System.out.println("going to add patient report item values for report");
        HealthForm temIx = (HealthForm) ptReport.getItem();
        System.out.println("Items getting for ix is - " + temIx.getName() );
        for (ReportItem ii : temIx.getReportItems()) {
            System.out.println("report items is " + ii.getName());
            if ( (ii.getIxItemType() == InvestigationItemType.Value || ii.getIxItemType() == InvestigationItemType.Calculation) && ii.isRetired() == false) {
                if (ptReport.getId() == null || ptReport.getId() == 0) {
                    FilledHealthFormReportItemValue val;
                    val = new FilledHealthFormReportItemValue();
                    if (ii.getIxItemValueType() == InvestigationItemValueType.Varchar) {
                        val.setStrValue(getDefaultVarcharValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                    } else if (ii.getIxItemValueType() == InvestigationItemValueType.Memo) {
                        val.setLobValue(getDefaultMemoValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                    } else if (ii.getIxItemValueType() == InvestigationItemValueType.Double) {
                        val.setDoubleValue(getDefaultDoubleValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                    } else if (ii.getIxItemValueType() == InvestigationItemValueType.Image) {
                        val.setBaImage(getDefaultImageValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                    } else {
                    }
                    val.setInvestigationItem((HealthFormItem) ii);
                    val.setPatient(ptReport.getPatientInvestigation().getPatient());
                    val.setPatientEncounter(ptReport.getPatientInvestigation().getEncounter());
                    val.setFilledHealthFormReport(ptReport);
                    ptReport.getFilledHealthFormReportItemValue().add(val);
                    System.out.println("New value added to pr teport" + ptReport);

                } else {
                    sql = "select i from PatientReportItemValue i where i.patientReport.id = " + ptReport.getId() + " and i.investigationItem.id = " + ii.getId() + " and (i.investigationItem.ixItemType = com.divudi.data.InvestigationItemType.Value or i.investigationItem.ixItemType = com.divudi.data.InvestigationItemType.Calculation)";
                    FilledHealthFormReportItemValue val = getPtRivFacade().findFirstBySQL(sql);
                    System.out.println("val is " + val);
                    if (val == null) {
                        System.out.println("val is null");
                        val = new FilledHealthFormReportItemValue();
                        if (ii.getIxItemValueType() == InvestigationItemValueType.Varchar) {
                            val.setStrValue(getDefaultVarcharValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                        } else if (ii.getIxItemValueType() == InvestigationItemValueType.Memo) {
                            val.setLobValue(getDefaultMemoValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                        } else if (ii.getIxItemValueType() == InvestigationItemValueType.Double) {
                            val.setDoubleValue(getDefaultDoubleValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                        } else if (ii.getIxItemValueType() == InvestigationItemValueType.Image) {
                            val.setBaImage(getDefaultImageValue((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                        } else {
                        }
                        val.setInvestigationItem((HealthFormItem) ii);
                        val.setPatient(ptReport.getPatientInvestigation().getPatient());
                        val.setPatientEncounter(ptReport.getPatientInvestigation().getEncounter());
                        val.setFilledHealthFormReport(ptReport);
                        ptReport.getFilledHealthFormReportItemValue().add(val);
                        System.out.println("value added to pr teport" + ptReport);


                    }

                }
            } else if (ii.getIxItemType() == InvestigationItemType.DynamicLabel && ii.isRetired() == false) {
                if (ptReport.getId() == null || ptReport.getId() == 0) {
                    FilledHealthFormReportItemValue val;
                    val = new FilledHealthFormReportItemValue();
                    val.setStrValue(getPatientDynamicLabel((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                    val.setInvestigationItem((HealthFormItem) ii);
                    val.setPatient(ptReport.getPatientInvestigation().getPatient());
                    val.setPatientEncounter(ptReport.getPatientInvestigation().getEncounter());
                    val.setFilledHealthFormReport(ptReport);
                    ptReport.getFilledHealthFormReportItemValue().add(val);
                    System.out.println("New value added to pr teport" + ptReport);

                } else {
                    sql = "select i from PatientReportItemValue i where i.patientReport.id = " + ptReport.getId() + " and i.investigationItem.id = " + ii.getId() + " and i.investigationItem.ixItemType = com.divudi.data.InvestigationItemType.Value";
                    FilledHealthFormReportItemValue val = getPtRivFacade().findFirstBySQL(sql);
                    System.out.println("val is " + val);
                    if (val == null) {
                        System.out.println("val is null");
                        val = new FilledHealthFormReportItemValue();
                        val.setStrValue(getPatientDynamicLabel((HealthFormItem) ii, ptReport.getPatientInvestigation().getPatient()));
                        val.setInvestigationItem((HealthFormItem) ii);
                        val.setPatient(ptReport.getPatientInvestigation().getPatient());
                        val.setPatientEncounter(ptReport.getPatientInvestigation().getEncounter());
                        val.setFilledHealthFormReport(ptReport);
                        ptReport.getFilledHealthFormReportItemValue().add(val);
                        System.out.println("value added to pr teport" + ptReport);


                    }

                }
            }



        }


    }
    @EJB
    CommonFunctions commonFunctions;

    public CommonFunctions getCommonFunctions() {
        return commonFunctions;
    }

    public void setCommonFunctions(CommonFunctions commonFunctions) {
        this.commonFunctions = commonFunctions;
    }
    @EJB
    InvestigationItemValueFlagFacade iivfFacade;

    public InvestigationItemValueFlagFacade getIivfFacade() {
        return iivfFacade;
    }

    public void setIivfFacade(InvestigationItemValueFlagFacade iivfFacade) {
        this.iivfFacade = iivfFacade;
    }

    public String getPatientDynamicLabel(HealthFormItem ii, Patient p) {
        String dl;
        String sql;
        dl = ii.getName();
        long ageInDays = commonFunctions.calculateAgeInDays(p.getPerson().getDob(), Calendar.getInstance().getTime());
        sql = "select f from InvestigationItemValueFlag f where  f.fromAge < " + ageInDays + " and f.toAge > " + ageInDays + " and f.investigationItemOfLabelType.id = " + ii.getId();
        List<HealthFormItemValueFlag> fs = getIivfFacade().findBySQL(sql);
        for (HealthFormItemValueFlag f : fs) {
            if (f.getSex() == p.getPerson().getSex()) {
                dl = f.getFlagMessage();
            }
        }
        return dl;
    }

    public InvestigationItemFacade getIxItemFacade() {
        return ixItemFacade;
    }

    public void setIxItemFacade(InvestigationItemFacade ixItemFacade) {
        this.ixItemFacade = ixItemFacade;
    }

    public PatientReportItemValueFacade getPtRivFacade() {
        return ptRivFacade;
    }

    public void setPtRivFacade(PatientReportItemValueFacade ptRivFacade) {
        this.ptRivFacade = ptRivFacade;
    }

    public PatientReportFacade getPrFacade() {
        return prFacade;
    }

    public void setPrFacade(PatientReportFacade prFacade) {
        this.prFacade = prFacade;
    }
}
