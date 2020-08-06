package tk.deriwotua.dashboard.utils;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    private static Logger logger = Logger.getLogger(DateUtils.class);
    public static final String LONG_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String SHORT_PATTERN = "yyyyMMddHHmmss";
    public static final String PATTERN_THREE = "yyyy-MM-dd";
    public static final String PATTERN_FOUR = "yyyyMM";
    public static final String SHORTTIME_PATTERN = "HH:mm:ss";
    public static final String[] normalDateFormatArray = new String[]{"yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd"};
    public static final int MINITE_TYPE = 0;
    public static final int HOUR_TYPE = 1;
    public static final int DAY_TYPE = 2;
    public static final int MONTH_TYPE = 3;
    public static final int YEAR_TYPE = 4;

    public DateUtils() {
    }

    public static String formatTime(Date date) {
        return null == date ? "" : (new DateTime(date)).toString("yyyy-MM-dd HH:mm:ss");
    }

    public static String formatShortTime(Date date) {
        return null == date ? "" : (new DateTime(date)).toString("HH:mm:ss");
    }

    public static Date parseDate(String source) {
        return parseDate(source, "yyyy-MM-dd");
    }

    public static Date parseDate(String source, String pattern) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(source).toDate();
    }

    public static Date parseTime(String source) {
        return parseDate(source, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateStrOfDayLastMonth() {
        Date date = new Date();
        int day = getDayInMonth(date);
        date = dateAddMonths(date, -1);
        int year = getYear(date);
        int month = getMonthOfYear(date);
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + month;
        }

        String dayStr = day + "";
        if (day < 10) {
            dayStr = "0" + day;
        }

        return year + "-" + monthStr + "-" + dayStr;
    }

    public static Date stringToDate(String strDate, String pattern) {
        if (strDate != null && strDate.trim().length() > 0) {
            try {
                DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
                return fmt.parseDateTime(strDate).toDate();
            } catch (Exception var3) {
                logger.warn("Parse date error! strDate [" + strDate + "], pattern [" + pattern + "].", var3);
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date stringToDate(String strDate) {
        if (strDate != null && strDate.trim().length() > 0) {
            String[] var1 = normalDateFormatArray;
            int var2 = var1.length;
            int var3 = 0;

            while (var3 < var2) {
                String df = var1[var3];
                SimpleDateFormat sdf = new SimpleDateFormat(df);

                try {
                    return sdf.parse(strDate);
                } catch (ParseException var7) {
                    ++var3;
                }
            }

            logger.warn("Parse date error! strDate [" + strDate + "], pattern [yyyy-MM-dd][yyyy/MM/dd][yyyyMMdd].");
            return null;
        } else {
            return null;
        }
    }

    public static boolean checkDate(String strDate) {
        if (strDate != null && strDate.length() != 0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.parse(strDate);
                return true;
            } catch (ParseException var2) {
                logger.info("Wrong strDate [" + strDate + "], pattern [yyyy-MM-dd].");
                return false;
            }
        } else {
            return true;
        }
    }

    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
    }

    public static Date dateAddSeconds(Date date, int addSeconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(addSeconds).toDate();
    }

    public static Date dateAddMinutes(Date date, int addMinutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(addMinutes).toDate();
    }

    public static Date dateAddHours(Date date, int addHours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(addHours).toDate();
    }

    public static Date dateAddDays(Date date, int addDays) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(addDays).toDate();
    }

    public static Date dateAddMonths(Date date, int addMonths) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(2, addMonths);
            return cal.getTime();
        }
    }

    public static Date dateAddWeeks(Date date, int addWeeks) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(3, addWeeks);
            return cal.getTime();
        }
    }

    public static Date dateAddYears(Date date, int addYears) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(1, addYears);
            return cal.getTime();
        }
    }

    public static int getDateLength(Date beginDate, Date endDate) {
        int length = 0;
        if (beginDate != null && endDate != null) {
            length = (int) ((endDate.getTime() - beginDate.getTime()) / 86400000L);
            ++length;
            return length;
        } else {
            return length;
        }
    }

    public static String getDateBegin(String strDateSegment) {
        return strDateSegment != null && strDateSegment.length() > 0 ? strDateSegment + " 00:00:00" : null;
    }

    public static String getDateEnd(String strDateSegment) {
        return strDateSegment != null && strDateSegment.length() > 0 ? strDateSegment + " 23:59:59" : null;
    }

    public static boolean checkLongDatePattern(String strDate) {
        String ps = "^\\d{4}\\d{1,2}\\d{1,2}-\\d{4}\\d{1,2}\\d{1,2}$|^\\d{4}\\d{1,2}\\d{1,2}$";
        Pattern p = Pattern.compile(ps);
        Matcher m = p.matcher(strDate);
        return m.matches();
    }

    public static boolean checkDatePattern(String strDate) {
        String ps = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
        Pattern p = Pattern.compile(ps);
        Matcher m = p.matcher(strDate);
        return m.matches();
    }

    public static List<Date> transformStrDateToListDate(String strDate) throws ParseException {
        List<Date> dateList = new ArrayList();
        String[] tempStrDate = strDate.split(",");

        for (int i = 0; i < tempStrDate.length; ++i) {
            Date tempDate = stringToDate(tempStrDate[i], "yyyy-MM-dd");
            dateList.add(tempDate);
        }

        return dateList;
    }

    public static HashMap<String, String> formatStrDateToDateMap(String strDate) throws ParseException {
        HashMap<String, String> dateMap = new HashMap();
        Date beginDate = null;
        Date endDate = null;
        if (strDate != null && strDate.trim().length() != 0) {
            if (strDate.indexOf("--") > 0) {
                String[] tempDate = strDate.split("--");
                if (tempDate.length == 2) {
                    if (tempDate[0].indexOf("-") > 0) {
                        beginDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
                    } else {
                        beginDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
                    }

                    if (tempDate[1].indexOf("-") > 0) {
                        endDate = stringToDateThrowsExe(tempDate[1], "yyyy-MM-dd");
                    } else {
                        endDate = stringToDateThrowsExe(tempDate[1], "yyyyMMdd");
                    }
                } else if (tempDate.length == 1) {
                    if (tempDate[0].indexOf("-") > 0) {
                        beginDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
                        endDate = stringToDateThrowsExe(tempDate[0], "yyyy-MM-dd");
                    } else {
                        beginDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
                        endDate = stringToDateThrowsExe(tempDate[0], "yyyyMMdd");
                    }
                }
            } else if (strDate.indexOf("-") > 0) {
                beginDate = stringToDateThrowsExe(strDate, "yyyy-MM-dd");
                endDate = stringToDateThrowsExe(strDate, "yyyy-MM-dd");
            } else {
                beginDate = stringToDateThrowsExe(strDate, "yyyyMMdd");
                endDate = stringToDateThrowsExe(strDate, "yyyyMMdd");
            }

            dateMap.put("beginDate", dateToString(beginDate, "yyyy-MM-dd"));
            dateMap.put("endDate", dateToString(endDate, "yyyy-MM-dd"));
            return dateMap;
        } else {
            return null;
        }
    }

    public static Date stringToDateThrowsExe(String strDate, String pattern) throws ParseException {
        if (strDate != null && strDate.trim().length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(strDate);
        } else {
            return null;
        }
    }

    public static boolean compareDate(String beginDate, String endDate) {
        if (endDate == null || endDate.trim().length() == 0) {
            endDate = beginDate;
        }

        Date bDate = stringToDate(beginDate, "yyyy-MM-dd");
        Date eDate = stringToDate(endDate, "yyyy-MM-dd");
        if (bDate != null && eDate != null) {
            return !bDate.after(eDate);
        } else {
            return true;
        }
    }

    public static List<String> checkRepeatDate(String strDate) throws ParseException {
        List<String> repeatDateList = new ArrayList();
        Map<Date, String> mediaPlanDateMap = new HashMap();
        String[] tempStrDate = strDate.split(",");

        for (int k = 0; k < tempStrDate.length; ++k) {
            new HashMap();
            HashMap<String, String> dateMap = formatStrDateToDateMap(tempStrDate[k]);
            if (dateMap != null) {
                String strBeginDate = (String) dateMap.get("beginDate");
                String strEndDate = (String) dateMap.get("endDate");
                if (strEndDate == null || strEndDate.length() == 0) {
                    strEndDate = strBeginDate;
                }

                Date tmpBeginDate = null;
                Date tmpEndDate = null;
                if (strBeginDate.indexOf("-") > 0) {
                    tmpBeginDate = stringToDate(strBeginDate, "yyyy-MM-dd");
                    tmpEndDate = stringToDate(strEndDate, "yyyy-MM-dd");
                } else {
                    tmpBeginDate = stringToDate(strBeginDate, "yyyyMMdd");
                    tmpEndDate = stringToDate(strEndDate, "yyyyMMdd");
                }

                int len = getDateLength(tmpBeginDate, tmpEndDate);
                Date tmpDate = null;

                for (int j = 0; j < len; ++j) {
                    tmpDate = dateAddDays(tmpBeginDate, j);
                    if (mediaPlanDateMap.size() > 0) {
                        if ("1".equals(mediaPlanDateMap.get(tmpDate))) {
                            repeatDateList.add(dateToString(tmpDate, "yyyy-MM-dd"));
                        } else {
                            mediaPlanDateMap.put(tmpDate, "1");
                        }
                    } else {
                        mediaPlanDateMap.put(tmpDate, "1");
                    }
                }
            }
        }

        return repeatDateList;
    }

    public static String parseYear(Date date) {
        return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)).format(date).substring(0, 4);
    }

    public static String parseYear() {
        Date date = Calendar.getInstance().getTime();
        return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)).format(date).substring(0, 4);
    }

    public static String dateToSimpleString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static Date stringSimpleToDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return (Date) format.parseObject(date);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static int getQuarterByMonth(int month) {
        return month >= 1 && month <= 12 ? (month + 2) / 3 : -1;
    }

    public static Date getSunday(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int intWeek = c.get(7);
            c.add(5, -(intWeek - 1));
            date = c.getTime();
        } catch (Exception var3) {
            System.err.println("ReportGatekeeper.java--getSunday:" + var3);
        }

        return date;
    }

    public static Date getSaturday(Date date) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(getSunday(date));
            c.add(5, 6);
            date = c.getTime();
        } catch (Exception var2) {
            System.err.println("ReportGatekeeper.java--getSunday:" + var2);
        }

        return date;
    }

    public static Date getFirstDayInMonth(Date date) {
        String dateString = dateToSimpleString(date);
        dateString = dateString.substring(0, 8);
        dateString = dateString.concat("01");
        date = stringToDate(dateString);
        return date;
    }

    public static int getDayInMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    public static int getYear(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getYear();
    }

    public static int getMonthOfYear(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getMonthOfYear();
    }

    public static Date getFirstDayInQuarter(Date date) {
        String dateString = dateToSimpleString(date);
        String t = dateString.substring(5, 7);
        dateString = dateString.substring(0, 5);
        int i = new Integer(t);
        int q = getQuarterByMonth(i);
        int month = (q - 1) * 3 + 1;
        if (month < 10) {
            dateString = dateString.concat("0" + month);
        } else {
            dateString = dateString.concat("" + month);
        }

        dateString = dateString.concat("-01");
        date = stringToDate(dateString);
        return date;
    }

    public static Date getFirstDayInYear(Date date) {
        String dateString = dateToSimpleString(date);
        String t = dateString.substring(0, 4);
        dateString = t.concat("-01-01");
        date = stringToDate(dateString);
        return date;
    }

    public static Date getLastDayInYear(Date date) {
        Date tmp = getFirstDayInYear(date);
        tmp = dateAddYears(tmp, 1);
        tmp = dateAddDays(tmp, -1);
        return tmp;
    }

    public static Date getLastDayInMonth(Date firstDayInMonth) {
        Date tmp = dateAddMonths(firstDayInMonth, 1);
        tmp = getFirstDayInMonth(tmp);
        tmp = dateAddDays(tmp, -1);
        return tmp;
    }

    public static Date getLastDayInQuarter(Date value) {
        Date tmp = getFirstDayInQuarter(value);
        tmp = dateAddMonths(tmp, 3);
        tmp = dateAddDays(tmp, -1);
        return tmp;
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(7) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        return org.apache.commons.lang3.time.DateUtils.addDays(date, 1 - dayOfWeek);
    }

    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(7) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        return org.apache.commons.lang3.time.DateUtils.addDays(date, 7 - dayOfWeek);
    }

    public static String formatDate(Date date) {
        return null == date ? "" : (new DateTime(date)).toString("yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        return null == date ? "" : (new DateTime(date)).toString(pattern);
    }

    public static String formatDate(int tag) {
        Date date = Calendar.getInstance().getTime();
        String dateStr = null;
        switch (tag) {
            case 0:
                dateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)).format(date);
                break;
            case 1:
                dateStr = (new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)).format(date);
                break;
            case 2:
                dateStr = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(date);
                break;
            case 3:
                dateStr = (new SimpleDateFormat("yyyyMM", Locale.CHINA)).format(date);
                break;
            default:
                dateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)).format(date);
        }

        return dateStr;
    }

    public static boolean dateCompare(Date date1, Date date2) {
        return date1.getTime() < date2.getTime();
    }

    public static int dateCal(Date startdate, Date enddate, int iType) {
        Calendar calBegin = parseDateTime(startdate);
        Calendar calEnd = parseDateTime(enddate);
        long lBegin = calBegin.getTimeInMillis();
        long lEnd = calEnd.getTimeInMillis();
        int ss = (int) ((lEnd - lBegin) / 1000L);
        int min = ss / 60;
        int hour = min / 60;
        int day = hour / 24;
        if (iType == 0) {
            return min;
        } else if (iType == 1) {
            return hour;
        } else {
            return iType == 2 ? day : -1;
        }
    }

    public static Calendar parseDateTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int yy = cal.get(1);
        int mm = cal.get(2);
        int dd = cal.get(5);
        int hh = cal.get(11);
        int mi = cal.get(12);
        int ss = cal.get(13);
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal;
    }

    public static void main(String[] args) throws ParseException {
        Date dateAddMinutes = dateAddMinutes(new Date(), 1);
        String dateToString = dateToString(dateAddMinutes, "yyyy-MM-dd HH:mm:ss");
        System.out.println(dateToString);
    }

}
