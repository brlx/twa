package hu.brlx.tinyweatheralert.logic.auth;

import hu.brlx.tinyweatheralert.persistence.entity.User;

public class ClientCookie {

    private String userName;

    private String token;

    public ClientCookie(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    public ClientCookie(User user) {
        this.userName = user.getUserName();
        this.token = AuthService.generateCookie(user.getUserName(), user.getLastLogin());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ClientCookie [" +
                "userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ']';
    }
}
