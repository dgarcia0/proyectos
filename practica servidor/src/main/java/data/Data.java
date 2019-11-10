package data;

import java.util.List;

public interface Data {
    User getUserByEmail(String email) throws UserNotFoundException;
    boolean exist(User user);
    void add(User user);
    void remove(String email) throws UserNotFoundException;
    boolean login(String email, String passwd);
    List getAllUsers();
}
