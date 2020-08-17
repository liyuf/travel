package com.heima.travel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ObjectToStringUtil {
    //json转换
    public static String objToJson(Object obj) throws JsonProcessingException {
        String json=null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        json = objectMapper.writeValueAsString(obj);
        return json;
    }
    //传递数据到前台
    public static void write(Object obj, HttpServletResponse response) throws IOException {
            response.setContentType("application/json;charset=utf-8");
            String json = objToJson(obj);
            response.getWriter().write(json);
    }

    public static <T> T jsonToObj(String json,Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = objectMapper.readValue(json,clazz);
        return obj;
    }
}
