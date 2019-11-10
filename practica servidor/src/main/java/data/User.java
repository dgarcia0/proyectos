package data;

import org.springframework.stereotype.Component;

//prototype
@Component
public class User {
    private String name = "";
    private String email = "";
    private String passwd = "";

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.name.equals(user.name) && this.email.equals(user.email) && this.passwd.equals(user.passwd);
    }
}
