
package gov.sp.health.bean;

import java.util.TimeZone;
import gov.sp.health.entity.Person;
import gov.sp.health.entity.Speciality;
import gov.sp.health.facade.StaffFacade;
import gov.sp.health.entity.Staff;
import gov.sp.health.facade.PersonFacade;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Named;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


@Named
@SessionScoped
public class StaffController implements Serializable {

    StreamedContent scCircular;
    StreamedContent scCircularById;
    private UploadedFile file;
    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private StaffFacade ejbFacade;
    @EJB
    private PersonFacade personFacade;
    List<Staff> selectedItems;
    private Staff current;
    private List<Staff> items = null;
    String selectText = "";

    public List<Staff> completeStaff(String query) {
        List<Staff> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Staff>();
        } else {
            sql = "select p from Staff p where p.retired=false and (upper(p.person.name) like '%" + query.toUpperCase() + "%'or  upper(p.code) like '%" + query.toUpperCase() + "%' ) order by p.person.name";
            System.out.println(sql);
            suggestions = getFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public List<Staff> getSpecialityStaff(Speciality speciality) {
        List<Staff> ss;
        String sql;
        if (speciality == null) {
            ss = new ArrayList<Staff>();
        } else {
            sql = "select p from Staff p where p.retired=false and  p.speciality.id = " + speciality.getId() + " order by p.person.name";
//            System.out.println(sql);
            ss = getFacade().findBySQL(sql);
        }
        return ss;
    }

    public List<Staff> completeStaffWithoutDoctors(String query) {
        List<Staff> suggestions;
        String sql;
        if (query == null) {
            suggestions = new ArrayList<Staff>();
        } else {
            sql = "select p from Staff p where p.retired=false and (upper(p.person.name) like '%" + query.toUpperCase() + "%'or  upper(p.code) like '%" + query.toUpperCase() + "%' ) and type(p) != Doctor order by p.person.name";
            System.out.println(sql);
            suggestions = getFacade().findBySQL(sql);
        }
        return suggestions;
    }

    public String saveSignature() {
        InputStream in;
        if (file == null || "".equals(file.getFileName())) {
            return "";
        }
        if (file == null) {
            UtilityController.addErrorMessage("Please select an image");
            return "";
        }
        if (getCurrent().getId() == null || getCurrent().getId() == 0) {
            UtilityController.addErrorMessage("Please select staff member");
            return "";
        }
        System.out.println("file name is not null");
        System.out.println(file.getFileName());
        try {
            in = getFile().getInputstream();
            File f = new File(getCurrent().toString() + getCurrent().getFileType());
            FileOutputStream out = new FileOutputStream(f);
            
            //            OutputStream out = new FileOutputStream(new File(fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            
            getCurrent().setRetireComments(f.getAbsolutePath());
            getCurrent().setFileName(file.getFileName());
            getCurrent().setFileType(file.getContentType());
            in = file.getInputstream();
            getCurrent().setBaImage(IOUtils.toByteArray(in));
            getFacade().edit(getCurrent());
            return "";
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return "";
        }

    }

    public StreamedContent getSignature() {
        Staff temCir = getCurrent();
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("Printing");
        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (getCurrent() == null) {
            return new DefaultStreamedContent();
        } else {
            if (temCir.getId() != null && temCir.getBaImage() != null) {

                System.out.println(temCir.getFileType());
                System.out.println(temCir.getFileName());
                return new DefaultStreamedContent(new ByteArrayInputStream(temCir.getBaImage()), temCir.getFileType(), temCir.getFileName());
            } else {
                return new DefaultStreamedContent();
            }
        }

    }

    public StreamedContent displaySignature(Long stfId) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getRenderResponse()) {
            return new DefaultStreamedContent();
        }
        if (stfId == null) {
            return new DefaultStreamedContent();
        }

        Staff temStaff = getFacade().findFirstBySQL("select s from Staff s where s.baImage != null and s.id = " + stfId);

        System.out.println("Printing");

        if (temStaff == null) {
            return new DefaultStreamedContent();
        } else {
            if (temStaff.getId() != null && temStaff.getBaImage() != null) {
                System.out.println(temStaff.getFileType());
                System.out.println(temStaff.getFileName());
                return new DefaultStreamedContent(new ByteArrayInputStream(temStaff.getBaImage()), temStaff.getFileType(), temStaff.getFileName());
            } else {
                return new DefaultStreamedContent();
            }
        }
    }

    public List<Staff> getSelectedItems() {
        if (selectText.trim().equals("")) {
            selectedItems = getFacade().findBySQL("select c from Staff c where c.retired=false order by c.person.name");
        } else {
            selectedItems = getFacade().findBySQL("select c from Staff c where c.retired=false and upper(c.person.name) like '%" + getSelectText().toUpperCase() + "%' order by c.person.name");
        }


        return selectedItems;
    }

    public List<Staff> completeItems(String qry) {
        List<Staff> s = getFacade().findBySQL("select c from Staff c where c.retired=false and upper(c.person.name) like '%" + qry.toUpperCase() + "%' order by c.person.name");
        return s;
    }

    public StreamedContent getScCircular() {
        return scCircular;
    }

    public void setScCircular(StreamedContent scCircular) {
        this.scCircular = scCircular;
    }

    public StreamedContent getScCircularById() {
        return scCircularById;
    }

    public void setScCircularById(StreamedContent scCircularById) {
        this.scCircularById = scCircularById;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void prepareAdd() {
        current = new Staff();
    }

    public void delete() {

        if (current != null) {
            current.setRetired(true);
            current.setRetiredAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setRetirer(sessionController.getLoggedUser());
            getFacade().edit(current);
            UtilityController.addSuccessMessage("DeleteSuccessfull");
        } else {
            UtilityController.addSuccessMessage("NothingToDelete");
        }
        recreateModel();
        getItems();
        current = null;
        getCurrent();
    }

    public void setSelectedItems(List<Staff> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public String getSelectText() {
        return selectText;
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {
        if (current == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getPerson() == null) {
            UtilityController.addErrorMessage("Nothing to save");
            return;
        }
        if (current.getPerson().getId() == null || current.getPerson().getId() == 0) {
            getPersonFacade().create(current.getPerson());
        } else {
            getPersonFacade().edit(current.getPerson());
        }
        if (getCurrent().getId() != null && getCurrent().getId() > 0) {
            getFacade().edit(current);
            UtilityController.addSuccessMessage("updated Successfully");
        } else {
            current.setCreatedAt(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
            current.setCreater(sessionController.getLoggedUser());
            getFacade().create(current);
            UtilityController.addSuccessMessage("saved Successfully");
        }
        recreateModel();
        getItems();
    }

    public void setSelectText(String selectText) {
        this.selectText = selectText;
    }

    public StaffFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(StaffFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public StaffController() {
    }

    public Staff getCurrent() {
        if (current == null) {
            Person p = new Person();
            current = new Staff();
            current.setPerson(p);
        }
        return current;
    }

    public void setCurrent(Staff current) {
        this.current = current;
    }

    private StaffFacade getFacade() {
        return ejbFacade;
    }

    public List<Staff> getItems() {
        String temSql;
        temSql = "SELECT i FROM Staff i where i.retired=false order by i.person.name";
        items = getFacade().findBySQL(temSql);
        return items;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    /**
     *
     */
    @FacesConverter(forClass = Staff.class)
    public static class StaffControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StaffController controller = (StaffController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "staffController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            if (value == null || value.trim().equals("null")) {
                value = "";
            }
            try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0l;
            }
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
            if (object instanceof Staff) {
                Staff o = (Staff) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + StaffController.class.getName());
            }
        }
    }

    @FacesConverter("stfcon")
    public static class StaffConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StaffController controller = (StaffController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "staffController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            if (value == null || value.trim().equals("null")) {
                value = "";
            }
            try {
                key = Long.valueOf(value);
            } catch (Exception e) {
                key = 0l;
            }
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
            if (object instanceof Staff) {
                Staff o = (Staff) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + StaffController.class.getName());
            }
        }
    }
}
