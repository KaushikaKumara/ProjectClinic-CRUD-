
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
@Named(value = "DeleteBookedAppointmentBean")
@RequestScoped

public class DeleteBookedAppointmentBean {
    
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
    
    public void deleteAppointment(int timeSlotId) throws SQLException {
    Connection connection = createConnection();

    String query = "DELETE FROM TIMESLOTS WHERE timeSlot_id = ?";

    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, timeSlotId);
        statement.executeUpdate();
        
        System.out.println("Success");
    } catch (SQLException e) {
        // Handle any SQL exceptions here
        e.printStackTrace();
        System.out.println("not Success");
    }
}
}
