/*
 * MSc(Biomedical Informatics) Project
 *
 * Development and Implementation of a Web-based Combined Data Repository of
 Genealogical, Clinical, Laboratory and Genetic Data
 * and
 * a Set of Related Tools
 */
package gov.sp.health.bean;

import java.util.TimeZone;
import gov.sp.health.data.StaffRole;
import gov.sp.health.entity.Area;
import gov.sp.health.entity.Institution;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.Speciality;
import gov.sp.health.entity.Staff;
import gov.sp.health.facade.WebUserFacade;
import gov.sp.health.facade.WebUserRoleFacade;
import gov.sp.health.entity.WebUser;
import gov.sp.health.entity.WebUserPrivilege;
import gov.sp.health.facade.AreaFacade;
import gov.sp.health.facade.InstitutionFacade;
import gov.sp.health.facade.PersonFacade;
import gov.sp.health.facade.StaffFacade;
import gov.sp.health.facade.WebUserPrivilegeFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, PGIM Trainee for MSc(Biomedical
 * Informatics)
 */
@Named
@SessionScoped
public class WebUserController implements Serializable {

    @Inject
    SessionController sessionController;
    @Inject
    SecurityController securityController;
    @EJB
    private WebUserFacade ejbFacade;
    @EJB
    WebUserRoleFacade roleFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private WebUserPrivilegeFacade webUserPrevilageFacade;
    @EJB
    private StaffFacade staffFacade;
    List<WebUser> items;
    List<WebUser> searchItems;
    private WebUser current;
    String selectText = "";
    List<Area> areas;
    List<Institution> institutions;
    @EJB
    private InstitutionFacade institutionFacade;
    private Institution institution;
    Area area;
    Speciality speciality;
    List<WebUserPrivilege> userPrivileges;
    WebUser removingUser;
    @EJB
    AreaFacade areaFacade;

    StaffRole staffRole;

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }
    
    
    
    
    public AreaFacade getAreaFacade() {
        return areaFacade;
    }

    public void setAreaFacade(AreaFacade areaFacade) {
        this.areaFacade = areaFacade;
    }
    
    
    

    public List<Area> getAreas() {
        areas=getAreaFacade().findAll();
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    
    
   
    public void saveUser() {
        if (current == null) {
            return;
        }
        if (current.getId() == null || current.getId() == 0) {
            getFacade().create(current);
            UtilityController.addSuccessMessage("Saved");
        } else {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("Updated");
        }
    }

    public WebUser getRemovingUser() {
        return removingUser;
    }

    public void setRemovingUser(WebUser removingUser) {
        this.removingUser = removingUser;
    }

    public void removeUser() {
        if (removingUser == null) {
            UtilityController.addErrorMessage("Select a user to remove");
            return;
        }
        removingUser.getWebUserPerson().setRetired(true);
        removingUser.getWebUserPerson().setRetirer(getSessionController().getLoggedUser());
        removingUser.getWebUserPerson().setRetiredAt(Calendar.getInstance().getTime());
        getPersonFacade().edit(removingUser.getWebUserPerson());


        removingUser.setRetired(true);
        removingUser.setRetirer(getSessionController().getLoggedUser());
        removingUser.setRetiredAt(Calendar.getInstance().getTime());
        //getFacade().edit(removingUser);
        getFacade().edit(removingUser);
        UtilityController.addErrorMessage("User Removed");
    }

    public List<WebUser> completeUser(String qry) {
        List<WebUser> a = null;
        if (qry != null) {
            a = getFacade().findBySQL("select c from WebUser c where c.retired=false and (upper(c.webUserPerson.name) like '%" + qry.toUpperCase() + "%' or upper(c.code) like '%" + qry.toUpperCase() + "%') order by c.webUserPerson.name");
        }
        if (a == null) {
            a = new ArrayList<WebUser>();
        }
        return a;
    }

    public void setUserPrivileges(List<WebUserPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    public StaffFacade getStaffFacade() {
        return staffFacade;
    }

    public void setStaffFacade(StaffFacade staffFacade) {
        this.staffFacade = staffFacade;
    }

    public boolean hasPrivilege(String privilege) {
        return true;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    

    public List<Institution> getInstitutions() {
        if (institutions == null) {
            String sql;
            sql = "select i from Institution i where i.retired=false order by i.name";
            institutions = getInstitutionFacade().findBySQL(sql);
        }
        return institutions;
    }

    public void setInstitutions(List<Institution> institutions) {
        this.institutions = institutions;

    }
    boolean skip;

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public WebUserController() {
    }

    public List<WebUser> getItems() {
        if (items == null) {
            items = getFacade().findBySQL("Select d From WebUser d where d.retired = false order by d.webUserPerson.name");
            dycryptName();
        }

        return items;
    }

    private void dycryptName() {
        List<WebUser> temp = items;

        for (int i = 0; i < temp.size(); i++) {
            WebUser w = temp.get(i);
            w.setName(getSecurityController().decrypt(w.getName()).toLowerCase());
            temp.set(i, w);
        }

        items = temp;
    }

    public void setItems(List<WebUser> items) {
        this.items = items;
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

    private WebUserFacade getFacade() {
        return ejbFacade;
    }

    public WebUser searchItem(String itemName, boolean createNewIfNotPresent) {
        WebUser searchedItem = null;
        List<WebUser> temItems;
        temItems = getFacade().findAll("name", itemName, true);
        if (temItems.size() > 0) {
            searchedItem = (WebUser) temItems.get(0);
        } else if (createNewIfNotPresent) {
            searchedItem = new WebUser();
            searchedItem.setName(itemName);
            searchedItem.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            searchedItem.setCreater(sessionController.loggedUser);
            getFacade().create(searchedItem);
        }
        return searchedItem;
    }

    private void recreateModel() {
        items = null;
    }

    public void prepareAdd() {
        current = new WebUser();
    }

    public String prepairAddNewUser() {
        setCurrent(new WebUser());
        Person p = new Person();
        getCurrent().setWebUserPerson(p);
        setSpeciality(null);
       
        return "admin_add_new_user";
    }

    public SecurityController getSecurityController() {
        return securityController;
    }

    public void setSecurityController(SecurityController securityController) {
        this.securityController = securityController;
    }

    public Boolean userNameAvailable(String userName) {
        boolean available = false;
        List<WebUser> allUsers = getFacade().findAll("name", true);
        if (allUsers == null) {
            return false;
        }
        for (WebUser w : allUsers) {

            if (userName != null && w != null && w.getName() != null) {
                if (userName.toLowerCase().equals(getSecurityController().decrypt(w.getName()).toLowerCase())) {
                    System.out.println("Ift");
                    available = true;
                    return available;// ok. that is may be the issue. we will try with it ok
                }
            }
        }
        return available;
    }

    public String saveNewUser() {
        // We Deal with a new Web ser only here
        //

        if (current == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return "";
        }

        if (userNameAvailable(getCurrent().getName())) {
            UtilityController.addErrorMessage("User name already exists. Plese enter another user name");
            return "";
        }
        Staff staff = new Staff();
        getCurrent().setActivated(true);
        getCurrent().setActivatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        getCurrent().setActivator(sessionController.getLoggedUser());

        //System.out.println("Start");
        //Save Person
        getCurrent().getWebUserPerson().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        getCurrent().getWebUserPerson().setCreater(sessionController.getLoggedUser());
        getPersonFacade().create(getCurrent().getWebUserPerson());
        System.out.println("Person Saved");
        //Save Staff
        staff.setPerson(getCurrent().getWebUserPerson());
        staff.setCreatedAt(Calendar.getInstance().getTime());
        staff.setInstitution(institution);
        staff.setArea(area);
        staff.setStaffRole(staffRole);
        staff.setCode(getCurrent().getCode());
        getStaffFacade().create(staff);
        //Save Web User
        getCurrent().setInstitution(getInstitution());
        getCurrent().setName(getSecurityController().encrypt(getCurrent().getName()));
        getCurrent().setWebUserPassword(getSecurityController().hash(getCurrent().getWebUserPassword()));
        getCurrent().setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
        getCurrent().setCreater(sessionController.loggedUser);
        getCurrent().setStaff(staff);
        getFacade().create(getCurrent());
        System.out.println("Web User Saved");
        //SetPrivilage
//        for (Privileges p : currentPrivilegeses) {
//            WebUserPrivilege pv = new WebUserPrivilege();
//            pv.setWebUser(current);
//            pv.setPrivilege(p);
//            pv.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
//            pv.setCreater(getSessionController().getLoggedUser());
//            getWebUserPrevilageFacade().create(pv);
//
//        }

        recreateModel();
        prepairAddNewUser();
        selectText = "";
        UtilityController.addSuccessMessage("New User Added");
        return "staff_login";
    }

    public List<WebUser> getToApproveUsers() {
        String temSQL;
        temSQL = "SELECT u FROM WebUser u WHERE u.retired=false AND u.activated=false";
        return getEjbFacade().findBySQL(temSQL);
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebUserFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(WebUserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public WebUserRoleFacade getRoleFacade() {
        return roleFacade;
    }

    public void setRoleFacade(WebUserRoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }

    public List<WebUser> getSearchItems() {
        if (searchItems == null) {
            if (selectText.equals("")) {
                searchItems = getFacade().findAll("name", true);
            } else {
                searchItems = getFacade().findAll("name", "%" + selectText + "%",
                        true);
                if (searchItems.size() > 0) {
                    current = searchItems.get(0);
                } else {
                    current = null;
                }
            }
        }
        return searchItems;
    }

    public void setSearchItems(List<WebUser> searchItems) {
        this.searchItems = searchItems;
    }

    public void delete() {
        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setRetirer(sessionController.loggedUser);
            getFacade().edit(current);
            UtilityController.addSuccessMessage(new MessageController().getValue("deleteSuccessful"));
        } else {
            UtilityController.addErrorMessage(new MessageController().getValue("nothingToDelete"));
        }
        recreateModel();
        getItems();
        current = null;
    }

    public List<WebUser> getSelectedItems() {

        if (selectText.trim().equals("")) {
            items = getFacade().findBySQL("select c from WebUser c where c.retired=false order by c.webUserPerson.name");
        } else {
            items = getFacade().findBySQL("select c from WebUser c where c.retired=false and upper(c.webUserPerson.name) like '%" + getSelectText().toUpperCase() + "%' order by c.webUserPerson.name");
        }
        dycryptName();
        return items;
    }

    public String getSelectText() {
        return selectText;
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    

    public InstitutionFacade getInstitutionFacade() {
        return institutionFacade;
    }

    public void setInstitutionFacade(InstitutionFacade institutionFacade) {
        this.institutionFacade = institutionFacade;
    }

    public Institution getInstitution() {
        return institution;
    }

   


    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

//    public List<Privileges> getCurrentPrivilegeses() {
//        return currentPrivilegeses;
//    }
//
//    public void setCurrentPrivilegeses(List<Privileges> currentPrivilegeses) {
//        this.currentPrivilegeses = currentPrivilegeses;
//    }
    public WebUserPrivilegeFacade getWebUserPrevilageFacade() {
        return webUserPrevilageFacade;
    }

    public void setWebUserPrevilageFacade(WebUserPrivilegeFacade webUserPrevilageFacade) {
        this.webUserPrevilageFacade = webUserPrevilageFacade;
    }

    

    @FacesConverter("webUs")
    public static class WebUserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebUserController controller = (WebUserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webUserController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WebUser) {
                WebUser o = (WebUser) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + WebUserController.class.getName());
            }
        }
    }
}
