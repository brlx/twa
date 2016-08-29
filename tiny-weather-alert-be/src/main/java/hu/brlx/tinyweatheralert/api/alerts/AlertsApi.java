package hu.brlx.tinyweatheralert.api.alerts;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.api.JAXRSConfiguration;
import hu.brlx.tinyweatheralert.logic.alerts.AlertService;
import hu.brlx.tinyweatheralert.logic.alerts.AlertsResponse;
import hu.brlx.tinyweatheralert.logic.weather.controller.WeatherService;
import hu.brlx.tinyweatheralert.persistence.entity.Alert;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("alerts")
@Stateless
public class AlertsApi {

    private static final Logger LOG = Logger.getLogger(AlertsApi.class.getName());

    private Gson gson;

    @Context
    private HttpHeaders headers;

    @Inject
    private AlertService service;

    @Inject
    private WeatherService weatherService;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAlertsForUser(@Context HttpHeaders headers) {
        final String userName = JAXRSConfiguration.extractUserNameFromHeader(headers);
        LOG.fine("listAlertsForUser, extracted userName=" + userName);
        final List<Alert> alerts = service.listAlertsForUser(userName);
        return gson.toJson(AlertListResponse.success(alerts, weatherService.getTempMap()));
    }

    @OPTIONS
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAlertsForUserOPTIONS() {
        return "{\"SUCCESS\": true}";
    }

    @GET
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addAlert(@QueryParam("cityid") String rawCityId,
                               @QueryParam("threshold") String rawThreshold,
                           @Context HttpHeaders headers) {
        final String userName = JAXRSConfiguration.extractUserNameFromHeader(headers);
        LOG.fine("add, extracted userName=" + userName);
        Long cityId;
        Integer threshold;
        try {
            cityId = Long.valueOf(rawCityId);
        } catch (NumberFormatException e) {
            return gson.toJson(AlertsResponse.failed("Invalid cityId '" + rawCityId + "'"));
        }
        try {
            threshold = Integer.valueOf(rawThreshold);
        } catch (NumberFormatException e) {
            return gson.toJson(AlertsResponse.failed("Invalid temperature threshold '" + rawThreshold + "'"));
        }
        final AlertsResponse alertsResponse = service.addAlert(cityId, threshold, userName);
        return gson.toJson(alertsResponse);
    }

    @OPTIONS
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addAlertOPTIONS() {
        return "{\"SUCCESS\": true}";
    }

    @GET
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteAlert(@QueryParam("alertid") String rawAlertId,
                              @Context HttpHeaders headers) {
        LOG.fine("delete, alertId:" + rawAlertId);
        final String userName = JAXRSConfiguration.extractUserNameFromHeader(headers);
        LOG.fine("delete, extracted userName: " + userName);
        Long alertId;
        try {
            alertId = Long.valueOf(rawAlertId);
        } catch (NumberFormatException e) {
            return gson.toJson(AlertsResponse.failed("Invalid alertId '" + rawAlertId + "'"));
        }
        final AlertsResponse alertsResponse = service.deleteAlert(alertId, userName);
        return gson.toJson(alertsResponse);
    }

    @OPTIONS
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteAlertOPTIONS() {
        return "{\"SUCCESS\": true}";
    }
}
