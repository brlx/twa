package hu.brlx.tinyweatheralert.logic.alerts;

import hu.brlx.tinyweatheralert.logic.Utils;
import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.Alert;
import hu.brlx.tinyweatheralert.persistence.entity.City;
import hu.brlx.tinyweatheralert.persistence.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@Transactional
public class AlertService {

    private static final Logger LOG = Logger.getLogger(AlertService.class.getName());

    @Inject
    private Dao dao;

    public List<Alert> listAlertsForUser(String userName) {
        return dao.searchAlertsByUser(userName);
    }

    /**
     * Adds a new alert with the parameters specified. If there already is an alert for the specifies user and city,
     * it's threshold will be adjusted to the new value insted of creating a new one.
     */
    public AlertsResponse addAlert(Long cityId, Integer threshold, String userName) {
        LOG.fine("addAlert, cityId=" + cityId +", threshold=" + threshold + ", userName=" + userName);
        final User user = dao.findUserByName(userName);
        if (user == null) {
            final String errorMsg = "The user with the name " + userName + " could not be found";
            LOG.fine(errorMsg);
            return AlertsResponse.failed(errorMsg);
        }
        final City city = dao.findById(cityId, City.class);
        if (city == null) {
            final String errorMsg = "The city with id " + cityId + " could not be found";
            LOG.fine(errorMsg);
            return AlertsResponse.failed(errorMsg);
        }

        final Alert existingAlert = dao.findAlertByCityAndUser(city.getId(), user.getId());
        if (existingAlert == null) {
            final Alert newAlert = new Alert();
            newAlert.setCity(city);
            newAlert.setThreshold(threshold);
            newAlert.setUser(user);
            newAlert.setLastTriggered(new Date(Utils.todayStart().getTime() - 1));
            newAlert.setLastNotified(new Date(Utils.todayStart().getTime() - 2));
            LOG.fine("alert not found, creating a new one: " + newAlert);
            dao.save(newAlert);
            return AlertsResponse.success();
        } else {
            existingAlert.setThreshold(threshold);
            existingAlert.setLastTriggered(new Date(Utils.todayStart().getTime() - 1));
            existingAlert.setLastNotified(new Date(Utils.todayStart().getTime() - 2));
            LOG.fine("existing alert found, threshold was adjusted: " + existingAlert);
            dao.save(existingAlert);
            return AlertsResponse.success("Updated already existing alert with new threshold");
        }
    }

    /**
     * Deletes the alert with the specified id, if it belongs to the specified userName
     * @param alertId
     * @param userName
     * @return
     */
    public AlertsResponse deleteAlert(Long alertId, String userName) {
        LOG.fine("deleteAlert, alertId=" + alertId + ", userName=" + userName);
        final Alert alert = dao.findById(alertId, Alert.class);

        if (alert != null && alert.getUser().getUserName().equals(userName)) {
            dao.delete(alert);
            LOG.fine("deleteAlert, success");
            return AlertsResponse.success();
        } else {
            LOG.fine("deleteAlert, not found");
            return AlertsResponse.failed("The alert could not be found for this user.");
        }
    }
}
