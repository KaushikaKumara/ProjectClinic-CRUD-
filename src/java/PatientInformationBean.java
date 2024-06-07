
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Statement;
import java.util.Iterator;

@Named(value = "PatientInformationBean")
@RequestScoped
public class PatientInformationBean implements Serializable {

    // Inject the TimeslotInformation bean
    @ManagedProperty(value = "#{timeslotInformation}")
    private TimeSlotInformationBean timeslotInformation;

    // Getter and setter for timeslotInformation
    public TimeSlotInformationBean getTimeslotInformation() {
        return timeslotInformation;
    }

    public void setTimeslotInformation(TimeSlotInformationBean timeslotInformation) {
        this.timeslotInformation = timeslotInformation;
    }

    public Connection createConnection() throws SQLException {
//        Connection connection = null;
        try {
            Connection connection = null;
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
//        return connection;
        return null;
    }

    public void test() throws SQLException {
        try {
            dataSource = (DataSource) new InitialContext().lookup("jdbc/clinic");
        } catch (NamingException ex) {
            Logger.getLogger(PatientInformationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PatientInformationBean() throws SQLException {
        validatePatientID = null; // Initialize validatePatientID to null
        test();
    }

    DataSource dataSource;
    private String patientName;
    private Integer patientAge;
    private String patientAddress;
    private String patientPhone;
    private int generatedPatientId;
    private Integer validatePatientID; // Changed type to Integer
    private String validatepatientName;

    public int getGeneratedPatientId() {
        return generatedPatientId;
    }

    public void setGeneratedPatientId(int generatedPatientId) {
        this.generatedPatientId = generatedPatientId;
    }

    public Integer getValidatePatientID() { // Changed return type to Integer
        return validatePatientID;
    }

    public void setValidatePatientID(Integer validatePatientID) { // Changed parameter type to Integer
        this.validatePatientID = validatePatientID;
    }

    public String getValidatepatientName() {
        return validatepatientName;
    }

    public void setValidatepatientName(String validatepatientName) {
        this.validatepatientName = validatepatientName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String save() throws SQLException {
        int status = 0;
        int generatedPatientId = -1;

        try (Connection connection = createConnection()) {
            if (isPatientNameExists(connection, getPatientName())) {
                FacesContext.getCurrentInstance().addMessage("patientForm:fullNameInputText",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Patient with the same name already exists!", null));
                return null; // Return null to stay on the same page
            }

            String sql = "INSERT INTO PATIENT "
                    + "(PATIENT_NAME, PATIENT_AGE, PATIENT_ADDRESS, PATIENT_PHONE) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement savePatientInformation = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            savePatientInformation.setString(1, getPatientName());
            savePatientInformation.setInt(2, getPatientAge());
            savePatientInformation.setString(3, getPatientAddress());
            savePatientInformation.setString(4, getPatientPhone());

            status = savePatientInformation.executeUpdate();

            if (status > 0) {
                ResultSet rs = savePatientInformation.getGeneratedKeys();
                if (rs.next()) {
                    generatedPatientId = rs.getInt(1);
                }
                rs.close();
            }

            System.out.println("Generated Patient ID: " + generatedPatientId);
            this.generatedPatientId = generatedPatientId;

            connection.close();
            return "successPatientInfo.xhtml";

        } catch (SQLException e) {
            System.out.println("Message.. " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    private boolean isPatientNameExists(Connection connection, String patientName) throws SQLException {
        PreparedStatement checkPatient = connection.prepareStatement("SELECT COUNT(*) FROM PATIENT WHERE PATIENT_NAME = ?");
        checkPatient.setString(1, patientName);
        ResultSet resultSet = checkPatient.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }
    
    public String patientValidater() throws SQLException {
    boolean exists = false;
    FacesContext context = FacesContext.getCurrentInstance();
    context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages on redirect

    try {
        Connection connection = createConnection();

        // Create a PreparedStatement to query the database for the patient
        PreparedStatement checkPatient = connection.prepareStatement("SELECT COUNT(*) FROM PATIENT WHERE PATIENT_ID = ? AND PATIENT_NAME = ?");
        checkPatient.setInt(1, validatePatientID);
        checkPatient.setString(2, validatepatientName);

        ResultSet resultSet = checkPatient.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            if (count > 0) {
                // Patient exists in the database
                exists = true;
            }
        }

        connection.close();
    } catch (SQLException e) {
        System.out.println("Error checking patient in database: " + e.getMessage());
        throw e; // Re-throw the exception to handle it at a higher level
    }

    if (exists) {
        TimeSlotInformationBean timeslotInformation = new TimeSlotInformationBean();
        timeslotInformation.setPatientId(validatePatientID);

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("validatedPatientId", validatePatientID);
        sessionMap.put("validatedPatientName", validatepatientName);
        return "welcomePage";
    } else {
        // Clear existing messages
        Iterator<FacesMessage> iter = context.getMessages();
        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Name or User ID! Please check again", null));
        return "error";
    }
}

}
