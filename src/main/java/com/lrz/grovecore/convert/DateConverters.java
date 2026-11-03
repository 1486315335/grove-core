package com.lrz.grovecore.convert;

import com.lrz.grovecore.json.ObjectMapperDateFormatExtend;
import com.lrz.grovecore.utils.DateUtil;
import com.lrz.grovecore.utils.ListUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public final class DateConverters {

    private static final ObjectMapperDateFormatExtend objectMapperDateFormatExtend = new ObjectMapperDateFormatExtend(new SimpleDateFormat());
    public static Collection<Converter<?, ?>> getConvertersToRegister() {

        List<Converter<?, ?>> converters = ListUtil.adaptIterable(Jsr310Converters.getConvertersToRegister());
        //补充 Jsr310 缺失的一些类型转换器
        converters.add(LocalDateToLocalDateTimeConverter.INSTANCE);

        converters.add(StringToDateConverter.INSTANCE);
        converters.add(StringToOffsetDateTimeConverter.INSTANCE);
        converters.add(StringToZonedDateTimeConverter.INSTANCE);

        converters.add(OffsetDateTimeToDateConverter.INSTANCE);
        converters.add(ZonedDateTimeToDateConverter.INSTANCE);

        return converters;
    }

    @ReadingConverter
    public enum LocalDateToLocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public LocalDateTime convert(LocalDate source) {
            return LocalDateTime.of(source, LocalTime.MIN);
        }
    }

    @ReadingConverter
    public enum StringToDateConverter implements Converter<String, Date> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public Date convert(String source) {
            return objectMapperDateFormatExtend.parse(source);
            //source = DateUtil.prepareDateText(source);
            //return DateUtil.Convert.localDateToDate(LocalDate.parse(source));
        }
    }

    @ReadingConverter
    public enum StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public OffsetDateTime convert(String source) {
            source = DateUtil.prepareDateText(source);
            return OffsetDateTime.parse(source);
        }
    }


    @ReadingConverter
    public enum StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public ZonedDateTime convert(String source) {
            source = DateUtil.prepareDateText(source);
            return ZonedDateTime.parse(source);
        }
    }

    @ReadingConverter
    public enum OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public Date convert(OffsetDateTime source) {
            return DateUtil.Convert.offsetDateTimeToDate(source);
        }
    }

    @ReadingConverter
    public enum ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        INSTANCE;

        @NonNull
        @Override
        @SuppressWarnings("NullableProblems")
        public Date convert(ZonedDateTime source) {
            return DateUtil.Convert.zonedDateTimeToDate(source);
        }
    }
}
