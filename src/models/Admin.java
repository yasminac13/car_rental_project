package models;

public class Admin extends User {
    private static final long serialVersionUID = 1L;
    private String employeeId;

    public Admin(String username, String password, String email,
                 String fullName, String employeeId) {
        super(username, password, email, fullName);
        this.employeeId = employeeId;
    }

    @Override
    public String getUserType() { return "Admin"; }

    public String getEmployeeId() { return employeeId; }

    @Override
    public String toString() {
        return super.toString() + " | Employee ID: " + employeeId;
    }
}
