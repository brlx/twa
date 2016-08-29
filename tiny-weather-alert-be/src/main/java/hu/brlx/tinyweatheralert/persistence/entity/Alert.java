package hu.brlx.tinyweatheralert.persistence.entity;


import hu.brlx.tinyweatheralert.persistence.TwaEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "alerts")

@NamedQueries({
        @NamedQuery(
                name = "Alert.query.byUserName",
                query = "SELECT a FROM Alert a WHERE a.user.userName LIKE :userName"
        ),
        @NamedQuery(
                name = "Alert.query.byCityAndUserId",
                query = "SELECT a FROM Alert a WHERE a.user.id = :userId AND a.city.id = :cityId"
        ),
        @NamedQuery(
                name = "Alert.query.notNotifiedAfter",
                query = "SELECT a FROM Alert a WHERE a.lastNotified < :notAfter"
        ),
        @NamedQuery(
                name = "Alert.query.notNotifiedAfterAndTriggered",
                query = "SELECT a FROM Alert a WHERE a.lastNotified < :notAfter AND a.lastTriggered > :notAfter"
        )
})


public class Alert implements TwaEntity {

    @Id
    @SequenceGenerator(name = "ALERT_PKID_GENERATOR", sequenceName = "seq_alerts", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALERT_PKID_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "temp_threshold")
    private Integer threshold;

    @Column(name = "last_triggered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTriggered;

    @Column(name = "last_notified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastNotified;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Date getLastTriggered() {
        return lastTriggered;
    }

    public void setLastTriggered(Date lastTriggered) {
        this.lastTriggered = lastTriggered;
    }

    public Date getLastNotified() {
        return lastNotified;
    }

    public void setLastNotified(Date lastNotified) {
        this.lastNotified = lastNotified;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Alert alert = (Alert) other;
        return user.equals(alert.user) && city.equals(alert.city);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Alert [" +
                "id=" + id +
                ", user=" + user +
                ", city=" + city +
                ", threshold=" + threshold +
                ", lastTriggered=" + lastTriggered +
                ", lastNotified=" + lastNotified +
                '}';
    }
}
