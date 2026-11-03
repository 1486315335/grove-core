package com.lrz.grovecore.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.lrz.grovecore.utils.DateUtil;
import com.lrz.grovecore.utils.JsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 只有在序列化自己的LocalDateTime时，才会触发，继承属性不触发
 *
 * @author lirenzhi
 * @since 2023/11/29
 */
public class ObjectMapperLocalDateTimeDeserializerExtend extends LocalDateTimeDeserializer {

    public static LocalDateTimeDeserializer INSTANCE = new ObjectMapperLocalDateTimeDeserializerExtend();

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        try {
            return super.deserialize(p, ctx);
        } catch (Exception e) {
            JsonNode node = p.getCodec().readTree(p);
            //JsonUtil.parse(JsonUtil.toJson(node.textValue()), Date.class)会触发 ObjectMapperDateFormatExtend.class
            return DateUtil.Convert.dateToLocalDateTime(JsonUtil.parse(JsonUtil.toJson(node.textValue()), Date.class));
        }
    }
}