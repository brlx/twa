package hu.brlx.tinyweatheralert.logic.auth;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.*;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

public class AuthServiceTest {

    private static Gson gson;

    @Mock(name = "dao")
    Dao dao;

    @InjectMocks
    private AuthService cut;
    private LoginResponse loginResponse;
    private AuthResponse authResponse;

    @BeforeClass
    public static void setupClass() {
        gson = new Gson();
    }

    @Before
    public void before() {
        cut = new AuthService();
        cut.init();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_login_ifUserNameNotFound_shouldBeUnsuccessful() {
        when(dao.findUserByName(anyString())).thenReturn(null);
        loginResponse = cut.login("aaa", "password");
        assertLoginResponseNotNull();
        assertFalse("The login should be unsuccessful if there's no user with the username specified", loginResponse.isSuccess());
    }

    @Test
    public void test_login_ifUserNameNotFound_shouldNotSaveAnything() {
        when(dao.findUserByName(anyString())).thenReturn(null);
        loginResponse = cut.login("aaa", "password");
        verify(dao, never()).save(any());
    }

    @Test
    public void test_login_ifPasswordDoesNotMatch_shouldBeUnsuccessful() {
        final User originalUser = givenUser1();
        givenDaoReturnsUserByName(originalUser);
        loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword() + "WRONGPASSWD");
        assertLoginResponseNotNull();
        assertFalse("The login should be unsuccessful if the password does not match", loginResponse.isSuccess());
    }

    @Test
    public void test_login_ifPasswordDoesNotMatch_shouldNotSaveAnything() {
        final User originalUser = givenUser1();
        givenDaoReturnsUserByName(originalUser);
        loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword() + "WRONGPASSWD");
        verify(dao, never()).save(any());
    }

    @Test
    public void test_login_ifUserPwdMatch_shouldBeSuccessful() {
        final User originalUser = givenUser1();
        givenDaoReturnsUserByName(originalUser);
        when(dao.save(any())).then(returnsFirstArg());
        loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword());
        assertLoginResponseNotNull();
        assertTrue("The login should be successful when the username and password do match", loginResponse.isSuccess());
    }

    @Test
    public void test_login_ifUserPwdMatch_shouldSaveNewCookieForTheUser() {
        final User originalUser = givenUser1();
        final Date originalLastLogin = originalUser.getLastLogin();
        final String originalCookie = originalUser.getCookie();
        ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        givenDaoReturnsUserByName(originalUser);
        try {
            loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword());
        } catch (Exception ignore) {
        }
        verify(dao, times(1)).save(savedUserCaptor.capture());
        final User capturedUser = savedUserCaptor.getValue();
        assertEquals("The userName should not be modified when successfully logged in",
                originalUser.getUserName(), capturedUser.getUserName());
        assertEquals("The password should not be modified when successfully logged in",
                originalUser.getPassword(), capturedUser.getPassword());
        assertTrue("The lastLogin should be bumped when successfully logged in",
                originalLastLogin.getTime() < capturedUser.getLastLogin().getTime());
        assertFalse("The cookie token should be updated", capturedUser.getCookie().equals(originalCookie));
        assertEquals("The cookie should be updated",
                AuthService.generateCookie(capturedUser.getUserName(), capturedUser.getLastLogin()), capturedUser.getCookie());
    }

    @Test
    public void test_login_ifUserPwdMatch_shouldReturnTheUpdatedUser() {
        final User originalUser = givenUser1();
        final Date originalLastLogin = originalUser.getLastLogin();
        final String originalCookie = originalUser.getCookie();
        givenDaoReturnsUserByName(originalUser);
        when(dao.save(any())).then(returnsFirstArg());
        loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword());
        assertLoginResponseNotNull();
        final ClientCookie responseCookie = loginResponse.getCookie();
        assertFalse("The cookie token should be updated", responseCookie.getToken().equals(originalCookie));
    }

    private void assertLoginResponseNotNull() {
        assertNotNull("The response for the login must never be null", loginResponse);
    }

    @Test
    public void test_authorize_shouldFailWithInvalidCookie() {
        authResponse = cut.authorize("INVALID");
        assertAuthResponseNotNull();
        assertFalse("An invalid cookie should return failure", authResponse.isSuccess());
    }

    @Test
    public void test_authorize_shouldFailIfTheUserIsNotFound() {
        final ClientCookie invalidCC = givenCookieWithInvalidUser();
        authResponse = cut.authorize(gson.toJson(invalidCC));
        assertAuthResponseNotNull();
        assertFalse("The authorization should fail if the user is not found", authResponse.isSuccess());
    }

    @Test
    public void test_authorize_shouldSucceedIfTheCookieIsOk_AndTheUserDidNotLoginBefore() {
        final User originalUser = givenUser1();
        final ClientCookie cc = givenCookieWithValidUserAndCookie(originalUser);
        givenDaoReturnsUserByName(originalUser);
        authResponse = cut.authorize(gson.toJson(cc));
        assertAuthResponseNotNull();
        assertTrue("The authorization should succeed if the cookie is OK even if the user has not logged in yet", authResponse.isSuccess());
    }

    @Test
    public void test_authorize_shouldSucceedIfTheCookieIsOk_AndTheUserHasLoggedInBefore() {
        final User originalUser = givenUser1();
        final ClientCookie cc = givenCookieWithValidUserAndCookie(originalUser);
        givenDaoReturnsUserByName(originalUser);
        cut.authorize(gson.toJson(cc));
        // authorizing again
        authResponse = cut.authorize(gson.toJson(cc));
        assertAuthResponseNotNull();
        // verifying the cache
        verify(dao, times(1)).findUserByName(anyString());
    }

    @Test
    public void test_authorize_shouldFailIfTheCookieDoesNotMatch_AndTheUserDidNotLoginBefore() {
        final ClientCookie cc = givenCookie1WithValidUserInvalidCookie();
        final User originalUser = givenUser1();
        givenDaoReturnsUserByName(originalUser);
        authResponse = cut.authorize(gson.toJson(cc));
        assertAuthResponseNotNull();
        assertFalse("The authorization should fail if the cookie is invalid", authResponse.isSuccess());
    }

    @Test
    public void test_authorize_shouldFailIfTheCookieDoesNotMatch_AndTheUserHasLoggedInBefore() {
        final User originalUser = givenUser1();
        final ClientCookie cc = givenCookieWithValidUserAndCookie(originalUser);
        givenDaoReturnsUserByName(originalUser);
        cut.authorize(gson.toJson(cc));
        // authorizing again, now with invalid cookie
        authResponse = cut.authorize(gson.toJson(givenCookie1WithValidUserInvalidCookie()));
        assertAuthResponseNotNull();
        assertFalse("The authorization should fail if the cookie is invalid even if the user has once logged in", authResponse.isSuccess());
    }

    @Test
    public void test_authorize_shouldFailWithAnOlderCookie_IfThereWasANewerLogin() {
        final User originalUser = givenUser1();
        givenDaoReturnsUserByName(originalUser);
        when(dao.save(any())).then(returnsFirstArg());
        loginResponse = cut.login(originalUser.getUserName(), originalUser.getPassword());
        // TODO
    }



    @Test
    public void test_authorize_shouldFailAfterLogout_ifTheUserDidNotAuthBefore() {
        // TODO
    }

    @Test
    public void test_authorize_shouldFailAfterLogout_ifTheUserHasAuthedBefore() {
        // TODO
    }

    @Test
    public void test_logout_shouldNotReturnFailure_ifTheUserNotFound() {
        // TODO
    }




    private void assertAuthResponseNotNull() {
        assertNotNull("The response for the auth request must never be null", authResponse);
    }

    private void givenDaoReturnsUserByName(User originalUser) {
        when(dao.findUserByName(originalUser.getUserName())).thenReturn(originalUser);
    }

    private User givenUser1() {
        final long time = System.currentTimeMillis() - 60000L;
        final User user = new User();
        user.setId(1L);
        user.setUserName("flynn");
        user.setPassword("flynnPW");
        user.setCookie("flynn-" + time);
        user.setLastLogin(new Date(time));
        return user;
    }

    private ClientCookie givenCookieWithInvalidUser() {
        return new ClientCookie("clue", "clue-" + System.currentTimeMillis());
    }

    private ClientCookie givenCookie1WithValidUserInvalidCookie() {
        return new ClientCookie("flynn", "flynn-" + System.currentTimeMillis());
    }

    private ClientCookie givenCookieWithValidUserAndCookie(User user) {
        return new ClientCookie(user.getUserName(), user.getCookie());
    }


}
