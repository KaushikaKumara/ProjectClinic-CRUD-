
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author kaushikakumara
 */
@Named(value = "ClinicFormBean")
@RequestScoped
public class ClinicFormBean {

    private int selectedOption;
    private String timeSlot;
    private String selectedDoctorType;
    private String validatedPatientName;
    private String validatedPatientId;

    public String getValidatedPatientId() {
        return validatedPatientId;
    }

    public void setValidatedPatientId(String validatedPatientId) {
        this.validatedPatientId = validatedPatientId;
    }

    public String getValidatedPatientName() {
        return validatedPatientName;
    }

    public void setValidatedPatientName(String validatedPatientName) {
        this.validatedPatientName = validatedPatientName;
    }

    public String getSelectedDoctorType() {
        return selectedDoctorType;
    }

    public void setSelectedDoctorType(String selectedDoctorType) {
        this.selectedDoctorType = selectedDoctorType;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String submit() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("selectedValue", selectedOption);

        return "timeSlots.xhtml?faces-redirect=true";

    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
    
    public ClinicFormBean (){
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        validatedPatientName = (String) sessionMap.get("validatedPatientName");
    }

}
