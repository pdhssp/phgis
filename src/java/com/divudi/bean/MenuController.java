/*
 * Author : Dr. M H B Ariyaratne
 *
 * MO(Health Information), Department of Health Services, Southern Province
 * and
 * Email : buddhika.ari@gmail.com
 */
package com.divudi.bean;

import com.divudi.data.dataStructure.PrivilageNode;
import com.divudi.data.Privileges;
import com.divudi.entity.WebUser;
import com.divudi.entity.WebUserPrivilege;
import com.divudi.facade.WebUserPrivilegeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named; import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class MenuController implements Serializable {

    private static final long serialVersionUID = 1L;
    MenuModel model;
    List<WebUserPrivilege> userPrivilages;
    @EJB
    WebUserPrivilegeFacade webUserPrivilegeFacade;
    WebUser webUser;

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }
    
    

    public List<WebUserPrivilege> getUserPrivilages() {
        return userPrivilages;
    }

    public void setUserPrivilages(List<WebUserPrivilege> userPrivilages) {
        this.userPrivilages = userPrivilages;
    }

    public WebUserPrivilegeFacade getWebUserPrivilegeFacade() {
        return webUserPrivilegeFacade;
    }

    public void setWebUserPrivilegeFacade(WebUserPrivilegeFacade webUserPrivilegeFacade) {
        this.webUserPrivilegeFacade = webUserPrivilegeFacade;
    }

    public boolean hasPrivilege(String privilege) {
        boolean hasPri = false;
        if (webUser == null) {
            return hasPri;
        }
        if (webUser.getId() == null) {
            return hasPri;
        }
        for (WebUserPrivilege w : getUserPrivileges()) {
            if (w.getPrivilege().equals(Privileges.valueOf(privilege))) {
                hasPri = true;
                return hasPri;
            }
        }
        return hasPri;
    }

    public List<WebUserPrivilege> getUserPrivileges() {
        if (userPrivilages == null) {
            String sql;
            sql = "select w from WebUserPrivilege w where w.retired=false and w.webUser.id = " + getWebUser().getId();
            userPrivilages = getWebUserPrivilegeFacade().findBySQL(sql);
        }
        if (userPrivilages == null) {
            userPrivilages = new ArrayList<WebUserPrivilege>();
        }
        return userPrivilages;
    }

    public MenuController() {
    }

    
    public void createMenu(WebUser user) {
        setWebUser(user);
        
        model = new DefaultMenuModel();
        MenuItem item;

        item = new MenuItem();
        item.setValue("Home");
        item.setUrl("index.xhtml");
        model.addMenuItem(item);

        if (hasPrivilege("Opd")) {
            model.addSubmenu(opdSubmenu());
        }
        if (hasPrivilege("Inward")) {
            model.addSubmenu(inwardSubmenu());
        }

        if (hasPrivilege("Lab")) {
            model.addSubmenu(labSubmenu());
        }
        if (hasPrivilege("Pharmacy")) {
            model.addSubmenu(pharmacySubmenu());
        }
        if (hasPrivilege("Payment")) {
            model.addSubmenu(paymentSubmenu());
        }
        if (hasPrivilege("Hr")) {
            model.addSubmenu(opdSubmenu());
        }
        if (hasPrivilege("Reports")) {
            model.addSubmenu(opdSubmenu());
        }
        model.addSubmenu(userSubmenu());
        if (hasPrivilege("Admin")) {
            model.addSubmenu(opdSubmenu());
        }


    }

    private Submenu hrSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("Human Resource"));

        MenuItem item;

        item = new MenuItem();
        item.setValue(("Under Construction"));
        item.setUrl("opd_bill.xhtml");
        submenu.getChildren().add(item);




        return submenu;
    }

    private Submenu opdSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("OPD"));

        MenuItem item;

        if (hasPrivilege(Privileges.OpdBilling.name())) {
            item = new MenuItem();
            item.setValue(("Bill"));
            item.setUrl("opd_bill_new.xhtml");
            submenu.getChildren().add(item);
        }



        if (hasPrivilege(Privileges.OpdBillSearch.name())) {
            item = new MenuItem();
            item.setValue(("Bill Search"));
            item.setUrl("opd_search_for_bills.xhtml");
            submenu.getChildren().add(item);
        }




        item = new MenuItem();
        item.setValue(("Payments"));
        item.setUrl("payment_bill.xhtml");
        submenu.getChildren().add(item);


        item = new MenuItem();
        item.setValue(("Search Due Payments"));
        item.setUrl("lab_search_for_payments.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Administration"));
        item.setUrl("opd_administration.xhtml");
        submenu.getChildren().add(item);




        return submenu;
    }

    private Submenu paymentSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("Payments"));

        MenuItem item;




        item = new MenuItem();
        item.setValue(("Payments"));
        item.setUrl("payment_bill.xhtml");
        submenu.getChildren().add(item);


        item = new MenuItem();
        item.setValue(("Search Due Payments"));
        item.setUrl("lab_search_for_payments.xhtml");
        submenu.getChildren().add(item);


        return submenu;
    }

    private Submenu labSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("Lab"));

        Submenu otherMenu = new Submenu();
        otherMenu.setLabel("Reports");

        MenuItem item;

        item = new MenuItem();
        item.setValue(("Bill"));
        item.setUrl("lab_bill.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Sample"));
        item.setUrl("lab_sample.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Data Entry"));
        item.setUrl("lab_dataentry.xhtml");
        otherMenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Print Worksheets / Receive in Lab"));
        item.setUrl("lab_receive.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Autherize"));
        item.setUrl("lab_autherize.xhtml");
        otherMenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Print"));
        item.setUrl("lab_print.xhtml");
        otherMenu.getChildren().add(item);


        item = new MenuItem();
        item.setValue(("Bill Search"));
        item.setUrl("lab_search_for_bills.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Report Search"));
        item.setUrl("lab_search_for_reporting.xhtml");
        submenu.getChildren().add(item);

        submenu.getChildren().add(otherMenu);

        item = new MenuItem();
        item.setValue(("Summeries"));
        item.setUrl("lab_reports.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Payments"));
        item.setUrl("lab_staff_payment_bill.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Administration"));
        item.setUrl("lab_administration.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu inwardSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("Inward"));

        MenuItem item;

        item = new MenuItem();
        item.setValue(("Admit"));
        item.setUrl("inward_admission.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Room Occupancy"));
        item.setUrl("inward_room_occupancy.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Cashier Summery"));
        item.setUrl("inward_search.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Services"));
        item.setUrl("inward_service_bill.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Bill By Bht"));
        item.setUrl("opd_bill_bht.xhtml");
        submenu.getChildren().add(item);


        item = new MenuItem();
        item.setValue(("Timed Service Bill"));
        item.setUrl("inward_timed_item_bill.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Professional Charges"));
        item.setUrl("inward_professional_bill.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Change Room"));
        item.setUrl("inward_room_change.xhtml");
        submenu.getChildren().add(item);


        Submenu otherMenu = new Submenu();
        otherMenu.setLabel("Payments");

        item = new MenuItem();
        item.setValue(("Pay"));
        item.setUrl("inward_payment_bill.xhtml");
        otherMenu.getChildren().add(item);


        submenu.getChildren().add(otherMenu);

        item = new MenuItem();
        item.setValue(("BHT Summery"));
        item.setUrl("inward_bht_summery.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Edit BHT Detail"));
        item.setUrl("inward_bht_edit.xhtml");
        submenu.getChildren().add(item);


        ///////////////////////




        item = new MenuItem();
        item.setValue(("Bill Refund"));
        item.setUrl("inward_refund_bill.xhtml");
        submenu.getChildren().add(item);






        item = new MenuItem();
        item.setValue(("Patient History"));
        item.setUrl("patient_history.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Reports"));
        item.setUrl("inward_reports.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Administration"));
        item.setUrl("inward_administration.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Other"));
        item.setUrl("inward_administration.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu pharmacySubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("Pharmacy"));

        MenuItem item;

        item = new MenuItem();
        item.setValue(("Sale"));
        item.setUrl("new_child.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Order"));
        item.setUrl("search_child.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Good Receive"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Purchase"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Transfer"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Cancel"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Return"));
        item.setUrl("under_construction.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Other"));
        item.setUrl("lab_administration.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("Administration"));
        item.setUrl("lab_other.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu userSubmenu() {
        Submenu submenu = new Submenu();
        submenu.setLabel(("user"));

        MenuItem item;


        item = new MenuItem();
        item.setValue(("Summery"));
        item.setUrl("opd_summery.xhtml");
        submenu.getChildren().add(item);


        item = new MenuItem();
        item.setValue(("changePassword"));
        item.setUrl("change_password.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("preferences"));
        item.setUrl("user_preferances.xhtml");
        submenu.getChildren().add(item);

        return submenu;
    }

    private Submenu adminSubmenu() {
        Submenu submenu;

        MenuItem item;

        submenu = new Submenu();
        submenu.setLabel(("admin"));

        item = new MenuItem();
        item.setValue(("manageAccounts"));
        item.setUrl("admin_manage_users.xhtml");
        submenu.getChildren().add(item);

        item = new MenuItem();
        item.setValue(("managePreviliges"));
        item.setUrl("demography_index.xhtml");
        submenu.getChildren().add(item);




        return submenu;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
}
