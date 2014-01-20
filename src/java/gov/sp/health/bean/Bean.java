/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

public class Bean {
private DashboardModel model;
public Bean() {
model = new DefaultDashboardModel();
DashboardColumn column1 = new DefaultDashboardColumn();
DashboardColumn column2 = new DefaultDashboardColumn();
DashboardColumn column3 = new DefaultDashboardColumn();
column1.addWidget("sports");
column1.addWidget("finance");
column2.addWidget("lifestyle");
column2.addWidget("weather");
column3.addWidget("politics");
model.addColumn(column1);
model.addColumn(column2);
model.addColumn(column3);
}
}