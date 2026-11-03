package com.lrz.grovecore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

/**
 * 日期工具类
 *
 * @author lirenzhi
 * @since 2023/02/22
 */
public final class SpringDates {

    public static Helper parse(String date) {
        return new Helper(date);
    }

    public static Helper.DateValueWrapper wrapper(Date date) {
        return new Helper.DateValueWrapper(date);
    }

    public static Helper.LocalDateValueWrapper wrapper(LocalDate localDate) {
        return new Helper.LocalDateValueWrapper(localDate);
    }

    public static Helper.LocalDateTimeValueWrapper wrapper(LocalDateTime localDateTime) {
        return new Helper.LocalDateTimeValueWrapper(localDateTime);
    }

    public static Helper.OffsetDateTimeValueWrapper wrapper(OffsetDateTime offsetDateTime) {
        return new Helper.OffsetDateTimeValueWrapper(offsetDateTime);
    }

    public static Helper.ZonedDateTimeValueWrapper wrapper(ZonedDateTime zonedDateTime) {
        return new Helper.ZonedDateTimeValueWrapper(zonedDateTime);
    }

    public static class Helper {
        private final String value;


        public Helper(String value) {
            this.value = value;
        }

        public DateValueWrapper asDate() {
            return doAs(value, value -> {
                Date date = ApplicationContextHolder.getBean(ConversionService.class).convert(value, Date.class);
                return new DateValueWrapper(date);
            });
        }

        public LocalDateValueWrapper asLocalDate() {
            return doAs(value, value -> {
                LocalDate convert = ApplicationContextHolder.getBean(ConversionService.class).convert(value, LocalDate.class);
                return new LocalDateValueWrapper(convert);
            });
        }

        public LocalDateTimeValueWrapper asLocalDateTime() {
            return doAs(value, value -> {
                value = DateUtil.prepareDateText(value);
                LocalDateTime convert = ApplicationContextHolder.getBean(ConversionService.class).convert(value, LocalDateTime.class);
                return new LocalDateTimeValueWrapper(convert);
            });
        }

        public OffsetDateTimeValueWrapper asOffsetDateTime() {
            return doAs(value, value -> {
                OffsetDateTime convert = ApplicationContextHolder.getBean(ConversionService.class).convert(value, OffsetDateTime.class);
                return new OffsetDateTimeValueWrapper(convert);
            });
        }

        public ZonedDateTimeValueWrapper asZonedDateTime() {
            return doAs(value, value -> {
                ZonedDateTime convert = ApplicationContextHolder.getBean(ConversionService.class).convert(value, ZonedDateTime.class);
                return new ZonedDateTimeValueWrapper(convert);
            });
        }

        private <T> T doAs(String value, Function<String, T> function) {
            if (!StringUtils.hasText(value)) {
                return null;
            }
            return function.apply(value);
        }

        static abstract class AbstractValueWrapper<T> implements ValueWrapper<T> {

            @Override
            public Date toDate() {
                return doTo(Date.class);
            }

            @Override
            public LocalDate toLocalDate() {
                return doTo(LocalDate.class);
            }

            @Override
            public LocalDateTime toLocalDateTime() {
                return doTo(LocalDateTime.class);
            }

            @Override
            public String toString() {
                return doTo(String.class);
            }

            @Override
            public String format(String pattern) {
                LocalDateTime localDateTime = toLocalDateTime();
                if (localDateTime == null) {
                    return null;
                }
                return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
            }

            @SuppressWarnings("unchecked")
            private <X> X doTo(Class<? extends X> dateClass) {
                T value = getValue();
                if (value == null) {
                    return null;
                }
                if (value.getClass() == dateClass) {
                    return (X) value;
                }
                return ApplicationContextHolder.getBean(ConversionService.class).convert(value, dateClass);
            }
        }


        public interface ValueWrapper<T> {

            Date toDate();

            LocalDate toLocalDate();

            LocalDateTime toLocalDateTime();

            T getValue();

            String format(String pattern);

            @Override
            String toString();

        }

        @Getter
        @AllArgsConstructor
        public static class DateValueWrapper extends AbstractValueWrapper<Date> {
            private Date value;
        }

        @Getter
        @AllArgsConstructor
        public static class LocalDateValueWrapper extends AbstractValueWrapper<LocalDate> {
            private LocalDate value;
        }

        @Getter
        @AllArgsConstructor
        public static class LocalDateTimeValueWrapper extends AbstractValueWrapper<LocalDateTime> {
            private LocalDateTime value;
        }

        @Getter
        @AllArgsConstructor
        public static class OffsetDateTimeValueWrapper extends AbstractValueWrapper<OffsetDateTime> {
            private OffsetDateTime value;
        }

        @Getter
        @AllArgsConstructor
        public static class ZonedDateTimeValueWrapper extends AbstractValueWrapper<ZonedDateTime> {
            private ZonedDateTime value;
        }
    }
}