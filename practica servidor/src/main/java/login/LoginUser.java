package login;

import data.UserData;

public class LoginUser implements Login{
    UserData data;

    public void setData(UserData data) {
        this.data = data;
    }

    public boolean authenticate (String email, String passwd) {
        return data.login(email,passwd);
    }
}