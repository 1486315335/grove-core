package com.lrz.grovecore.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.base.Strings;
import com.lrz.grovecore.exception.JsonParseException;
import com.lrz.grovecore.json.ObjectMapperDateFormatExtend;
import com.lrz.grovecore.json.ObjectMapperLocalDateTimeDeserializerExtend;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.lrz.grovecore.envm.ErrorCodeEnum.JSON_CONVERT_ERROR;

/**
 * Json工具类
 *
 * @author lirenzhi
 * @since 2022/12/23 3:14 下午
 */
@Slf4j
public final class JsonUtil {

    public static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.create();
    public static final ObjectMapper MAPPER_4_REDIS_CACHE = ObjectMapperFactory.create();
    private static final ObjectMapper SNAKE_CASE_MAPPER = ObjectMapperFactory.create();

    public static class ObjectMapperFactory {
        public static ObjectMapper create() {
            //格式化输出
            Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
            builder
                    //未知属性不失败
                    .failOnUnknownProperties(false)
                    //空对象不失败
                    .failOnEmptyBeans(false)
                    //字段可见性设置
                    .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                    //启用以下特性
                    .featuresToEnable(
                            //枚举写为字符串
                            SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                            //允许单个元素匹配到数组上
                            DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                            //不序列化被transient修饰的属性
                            MapperFeature.PROPAGATE_TRANSIENT_MARKER,
                            //未知枚举识别为null
                            DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL
                    )
                    //禁用以下特性
                    .featuresToDisable(
                            //缺少字段报错
                            DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                            //禁止Date类型转化为时间戳
                            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                    )
                    //时区
                    .timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                    //只包含非空字段
                    .serializationInclusion(JsonInclude.Include.NON_NULL)
                    .deserializerByType(LocalDateTime.class, ObjectMapperLocalDateTimeDeserializerExtend.INSTANCE)
            ;

            ObjectMapper objectMapper = builder.build();

            // 时间格式，它仅对 Date 类型的字段生效
            // 支持 两种格式：#YYYY_MM_DD_HH_MM_SS、YYYY_MM_DD_T_HH_MM_SS_SSSXXX
            // 同时，这也意味着，项目里 Date 类型会被序列化成 jackson 默认的：YYYY_MM_DD_T_HH_MM_SS_SSSXXX 格式（带时区，因为上面设置了时区）
            objectMapper.setDateFormat(new ObjectMapperDateFormatExtend(objectMapper.getDateFormat()));

            return objectMapper;
        }
    }

    static {
        // 下划线风格
        SNAKE_CASE_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 用于缓存时，要激活类型信息保存
        // activateDefaultTyping方法用于设置序列化和反序列化时需要存储Java对象的类型信息。
        // 具体来说，LaissezFaireSubTypeValidator.instance参数用于指定验证器，
        // 它可以验证Java对象的类型是否是已知的，从而防止Java反序列化漏洞的发生。
        // ObjectMapper.DefaultTyping.NON_FINAL参数用于指定序列化和反序列化时，
        // 只需要存储非final的Java对象类型信息，以减少存储空间。
        MAPPER_4_REDIS_CACHE.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    /**
     * 转换对象为json
     *
     * @param object 对象
     * @return {@link String}
     */
    public static String toJson(Object object) {
        return doToJson(object, OBJECT_MAPPER);
    }

    /**
     * 转换成 SnakeCase 形式的 Json
     * 对象字段驼峰会转成下划线
     *
     * @param object 对象
     * @return {@link String}
     */
    public static String toSnakeCaseJson(Object object) {
        return doToJson(object, SNAKE_CASE_MAPPER);
    }

    /**
     * 转换成 SnakeCase 形式的格式化 Json
     * 对象字段驼峰会转成下划线
     *
     * @param object 对象
     * @return {@link String}
     */
    @SneakyThrows
    public static String toSnakeCasePrettyJson(Object object) {
        return SNAKE_CASE_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    /**
     * 会格式化序列化后的字符串
     * {
     *      "id" : 1,
     *      "isDelete" : 1,
     *      "name" : "1213"
     * }
     *
     * @param object 对象
     * @return {@link String}
     */
    @SneakyThrows
    public static String toPrettyJson(Object object) {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    private static String doToJson(Object object, ObjectMapper objectMapper) {
        if (object == null) return null;

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("json转换失败，原对象：{}，原因：{}", object.getClass(), e.getMessage());
            throw new JsonParseException(JSON_CONVERT_ERROR.getMessage());
        }
    }

    /**
     * 解析json为对象
     *
     * @param json  json
     * @param clazz clazz
     * @return {@link T}
     */
    public static <T> T parse(String json, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(clazz);
        return doParse(json, javaType);
    }

    /**
     * 解析 Json 为 集合 MutableList 可用增强功能
     *
     * @param json  json
     * @param clazz clazz
     * @return {@link MutableList}<{@link T}>
     */
    public static <T> MutableList<T> parseToList(String json, Class<T> clazz) {
        if (Strings.isNullOrEmpty(json)) return ListUtil.empty();
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        return ListUtil.adaptIterable(doParse(json, javaType));
    }

    /**
     * 解析 Json 为 Map
     *
     * @param json json
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K, V> Map<K, V> parseToMap(String json) {
        TypeReference<HashMap<K, V>> typeReference = new TypeReference<HashMap<K, V>>() {
        };
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(typeReference);
        return doParse(json, javaType);
    }

    private static <T> T doParse(String json, JavaType javaType) {
        if (Strings.isNullOrEmpty(json)) return null;

        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("Json转换为对象失败，原Json串为：{}，原因为：{}", json, e.getMessage());
            throw new JsonParseException(JSON_CONVERT_ERROR.getMessage());
        }
    }

    public static JsonNode parseToJsonNode(String json) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            log.error("Json转换为对象失败，原Json串为：{}，原因为：{}", json, e.getMessage());
            throw new JsonParseException(JSON_CONVERT_ERROR.getMessage());
        }
    }

    /**
     * 深拷贝
     *
     * @param source 源
     * @param clazz  clazz
     * @return {@link T}
     */
    public static <T> T deepCopy(T source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(source), clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
