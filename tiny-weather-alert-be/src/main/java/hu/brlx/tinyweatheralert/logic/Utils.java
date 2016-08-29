package hu.brlx.tinyweatheralert.logic;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date todayStart() {
        Calendar nowCal = Calendar.getInstance();
        Calendar resp = Calendar.getInstance();
        resp.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        resp.set(Calendar.MILLISECOND, 0);
        return new Date(resp.getTimeInMillis());
    }
}
