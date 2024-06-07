
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author kaushikakumara
 */
@Named(value = "EditAppointmentBean")
@RequestScoped
public class EditAppointmentBean {

    DataSource dataSource;

    public Connection createConnection() throws SQLException {
        test();
        Connection connection = null;
        try {

            System.out.println("Connecting to database...");
            connection = dataSource.getConnection();

            if (connection != null) {
                System.out.println("Connected successfully!");
                return connection;

            } else {
                System.out.println("Failed to make connection!");
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void test() throws SQLException {
        try {
            dataSource = (DataSource) new InitialContext().lookup("jdbc/clinic");
        } catch (NamingException ex) {
            Logger.getLogger(PatientInformationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private AppointmentDetails selectedAppointment;
    private List<String> specialtyList;
    private String newTime;
    private String newSpecialtyName;

    public String getNewTime() {
        return newTime;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getNewSpecialtyName() {
        return newSpecialtyName;
    }

    public void setNewSpecialtyName(String newSpecialtyName) {
        this.newSpecialtyName = newSpecialtyName;
    }

    {
        specialtyList = new ArrayList<>();
        specialtyList.add("General practitioner");
        specialtyList.add("Surgeon");
        specialtyList.add("Cardiologist");
        specialtyList.add("Oncologist");
        specialtyList.add("Psychiatrist");
        specialtyList.add("Obstetrician and gynaecologist");
    }

    private List<String> times;

    {
        times = new ArrayList<>();
        times.add("11:AM");
        times.add("12:PM");
        times.add("1:PM");
        times.add("2:PM");
        times.add("3:PM");
        times.add("4:PM");
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public List<String> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<String> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public AppointmentDetails getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(AppointmentDetails selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public void editAppointment(AppointmentDetails appointment) throws IOException {
        // Set the selected appointment for editing
        this.selectedAppointment = appointment;

        FacesContext.getCurrentInstance().getExternalContext().redirect("editAppointment.xhtml?timeSlotId=" + appointment.getTimeSlotId());
    }
    
    public String navigateToSaveEditPage(int timeSlotId, int patientId) {
    Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    sessionMap.put("timeSlotId", timeSlotId);
    sessionMap.put("patientIdEdit", patientId);
    return "editAppointment.xhtml";
}

}
