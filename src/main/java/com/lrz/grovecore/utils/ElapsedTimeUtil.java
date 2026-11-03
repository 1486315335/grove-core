package com.lrz.grovecore.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 消耗时间计算工具类
 *
 * @author lirenzhi
 * @since 2023/02/22 5:46 下午
 */
public final class ElapsedTimeUtil {

    public static String elapsedTimeFrom(long start) {
        long elapsedTime = System.currentTimeMillis() - start;
        return elapsedTimeOf(elapsedTime);
    }

    public static String elapsedTimeOf(long elapsedTimes) {
        ElapsedTime elapsedTime = new ElapsedTime();
        if(elapsedTimes < 1000) {
            elapsedTime.setMills(elapsedTimes);
            return elapsedTime.toString();
        }

        long seconds = elapsedTimes / 1000;
        long mills = elapsedTimes % 1000;

        elapsedTime.setSeconds(seconds);
        elapsedTime.setMills(mills);
        if(seconds < 60) {
            return elapsedTime.toString();
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        elapsedTime.setMinutes(minutes);
        elapsedTime.setSeconds(seconds);
        if(minutes < 60) {
            return elapsedTime.toString();
        }

        long hours = minutes / 60;
        minutes = minutes % 60;
        elapsedTime.setHours(hours);
        elapsedTime.setMinutes(minutes);
        if(hours < 24) {
            return elapsedTime.toString();
        }

        long days = hours / 24;
        hours = hours % 24;
        elapsedTime.setDays(days);
        elapsedTime.setHours(hours);

        return elapsedTime.toString();
    }

    @Setter
    @Getter
    static class ElapsedTime {
        private long days;
        private long hours;
        private long minutes;
        private long seconds;
        private long mills;

        private static Map<String, String> REPLACE_MAP;

        static {
            REPLACE_MAP = MapUtil.of(
                    " 0小时0分钟0秒0毫秒", " 0小时0分钟0秒0毫秒",
                    "小时0分钟0秒0毫秒",  "0分钟0秒0毫秒",
                    "分钟0秒0毫秒",  "0秒0毫秒",
                    "秒0毫秒", "0毫秒"
            );
        }

        @Override
        public String toString() {
            if(days + hours + minutes + seconds + mills == 0) {
                return "0";
            }

            String dayPart = "";
            if(days > 0) {
                dayPart = days + "天 ";
            }

            String hourPart = "";
            if(hours > 0 || days > 0) {
                hourPart = hours + "小时";
            }

            String minutesPart = "";
            if(minutes > 0 || hours > 0 || days > 0) {
                minutesPart = minutes + "分钟";
            }

            String secondPart = "";
            if(seconds > 0 || minutes > 0 || hours > 0 || days > 0) {
                secondPart = seconds + "秒";
            }

            String millPart = "";
            if(mills > 0 || seconds > 0 || minutes > 0 || hours > 0 || days > 0) {
                millPart = mills + "毫秒";
            }

            //eg：5天 0小时10分钟13秒512毫秒
            String format = StringUtil.format("{}{}{}{}{}", dayPart, hourPart, minutesPart, secondPart, millPart);

            //eg：5天 0小时10分钟13秒0毫秒  ---->  5天 0小时10分钟13秒
            List<String> special = ListUtil.of(" 0小时0分钟0秒0毫秒", "小时0分钟0秒0毫秒", "分钟0秒0毫秒", "秒0毫秒");
            if (StringUtil.containsAny(format, special)) {
                for (String s : special) {
                    if(format.contains(s)) {
                        format = format.replace(REPLACE_MAP.get(s), "");
                        break;
                    }
                }
            }
            return format;
        }
    }
}
