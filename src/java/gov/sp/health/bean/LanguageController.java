/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.facade.WebUserFacade;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

/**
 *
 *
 */
@Named
@SessionScoped
public class LanguageController implements Serializable {

    /**
     * Creates a new instance of LanguageController
     */
    public LanguageController() {
    }
    private static final long serialVersionUID = 1L;
    private String localeCode;
    private static Map<String, Object> countries;
    String strLocale;
    @EJB
    WebUserFacade webUserFacade;
    @Inject
    SessionController sessionController;

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebUserFacade getWebUserFacade() {
        return webUserFacade;
    }

    public void setWebUserFacade(WebUserFacade webUserFacade) {
        this.webUserFacade = webUserFacade;
    }

    public static Map<String, Object> getCountries() {
        return countries;
    }

    public static void setCountries(Map<String, Object> countries) {
        LanguageController.countries = countries;
    }

    public String getStrLocale() {
        if (strLocale == null) {
            strLocale = "si";
        }
        return strLocale;
    }

    public void setStrLocale(String strLocale) {
        this.strLocale = strLocale;
    }

    public void makeSiDefault() {
        strLocale = "si";
        if (getSessionController()!=null && getSessionController().getLoggedUser() != null) {
            getSessionController().getLoggedUser().setDefLocale("si");
            getWebUserFacade().edit(getSessionController().getLoggedUser());
        }
    }

    public void makeTaDefault() {
        strLocale = "ta";
        getSessionController().getLoggedUser().setDefLocale("ta");
        getWebUserFacade().edit(getSessionController().getLoggedUser());
    }

    public void makeEnDefault() {
        strLocale = "en";
        getSessionController().getLoggedUser().setDefLocale("en");
        getWebUserFacade().edit(getSessionController().getLoggedUser());
    }

    static {
        Locale sinhalaLocale = new Locale("si", "LK");
        Locale tamilLocale = new Locale("ta", "LK");
        countries = new LinkedHashMap<String, Object>();
        countries.put("English", Locale.ENGLISH); //label, value
        countries.put("Sinhala", sinhalaLocale);
        countries.put("Tamil", tamilLocale);
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    //value change event listener
    public void countryLocaleCodeChanged(ValueChangeEvent e) {

        String newLocaleValue = e.getNewValue().toString();

        //loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(newLocaleValue)) {

                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());

            }
        }
    }
}
