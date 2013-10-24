package gov.sp.health.bean;

import static gov.sp.health.bean.MessageController.splitCamelCase;
import java.io.Serializable;
import java.util.*;
import javax.inject.Named; import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * 
 */
@Named
@SessionScoped
public class MessageController implements Serializable {

    private static final long serialVersionUID = 1L;

    public MessageController() {
    }
    private String defLocale;

    public MessageController(String defLocale) {
        this.defLocale = defLocale;
    }
    private ResourceBundle bundle;

    public ResourceBundle resetBundle() {
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        try {
            if (getDefLocale() == null) {
                System.out.println("Session controller is NULL");
                myLocale = new Locale("en");
            } else if (getDefLocale().equals("si_LK")) {
                myLocale = new Locale("si", "LK");
            } else if (getDefLocale().equals("ta_LK")) {
                myLocale = new Locale("ta", "LK");
            } else {
                myLocale = new Locale("en");
            }
            System.out.println(myLocale.toString());
            FacesContext context = FacesContext.getCurrentInstance();
            context.getViewRoot().setLocale(myLocale);
            bundle = context.getApplication().getResourceBundle(context, "labels");
            return bundle;
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "labels");
            return bundle;
        }
    }

    public ResourceBundle getBundle() {
        if (bundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, "labels");
        }
        return bundle;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }

    public String getValue(String key) {
        Locale myLocale;
        myLocale = new Locale("si", "LK");
        if (getDefLocale() == null) {
            System.out.println("Session controller is NULL");
            myLocale = new Locale("en");
        } else if (getDefLocale().equals("si_LK")) {
            myLocale = new Locale("si", "LK");
        } else if (getDefLocale().equals("ta_LK")) {
            myLocale = new Locale("ta", "LK");
        } else {
            myLocale = new Locale("en");
        }
        if (!getBundle().getLocale().equals(myLocale)) {
            bundle = null;
        }
        String result = splitCamelCase(key);
        try {
            result = getBundle().getString(key);
        } catch (MissingResourceException e) {
//            System.out.println("Error in Message Provider. getValue()\n" + e.getMessage());
        }
        return result;
    }

    public String getDefLocale() {
        return defLocale;
    }

    public void setDefLocale(String defLocale) {
        this.defLocale = defLocale;
    }
}
