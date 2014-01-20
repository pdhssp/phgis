
package gov.sp.health.bean;

import static gov.sp.health.bean.SessionController.isValidEmailAddress;

import gov.sp.health.ejb.ApplicationEjb;
import gov.sp.health.ejb.WebUserBean;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.Logins;
import gov.sp.health.entity.WebUser;
import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.WebUserPrivilege;
import gov.sp.health.entity.WebUserRole;
import gov.sp.health.facade.LoginsFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.WebUserFacade;
import gov.sp.health.facade.WebUserPrivilegeFacade;
import gov.sp.health.facade.WebUserRoleFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


@Named
@SessionScoped
public class SessionController implements Serializable, HttpSessionListener {

    @EJB
    WebUserBean webUserBean;
    private static final long serialVersionUID = 1L;
    WebUser loggedUser = null;
    boolean logged = false;
    boolean activated = false;
    String primeTheme;
    String defLocale;
    @Inject
    private MessageController messageController;
    @Inject
    SecurityController securityController;
    Institution institution;
    Area area;

    public Area getArea() {
        if (getLoggedUser() != null && getLoggedUser().getStaff() != null) {
            area = getLoggedUser().getWebUserPerson().getArea();
        }else{
            area =null;
        }
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public WebUserBean getWebUserBean() {
        return webUserBean;
    }

    public void setWebUserBean(WebUserBean webUserBean) {
        this.webUserBean = webUserBean;
    }

    public MessageController getMessageController() {
        return messageController;
    }

    public SecurityController getSecurityController() {
        return securityController;
    }

    public void setSecurityController(SecurityController securityController) {
        this.securityController = securityController;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }
    @EJB
    WebUserFacade uFacade;
    @EJB
    PersonFacade pFacade;
    @EJB
    WebUserRoleFacade rFacade;
    //
    WebUser current;
    String userName;
    String passord;
    String newPassword;
    String newPasswordConfirm;
    String newPersonName;
    String newUserName;
    String newDesignation;
    String newInstitution;
    String newPasswordHint;
    String telNo;
    String email;
    private String displayName;
    WebUserRole role;

    public WebUserRole getRole() {
        return role;
    }

    public void setRole(WebUserRole role) {
        this.role = role;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private WebUserFacade getFacede() {
        return uFacade;
    }

    public String loginAction() {
        if (login()) {
            return "";
        } else {
            UtilityController.addErrorMessage("Login Failure. Please try again");
            return "";
        }
    }

    private boolean login() {
        getApplicationEjb().recordAppStart();
        if (userName.trim().equals("")) {
            UtilityController.addErrorMessage("Please enter a username");
            return false;
        }
        if (isFirstVisit()) {
            prepareFirstVisit();
            return true;
        } else {
            return checkUsers();
        }
    }

    private void prepareFirstVisit() {
        WebUser user = new WebUser();
        Person person = new Person();
        person.setName(userName);
        pFacade.create(person);

        WebUserRole myRole;
        myRole = new WebUserRole();
        myRole.setName("circular_editor");
        rFacade.create(myRole);

        myRole = new WebUserRole();
        myRole.setName("circular_adder");
        rFacade.create(myRole);

        myRole = new WebUserRole();
        myRole.setName("circular_viewer");
        rFacade.create(myRole);


        myRole = new WebUserRole();
        myRole.setName("admin");
        rFacade.create(myRole);

        user.setName(getSecurityController().encrypt(userName));
        user.setWebUserPassword(getSecurityController().hash(passord));
        user.setWebUserPerson(person);
        user.setActivated(true);
        user.setRole(myRole);
        uFacade.create(user);


//        JsfUtil.addSuccessMessage("New User Added");


    }

    @SuppressWarnings("empty-statement")
    private boolean telNoOk() {

        int temp; // temp value to check if the telNo is numeric
        String[] telCodes = {"071", "072", "075", "077", "078"};

        // tel no validation
        //Chaminda to write
        if (telNo.trim().length() == 10) {
            // check if the length of the String is 10 chars
            //System.out.println("length OK !");

            try {
                temp = Integer.parseInt(telNo);
                //check if this is a number
                //System.out.println("Integer OK !");

                for (int j = 0; j < telCodes.length; j++) {
                    // check if the number starts with a valid value
                    //System.out.println("looping OK ! " + telNo.substring(0, 3) + " " + telCodes[j]);

                    if (telNo.substring(0, 3).equalsIgnoreCase(telCodes[j])) {
                        return true;
                    }
                }
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            UtilityController.addErrorMessage("Please enter a valid Email \n" + ex.getMessage());
            result = false;
        }
        return result;
    }

    public String registeUser() {
//        if (!telNoOk()) {
//            JsfUtil.addErrorMessage("Telephone number in correct, Please enter a valid phone number");
//            return "";
//        }


        if (!userNameAvailable(newUserName)) {
            UtilityController.addErrorMessage("User name already exists. Plese enter another user name");
            return "";
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            UtilityController.addErrorMessage("Password and Re-entered password are not matching");
            return "";
        }

        if (!isValidEmailAddress(email)) {
            return "";
        }

        WebUser user = new WebUser();
        Person person = new Person();
        user.setWebUserPerson(person);
        user.setRole(role);

        person.setName(newPersonName);

        pFacade.create(person);
        user.setName(getSecurityController().encrypt(newUserName));
        user.setWebUserPassword(getSecurityController().hash(newPassword));
        user.setWebUserPerson(person);
        user.setTelNo(telNo);
        user.setEmail(email);
        user.setActivated(Boolean.TRUE);

        uFacade.create(user);
        UtilityController.addSuccessMessage("New User Registered.");
        return "";
    }

    public String changePassword() {
        WebUser user = getLoggedUser();
        if (!getSecurityController().matchPassword(passord, user.getWebUserPassword())) {
            UtilityController.addErrorMessage("The old password you entered is incorrect");
            return "";
        }
        if (!newPassword.equals(newPasswordConfirm)) {
            UtilityController.addErrorMessage("Password and Re-entered password are not maching");
            return "";
        }

        user.setWebUserPassword(getSecurityController().hash(newPassword));
        uFacade.edit(user);
        //
        UtilityController.addSuccessMessage("Password changed");
        return "index";
    }

    public Boolean userNameAvailable(String userName) {
        Boolean available = true;
        List<WebUser> allUsers = getFacede().findAll();
        for (WebUser w : allUsers) {
            if (userName.toLowerCase().equals(getSecurityController().decrypt(w.getName()).toLowerCase())) {
                available = false;
            }
        }
        return available;
    }

    private boolean isFirstVisit() {
        if (getFacede().count() <= 0) {
//            JsfUtil.addSuccessMessage("First Visit");
            return true;
        } else {
//            JsfUtil.addSuccessMessage("Not, Not First Visit");
            return false;
        }

    }

    private boolean checkUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired = false";
        List<WebUser> allUsers = getFacede().findBySQL(temSQL);
        for (WebUser u : allUsers) {
            if (getSecurityController().decrypt(u.getName()).equalsIgnoreCase(userName)) {
                if (getSecurityController().matchPassword(passord, u.getWebUserPassword())) {
                    if (getApplicationController().isLogged(u) != null) {
                        UtilityController.addErrorMessage("This user already logged. Other instances will be logged out now.");
                    }
                    u.setInstitution(institution);
                    getFacede().edit(u);

                    setLoggedUser(u);
                    setLogged(Boolean.TRUE);
                    setArea(u.getWebUserPerson().getArea());
                    setActivated(u.isActivated());
                    setRole(u.getRole());
                    getMessageController().setDefLocale(u.getDefLocale());
                    getWebUserBean().setLoggedUser(u);

                    recordLogin();

                    UtilityController.addSuccessMessage("Logged successfully");
                    return true;
                }
            }
        }
        return false;
    }
    @Inject
    ApplicationController applicationController;
    @EJB
    ApplicationEjb applicationEjb;

    public ApplicationEjb getApplicationEjb() {
        return applicationEjb;
    }

    public void setApplicationEjb(ApplicationEjb applicationEjb) {
        this.applicationEjb = applicationEjb;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public void setApplicationController(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    public void logout() {
        userPrivilages = null;
        recordLogout();
        setLoggedUser(null);
        getWebUserBean().setLoggedUser(null);
        setLogged(false);
        setActivated(false);

    }

    public WebUser getCurrent() {
        if (current == null) {
            current = new WebUser();
            Person p = new Person();
            current.setWebUserPerson(p);
        }
        return current;
    }

    public void setCurrent(WebUser current) {
        this.current = current;
    }

    public WebUserFacade getEjbFacade() {
        return uFacade;
    }

    public void setEjbFacade(WebUserFacade ejbFacade) {
        this.uFacade = ejbFacade;
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewDesignation() {
        return newDesignation;
    }

    public void setNewDesignation(String newDesignation) {
        this.newDesignation = newDesignation;
    }

    public String getNewInstitution() {
        return newInstitution;
    }

    public void setNewInstitution(String newInstitution) {
        this.newInstitution = newInstitution;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getNewPasswordHint() {
        return newPasswordHint;
    }

    public void setNewPasswordHint(String newPasswordHint) {
        this.newPasswordHint = newPasswordHint;
    }

    public String getNewPersonName() {
        return newPersonName;
    }

    public void setNewPersonName(String newPersonName) {
        this.newPersonName = newPersonName;
    }

    public PersonFacade getpFacade() {
        return pFacade;
    }

    public void setpFacade(PersonFacade pFacade) {
        this.pFacade = pFacade;
    }

    public WebUserFacade getuFacade() {
        return uFacade;
    }

    public void setuFacade(WebUserFacade uFacade) {
        this.uFacade = uFacade;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        setLogged(activated);
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public WebUserRoleFacade getrFacade() {
        return rFacade;
    }

    public void setrFacade(WebUserRoleFacade rFacade) {
        this.rFacade = rFacade;
    }

    public String getDisplayName() {
        return getSecurityController().decrypt(getLoggedUser().getName());
    }

    /**
     * Creates a new instance of SessionController
     */
    public SessionController() {
        System.out.println("session started");
    }

    public String getDefLocale() {
        defLocale = "en";
        if (getLoggedUser() != null) {
            if (getLoggedUser().getDefLocale() != null) {
                if (!getLoggedUser().getDefLocale().equals("")) {
                    return getLoggedUser().getDefLocale();
                }
            }
        }
        return defLocale;
    }

    public void setDefLocale(String defLocale) {
        this.defLocale = defLocale;
    }

    public String getPrimeTheme() {
        if (primeTheme == null || primeTheme.equals("")) {
            primeTheme = "hot-sneaks";
        }
        if (getLoggedUser() != null) {
            if (getLoggedUser().getPrimeTheme() != null) {
                if (!getLoggedUser().getPrimeTheme().equals("")) {
                    return getLoggedUser().getPrimeTheme();
                }
            }
        }
        return primeTheme;
    }

    public void setPrimeTheme(String primeTheme) {
        this.primeTheme = primeTheme;
    }

    /**
     *
     * @return
     */
    public WebUser getLoggedUser() {
        return loggedUser;
    }

    /**
     *
     * @param loggedUser
     */
    public void setLoggedUser(WebUser loggedUser) {
        this.loggedUser = loggedUser;
        getWebUserBean().setLoggedUser(loggedUser);
    }
    private List<WebUserPrivilege> userPrivilages;
    @EJB
    private WebUserPrivilegeFacade webUserPrivilegeFacade;

    public List<WebUserPrivilege> getUserPrivileges() {
        if (userPrivilages == null) {
            String sql;
            sql = "select w from WebUserPrivilege w where w.retired=false and w.webUser.id = " + getLoggedUser().getId();
            System.out.println("5");
            userPrivilages = getWebUserPrivilegeFacade().findBySQL(sql);
        }
        if (userPrivilages == null) {
            userPrivilages = new ArrayList<WebUserPrivilege>();
        }
        return userPrivilages;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public WebUserPrivilegeFacade getWebUserPrivilegeFacade() {
        return webUserPrivilegeFacade;
    }

    public void setWebUserPrivilegeFacade(WebUserPrivilegeFacade webUserPrivilegeFacade) {
        this.webUserPrivilegeFacade = webUserPrivilegeFacade;
    }

    public void setUserPrivilages(List<WebUserPrivilege> userPrivilages) {
        this.userPrivilages = userPrivilages;
    }
    Logins thisLogin;

    public Logins getThisLogin() {
        return thisLogin;
    }

    public void setThisLogin(Logins thisLogin) {
        this.thisLogin = thisLogin;
    }
    @EJB
    LoginsFacade loginsFacade;

    public LoginsFacade getLoginsFacade() {
        return loginsFacade;
    }

    public void setLoginsFacade(LoginsFacade loginsFacade) {
        this.loginsFacade = loginsFacade;
    }

    private void recordLogin() {
        if (thisLogin != null) {
            thisLogin.setLogoutAt(Calendar.getInstance().getTime());
            if (thisLogin.getId() != null && thisLogin.getId() != 0) {
                getLoginsFacade().edit(thisLogin);
            }
        }

        thisLogin = new Logins();
        thisLogin.setLogedAt(Calendar.getInstance().getTime());
        thisLogin.setInstitution(institution);
        thisLogin.setWebUser(loggedUser);

        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String ip = httpServletRequest.getRemoteAddr();
        String host = httpServletRequest.getRemoteHost();

        thisLogin.setIpaddress(ip);
        thisLogin.setComputerName(host);


        getLoginsFacade().create(thisLogin);
        getApplicationController().addToLoggins(this);
    }

    @PreDestroy
    private void recordLogout() {
        System.out.println("session distroyed");
        if (thisLogin == null) {
            return;
        }
        applicationController.removeLoggins(this);
        thisLogin.setLogoutAt(Calendar.getInstance().getTime());
        getLoginsFacade().edit(thisLogin);
        thisLogin = null;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("starting session");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("recording logout as session is distroid");
        recordLogout();
    }
}
