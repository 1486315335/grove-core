package com.lrz.grovecore.json;

import com.lrz.grovecore.utils.DateUtil;
import com.lrz.grovecore.utils.ExceptionUtil;
import com.lrz.grovecore.utils.ObjectUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.lrz.grovecore.envm.Constant.DateFormat.*;


/**
 * 扩展 dateFormat 用于解决 jackson 在序列化时间（Date）时，只能指定一种格式
 */
public class ObjectMapperDateFormatExtend extends SimpleDateFormat {

    private final DateFormat dateFormat;

    public ObjectMapperDateFormatExtend(DateFormat dateFormat) {
        super(YYYY_MM_DD_HH_MM_SS);
        this.dateFormat = dateFormat;
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return dateFormat.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source) {
        if (ObjectUtil.isNullOrEmpty(source)) {
            return null;
        }
        try {
            return super.parse(source);
        } catch (Exception e) {
            return parseDate(source);
        }
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        //支持几下几种日期格式
        try {
            return super.parse(source, pos);
        } catch (Exception e) {
            return parseDate(source);
        }
    }

    private Date parseDate(String source) {
        String prepareDateText = DateUtil.prepareDateText(source);
        return ObjectUtil.nvl(tryToLocalDate(prepareDateText),
                () -> tryToLocalDateTime(prepareDateText),
                () -> tryToOffsetDateTime(prepareDateText),
                () -> tryToZonedDateTime(prepareDateText));
    }

    private Date tryToLocalDate(String source) {
        return ExceptionUtil.eatExceptionAndLog(() -> DateUtil.Convert.localDateToDate(LocalDate.parse(source)), null);
    }

    private Date tryToLocalDateTime(String source) {
        return ExceptionUtil.eatExceptionAndLog(() -> DateUtil.Convert.localDateTimeToDate(LocalDateTime.parse(source)), null);
    }

    private Date tryToOffsetDateTime(String source) {
        return ExceptionUtil.eatExceptionAndLog(() -> DateUtil.Convert.offsetDateTimeToDate(OffsetDateTime.parse(source)), null);
    }

    private Date tryToZonedDateTime(String source) {
        return ExceptionUtil.eatExceptionAndLog(() -> DateUtil.Convert.zonedDateTimeToDate(ZonedDateTime.parse(source)), null);
    }

    @Override
    public Object clone() {
        DateFormat dateFormat = (DateFormat) this.dateFormat.clone();
        return new ObjectMapperDateFormatExtend(dateFormat);
    }
}
