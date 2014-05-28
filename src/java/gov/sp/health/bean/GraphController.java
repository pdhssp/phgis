/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.data.AreaType;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.facade.FilledHealthFormReportItemValueFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.primefaces.model.chart.BarChartModel;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import java.io.Serializable;

/**
 *
 * @author Etreame IT
 */
@Named(value = "graphController")
@SessionScoped
public class GraphController implements Serializable {

    @Inject
    private SessionController sessionController;

    private List<HealthForm> healthForms;
    private List<HealthFormItem> healthFormItems;
    private Date from;
    private Date to;
    HealthForm healthForm;
    HealthFormItem healthFormItem;
    private Double total;
    private Double avrage;
    private Double minVal;
    private Double max;
    private String[][] arr;
    private Area area;

    public HealthForm getHealthForm() {
        return healthForm;
    }

    public void setHealthForm(HealthForm healthForm) {
        this.healthForm = healthForm;
    }

    public HealthFormItem getHealthFormItem() {
        return healthFormItem;
    }

    public void setHealthFormItem(HealthFormItem healthFormItem) {
        this.healthFormItem = healthFormItem;
    }
    private CartesianChartModel linearModel;
    BarChartModel barchartModel;

    public void createChartBean() {
        createLinearModel();
        createBarchartModel();
    }

    public void createChartBeanSummary() {
        createLinechartModelSum();
        createBarchartModelSum();
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public BarChartModel getBarchartModel() {
        return barchartModel;
    }

    @EJB
    FilledHealthFormReportItemValueFacade fhfivFacade;

    public FilledHealthFormReportItemValueFacade getFhfivFacade() {
        return fhfivFacade;
    }

    public void setFhfivFacade(FilledHealthFormReportItemValueFacade fhfivFacade) {
        this.fhfivFacade = fhfivFacade;
    }

//    private void createLinearModel() {
//        System.out.println("creating line modeal");
//        
//        linearModel = new CartesianChartModel();
//        
//        
//         
//        
//        List<FilledHealthFormItemValue> ffivs;
//        Map m = new HashMap();
//        String jpql;
//        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
////        jpql = "select v from FilledHealthFormItemValue v ";
//        System.out.println("jpql is " + jpql);
//        m.put("fd", from);
//        m.put("td", to);
//        m.put("hfi", healthFormItem);
//        m.put("a", getSessionController().getArea());
//        
//        System.out.println("m = " + m);
//        System.out.println("from=" + from);
//        System.out.println("to" + to);
//
//        ffivs = getFhfivFacade().findBySQL(jpql, m);
////        ffivs = getFhfivFacade().findBySQL(jpql);
//        System.out.println("ffivs = " + ffivs);
//        ChartSeries series1 = new ChartSeries();
//
//        series1.setLabel(healthFormItem.getName());
//
//        int i = 0;
//        for (FilledHealthFormItemValue v : ffivs) {
//            System.out.println("value is " + ffivs.get(i).getDoubleValue());
//            if (v.getDoubleValue() != null) {
//                
//               String temp=v.getFilledHealthFormReport().getFromDate() + " : "+v.getFilledHealthFormReport().getArea().getName();
//               
//                series1.set(temp, v.getDoubleValue());
//               
//             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
//          
//            }
//            i++;
//        }
//
//        
//        linearModel.addSeries(series1);
//
//    }
    public void createBarchartModel() {
        if (healthFormItem == null) {
            return;
        }
        switch (healthForm.getDurationType()) {
            case Annually:
                createBarchartModelAnnually();
                break;

            case Monthly:
                createBarchartModelMonthly();
                break;

            case Daily:
                createBarchartModelDaily();
                break;

        }
    }

    private void createBarchartModelAnnually() {

        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + "" + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelMonthly() {

        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + ":" + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelDaily() {

        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    public void createArea() {
        if (area == null) {
            UtilityController.addErrorMessage("Please Select an Area");
            return;
        }
        createBarchartModelAreaV();
        createLinechartModelAreaTest() ;
    }

    public void createAreaSum() {
        if (area == null) {
            UtilityController.addErrorMessage("Please Select an Area");
            return;
        }
        createBarchartModelAreaSum();
        createLinechartModelAreaSum();
    }

      
        public void createLinechartModelAreaTest() {

        if (healthFormItem == null) {
            return;
        }

        String sql;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());

        switch (at) {
            case Country:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.Province)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               UtilityController.addErrorMessage("Not Applicable");
                return;
            }
                
                break;

            case District:
               // sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
              UtilityController.addErrorMessage("Not Applicable");
                return;
            }
                
                break;

            case MohArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea=:ma";
                if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql= "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               UtilityController.addErrorMessage("Not Applicable");
                return;
            }
                
                
                break;

            case PhiArea:
              //  sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea=:ma";
    if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               UtilityController.addErrorMessage("Not Applicable");
                return;
            }          
                
                break;

            case PhmArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area=:ma";
                 if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               UtilityController.addErrorMessage("Not Applicable");
                return;
            }
                
                break;
            default:
                sql = "select a from Area a where a.retired=false ";
        }

        switch (healthForm.getDurationType()) {
            case Annually:
                createLinechartModelAnnuallyAreaTest(sql);
                break;

            case Monthly:
                createLinechartModelMonthlyAreaTest(sql);
                break;

            case Daily:
                createLinechartModelDailyAreaTest(sql);
                break;

        }
    }

    private void createLinechartModelAnnuallyAreaTest(String jpql) {

        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();

        //        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("a", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }
    
     private void createLinechartModelAnnuallyArea(String jpql) {

        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();

        //        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("ma", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    private void createLinechartModelMonthlyAreaTest(String jpql) {

        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("a", area);
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    private void createLinechartModelDailyAreaTest(String jpql) {

        System.out.println("ma=" + area.getName());
        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        System.out.println("jpql is " + jpql);
        m.put("a", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    public void createBarchartModelArea() {

        if (healthFormItem == null) {
            return;
        }

        String sql;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());

        sql = null;

      
            switch (at) {
                case Country:
                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea.superArea=:ma";
                    break;

                case Province:
                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:ma";
                    break;

                case District:
                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea=:ma";
                    break;

                case MohArea:

                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea=:ma";

                    break;

                case PhiArea:
                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea=:ma";
                    break;

                case PhmArea:
                    sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area=:ma";
                    break;
                default:
                    sql = "select a from Area a where a.retired=false ";
            
        
        }

        switch (healthForm.getDurationType()) {
            case Annually:
                createBarchartModelAnnuallyArea(sql);
                break;

            case Monthly:
                createBarchartModelMonthlyArea(sql);
                break;

            case Daily:
                createBarchartModelDailyArea(sql);
                break;

        }
    }
    
    public void createBarchartModelAreaV() {

        if (healthFormItem == null) {
            return;
        }

        String sql=null;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());
        
             System.out.println("@@@@@@@@" + at);
        System.out.println("@@@@@@@@@" + healthForm.getAreaType());
        

         if (at.equals(AreaType.Province)) {
            if (healthForm.getAreaType().equals(AreaType.Province)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                UtilityController.addErrorMessage("Not Applicable");
                return;
            }
        }
                  
                  else
                      
                      if (at.equals(AreaType.District)) {
            if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               UtilityController.addErrorMessage("Not Applicable");
                return;
            }
        }
                  else
                      
                      if (at.equals(AreaType.MohArea)) {
             if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql= "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                UtilityController.addErrorMessage("Not Applicable");
                return;
            }
        }
                   else
                      
                      if (at.equals(AreaType.PhiArea)) {
            if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                UtilityController.addErrorMessage("Not Applicable");
                return;
            }
        }
                   else
                      
                      if (at.equals(AreaType.PhmArea)) {
             if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                UtilityController.addErrorMessage("Not Applicable");
                return;
            }
             
             
        }
            switch (healthForm.getDurationType()) {
            case Annually:
                createBarchartModelAnnuallyAreaV(sql);
                break;

            case Monthly:
                createBarchartModelMonthlyAreaV(sql);
                break;

            case Daily:
                createBarchartModelDailyAreaV(sql);
                break;

        
        }
    }


    private void createBarchartModelAnnuallyArea(String jpql) {

        barchartModel = new HorizontalBarChartModel();
        barchartModel.setLegendPosition("e");
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();

        //        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("a", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelMonthlyArea(String jpql) {

        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("ma", area);
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelDailyArea(String jpql) {

        System.out.println("ma=" + area.getName());
        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        System.out.println("jpql is " + jpql);
        m.put("ma", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }
    
    
    
    private void createBarchartModelAnnuallyAreaV(String jpql) {

        barchartModel = new HorizontalBarChartModel();
        barchartModel.setLegendPosition("e");
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();

        //        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("a", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelMonthlyAreaV(String jpql) {

        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("a", area);
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    private void createBarchartModelDailyAreaV(String jpql) {

        System.out.println("ma=" + area.getName());
        barchartModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        System.out.println("jpql is " + jpql);
        m.put("a", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        barchartModel.addSeries(series1);

    }

    public void createLinechartModelArea() {

        if (healthFormItem == null) {
            return;
        }

        String sql;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());

        switch (at) {
            case Country:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:ma";
                break;

            case District:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea=:ma";
                break;

            case MohArea:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea=:ma";
                break;

            case PhiArea:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea=:ma";
                break;

            case PhmArea:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area=:ma";
                break;
            default:
                sql = "select a from Area a where a.retired=false ";
        }

        switch (healthForm.getDurationType()) {
            case Annually:
                createLinechartModelAnnuallyArea(sql);
                break;

            case Monthly:
                createLinechartModelMonthlyArea(sql);
                break;

            case Daily:
                createLinechartModelDailyArea(sql);
                break;

        }
    }
/*
    private void createLinechartModelAnnuallyArea(String jpql) {

        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();

        //        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("ma", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }
*/
    private void createLinechartModelMonthlyArea(String jpql) {

        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("ma", area);
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    private void createLinechartModelDailyArea(String jpql) {

        System.out.println("ma=" + area.getName());
        linearModel = new HorizontalBarChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        System.out.println("jpql is " + jpql);
        m.put("ma", area);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    public void clearLinearModel() {

        LineChartSeries series1 = new LineChartSeries();

        series1.setLabel(healthFormItem.getName());

               // series1.set(null, 0);  
        linearModel.addSeries(series1);

    }

    public void createLinearModel() {
        if (healthFormItem == null) {
            return;
        }
        switch (healthForm.getDurationType()) {
            case Annually:
                createLinechartModelAnnually();
                break;

            case Monthly:
                createLinechartModelMonthly();
                break;

            case Daily:
                createLinechartModelDaily();
                break;

        }
    }

    private void createLinechartModelAnnually() {

        linearModel = new CartesianChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + "" + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    private void createLinechartModelMonthly() {

        linearModel = new CartesianChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getYearVal() + " : " + v.getFilledHealthFormReport().getMonthVal() + ":" + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    private void createLinechartModelDaily() {

        linearModel = new CartesianChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a)  order by v.filledHealthFormReport.fromDate ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                String temp = v.getFilledHealthFormReport().getFromDate() + " : " + v.getFilledHealthFormReport().getArea().getName();

                series1.set(temp, v.getDoubleValue());

             //   series1.setLabel(v.getFilledHealthFormReport().getYearVal() + " : "+v.getFilledHealthFormReport().getMonthVal()+":" +v.getFilledHealthFormReport().getDateVal() );
            }
            i++;
        }

        linearModel.addSeries(series1);

    }

    public void createBarchartModelSum() {
        if (healthFormItem == null) {
            return;
        }
        switch (healthForm.getDurationType()) {
            case Annually:
                createBarchartModelSumAnnually();
                break;

            case Monthly:
                createBarchartModelSumMonthly();
                break;

            case Daily:
                createBarchartModelSumDaily();
                break;

        }
    }

    public void createBarchartModelSumAnnually() {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createBarchartModelSumMonthly() {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal() && r.getMonth() == v.getFilledHealthFormReport().getMonthVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setMonth(v.getFilledHealthFormReport().getMonthVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "/" + r.getMonth();
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createBarchartModelSumDaily() {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getFromDate().equals(v.getFilledHealthFormReport().getFromDate())) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setFromDate(v.getFilledHealthFormReport().getFromDate());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {

            String temp = r.getFromDate() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createLinechartModelSum() {
        if (healthFormItem == null) {
            return;
        }
        switch (healthForm.getDurationType()) {
            case Annually:
                createLinechartModelSumAnnually();
                break;

            case Monthly:
                createLinechartModelSumMonthly();
                break;

            case Daily:
                createLinechartModelSumDaily();
                break;

        }
    }

    public void createLinechartModelSumAnnually() {
        linearModel = new CartesianChartModel();

        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

    public void createLinechartModelSumMonthly() {
        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal() && r.getMonth() == v.getFilledHealthFormReport().getMonthVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setMonth(v.getFilledHealthFormReport().getMonthVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "/" + r.getMonth();
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

    public void createLinechartModelSumDaily() {
        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", getSessionController().getArea());

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getFromDate().equals(v.getFilledHealthFormReport().getFromDate())) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setFromDate(v.getFilledHealthFormReport().getFromDate());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {

            String temp = r.getFromDate() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

    public void createBarchartModelAreaSum() {

        if (healthFormItem == null) {
            return;
        }

        String sql;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());

       switch (at) {
            case Country:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.Province)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                break;

            case District:
               // sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               sql = null;
            }
                
                break;

            case MohArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea=:ma";
                if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql= "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                
                break;

            case PhiArea:
              //  sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea=:ma";
    if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }          
                
                break;

            case PhmArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area=:ma";
                 if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                break;
            default:
                sql = "select a from Area a where a.retired=false ";
        }

        

        switch (healthForm.getDurationType()) {
            case Annually:
                createBarchartModelSumAnnuallyArea(sql);
                break;

            case Monthly:
                createBarchartModelSumMonthlyArea(sql);
                break;

            case Daily:
                createBarchartModelSumDailyArea(sql);
                break;

        }
    }

    public void createBarchartModelSumAnnuallyArea(String jpql) {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createBarchartModelSumMonthlyArea(String jpql) {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal() && r.getMonth() == v.getFilledHealthFormReport().getMonthVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setMonth(v.getFilledHealthFormReport().getMonthVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "/" + r.getMonth();
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createBarchartModelSumDailyArea(String jpql) {
        barchartModel = new HorizontalBarChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getFromDate().equals(v.getFilledHealthFormReport().getFromDate())) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setFromDate(v.getFilledHealthFormReport().getFromDate());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {

            String temp = r.getFromDate() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        barchartModel.addSeries(series1);

    }

    public void createLinechartModelAreaSum() {

        if (healthFormItem == null) {
            return;
        }

        String sql;
        AreaType at = area.getAreaType();

        System.out.println("----" + at);
        System.out.println("ma" + area.getName());

      switch (at) {
            case Country:
                sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea.superArea=:ma";
                break;

            case Province:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.Province)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                break;

            case District:
               // sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea.superArea=:ma";
               if (healthForm.getAreaType().equals(AreaType.District)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
               sql = null;
            }
                
                break;

            case MohArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea.superArea=:ma";
                if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                sql= "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
               sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                
                break;

            case PhiArea:
              //  sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area.superArea=:ma";
    if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }          
                
                break;

            case PhmArea:
                //sql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   v.filledHealthFormReport.area=:ma";
                 if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                sql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                sql = null;
            }
                
                break;
            default:
                sql = "select a from Area a where a.retired=false ";
        }


        switch (healthForm.getDurationType()) {
            case Annually:
                createLinechartModelSumAnnuallyArea(sql);
                break;

            case Monthly:
                createLinechartModelSumMonthlyArea(sql);
                break;

            case Daily:
                createLinechartModelSumDailyArea(sql);
                break;

        }
    }

    public void createLinechartModelSumAnnuallyArea(String jpql) {

        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

    public void createLinechartModelSumMonthlyArea(String jpql) {
        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getYear() == v.getFilledHealthFormReport().getYearVal() && r.getMonth() == v.getFilledHealthFormReport().getMonthVal()) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setYear(v.getFilledHealthFormReport().getYearVal());
                    sr.setMonth(v.getFilledHealthFormReport().getMonthVal());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {
            String temp = r.getYear() + "/" + r.getMonth();
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

    public void createLinechartModelSumDailyArea(String jpql) {
        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", area);

        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);

        ChartSeries series1 = new ChartSeries();

        series1.setLabel(healthFormItem.getName());

        List<SummeryRowForFieldValues> rows = new ArrayList<SummeryRowForFieldValues>();

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {

            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {

                boolean found = false;
                for (SummeryRowForFieldValues r : rows) {
                    if (r.getFromDate().equals(v.getFilledHealthFormReport().getFromDate())) {
                        found = true;
                        r.setValue(r.getValue() + v.getDoubleValue());
                    }
                }
                if (!found) {
                    SummeryRowForFieldValues sr = new SummeryRowForFieldValues();
                    sr.setFromDate(v.getFilledHealthFormReport().getFromDate());
                    sr.setValue(v.getDoubleValue());
                    rows.add(sr);
                }

            }
            i++;
        }

        for (SummeryRowForFieldValues r : rows) {

            String temp = r.getFromDate() + "";
            series1.set(temp, r.getValue());
            series1.setLabel(healthFormItem.getName());
        }

        linearModel.addSeries(series1);

    }

//     public void calTotal() {
//      System.out.println("creating data");
//       
//        List<FilledHealthFormItemValue> ffivs;
//        Map m = new HashMap();
//        String jpql;
//        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi ";
////        jpql = "select v from FilledHealthFormItemValue v ";
//        System.out.println("jpql is " + jpql);
//        m.put("fd", from);
//        m.put("td", to);
//        m.put("hfi", healthFormItem);
//        System.out.println("m = " + m);
//        System.out.println("from="+from);
//        System.out.println("to"+to);
//        
//        ffivs = getFhfivFacade().findBySQL(jpql, m);
////        ffivs = getFhfivFacade().findBySQL(jpql);
//        System.out.println("ffivs = " + ffivs);
//        
//        double temp=0;
//        
//        int i = 0;
//        for (FilledHealthFormItemValue v : ffivs) {
//            System.out.println("value is " + ffivs.get(i).getDoubleValue());
//            if (v.getDoubleValue() != null) {
//                temp=temp+ v.getDoubleValue();
//            }
//         
//           
//        }
//          total=temp;
//
//         System.out.println("total="+temp);
//        
//    }
    private List<FilledHealthFormItemValue> ffivs;

    public void viewHealthFormItems() {
        System.out.println("creating data" + sessionController.getLoggedUser().getStaff().getArea().getName());

        Map m = new HashMap();
        String jpql;
        //jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        m.put("a", sessionController.getArea());
        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("m=" + m);
        System.out.println("ffivs = " + ffivs);

        for (FilledHealthFormItemValue v : ffivs) {
//            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {
//                temp = temp + v.getDoubleValue();
//                i++;
//                if (v.getDoubleValue() > tmax) {
//                    tmax = v.getDoubleValue();
//                }
//                if (v.getDoubleValue() < tmin) {
//                    tmin = v.getDoubleValue();
//                }
                System.out.println("out=" + v.getFilledHealthFormReport().getArea().getName());
            }

        }
//        total = temp;
//        avrage = temp / i;
//        max = tmax;
//
//        if (tmin == 999999999) {
//            tmin = 0;
//        }
//
//        minVal = tmin;
//        System.out.println("avrage=" + avrage);
//
    }

    public void calAvrage() {
        System.out.println("creating data");

        String jpql=null;
        Map m = new HashMap();
      
        //jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi and   (v.filledHealthFormReport.area=:a or v.filledHealthFormReport.area.superArea=:a or v.filledHealthFormReport.area.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea=:a or v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a) order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";

            
        
                  if (getSessionController().getArea().getAreaType().equals(AreaType.Province)) {
            if (healthForm.getAreaType().equals(AreaType.Province)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.District)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                jpql = null;
            }
        }
                  
                  else
                      
                      if (getSessionController().getArea().getAreaType().equals(AreaType.District)) {
            if (healthForm.getAreaType().equals(AreaType.District)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                jpql = null;
            }
        }
                  else
                      
                      if (getSessionController().getArea().getAreaType().equals(AreaType.MohArea)) {
             if (healthForm.getAreaType().equals(AreaType.MohArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                jpql = null;
            }
        }
                   else
                      
                      if (getSessionController().getArea().getAreaType().equals(AreaType.PhiArea)) {
            if (healthForm.getAreaType().equals(AreaType.PhiArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area.superArea=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                jpql = null;
            }
        }
                   else
                      
                      if (getSessionController().getArea().getAreaType().equals(AreaType.PhmArea)) {
             if (healthForm.getAreaType().equals(AreaType.PhmArea)) {
                jpql = "select v from FilledHealthFormItemValue v where  v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi  and  v.filledHealthFormReport.area=:a  order by v.filledHealthFormReport.fromDate ";//        jpql = "select v from FilledHealthFormItemValue v ";
            } else {
                jpql = null;
            }
        }
      //  System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        // m.put("hf", healthForm);
        m.put("a", getSessionController().getArea());
        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);

        double temp = 0;
        double tmax = 0;
        double tmin = 999999999;

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {
                temp = temp + v.getDoubleValue();
                i++;
                if (v.getDoubleValue() > tmax) {
                    tmax = v.getDoubleValue();
                }
                if (v.getDoubleValue() < tmin) {
                    tmin = v.getDoubleValue();
                }
                System.out.println("out=" + v.getFilledHealthFormReport().getFromDate());
            }

        }
        total = temp;
        avrage = temp / i;
        max = tmax;

        if (tmin == 999999999) {
            tmin = 0;
        }

        minVal = tmin;
        System.out.println("avrage=" + avrage);

    }

    public void calAll() {
        System.out.println("creating data");

        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        System.out.println("m = " + m);
        System.out.println("from=" + from);
        System.out.println("to" + to);

        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);

        double temp = 0;
        double tmax = 0;
        double tmin = 999999999;

        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + ffivs.get(i).getDoubleValue());
            if (v.getDoubleValue() != null) {
                temp = temp + v.getDoubleValue();
                i++;
                if (v.getDoubleValue() > tmax) {
                    tmax = v.getDoubleValue();
                }
                if (v.getDoubleValue() < tmin) {
                    tmin = v.getDoubleValue();
                }
                System.out.println("out=" + v.getFilledHealthFormReport().getFromDate());
            }

        }
        total = temp;
        avrage = temp / i;
        max = tmax;

        if (tmin == 999999999) {
            tmin = 0;
        }

        minVal = tmin;
        System.out.println("avrage=" + avrage);

    }

    public GraphController() {
    }

    public List<HealthForm> getHealthForms() {
        return healthForms;
    }

    public void setHealthForms(List<HealthForm> healthForms) {
        this.healthForms = healthForms;
    }

    public List<HealthFormItem> getHealthFormItems() {
        return healthFormItems;
    }

    public void setHealthFormItems(List<HealthFormItem> healthFormItems) {
        this.healthFormItems = healthFormItems;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getAvrage() {
        return avrage;
    }

    public void setAvrage(Double avrage) {
        this.avrage = avrage;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMinVal() {
        return minVal;
    }

    public void setMinVal(Double minVal) {
        this.minVal = minVal;
    }

    public String[][] getArr() {
        return arr;
    }

    public void setArr(String[][] arr) {
        this.arr = arr;
    }

    public List<FilledHealthFormItemValue> getFfivs() {
        return ffivs;
    }

    public void setFfivs(List<FilledHealthFormItemValue> ffivs) {
        this.ffivs = ffivs;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public class SummeryRowForFieldValues {

        Area area;
        int year;
        int month;
        Date fromDate;
        double value;

        public Area getArea() {
            return area;
        }

        public Date getFromDate() {
            return fromDate;
        }

        public void setFromDate(Date fromDate) {
            this.fromDate = fromDate;
        }

        public void setArea(Area area) {
            this.area = area;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
        
        
      

    }

}
