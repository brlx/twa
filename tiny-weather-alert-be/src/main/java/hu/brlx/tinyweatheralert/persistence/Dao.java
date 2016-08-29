package hu.brlx.tinyweatheralert.persistence;

import hu.brlx.tinyweatheralert.persistence.entity.Alert;
import hu.brlx.tinyweatheralert.persistence.entity.City;
import hu.brlx.tinyweatheralert.persistence.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class Dao {

    private static final Logger LOG = Logger.getLogger(Dao.class.getName());

    @PersistenceContext(unitName = "TwaEntities")
    protected EntityManager em;

    public <T extends TwaEntity> T save(T e) {
        return em.merge(e);
    }

    public <T extends TwaEntity> List<T> saveAll(Collection<T> list) {
        return list.stream().map(em::merge).collect(Collectors.toList());
    }

    public <T extends TwaEntity> void delete(T e) {
        em.remove(em.contains(e) ? e : em.merge(e));
    }

    public <T extends TwaEntity> T findById(Long id, Class<T> klass) {
        return em.find(klass, id);
    }

    public <T extends TwaEntity> Collection<T> findAllById(List<Long> idList, Class<T> klass) {
        TypedQuery<T> query = em.createQuery("SELECT e FROM " + klass.getSimpleName() + " e WHERE e.pkid in :idList", klass);
        query.setParameter("idList", idList);
        return query.getResultList();
    }

    public List<City> searchCityByName(String name) {
        TypedQuery<City> query = em.createNamedQuery("City.query.byName", City.class);
        query.setParameter("cityName", name);
        return query.getResultList();
    }

    public List<Long> searchUsedCitiesIds() {
        TypedQuery<Long> query = em.createNamedQuery("City.query.distinctIdByAlerts", Long.class);
        return query.getResultList();
    }

    public User findUserByName(String name) {
        TypedQuery<User> query = em.createNamedQuery("User.query.byName", User.class);
        query.setParameter("userName", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<Alert> searchAlertsByUser(String userName) {
        TypedQuery<Alert> query = em.createNamedQuery("Alert.query.byUserName", Alert.class);
        query.setParameter("userName", userName);
        return query.getResultList();
    }

    /**
     * Fetches all the alerts that were notified earlier than the specified date
     */
    public List<Alert> searchAlertsNotNotifiedAfter(Date notAfter) {
        TypedQuery<Alert> query = em.createNamedQuery("Alert.query.notNotifiedAfter", Alert.class);
        query.setParameter("notAfter", notAfter);
        return query.getResultList();
    }

    /**
     * Returns the list of alerts which where notified before the specified date, and also, triggered after that date
     */
    public List<Alert> searchAlertsNotNotifiedAfterAndTriggered(Date notAfter) {
        TypedQuery<Alert> query = em.createNamedQuery("Alert.query.notNotifiedAfterAndTriggered", Alert.class);
        query.setParameter("notAfter", notAfter);
        return query.getResultList();
    }

    public Alert findAlertByCityAndUser(Long cityId, Long userId) {
        TypedQuery<Alert> query = em.createNamedQuery("Alert.query.byCityAndUserId", Alert.class);
        query.setParameter("cityId", cityId);
        query.setParameter("userId", userId);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
