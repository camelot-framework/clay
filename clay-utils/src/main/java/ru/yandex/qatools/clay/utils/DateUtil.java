package ru.yandex.qatools.clay.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date util class
 * User: isadykov
 * Date: 18.01.12
 * Time: 19:49
 */
@SuppressWarnings("unused")
public final class DateUtil {

    DateUtil() {
    }

    /**
     * Get timestamp for a date
     *
     * @param date date
     * @return timestamp
     */
    public static Long timestamp(Date date) {
        return date.getTime();
    }

    /**
     * Get current timestamp
     *
     * @return timestamp
     */
    public static Long timestamp() {
        return timestamp(now());
    }

    /**
     * Get date for calendar difference from current date
     *
     * @param field  difference field (Calendar.DATE, Calendar.MINUTE ... etc)
     * @param amount how much?
     * @return date for the difference
     */
    public static Date forCalendarDiff(int field, int amount) {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.add(field, amount);
        return new Date(gCal.getTimeInMillis());
    }

    /**
     * Get date for now
     *
     * @return date
     */
    public static Date now() {
        return forCalendarDiff(Calendar.SECOND, 0);
    }

    /**
     * Checks if the difference between current moment and timestamp is greater than diff
     */
    public static boolean isTimePassedSince(long diff, long timestamp) {
        return now().getTime() - timestamp >= diff;
    }

    /**
     * Returns calendar for time
     */
    public static Calendar calThen(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal;
    }

}
