package com.lrz.grovecore.envm;

import com.lrz.grovecore.utils.ListUtil;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 常量类
 *
 * @author lirenzhi
 * @since 2022/12/9 2:47 下午
 */
public interface Constant {
    List<String> DEFAULT_EXCLUDE_PATH_PATTERNS =
            ListUtil.adaptItems(
                    //错误model
                    "/error/**",
                    //静态资源
                    "/statics/**",
                    "/favicon.ico",
                    //swagger资源
                    "/webjars/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/doc.html",
                    "/actuator/**",
                    "/v2/**",
                    "/swagger**/**",
                    "/v3/**",
                    "/.well-known/**"
            );

    interface DateFormat {

        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String ZONE8 = "GMT+08:00";
        String OFFSET_ID = "+8";
        ZoneId DEFAULT_ZONE_ID = ZoneId.of(ZONE8);
        ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of(OFFSET_ID);
        String YYYY_MM = "yyyy-MM";
        String YYYYMMDD = "yyyyMMdd";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String YYYY_MM_DD_2 = "yyyy/MM/dd";
        String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS"; // 2022-04-30T15:59:59.000Z,2022-04-12T18:30:16.016
        String YYYY_MM_DD_T_HH_MM_SS_XXX = "yyyy-MM-dd'T'HH:mm:ssXXX"; // 2022-04-12T18:30:37+08:00
        String YYYY_MM_DD_T_HH_MM_SS_SXXX = "yyyy-MM-dd'T'HH:mm:ss.SXXX"; // 2022-04-12T18:53:33.3+08:00
        String YYYY_MM_DD_T_HH_MM_SS_SSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSXXX"; // 2022-04-12T18:53:33.33+08:00
        String YYYY_MM_DD_T_HH_MM_SS_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // 2022-04-12T18:31:01.135+08:00
    }

    interface Login {
        String AUTHORIZATION = "Authorization";
        String BEARER = "Bearer ";
        long MANAGE_EXPIRE_IN_SECOND = 3600 * 24L;
        long MANAGE_EXPIRE_IN_MILLI_SECOND = MANAGE_EXPIRE_IN_SECOND * 1000L;

        long MEMBER_EXPIRE_IN_SECOND = 3600 * 24L;
        long MEMBER_EXPIRE_IN_MILLI_SECOND = MEMBER_EXPIRE_IN_SECOND * 1000L;
    }


    interface Spec {
        String WILDCARD = "*";
        String SEPARATOR = ".";
        String BRACKET_FOR_ESCAPING = "'";
        String HORIZONTAL_BAR = "-";
        String UNDERLINE = "_";
        String COMMA = ",";
        String LEFT_SLASH = "/";
        String EMPTY = "";
        String SPACE = " ";
        String T = "T";
    }

    interface RequestMethod {
        String OPTIONS = "OPTIONS";
        String DELETE = "DELETE";
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
    }

    interface CacheNames {
        String CACHE_1_DAYS = "CACHE_1_DAYS";
        String CACHE_5_MINUTES = "CACHE_5_MINUTES";
        String CACHE_6_HOURS = "CACHE_6_HOURS";
    }

    interface ExchangeNames {
        String DIRECT_EXCHANGE = "wg2.direct.exchange";
        String DEAD_LETTER_EXCHANGE = "wg2.direct.dead.exchange";
    }

    interface QueueNames {
        String WG_GUIDE_SEND_SMS_MESSAGE_QUEUE = "wg2.guide.send.sms.message.queue";
        String WG_DEAD_LETTER_MESSAGE_QUEUE = "wg2.dead.letter.message.queue";
    }

    interface RoutingKey {
        String WG_GUIDE_SEND_SMS_MESSAGE_KEY = "wg2.guide.send.sms.message.key";
        String WG_DEAD_LETTER_MESSAGE_KEY = "wg2.dead.letter.message.key";
    }
}
