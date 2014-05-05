/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.form.FilledHealthFormItemValue;
import gov.sp.health.entity.form.HealthForm;
import gov.sp.health.entity.form.HealthFormItem;
import gov.sp.health.facade.FilledHealthFormReportItemValueFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Etreame IT
 */
@Named(value = "graphController")
@SessionScoped
public class GraphController implements Serializable {

    private List<HealthForm> healthForms;
    private List<HealthFormItem> healthFormItems;
    private Date from;
    private Date to;
    HealthForm healthForm;
    HealthFormItem healthFormItem;

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

    public void createChartBean() {
        createLinearModel();
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }
    @EJB
    FilledHealthFormReportItemValueFacade fhfivFacade;

    public FilledHealthFormReportItemValueFacade getFhfivFacade() {
        return fhfivFacade;
    }

    public void setFhfivFacade(FilledHealthFormReportItemValueFacade fhfivFacade) {
        this.fhfivFacade = fhfivFacade;
    }

    private void createLinearModel() {
        System.out.println("creating line modeal");
        linearModel = new CartesianChartModel();
        List<FilledHealthFormItemValue> ffivs;
        Map m = new HashMap();
        String jpql;
        jpql = "select v from FilledHealthFormItemValue v where v.filledHealthFormReport.fromDate >= :fd and v.filledHealthFormReport.toDate<=:td and v.healthFormItem =:hfi ";
//        jpql = "select v from FilledHealthFormItemValue v ";
        System.out.println("jpql is " + jpql);
        m.put("fd", from);
        m.put("td", to);
        m.put("hfi", healthFormItem);
        System.out.println("m = " + m);
        System.out.println("from="+from);
        System.out.println("to"+to);
        
        ffivs = getFhfivFacade().findBySQL(jpql, m);
//        ffivs = getFhfivFacade().findBySQL(jpql);
        System.out.println("ffivs = " + ffivs);
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel(healthFormItem.getName());
        int i = 0;
        for (FilledHealthFormItemValue v : ffivs) {
            System.out.println("value is " + v.getDoubleValue());
            if (v.getDoubleValue() != null) {
                series1.set(i, v.getDoubleValue());
            }
            i++;
        }
        linearModel.addSeries(series1);
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
}
