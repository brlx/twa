package hu.brlx.tinyweatheralert.api.cities;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.api.JAXRSConfiguration;
import hu.brlx.tinyweatheralert.logic.cities.CitiesService;
import hu.brlx.tinyweatheralert.logic.cities.CityResponse;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("cities")
@Stateless
public class CitiesApi {

    private static final Logger LOG = Logger.getLogger(CitiesApi.class.getName());

    private Gson gson;

    @Inject
    private CitiesService service;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public String searchCitiesByName(@QueryParam("name") String name) {
        LOG.fine("searchCitiesByName, name=" + name);
        final CityResponse resp = service.searchCitiesByName(name);
        return gson.toJson(resp);
    }

    @OPTIONS
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public String searchCitiesByNameOPTIONS() {
        return "{\"SUCCESS\": true}";
    }
}
