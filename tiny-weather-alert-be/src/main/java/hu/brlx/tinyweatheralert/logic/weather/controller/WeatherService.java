package hu.brlx.tinyweatheralert.logic.weather.controller;

import hu.brlx.tinyweatheralert.logic.Utils;
import hu.brlx.tinyweatheralert.logic.notify.websocket.WebsocketEndpointManager;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.AlertMessage;
import hu.brlx.tinyweatheralert.logic.weather.boundary.WeatherApiConsumer;
import hu.brlx.tinyweatheralert.logic.weather.boundary.WeatherApiResponse;
import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.Alert;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
@Lock(LockType.READ)
@Transactional
public class WeatherService {

    private static final Logger LOG = Logger.getLogger(WeatherService.class.getName());

    private Map<Long, Float> publicTempsById = Collections.unmodifiableMap(new HashMap<>());

    @Inject
    Dao dao;

    @Inject
    private WebsocketEndpointManager messenger;

    @Lock(LockType.WRITE)
    @Schedule(second = "0", minute = "*/15", hour = "*", persistent = false)
    public void refreshWeatherFromProvider() {
        LOG.fine("refreshWeatherFromProvider");
        WeatherApiConsumer api = new WeatherApiConsumer();

        final List<Long> cityIds = dao.searchUsedCitiesIds();
        publicTempsById = fetchTempForAllCities(cityIds, api);

        LOG.fine("temperature results:");
        for (Map.Entry<Long, Float> e : publicTempsById.entrySet()) {
            LOG.fine(" " + e.getKey() + " -> " + e.getValue());
        }

        final Date todayStart = Utils.todayStart();
        final List<Alert> notNotifiedAlerts = dao.searchAlertsNotNotifiedAfter(todayStart);
        LOG.fine("notNotifiedAlerts:");
        notNotifiedAlerts.forEach(a -> LOG.fine("  " + a));
        final List<Alert> triggeredAlerts = updateAndTriggerAlerts(publicTempsById, notNotifiedAlerts);
        LOG.fine("triggeredAlerts:");
        dao.saveAll(triggeredAlerts).forEach(a -> LOG.fine("  " + a));

        final List<Alert> alertsToNotify = dao.searchAlertsNotNotifiedAfterAndTriggered(todayStart);
        LOG.fine("alertsToNotify:");
        alertsToNotify.forEach(a -> LOG.fine("  " + a));
        final List<Alert> alertsNotified = notifyAlerts(alertsToNotify, messenger, publicTempsById);
        LOG.fine("alertsNotified");
        dao.saveAll(alertsNotified).forEach(a -> LOG.fine("  " + a));
        LOG.fine("refreshWeatherFromProvider finished");
    }

    /**
     * Fetches the temperature from the weather API of the specified cities
     * @param cityIds
     * @return The temperatures by the id of the city
     */
    protected static Map<Long, Float> fetchTempForAllCities(final List<Long> cityIds, WeatherApiConsumer api) {
        final Map<Long, Float> cityTempMap = new HashMap<>();

        for (Long cityId : cityIds) {
            try {
                final WeatherApiResponse apiResponse = api.requestWeather(cityId);
                LOG.finer("  response=" + apiResponse);
                if (apiResponse.isSuccess()
                        && apiResponse.getCityWeather() != null
                        && apiResponse.getCityWeather().getMain() != null
                        && apiResponse.getCityWeather().getMain().getTemp() != null) {
                    cityTempMap.put(cityId, apiResponse.getCityWeather().getMain().getTemp());
                } else {
                    LOG.log(Level.WARNING, "Could not fetch data for city " + cityId);
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Error when requesting city " + cityId, e);
            }
        }
        return Collections.unmodifiableMap(cityTempMap);
    }

    /**
     * Returns those alerts for which there is available temperature and which also are triggered
     * @param publicTempsById The available temperature data
     * @param notNotifiedAlerts The alerts to filter and trigger
     * @return The filtered and triggered alerts
     */
    protected static List<Alert> updateAndTriggerAlerts(final Map<Long, Float> publicTempsById,
                                                        final List<Alert> notNotifiedAlerts) {
        LOG.fine("updateAndTriggerAlerts");
        Date now = new Date();
        return notNotifiedAlerts.stream()
                .filter(alert -> {
                    Float currentTempForAlert = publicTempsById.get(alert.getCity().getId());
                    // if we don't have a temp for the alert, it was probably added since the last weather update
                    // will be alerted after the next one
                    LOG.fine("  currentTemp=" + currentTempForAlert + ", " + alert);
                    return currentTempForAlert != null && currentTempForAlert > alert.getThreshold();
                })
                .peek((alert) -> {
                    alert.setLastTriggered(now);
                    LOG.fine("  triggered " + alert);
                })
                .collect(Collectors.toList());
    }

    /**
     * Tries to notify the alerts. Returns the one that could be notified, with the notification time adjusted.
     * @param alertsToNotify
     * @param messenger
     * @return
     */
    protected static List<Alert> notifyAlerts(final List<Alert> alertsToNotify,
                                              final WebsocketEndpointManager messenger,
                                              final Map<Long, Float> tempsById) {
        Date now = new Date();
        return alertsToNotify.stream()
                .filter(alert -> {
                    String city = String.format("%s (%s)", alert.getCity().getName(), alert.getCity().getCountry());
                    return messenger.sendMessage(alert.getUser().getUserName(),
                            new AlertMessage(city,
                                    alert.getThreshold().toString(),
                                    tempsById.get(alert.getCity().getId()).toString()));
                })
                .peek(alert1 -> alert1.setLastNotified(now))
                .collect(Collectors.toList());
    }

    /**
     * Returns an unmodifiable map with the cities' id as key and the current temperature as the value
     */
    public Map<Long, Float> getTempMap() {
        return publicTempsById;
    }

}
