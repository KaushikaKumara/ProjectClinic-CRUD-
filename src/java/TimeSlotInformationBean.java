
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author kaushikakumara
 */
@Named(value = "TimeSlotInformationBean")
@RequestScoped

public class TimeSlotInformationBean implements Serializable {

    @Inject
    private SpecialtyIdBean specialtyIdBean;

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
    private int patientId;
    private int specialtyId;
    private String selectedDoctorType;
    private List<String> availableTimeSlots;

    private List<String> allTimeSlots;

    {
        allTimeSlots = new ArrayList<>();
        allTimeSlots.add("11:AM");
        allTimeSlots.add("12:PM");
        allTimeSlots.add("1:PM");
        allTimeSlots.add("2:PM");
        allTimeSlots.add("3:PM");
        allTimeSlots.add("4:PM");
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public String getSelectedDoctorType() {
        return selectedDoctorType;
    }

    public void setSelectedDoctorType(String selectedDoctorType) {
        this.selectedDoctorType = selectedDoctorType;
    }
    DataSource dataSource;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String returnDoctorType() {
        return selectedDoctorType;
    }

    public String navigateToUserDetails() throws SQLException {
        int specialtyId = specialtyIdBean.getSelectedSpecialtyId();
        int patientId = specialtyIdBean.getPatientId();
        String time = this.time;
        System.out.println("selected doctor id" + specialtyId);
        System.out.println("patient id" + patientId);
        System.out.println("selected time slot" + time);

        int status = 0;
        try (Connection connection = createConnection()) {

            PreparedStatement saveTimeSlotInformation
                    = connection.prepareStatement("INSERT INTO TIMESLOTS "
                            + "(TIME,PATIENT_ID,SPECIALTY_ID)"
                            + "VALUES ( ?, ?, ?)");

            saveTimeSlotInformation.setString(1, time);
            saveTimeSlotInformation.setInt(2, patientId);
            saveTimeSlotInformation.setInt(3, specialtyId);

            status = saveTimeSlotInformation.executeUpdate();
            System.out.println(status);
            connection.close();
            return "success";
        } catch (SQLException e) {
            System.out.println("Message.. " + e.getMessage());
            e.printStackTrace();
            return "error";
        }

    }

    public List<String> getBlockedTimeSlots(Connection connection, int specialtyId) throws SQLException {
        List<String> blockedTimeSlots = new ArrayList<>();
        String query = "SELECT TIME FROM TIMESLOTS WHERE SPECIALTY_ID = ?";

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, specialtyId);

        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            String time = resultSet.getString("TIME");
            blockedTimeSlots.add(time);
        }

        resultSet.close();
        stmt.close();

        return blockedTimeSlots;
    }

    public List<SelectItem> getAvailableTimeSlot() throws SQLException {
        List<SelectItem> timeSlots = new ArrayList<>();
        int specialtyId = specialtyIdBean.getSelectedSpecialtyId();
        Connection connection = createConnection();

        try {
            List<String> remaningTimeSlots = new ArrayList<>();
            Set<String> availableTimeSlotSet = new HashSet<>(getBlockedTimeSlots(connection, specialtyId));

            for (String timeSlot : allTimeSlots) {
                if (!availableTimeSlotSet.contains(timeSlot)) {
                    remaningTimeSlots.add(timeSlot);
                }
            }

            for (String timeSlot : remaningTimeSlots) {
                timeSlots.add(new SelectItem(timeSlot, timeSlot));
            }
        } catch (SQLException e) {
            // Handle exception
        } finally {
           connection.close();
        }

        return timeSlots;
    }

}
