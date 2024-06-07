
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
@Named(value = "DisplayBookedAppointmentBean")
@RequestScoped

public class DisplayBookedAppointmentBean {
    
    DataSource dataSource;
    private List<AppointmentDetails> allBookedAppointmentsDetails;

    public void setAllBookedAppointmentsDetails(List<AppointmentDetails> allBookedAppointmentsDetails) {
        this.allBookedAppointmentsDetails = allBookedAppointmentsDetails;
    }
    
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
    
    
    public List<AppointmentDetails> getAllBookedAppointmentsDetails() throws SQLException {
        Connection connection = createConnection();
    List<AppointmentDetails> bookedAppointmentsDetails = new ArrayList<>();
    
    String query = "SELECT TIMESLOTS.timeslot_id, TIMESLOTS.time, PATIENT.patient_id, PATIENT.patient_name, SPECIALTY.specialty_name " +
                   "FROM TIMESLOTS " +
                   "INNER JOIN PATIENT ON TIMESLOTS.patient_id = PATIENT.patient_id " +
                   "INNER JOIN SPECIALTY ON TIMESLOTS.specialty_id = SPECIALTY.specialty_id";
    
    try (PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {
        
        while (resultSet.next()) {
            int timeslotId = resultSet.getInt("timeslot_id");
            String appointmentTime = resultSet.getString("time");
            int appointmentPatientId = resultSet.getInt("patient_id");
            String patientName = resultSet.getString("patient_name");
            String specialtyName = resultSet.getString("specialty_name");
            
            AppointmentDetails appointment = new AppointmentDetails(appointmentTime, appointmentPatientId, patientName, specialtyName, timeslotId);
            bookedAppointmentsDetails.add(appointment);
        }
        connection.close();
        
    } catch (SQLException e) {
        // Handle any SQL exceptions here
        e.printStackTrace();
    }
        return bookedAppointmentsDetails;
}
    
}
