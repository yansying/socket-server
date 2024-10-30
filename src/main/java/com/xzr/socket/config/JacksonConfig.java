package com.xzr.socket.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;
/**
 * @author xzr
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册 JavaTimeModule 以支持 Java 8 日期和时间
        objectMapper.registerModule(new JavaTimeModule());

        // 设置全局时区为上海
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        // 设置日期格式
        objectMapper.configOverride(java.time.LocalDate.class)
                .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd"));
        objectMapper.configOverride(java.time.LocalDateTime.class)
                .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")
                        .withTimeZone(TimeZone.getTimeZone("Asia/Shanghai")));  // 设置日期时区

        objectMapper.configOverride(java.util.Date.class)
                .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss")
                        .withTimeZone(TimeZone.getTimeZone("Asia/Shanghai")));  // 设置时区

        // 配置序列化特性，支持空值
        objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);

        // 配置ObjectMapper，遇到未知属性时不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 配置ObjectMapper，遇到基本数据类型的空值时抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        return objectMapper;
    }
}
