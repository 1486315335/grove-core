package com.lrz.grovecore.convert;

import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * SpringDates 专用转换器，由 Spring 管理，不改变业务项目的 MVC 日期转换规则。
 */
public class GroveDateConverter {
    private final DefaultFormattingConversionService conversionService;

    public GroveDateConverter() {
        conversionService = new DefaultFormattingConversionService();
        DateConverters.getConvertersToRegister().forEach(conversionService::addConverter);
    }

    public <T> T convert(Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }
}
