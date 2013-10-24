/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.sp.health.bean;

import gov.sp.health.entity.Logins;
import gov.sp.health.entity.WebUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * 
 */
@Named
@ApplicationScoped
public class ApplicationController {

    Date startTime;
    List<SessionController> sessionControllers;

    public List<SessionController> getSessionControllers() {
        return sessionControllers;
    }

    public void setSessionControllers(List<SessionController> sessionControllers) {
        this.sessionControllers = sessionControllers;
    }

    
    
    
    
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @PostConstruct
    public void recordStart() {
        startTime = Calendar.getInstance().getTime();
        if(sessionControllers==null){
            sessionControllers = new ArrayList<SessionController>();
        }
    }
    
    List<Logins> loggins;

    public List<Logins> getLoggins() {
        if (loggins == null) {
            loggins = new ArrayList<Logins>();
        }
        return loggins;
    }

    public void setLoggins(List<Logins> loggins) {
        this.loggins = loggins;
    }

    public Logins isLogged(WebUser u) {
        Logins tl = null;
        for (Logins l : getLoggins()) {
            if (l.getWebUser().equals(u)) {
                tl = l;
            }
        }
        return tl;
    }

    public void addToLoggins(SessionController sc) {
        Logins login =sc.getThisLogin();
        loggins.add(login);
        getSessionControllers().add(sc);
        for(SessionController s : getSessionControllers()){
            System.out.println("sessions " + s.toString());
            System.out.println("web user is " + login.getWebUser());
            System.out.println("Login of adding is " + login);
            System.out.println("Login of session is " + s.getThisLogin() );
            if (s.getLoggedUser().equals(login.getWebUser()) && !s.getThisLogin().equals(login)){
                System.out.println("making log out");
                s.logout();
            }
        }
    }

    public void removeLoggins(SessionController sc) {
        Logins login=sc.getThisLogin();
        System.out.println("sessions logged before removing is " + getLoggins().size());
        loggins.remove(login);
        sessionControllers.remove(sc);
    }

    /**
     * Creates a new instance of ApplicationController
     */
    public ApplicationController() {
    }
}
