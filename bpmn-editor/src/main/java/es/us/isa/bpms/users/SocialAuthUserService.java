package es.us.isa.bpms.users;

import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: resinas
 * Date: 20/05/13
 * Time: 21:12
 */
@Service
public class SocialAuthUserService implements UserService {
    @Autowired
    private SocialAuthTemplate socialAuthTemplate;

    @Override
    public String getLoggedUser() {
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        if (manager == null) {
            throw new RuntimeException("Not authenticated");
        }
        else {
            try {
                return manager.getCurrentAuthProvider().getUserProfile().getEmail();
            } catch (Exception e) {
                throw new RuntimeException("Not authenticated");
            }
        }
    }

    @Override
    public boolean isLogged() {
        boolean result = false;
        try {
            String user = getLoggedUser();
            if (user != null && !user.isEmpty())
                result = true;
        } catch (Exception e) {

        }

        return result;
    }

    @Override
    public void logout() {
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        if (manager != null) {
            for(String id: manager.getConnectedProvidersIds()) {
                manager.disconnectProvider(id);
            }
        }
    }
}
