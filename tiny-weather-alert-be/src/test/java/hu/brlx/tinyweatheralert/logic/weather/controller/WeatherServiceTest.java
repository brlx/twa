package hu.brlx.tinyweatheralert.logic.weather.controller;

import hu.brlx.tinyweatheralert.logic.Utils;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class WeatherServiceTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private WeatherService cut;

    @Before
    public void init() {
        cut = new WeatherService();
    }

    @Test
    public void testTodayStart() throws Exception {
        final String receivedString = new SimpleDateFormat(DATE_FORMAT).format(Utils.todayStart());
        String nowString = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String expectedString = nowString.substring(0, 11) + "00:00:00.000";
        assertEquals(expectedString, receivedString);
    }
}
