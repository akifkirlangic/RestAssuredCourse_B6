package Pojo;
// pojo = Plain Old Java Object

public class GoRestUser {

    // This is our pojo class(GoRestUser class) -> bu pojo class konseptidir
    private String id;  // necessary fields (id,name,email,gender,status)
    private String name;  // name = "Luis Figo"
    private String email; // email = "luisFigo@tchno.com"
    private String gender; // gender = "male"
    private String status;  // status = "active"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
