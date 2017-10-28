package com.luolei.template.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.common.exception.TException;

import java.io.IOException;

/**
 * @author luolei
 * @email askluolei@gmail.com
 * @date 2017/10/28 11:35
 */
public class JsonUtils {

    public static <T> String toJson(T t) {
        try {
            return SpringContextUtils.getBean(ObjectMapper.class).writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new TException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> requireType) {
        try {
            return SpringContextUtils.getBean(ObjectMapper.class).readValue(json, requireType);
        } catch (IOException e) {
            throw new TException(e);
        }
    }
}
