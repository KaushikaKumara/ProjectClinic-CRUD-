
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
@Named(value = "SaveEditAppointmentBean")
@RequestScoped
public class SaveEditAppointmentBean {

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

    private String time;
    private List<String> times;
    private String specialtyName;
    private List<String> specialtyList;
    private int patientId;
    private String patientName;
    private int timeSlotId;

    {
        times = new ArrayList<>();
        times.add("11:AM");
        times.add("12:PM");
        times.add("1:PM");
        times.add("2:PM");
        times.add("3:PM");
        times.add("4:PM");
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public List<String> getSpecialtyList() {
        return specialtyList;
    }

    public void setSpecialtyList(List<String> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

//    public String action() {
//
//        return "welcomePage.xhtml";
//    }

    public void saveChanges() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        timeSlotId = (int) sessionMap.get("timeSlotId");

        try {
            Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE timeSlots SET time = ?, specialty_id = (SELECT specialty_id FROM specialty WHERE specialty_name = ?) WHERE timeSlot_id = ?");

            preparedStatement.setString(1, getTime());
            preparedStatement.setString(2, getSpecialtyName());
            preparedStatement.setInt(3, timeSlotId);

            // Execute update
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();

            // Redirect back to the main menu or desired page
            FacesContext.getCurrentInstance().getExternalContext().redirect("appointments.xhtml");
        } catch (SQLException | IOException e) {
            // Handle exceptions
            e.printStackTrace();
        }

    }
}
