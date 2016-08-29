package hu.brlx.tinyweatheralert.logic.users;

import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@Stateless
@Transactional
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    @Inject
    private Dao dao;

    public UserResponse loginUser(String userName, String password) {
        LOG.fine("loginUser, userName=" + userName + ", password=" + password);
        return UserResponse.success("BLABLA_COOKIE");
    }

    public UserResponse createUser(String userName, String password) {
        LOG.fine("createUser, userNAme=" + userName + ", password=" + password);
        final User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        dao.save(user);
        return UserResponse.success();
    }

}
