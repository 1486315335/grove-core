//package com.lrz.grovecore.convert;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class DateConverterRegister implements WebMvcConfigurer {
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        //添加JSR310 中定义的相关日期转换器
//        DateConverters.getConvertersToRegister().forEach(registry::addConverter);
//    }
//}