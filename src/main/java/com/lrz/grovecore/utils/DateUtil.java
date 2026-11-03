package com.lrz.grovecore.utils;

import com.lrz.grovecore.envm.Constant;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Supplier;

import static com.lrz.grovecore.envm.Constant.DateFormat.*;


/**
 * 日期工具类
 *
 * @author lirenzhi
 * @since 2023/3/8 4:53 PM
 */
public final class DateUtil {

    public static String prepareDateText(String source) {
        if (source == null) {
            return null;
        }

        //如：2022/05/05 --> 2022-05-05
        if (source.length() == 10 && source.contains(Constant.Spec.LEFT_SLASH)) {
            source = source.replace(Constant.Spec.LEFT_SLASH, Constant.Spec.HORIZONTAL_BAR);
        }

        //JDK 8 强制要求有个 T 来隔开时间，将：2020-02-02 15:00:00 --> 2020-02-02T15:00:00
        return source.replace(Constant.Spec.SPACE, Constant.Spec.T);
    }

    public static class Constants {

        public static String currentDate() {
            return getNowAs(YYYYMMDD);
        }

        public static Integer currentWeekDay() {
            return LocalDate.now().getDayOfWeek().getValue();
        }

        public static String getNowAs(String format) {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        }

        public static int currentSecondOfDay() {
            return LocalTime.now().toSecondOfDay();
        }
    }

    @Getter
    @Setter
    public static class DateDiff {
        private Date start;
        private Date end;

        private long years;
        private long months;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;
    }

    /**
     * 计算时差
     * startDate一定要早于endDate，不然计算出来的就是负数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link DateDiff}
     */
    public static DateDiff calculateTimeDifference(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime fromDateTime = LocalDateTime.ofInstant(startDate.toInstant(), zoneId);
        LocalDateTime toDateTime = LocalDateTime.ofInstant(endDate.toInstant(), zoneId);

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);

        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);

        DateDiff dateDiff = new DateDiff();
        dateDiff.setStart(startDate);
        dateDiff.setEnd(endDate);
        dateDiff.setYears(years);
        dateDiff.setMonths(months);
        dateDiff.setDays(days);
        dateDiff.setHours(hours);
        dateDiff.setMinutes(minutes);
        dateDiff.setSeconds(seconds);

        return dateDiff;
    }

    public static class Convert {

        public static Date localDateToDate(LocalDate localDate) {
            return doConvert(localDate, () -> Date.from(localDate.atStartOfDay().atZone(DEFAULT_ZONE_ID).toInstant()));
        }

        public static Date localDateTimeToDate(LocalDateTime localDateTime) {
            return doConvert(localDateTime, () -> Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant()));
        }

        public static Date zonedDateTimeToDate(ZonedDateTime zonedDateTime) {
            return doConvert(zonedDateTime, () -> Date.from(zonedDateTime.withZoneSameInstant(DEFAULT_ZONE_ID).toInstant()));
        }

        public static Date offsetDateTimeToDate(OffsetDateTime offsetDateTime) {
            return doConvert(offsetDateTime, () -> Date.from(offsetDateTime.atZoneSameInstant(DEFAULT_ZONE_ID).toInstant()));
        }

        private static <T> T doConvert(Object obj, Supplier<T> supplier) {
            if (obj == null) {
                return null;
            }
            return supplier.get();
        }

        public static LocalDate offsetDateTimeToLocalDate(OffsetDateTime offsetDateTime) {
            return doConvert(offsetDateTime, () -> offsetDateTime.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate());
        }

        public static LocalDate zonedDateTimeToLocalDate(ZonedDateTime zonedDateTime) {
            return doConvert(zonedDateTime, () -> zonedDateTime.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDate());
        }

        public static LocalDate dateToLocalDate(Date date) {
            if (date == null) {
                return null;
            }
            return dateToLocalDateTime(date).toLocalDate();
        }

        public static LocalDateTime dateToLocalDateTime(Date date) {
            return doConvert(date, () -> LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID));
        }

        public static LocalDateTime offsetDateTimeToLocalDateTime(OffsetDateTime offsetDateTime) {
            return doConvert(offsetDateTime, () -> offsetDateTime.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime());
        }

        public static OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
            return doConvert(localDateTime, () -> localDateTime.atOffset(DEFAULT_ZONE_OFFSET));
        }
    }
}
