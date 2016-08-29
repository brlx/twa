package hu.brlx.tinyweatheralert.persistence.entity;

import hu.brlx.tinyweatheralert.persistence.TwaEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")

@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "User.graph.withAlerts",
        attributeNodes = @NamedAttributeNode("alerts"))
})
@NamedQueries({
    @NamedQuery(
        name = "User.query.byName",
        query = "SELECT u FROM User u WHERE u.userName LIKE :userName"
    )
})

public class User implements TwaEntity {

    @Id
    @SequenceGenerator(name = "USER_PKID_GENERATOR", sequenceName = "seq_users", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_PKID_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Alert> alerts;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Column(name = "cookie")
    private String cookie;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastTriggered) {
        this.lastLogin = lastTriggered;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        User user = (User) other;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User [" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", lastTriggered=" + lastLogin +
                ", cookie=" + cookie +
                ']';
    }
}
