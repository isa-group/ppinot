package es.us.isa.bpms.users;

/**
 * User: resinas
 * Date: 20/05/13
 * Time: 21:10
 */
public interface UserService {
    public String getLoggedUser();
    public boolean isLogged();
    public void logout();
}
