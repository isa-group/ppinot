package es.us.isa.bpms.users;

import org.jboss.resteasy.spi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * User: resinas
 * Date: 20/05/13
 * Time: 14:16
 */
@Path("/service/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @GET
    public String getLoggedUser() {
        try {
            return userService.getLoggedUser();
        } catch (Exception e) {
            throw new NotFoundException("Not authenticated");
        }
    }

    @Path("/logout")
    @POST
    public void logout() {
        userService.logout();
    }
}
