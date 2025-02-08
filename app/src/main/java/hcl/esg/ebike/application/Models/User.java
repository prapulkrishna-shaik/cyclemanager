package hcl.esg.ebike.application.Models;

public class User {
    private String emp_id;
    private String uid;
    private String email;
    private String name;
    private String UserRole;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String emp_id, String email,String name,String userRole) {
        this.emp_id = emp_id;
        this.email = email;
        this.name = name;
        this.UserRole = userRole;
    }
    public User(String uid,String emp_id, String email,String name,String userRole) {
        this.uid = uid;
        this.emp_id = emp_id;
        this.email = email;
        this.name = name;
        this.UserRole = userRole;
    }
    public User(String emp_id, String email,String name) {
        this.emp_id = emp_id;
        this.email = email;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public void setame(String eName) {
        this.name = eName;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
