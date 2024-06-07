
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
@Named(value = "SpecialtyIdBean")
@RequestScoped
public class SpecialtyIdBean {

    private int selectedSpecialtyId;
    private String doctorType;
    private int patientId;

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public SpecialtyIdBean() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        selectedSpecialtyId = (int) sessionMap.get("selectedValue");
        patientId = (int) sessionMap.get("validatedPatientId");

        if (selectedSpecialtyId == 1) {
            doctorType = "General practitioner";
        } else if (selectedSpecialtyId == 2) {
            doctorType = "Surgeon";
        } else if (selectedSpecialtyId == 3) {
            doctorType = "Cardiologist";
        } else if (selectedSpecialtyId == 4) {
            doctorType = "Oncologist";
        } else if (selectedSpecialtyId == 5) {
            doctorType = "Psychiatrist";
        } else if (selectedSpecialtyId == 6) {
            doctorType = "Obstetrician and gynaecologist";
        }

    }

    public int getSelectedSpecialtyId() {
        return selectedSpecialtyId;
    }

}
